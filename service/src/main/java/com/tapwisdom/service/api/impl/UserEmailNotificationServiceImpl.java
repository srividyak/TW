package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.common.util.TemplateFileReader;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserSource;
import com.tapwisdom.core.notification.IEmailNotificationService;
import com.tapwisdom.core.notification.UserEmailNotificationIdentity;
import com.tapwisdom.service.api.IUserEmailNotificationService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserEmailNotificationServiceImpl implements IUserEmailNotificationService {
    private static final TemplateFileReader templateFileReader = TemplateFileReader.getInstance();
    private static final PropertyReader propertyReader = PropertyReader.getInstance();
    private static final Boolean isSandbox = Boolean.parseBoolean(propertyReader.getProperty(Constants.SES_IS_SANDBOX, "true"));
    private static final String defaultSender = propertyReader.getProperty(Constants.SES_DEFAULT_SENDER, "");
    private static final String defaultRecipient = propertyReader.getProperty(Constants.SES_DEFAULT_RECIPIENT, "");
    private static final Logger LOG = Logger.getLogger(UserEmailNotificationServiceImpl.class);
    
    @Autowired
    private IEmailNotificationService notificationService;
    
    @Override
    public void sendWelcomeEmail(User user, UserSource userSource) throws TapWisdomException {
        String email = isSandbox ? defaultRecipient : user.getEmail();
        UserEmailNotificationIdentity recipientIdentity = new UserEmailNotificationIdentity(user.getId(), email);
        String name = "";
        switch (userSource) {
            case google:
                name = user.getGoogleProfile().getName() == null ? "" : user.getGoogleProfile().getName();
                break;
            
            case facebook:
                name = user.getFacebookProfile().getName() == null ? "" : user.getFacebookProfile().getName();
                break;
            
            case linkedIn:
            case advisor_form:
                name = user.getLinkedInProfile().getFirstName() == null ? "" : user.getLinkedInProfile().getFirstName();
                break;
            
            default:
                throw new TapWisdomException(1, "Invalid userSource passed. Cannot retrieve name from user");
        }
        String emailBody = getEmailBody(propertyReader.getProperty(Constants.WELCOME_EMAIL_TEMPLATE_FILE), new String[]{"name"}, new String[]{name});
        String subject = propertyReader.getProperty(Constants.WELCOME_EMAIL_SUBJECT, "");
        notificationService.send(subject, emailBody, defaultSender, recipientIdentity);
    }
    
    private String getEmailBody(String templateFile, String[] finders, String[] replacements) {
        String content = templateFileReader.getTemplate(templateFile, "");
        if (finders == null || replacements == null || finders.length != replacements.length) {
            return content;
        }
        int index = 0;
        for (String finder : finders) {
            content = content.replaceFirst("\\{" + finder + "\\}", replacements[index++]);
        }
        return content;
    }
}
