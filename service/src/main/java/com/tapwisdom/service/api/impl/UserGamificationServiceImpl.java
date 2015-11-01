package com.tapwisdom.service.api.impl;

import com.google.common.collect.Sets;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.service.api.IUserGamificationService;
import com.tapwisdom.service.entity.TwUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.tapwisdom.core.common.util.Utils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by shravan on 26/8/15.
 */
@Component
public class UserGamificationServiceImpl implements IUserGamificationService {

    private static final Logger LOG = Logger.getLogger(QuestionServiceImpl.class);
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Map<Long, String> upvoteConfig = Utils.getObjectFromString(reader.getProperty(Constants.UPVOTEBADGE_CONFIG, " "), HashMap.class);
    private static final Map<Long, String> evaluateConfigVerified = Utils.getObjectFromString(reader.getProperty(Constants.EVALUATION_BADGE_VERIFIED_PROFILE, " "), HashMap.class);
    private static final Map<Long, String> evaluateConfigNonVerified = Utils.getObjectFromString(reader.getProperty(Constants.EVALUATION_BADGE_NON_VERIFIED_PROFILE, " "), HashMap.class);
    private static List upVoteConfigKeys, evaluateVerifiedConfigKeys, evaluateNonVerifiedConfigKeys;

    static {
        try {
            upVoteConfigKeys = new ArrayList<>(upvoteConfig.keySet());
            Collections.sort(upVoteConfigKeys, new Comparator<String>() {
                public int compare(String a, String b) {
                    return Integer.valueOf(a).compareTo(Integer.valueOf(b));
                }
            });

            evaluateVerifiedConfigKeys = new ArrayList<>(evaluateConfigVerified.keySet());
            Collections.sort(evaluateVerifiedConfigKeys, new Comparator<String>() {
                public int compare(String a, String b) {
                    return Integer.valueOf(a).compareTo(Integer.valueOf(b));
                }
            });

            evaluateNonVerifiedConfigKeys = new ArrayList<>(evaluateConfigNonVerified.keySet());
            Collections.sort(evaluateNonVerifiedConfigKeys, new Comparator<String>() {
                public int compare(String a, String b) {
                    return Integer.valueOf(a).compareTo(Integer.valueOf(b));
                }
            });
        } catch (Exception e) {
            LOG.error("Error sorting the configs");
        }


    }

    @Autowired
    private QnASessionDao qnASessionDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CompanyDao companyDao;


    private User updateBadge(User user, Map<Long, String> badgeConfig, List configKeys, Long numToCompare, Boolean isUpvoteBadge) {
        String previousEntry = null;
        for (Object configKey : configKeys) {
            if (numToCompare < Long.valueOf(configKey.toString())) {
                break;
            }
            previousEntry = configKey.toString();
        }

        if (previousEntry != null) {
            if (isUpvoteBadge) {
                user.setUpvoteBadge(UpvoteBadge.valueOf(badgeConfig.get(previousEntry)));
            } else {
                user.setEvaluateBadge(EvaluateBadge.valueOf(badgeConfig.get(previousEntry)));
            }
        }
        return user;
    }

    @Override
    public void updateUserUpVoteBadge(QnAEntity qnaEntity) throws TapWisdomException {
        String qnaSessionId = qnaEntity.getQnaSessionId();
        QnASession qnASession = qnASessionDao.getById(qnaSessionId, QnASession.class);
        String userId = qnASession.getAdvisorId();
        User user = userDao.getUser(userId); //adviser
        Long userUpvotes = user.getQnAUpvoteCount();
        userUpvotes++;
        this.updateBadge(user, upvoteConfig, upVoteConfigKeys, userUpvotes, true);
        user.setQnAUpvoteCount(userUpvotes);
        userDao.updateUser(user);
    }

    @Override
    public void updateUserEvaluateBadge(QnASession qnASession) throws TapWisdomException {
        String answeredUserId = qnASession.getAdvisorId();
        String companyId = qnASession.getCompanyId();
        User answeredUser = userDao.getUser(answeredUserId); //adviser
        Company company = companyDao.getById(companyId, Company.class);
        Long numQuestionsAnsweredInCompany = company.getNumQuestionsAnswered();
        //Get the information of the one who answered
        Long numQuestionsAnsweredByUser = answeredUser.getNumQuestionsAnswered();
        Boolean isVerifiedAdviser = answeredUser.getIsVerifiedAdvisor();

        //Increment the number of answers in company and also user
        numQuestionsAnsweredInCompany++;
        numQuestionsAnsweredByUser++;
        if (isVerifiedAdviser == true) {
            //give the adviser the evaluateBadge
            answeredUser = this.updateBadge(answeredUser, evaluateConfigVerified, evaluateVerifiedConfigKeys, numQuestionsAnsweredByUser, false);
        } else {
            //give the seeker evaluateBadge based on number of completed evaluations
            answeredUser = this.updateBadge(answeredUser, evaluateConfigNonVerified, evaluateNonVerifiedConfigKeys, numQuestionsAnsweredByUser, false);
        }

        //first answer in a company: give the user a TapBadge
        if (numQuestionsAnsweredInCompany == 1) {
            List tapBadges = answeredUser.getTapBadges();
            if (tapBadges == null) {
                tapBadges = new ArrayList();
                tapBadges.add(TapBadge.PARTNER);
            } else if (!tapBadges.contains(TapBadge.PARTNER)) {
                tapBadges.add(TapBadge.PARTNER);
            }
            answeredUser.setTapBadges(tapBadges);
        }
        answeredUser.setNumQuestionsAnswered(numQuestionsAnsweredByUser);
        company.setNumQuestionsAnswered(numQuestionsAnsweredInCompany);
        userDao.updateUser(answeredUser);
        companyDao.updateCompany(company);
    }

    @Override
    public void updateUserTapBadge(QnASession qnASession) throws TapWisdomException {
        String userId = qnASession.getSeekerId();
        String companyId = qnASession.getCompanyId();
        User seeker = userDao.getUser(userId); //seeker
        Company company = companyDao.getById(companyId, Company.class);
        Long numQuestionsAskedInCompany = company.getNumQuestionsAsked();
        Long numQuestionsAskedByUser = seeker.getNumQuestionsAsked();
        //first question in a comapany
        if (numQuestionsAskedInCompany == 0) {
            List tapBadges = seeker.getTapBadges();
            if (tapBadges == null) {
                tapBadges = new ArrayList();
                tapBadges.add(TapBadge.VOYAGER);
            } else if (!tapBadges.contains(TapBadge.VOYAGER)) {
                tapBadges.add(TapBadge.VOYAGER);
            }
            seeker.setTapBadges(tapBadges);
        }
        //Increment the number of questions in company and also user
        numQuestionsAskedInCompany++;
        numQuestionsAskedByUser++;
        seeker.setNumQuestionsAsked(numQuestionsAskedByUser);
        company.setNumQuestionsAsked(numQuestionsAskedInCompany);
        userDao.updateUser(seeker);
        companyDao.updateCompany(company);
    }

    @Override
    public void updateUserEvaluateBadge(String qnASessionId) throws TapWisdomException {
        QnASession qnASession = qnASessionDao.getById(qnASessionId, QnASession.class);
        this.updateUserEvaluateBadge(qnASession);
    }

    @Override
    public void updateUserTapBadge(String qnASessionId) throws TapWisdomException {
        QnASession qnASession = qnASessionDao.getById(qnASessionId, QnASession.class);
        this.updateUserTapBadge(qnASession);
    }
}
