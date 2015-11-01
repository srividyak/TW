package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.notification.IMobileNotificationService;
import com.tapwisdom.core.notification.UserMobileNotificationIdentity;
import com.tapwisdom.service.api.IUserMobileNotificationService;
import com.tapwisdom.service.mobilenotifications.*;
import com.tapwisdom.service.mobilenotifications.payload.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserMobileNotificationServiceImpl implements IUserMobileNotificationService {
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Boolean IS_NOTIFICATIONS_ENABLED = Boolean.parseBoolean(reader.getProperty(Constants.GCM_NOTIFICATION_ENABLED, "false"));
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private IMobileNotificationService mobileNotificationService;
    
    private void sendNotification(MobileNotificationType type, Payload payload, UserMobileNotificationIdentity identity) {
        if (IS_NOTIFICATIONS_ENABLED) {
            Map<String, String> notificationMap = new HashMap<>();
            notificationMap.put(type.name(), payload.toString());
            mobileNotificationService.send(notificationMap, identity);
        }
    }
    
    private UserMobileNotificationIdentity getUserMobileIdentity(User user) {
        UserMobileNotificationIdentity identity = new UserMobileNotificationIdentity(user.getId(), user.getDeviceId());
        return identity;
    }

    @Override
    public void requestAnswer(String seekerId, String advisorId) throws TapWisdomException {
        User advisor = userDao.getById(advisorId, User.class);
        RequestAnswerPayload payload = new RequestAnswerPayload(seekerId);
        sendNotification(MobileNotificationType.REQUEST_ANSWER, payload, getUserMobileIdentity(advisor));
    }

    @Override
    public void questionAnswered(String seekerId, String advisorId) throws TapWisdomException {
        User seeker = userDao.getById(seekerId, User.class);
        QuestionAnsweredPayload payload = new QuestionAnsweredPayload(advisorId);
        sendNotification(MobileNotificationType.QUESTION_ANSWERED, payload, getUserMobileIdentity(seeker));
    }

    @Override
    public void qnaUpVoted(String userId, String qnaSessionId, String questionId) throws TapWisdomException {
        User user = userDao.getById(userId, User.class);
        QnAUpVotedPayload payload = new QnAUpVotedPayload(qnaSessionId, questionId);
        sendNotification(MobileNotificationType.QNA_UPVOTED, payload, getUserMobileIdentity(user));
    }

    @Override
    public void creditsAdded(String userId, Double credits) throws TapWisdomException {
        User user = userDao.getById(userId, User.class);
        CreditsPayload payload = new CreditsPayload(Double.toString(credits));
        sendNotification(MobileNotificationType.CREDITS_ADDED, payload, getUserMobileIdentity(user));
    }

    @Override
    public void creditsDeducted(String userId, Double credits) throws TapWisdomException {
        User user = userDao.getById(userId, User.class);
        CreditsPayload payload = new CreditsPayload(Double.toString(credits));
        sendNotification(MobileNotificationType.CREDITS_DEDUCTED, payload, getUserMobileIdentity(user));
    }
}
