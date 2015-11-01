package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserSource;

public interface IUserEmailNotificationService {
    void sendWelcomeEmail(User user, UserSource userSource) throws TapWisdomException;
}
