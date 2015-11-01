package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.core.daos.documents.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.data.mongodb.core.MongoOperations;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by shravan on 28/8/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
@Component
public class UserGamificationServiceTest {

    @Autowired
    private IUserGamificationService gamificationService;
    @Autowired
    private UserDao userDao;

    @Autowired
    private QnASessionDao qnASessionDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private MongoOperations operations;

    private User adviser = new User();
    private User seeker = new User();
    private QnAEntity qnAEntity = new QnAEntity();
    private QnASession qnASession = new QnASession();
    private Company company = new Company();


    @Before
    public void createQnAEntity() {
        seeker.setNumQuestionsAsked(3L);
        adviser.setRole(UserRole.adviser);
        adviser.setQnAUpvoteCount(14L);
        adviser.setIsVerifiedAdvisor(true);
        adviser.setUpvoteBadge(UpvoteBadge.BRONZE);
        adviser.setNumQuestionsAsked(4L);
        adviser.setNumQuestionsAnswered(4L);
        try {
            userDao.save(seeker);
            userDao.save(adviser);
        } catch (TapWisdomException e) {
            assert false;
        }

        company.setIndustry("software");
        Location location = new Location();
        location.setName("bangalore");
        company.setLocations(new Location[]{location});
        company.setName("tapwisdom");
        company.setNumEmployees(4L);
        company.setWebsite("http://tapwisdom.com");
        try {
            companyDao.save(company);
        } catch (TapWisdomException e) {
            assert false;
        }

        qnAEntity.setId("qnaSessionId__0");
        QnA qnA = new QnA();
        Question question = new Question();
        question.setContent("how is culture");
        qnA.setQuestion(question);
        qnA.setAnswer("it is good");
        qnASession.setAdvisorId(adviser.getId());
        qnASession.setSeekerId(seeker.getId());
        qnASession.setCompanyId(company.getId());
        qnASession.setQnAList(Arrays.asList(qnA));
        qnASession.setId("qnaSessionId");
        try {
            qnASessionDao.save(qnASession);
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @Test
    public void testUpdateUserUpVoteBadge() {
        try {
            gamificationService.updateUserUpVoteBadge(qnAEntity);
            adviser = userDao.getUser(adviser.getId());
            assert adviser.getQnAUpvoteCount().equals(15L);
            assert adviser.getUpvoteBadge().equals(UpvoteBadge.SILVER);
        } catch (TapWisdomException e) {
            assert false;
        }

    }

    @Test
    public void testUpdateUserEvaluateBadge() {
        try {
            gamificationService.updateUserEvaluateBadge(qnASession);
            adviser = userDao.getUser(adviser.getId());
            company = companyDao.getById(company.getId(), Company.class);
            adviser.getEvaluateBadge().equals(EvaluateBadge.LEAD);
            adviser.getNumQuestionsAnswered().equals(5L);
            adviser.getTapBadges().equals(TapBadge.PARTNER);
            adviser.getRole().equals(UserRole.adviser);
            company.getNumQuestionsAnswered().equals(1L);
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @Test
    public void testUpdateUserEvaluateBadgeWithPublicEvaluation() {
        try {
            qnASession.setAdvisorId(seeker.getId());
            gamificationService.updateUserEvaluateBadge(qnASession);
            adviser = userDao.getUser(seeker.getId());
            company = companyDao.getById(company.getId(), Company.class);
            adviser.getEvaluateBadge().equals(EvaluateBadge.ADVISOR);
            adviser.getNumQuestionsAnswered().equals(5L);
            adviser.getTapBadges().equals(TapBadge.PARTNER);
            adviser.getRole().equals(UserRole.seeker);
            company.getNumQuestionsAnswered().equals(1L);
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @Test
    public void testUpdateUserTapBadge() {
        try {
            gamificationService.updateUserTapBadge(qnASession);
            seeker = userDao.getUser(seeker.getId());
            company = companyDao.getById(company.getId(), Company.class);
            seeker.getNumQuestionsAsked().equals(4L);
            List<TapBadge> tapBadges = new ArrayList<>();
            tapBadges.add(TapBadge.VOYAGER);
            tapBadges.add(TapBadge.PARTNER); //was added by last test
            seeker.getTapBadges().equals(tapBadges);
            company.getNumQuestionsAsked().equals(1L);
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @After
    public void deleteEntities(){
        Query query = new Query();
        operations.findAllAndRemove(query, User.class);
        operations.findAllAndRemove(query, Company.class);
        operations.findAllAndRemove(query, QnASession.class);
    }
}
