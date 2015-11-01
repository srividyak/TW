package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.apis.CreditMgtDao;
import com.tapwisdom.service.api.IQnASessionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.apis.QuestionDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.service.api.IUserMobileNotificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class QnASessionServiceImpl implements IQnASessionService {

    private static final Logger LOG = Logger.getLogger(QnASessionServiceImpl.class);

    @Autowired
    private QnASessionDao qnASessionDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private EntityDao<QnAEntity> entityDao;

    @Autowired
    private CreditMgtDao creditMgtDao;

    @Autowired
    private IUserMobileNotificationService mobileNotificationService;

    private static String getQnASessionKey(String sessionId, int qnaId) {
        return sessionId + "__" + qnaId;
    }

    private List<QnASession> getQnASessionsWithUpvote(String userId, List<QnASession> qnASessions) throws TapWisdomException {
        List<QnAEntity> qnAEntities = entityDao.getUpVotedEntities(userId, EntityType.QNA, 0);
        Set<String> qnaEntityIds = new HashSet<String>();
        for (QnAEntity qnAEntity : qnAEntities) {
            qnaEntityIds.add(qnAEntity.getId());
        }
        for (QnASession qnASession : qnASessions) {
            for (QnA qnA : qnASession.getQnAList()) {
                String qnaEntityId = QnAEntity.getQnAEntityId(qnASession.getId(), qnA.getQuestionId());
                if (qnaEntityIds.contains(qnaEntityId)) {
                    qnA.setIsUpvoted(true);
                }
            }
        }
        return qnASessions;
    }

    @Override
    public List<QnASession> getQuestionsAskedBySeeker(String seeker, int page) throws TapWisdomException {
        List<QnASession> qnASessions = qnASessionDao.getQuestionsAskedBySeeker(seeker, page);
        return getQnASessionsWithUpvote(seeker, qnASessions);
    }

    @Override
    public List<QnASession> getQuestionsDirectedToAdvisor(String advisor, int page) throws TapWisdomException {
        List<QnASession> qnASessions = qnASessionDao.getQuestionsDirectedToAdvisor(advisor, page);
        return getQnASessionsWithUpvote(advisor, qnASessions);
    }

    @Override
    public List<QnASession> getQuestionsAskedAboutCompany(String companyId, int page) throws TapWisdomException {
        return qnASessionDao.getQuestionsByCompany(companyId, page);
    }

    @Override
    public void sendQuestionsToAdvisors(String qnaSessionId, String[] advisorIds) throws TapWisdomException {
        QnASession qnASession = qnASessionDao.getById(qnaSessionId, QnASession.class);
        String seekerId = qnASession.getSeekerId();
        int index = 0;
        if (qnASession.getAdvisorId() == null) {
            index++;
            qnASession.setAdvisorId(advisorIds[0]);
            if (!advisorIds[0].equals(qnASession.getSeekerId())) {
                qnASessionDao.save(qnASession);
            } else {
                LOG.error("Attempt was made to send question to the same user for answering. User id: " + advisorIds[0]);
            }
        }
        for (int i = index; i < advisorIds.length; i++) {
            String advisorId = advisorIds[i];
            if (!seekerId.equals(advisorId)) {
                QnASession q = (QnASession) qnASession.clone();
                q.setAdvisorId(advisorId);
                q.setId(null);
                q.setQuestionSubmittedTimestamp(new Date().getTime());
                q.setIsAnswered(false);
                qnASessionDao.save(q);
                mobileNotificationService.requestAnswer(seekerId, advisorId);
            } else {
                LOG.error("Attempt was made to send question to the same user for answering. User id: " + advisorIds[i]);
            }
        }
    }

    @Override
    public String answerQuestion(String qnaSessionId, String advisorId, List<QnA> qnAList) throws TapWisdomException {
        QnASession qnASession = qnASessionDao.getById(qnaSessionId, QnASession.class);
        String seekerId = qnASession.getSeekerId();
        if (!seekerId.equals(advisorId)) {
            if (qnASession.getAdvisorId() != null) {
                qnASession.setQnAList(qnAList);
                qnASession.setAnswerSubmittedTimestamp(new Date().getTime());
                qnASession.setIsAnswered(true);
                qnASessionDao.save(qnASession);
            } else {
                QnASession q = (QnASession) qnASession.clone();
                q.setQnAList(qnAList);
                q.setAdvisorId(advisorId);
                q.setId(null);
                q.setAnswerSubmittedTimestamp(new Date().getTime());
                q.setIsAnswered(true);
                qnASessionDao.save(q);
            }
            creditMgtDao.addCredits(advisorId, 7);
            mobileNotificationService.questionAnswered(seekerId, advisorId);
            mobileNotificationService.creditsAdded(advisorId, 7.0);

            List<QnASession> qnASessions = getQuestionsDirectedToAdvisor(advisorId, 0);
            return qnASessions.get(0).getId();
        } else {
            LOG.error("Attempt was made to send question to the same user for answering. User id: " + advisorId);
            throw new TapWisdomException(1, "Attempt was made to send question to the same user for answering. User id: " + advisorId);
        }
    }

    @Override
    public List<QnASession> getQuestionsByFilter(QnASession qnASession, int page) throws TapWisdomException {
        return qnASessionDao.getQuestionsByFilters(qnASession, page);
    }

    @Override
    public QnASession addQuestionsBySeeker(List<QnA> qnAList, String seekerId, String companyId,
                                           String designation) throws TapWisdomException {
        for (QnA qnA : qnAList) {
            if (qnA.getQuestion().getContent() == null) {
                Question question = questionDao.getById(qnA.getQuestion().getId(), Question.class);
                qnA.setQuestion(question);
            }
        }
        QnASession qnASession = new QnASession();
        qnASession.setSeekerId(seekerId);
        qnASession.setQnAList(qnAList);
        qnASession.setQuestionSubmittedTimestamp(new Date().getTime());
        qnASession.setCompanyId(companyId);
        qnASession.setDesignation(designation);
        qnASessionDao.save(qnASession);
        return qnASession;
    }
}
