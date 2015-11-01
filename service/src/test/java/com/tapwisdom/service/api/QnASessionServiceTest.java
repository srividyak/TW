package com.tapwisdom.service.api;

import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.service.api.IQnASessionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class QnASessionServiceTest {

    @Autowired
    private IQnASessionService service;

    @Autowired
    private MongoOperations operations;

    @Autowired
    private QnASessionDao qnASessionDao;

    @Autowired
    private UserDao userDao;

    private Question getQuestion() {
        Question question = new Question();
        question.setCompanyId("companyId");
        question.setContent("How is the company culture?");
        question.setTags("culture");
        question.setType(QuestionType.TEXT);
        return question;
    }

    @Test
    public void createQuestionWithQuestionObject() {
        List<QnA> qnAList = new ArrayList<QnA>();
        QnA qnA = new QnA();
        Question question = getQuestion();
        qnA.setQuestion(question);
        qnAList.add(qnA);
        try {
            QnASession qnASession = service.addQuestionsBySeeker(qnAList, "seeker", "companyId", "designation");
            assert qnASession != null;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void createQuestionsWithQuestionId() {
        Question question = getQuestion();
        operations.save(question);
        String questionId = question.getId();
        List<QnA> qnAList = new ArrayList<QnA>();
        QnA qnA = new QnA();
        question = new Question();
        question.setId(questionId);
        qnA.setQuestion(question);
        qnAList.add(qnA);
        try {
            QnASession qnASession = service.addQuestionsBySeeker(qnAList, "seeker", "companyId", "designation");
            assert qnASession != null;
            assert qnASession.getQnAList() != null;
            assert qnASession.getQnAList().size() == 1;
            assert qnASession.getQnAList().get(0).getQuestion().getType() == QuestionType.TEXT;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    private QnASession getQnASession() {
        Question question = getQuestion();
        operations.save(question);
        List<QnA> qnAList = new ArrayList<QnA>();
        QnA qnA = new QnA();
        qnA.setQuestion(question);
        qnA.setQuestionId(1);
        qnAList.add(qnA);
        QnASession qnASession = new QnASession();
        qnASession.setQnAList(qnAList);
        qnASession.setSeekerId("seeker");
        qnASession.setCompanyId("companyId");
        qnASession.setDesignation("designation");
        qnASession.setQuestionSubmittedTimestamp(new Date().getTime());
        return qnASession;
    }

    @Test
    public void testSendQuestionToAdvisor() {
        QnASession qnASession = getQnASession();
        operations.save(qnASession);
        String[] advisor = {"advisor"};
        try {
            service.sendQuestionsToAdvisors(qnASession.getId(), advisor);
            qnASession = operations.findById(qnASession.getId(), QnASession.class);
            assert qnASession != null;
            assert qnASession.getAdvisorId().equals("advisor");
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetQuestionsBySeeker() {
        QnASession qnASession = getQnASession();
        try {
            operations.save(qnASession);
            List<QnASession> qnASessions = service.getQuestionsAskedBySeeker(qnASession.getSeekerId(), 0);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
            assert qnASessions.get(0).getSeekerId().equals(qnASession.getSeekerId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetQuestionsToAdvisor() {
        QnASession qnASession = getQnASession();
        qnASession.setAdvisorId("advisor");
        try {
            operations.save(qnASession);
            List<QnASession> qnASessions = service.getQuestionsDirectedToAdvisor(qnASession.getAdvisorId(), 0);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
            assert qnASessions.get(0).getAdvisorId().equals(qnASession.getAdvisorId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetQuestionsAboutCompany() {
        QnASession qnASession = getQnASession();
        try {
            operations.save(qnASession);
            List<QnASession> qnASessions = service.getQuestionsAskedAboutCompany(qnASession.getCompanyId(), 0);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
            assert qnASessions.get(0).getCompanyId().equals(qnASession.getCompanyId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testAnswerQuestionByAdvisor() {
        QnASession qnASession = getQnASession();
        try {
            qnASession.setAdvisorId("advisor");
            qnASession.setSeekerId("seeker");
            User user = new User();
            user.setId("advisor");
            userDao.save(user);
            user.setId("seeker");
            userDao.save(user);
            operations.save(qnASession);
            List<QnA> qnAList = qnASession.getQnAList();
            QnA qnA = qnAList.get(0);
            qnA.setAnswer("It is great");
            String qnASessionId = service.answerQuestion(qnASession.getId(), qnASession.getAdvisorId(), qnAList);
            qnASession = qnASessionDao.getById(qnASessionId, QnASession.class);
            List<QnASession> qnASessions = service.getQuestionsDirectedToAdvisor(qnASession.getAdvisorId(), 0);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
            assert qnASessions.get(0).getIsAnswered() == true;
            assert qnASessions.get(0).getQnAList().size() == 1;
            assert qnASessions.get(0).getQnAList().get(0).getAnswer().equals(qnA.getAnswer());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testAnswerQuestionByPublic() {
        QnASession qnASession = getQnASession();
        try {
            String publicAdvisorId = "public_advisor";
            qnASession.setAdvisorId(null);
            qnASession.setSeekerId("seeker");
            User user = new User();
            user.setId(publicAdvisorId);
            userDao.save(user);
            user.setId("seeker");
            userDao.save(user);
            operations.save(qnASession);
            List<QnA> qnAList = qnASession.getQnAList();
            QnA qnA = qnAList.get(0);
            qnA.setAnswer("It is good");
            String qnASessionId = service.answerQuestion(qnASession.getId(), publicAdvisorId, qnAList);
            qnASession = qnASessionDao.getById(qnASessionId, QnASession.class);
            List<QnASession> qnASessions = service.getQuestionsDirectedToAdvisor(qnASession.getAdvisorId(), 0);
            assert qnASessions != null;
            assert qnASessions.size() == 1;
            assert qnASessions.get(0).getIsAnswered() == true;
            assert qnASessions.get(0).getQnAList().size() == 1;
            assert qnASessions.get(0).getQnAList().get(0).getAnswer().equals(qnA.getAnswer());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testSendQuestionToMultipleAdvisors() {
        QnASession qnASession = getQnASession();
        qnASession.setAdvisorId("advisor");
        operations.save(qnASession);
        String[] advisors = {"advisor1", "advisor2"};
        try {
            User user = new User();
            user.setId("advisor1");
            userDao.save(user);
            user = new User();
            user.setId("advisor2");
            userDao.save(user);
            service.sendQuestionsToAdvisors(qnASession.getId(), advisors);
            List<QnASession> qnASessionsToAdvisor = service.getQuestionsDirectedToAdvisor("advisor", 0);
            List<QnASession> qnASessionsToAdvisor1 = service.getQuestionsDirectedToAdvisor("advisor1", 0);
            List<QnASession> qnASessionsToAdvisor2 = service.getQuestionsDirectedToAdvisor("advisor2", 0);
            assert qnASessionsToAdvisor != null;
            assert qnASessionsToAdvisor1 != null;
            assert qnASessionsToAdvisor2 != null;
            assert qnASessionsToAdvisor.size() == 1;
            assert qnASessionsToAdvisor.get(0).getId().equals(qnASession.getId());
            assert !qnASessionsToAdvisor1.get(0).getId().equals(qnASession.getId());
            assert !qnASessionsToAdvisor2.get(0).getId().equals(qnASession.getId());
            assert !qnASessionsToAdvisor1.get(0).getId().equals(qnASessionsToAdvisor2.get(0).getId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @After
    public void deleteAllQuestions() {
        Query query = new Query();
        operations.findAllAndRemove(query, QnASession.class);
        operations.findAllAndRemove(query, Question.class);
        operations.findAllAndRemove(query, User.class);
    }

}
