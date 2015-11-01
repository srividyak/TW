package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.documents.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("QnATapService")
public class QnATapServiceImpl extends TapItServiceImpl<QnAEntity> {
    
    @Autowired
    private EntityDao<QnAEntity> entityDao;
    
    @Autowired
    private QnASessionDao qnASessionDao;
    
    @Override
    protected EntityDao getEntityDao() {
        return entityDao;
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.QNA;
    }

    @Override
    public void addTap(QnAEntity entity, String userId, String folderName) throws TapWisdomException {
        QnASession qnASession = qnASessionDao.getById(entity.getQnaSessionId(), QnASession.class);
        int qnaIndex = entity.getQuestionId();
        if (qnaIndex < qnASession.getQnAList().size()) {
            QnA qnA = qnASession.getQnAList().get(qnaIndex);
            entity.setQuestion(qnA.getQuestion());
            entity.setAnswer(qnA.getAnswer());
            entityDao.tapEntity(entity, getEntityType(), userId, folderName);
        } else {
            throw new TapWisdomException(1, "Invalid qna to tap");
        }
    }
}
