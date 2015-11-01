package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.service.api.ICompanyService;
import com.tapwisdom.service.api.IUserEmailNotificationService;
import com.tapwisdom.service.api.IUserService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ICompanyService companyService;
    
    @Autowired
    private IUserEmailNotificationService emailNotificationService;
    
    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);
    
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Boolean fetchCompanies = Boolean.parseBoolean(reader.getProperty(Constants.FETCH_COMPANIES_FROM_USER, "false"));

    /**
     * * supports updating status (activate | deactivate), email, customProfile 
     * @param user
     * @return
     * @throws TapWisdomException
     */
    @Override
    public Boolean updateUser(User user) throws TapWisdomException {
        user.setUpdatedAt(new Date().getTime());
        if(user.getAliasName() != null) {
            return userDao.updateUser(user);
        }else if(user.getVisibilityFlag() == UserPrivacy.PUBLIC ) {
            user.setAliasName(user.getId());
            return userDao.updateUser(user);
        }else {
            throw new TapWisdomException(1, "Alias name is mandatory");
        }
    }

    @Override
    public User getUser(String id) throws TapWisdomException {
        return userDao.getUser(id);
    }

    @Override
    public User createUser(User user) throws TapWisdomException {
        User retUser = user;
        if (user.getSource() == UserSource.facebook) {
            FacebookProfile facebookProfile = user.getFacebookProfile();
            if (facebookProfile != null) {
                String email = facebookProfile.getEmail();
                User fbUser = userDao.getUserByEmail(email);
                if (fbUser == null) {
                    LOG.info("creating new facebook user with email: " + email);
                    user.setEmail(facebookProfile.getEmail());
                    userDao.save(user);
                    emailNotificationService.sendWelcomeEmail(user, user.getSource());
                } else {
                    fbUser.setFacebookProfile(facebookProfile);
                    userDao.save(fbUser);
                    retUser = fbUser;
                }
            } else {
                throw new TapWisdomException(1, "facebook profile cannot be null if source is facebook");
            }
        } else if (user.getSource() == UserSource.google) {
            GoogleProfile googleProfile = user.getGoogleProfile();
            if (googleProfile != null) {
                String email = googleProfile.getEmail();
                User googleUser = userDao.getUserByEmail(email);
                if (googleUser == null) {
                    LOG.info("creating new google user with email: " + email);
                    user.setEmail(googleProfile.getEmail());
                    userDao.save(user);
                    emailNotificationService.sendWelcomeEmail(user, user.getSource());
                } else {
                    googleUser.setGoogleProfile(googleProfile);
                    userDao.save(googleUser);
                    retUser = googleUser;
                }
            } else {
                throw new TapWisdomException(1, "google profile cannot be null if source is google");
            }
        } else if (user.getSource() == UserSource.linkedIn || user.getSource() == UserSource.advisor_form) {
            LiProfile linkedInProfile = user.getLinkedInProfile();
            if (linkedInProfile != null) {
                String email = linkedInProfile.getEmailAddress();
                User linkedInUser = userDao.getUserByEmail(email);
                if (linkedInUser == null) {
                    LOG.info("creating new linkedIn user with email: " + email);
                    user.setEmail(linkedInProfile.getEmailAddress());
                    userDao.save(user);
                } else {
                    linkedInUser.setLinkedInProfile(linkedInProfile);
                    emailNotificationService.sendWelcomeEmail(user, user.getSource());
                    userDao.save(linkedInUser);
                    retUser = linkedInUser;
                }
                
                // commenting this code since we will seed DB with many companies
//                if (fetchCompanies) {
//                    List<Position> positions = linkedInProfile.getPositions();
//                    List<Company> companies = new ArrayList<Company>();
//                    if (positions != null) {
//                        for (Position position : positions) {
//                            Company companyFromLinkedInProfile =
//                                    position.getCompany();
//                            companies.add(companyFromLinkedInProfile);
//                        }
//                    }
//                    companyService.createCompanies(companies);
//                }
            } else {
                throw new TapWisdomException(1, "linkedIn profile cannot be null if source is linkedIn");
            }
        } else {
            throw new TapWisdomException(1, "Invalid source passed");
        }
        return retUser;
    }
    
}
