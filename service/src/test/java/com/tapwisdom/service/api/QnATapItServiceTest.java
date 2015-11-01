package com.tapwisdom.service.api;

import com.tapwisdom.service.api.ITapItService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class QnATapItServiceTest {
    
    @Autowired
    @Qualifier("QnATapService")
    private ITapItService qnaService;
    
    @Autowired
    private QnASessionDao qnASessionDao;
    
    @Autowired
    private MongoOperations operations;
    
    @Test
    public void testTap() {
        QnAEntity qnAEntity = new QnAEntity();
        qnAEntity.setId("qnaSessionId__0");
        QnA qnA = new QnA();
        Question question = new Question();
        question.setContent("how is culture");
        qnA.setQuestion(question);
        qnA.setAnswer("it is good");
        QnASession qnASession = new QnASession();
        qnASession.setQnAList(Arrays.asList(qnA));
        qnASession.setId("qnaSessionId");
        String userId = "user";
        try {
            qnASessionDao.save(qnASession);
            qnaService.addTap(qnAEntity, userId, "folder");
            List<QnAEntity> entities = qnaService.getTaps(userId, "folder", 0);
            assert entities != null;
            assert entities.size() == 1;
            assert entities.get(0).getId().equals(qnAEntity.getId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), QnASession.class);
        operations.findAllAndRemove(new Query(), EntityCharacteristics.class);
        operations.findAllAndRemove(new Query(), UserEntityRelation.class);
    }
    
}
