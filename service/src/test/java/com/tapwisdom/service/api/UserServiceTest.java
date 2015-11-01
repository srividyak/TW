package com.tapwisdom.service.api;

import com.tapwisdom.service.api.ICompanyService;
import com.tapwisdom.service.api.IUserService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by srividyak on 02/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserServiceTest {
    
    @Autowired
    private IUserService userService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private ICompanyService companyService;

    @Autowired
    private MongoOperations operations;
    
    @Test
    public void testCreateFacebookUser() {
        User user = new User();
        FacebookProfile facebookProfile = new FacebookProfile();
        facebookProfile.setEmail("vidya@gmail.com");
        facebookProfile.setFirstName("vidya");
        facebookProfile.setLastName("murthy");
        facebookProfile.setProfileImage("image");
        facebookProfile.setProfileUrl("http://facebook.com/vidya");
        user.setFacebookProfile(facebookProfile);
        user.setSource(UserSource.facebook);

        try {
            User createdUser = userService.createUser(user);
            assert createdUser != null;
            assert createdUser.getEmail().equals(facebookProfile.getEmail());
            assert createdUser.getId() != null;
            assert createdUser.getFacebookProfile() != null;
            assert createdUser.getRole() == UserRole.seeker;
            userDao.delete(createdUser);
        } catch (TapWisdomException e) {
            assert false;
        }
    }
    
    @Test
    public void testCreateGoogleUser() {
        User user = new User();
        GoogleProfile googleProfile = new GoogleProfile();
        googleProfile.setEmail("vidya@gmail.com");
        googleProfile.setFirstName("vidya");
        googleProfile.setLastName("murthy");
        googleProfile.setProfileImage("image");
        googleProfile.setProfileUrl("http://facebook.com/vidya");
        user.setGoogleProfile(googleProfile);
        user.setSource(UserSource.google);

        try {
            User createdUser = userService.createUser(user);
            assert createdUser != null;
            assert createdUser.getEmail().equals(googleProfile.getEmail());
            assert createdUser.getId() != null;
            assert createdUser.getGoogleProfile() != null;
            assert createdUser.getRole() == UserRole.seeker;
            userDao.delete(createdUser);
        } catch (TapWisdomException e) {
            assert false;
        }
    }
    
    @Test
    public void testCreateLinkedInUser() {
        User user = new User();
        String linkedInProfileStr = "{ \"_id\" : \"122cwK0-fE\", \"dateOfBirth\" : { \"day\" : 19, \"month\" : 3, \"year\" : 1989 }, \"educationArray\" : [ { \"_id\" : 47417514, \"degree\" : \"BE\", \"startDate\" : { \"year\" : 2006 }, \"endDate\" : { \"year\" : 2010 }, \"schoolName\" : \"R. V. College of Engineering, Bangalore\", \"fieldOfStudy\" : \"Computer Science\" }, { \"_id\" : 30810383, \"degree\" : \"BE\", \"startDate\" : { \"year\" : 2006 }, \"endDate\" : { \"year\" : 2010 }, \"schoolName\" : \"Visvesvaraya Technological University\", \"fieldOfStudy\" : \"Computer Science\" } ], \"formattedName\" : \"Srividya Krishnamurthy\", \"positions\" : [ { \"_id\" : 417303462, \"company\" : { \"_id\" : null, \"name\" : \"iLabs 24x7 Inc\" }, \"isCurrent\" : true, \"title\" : \"Senior Member of Technical Staff\", \"startDate\" : { \"month\" : 7, \"year\" : 2013 } }, { \"_id\" : 130922281, \"company\" : { \"_id\" : 1288, \"name\" : \"Yahoo!\" }, \"isCurrent\" : false, \"title\" : \"Senior Software Engineer\", \"startDate\" : { \"month\" : 10, \"year\" : 2011 }, \"endDate\" : { \"month\" : 7, \"year\" : 2013 } }, { \"_id\" : 314294209, \"company\" : { \"_id\" : null, \"name\" : \"Yahoo!\" }, \"isCurrent\" : false, \"title\" : \"Software engineer\", \"startDate\" : { \"month\" : 8, \"year\" : 2010 }, \"endDate\" : { \"month\" : 9, \"year\" : 2011 } } ], \"publicProfileUrl\" : \"https://www.linkedin.com/in/srividyakrishnamurthy\", \"numConnections\" : 350, \"numConnectionsCapped\" : false, \"numRecommenders\" : 0, \"skillArray\" : [ { \"name\" : \"MongoDB\" }, { \"name\" : \"Spring\" }, { \"name\" : \"Algorithms\" }, { \"name\" : \"Data Structures\" }, { \"name\" : \"REST\" }, { \"name\" : \"Java\" }, { \"name\" : \"Hibernate\" }, { \"name\" : \"ElasticSearch\" }, { \"name\" : \"MySQL\" }, { \"name\" : \"JavaScript\" }, { \"name\" : \"C\" }, { \"name\" : \"C++\" }, { \"name\" : \"PHP\" }, { \"name\" : \"Node.js\" } ], \"followingMap\" : { \"industries\" : [ { \"_id\" : 4 } ], \"companies\" : [ { \"_id\" : 4192, \"name\" : \"Walmart eCommerce\" }, { \"_id\" : 29570, \"name\" : \"Directi\" }, { \"_id\" : 39946, \"name\" : \"Hightail\" }, { \"_id\" : 268799, \"name\" : \"BloomReach\" }, { \"_id\" : 272972, \"name\" : \"InMobi\" }, { \"_id\" : 2322204, \"name\" : \"Spreecast\" }, { \"_id\" : 2587638, \"name\" : \"Drawbridge, Inc.\" } ] }, \"emailAddress\" : \"vidya.vasishtha5@gmail.com\", \"industry\" : \"Computer Software\", \"firstName\" : \"Srividya\", \"lastName\" : \"Krishnamurthy\", \"pictureUrl\" : \"https://media.licdn.com/mpr/mprx/0_FEYCE7OJnDaNL8jkFeMAEDyBNwUNb8DkLwEjE20Qwm75ZXVXwummXuwesLREXb2e6Wyy5wTYSJyG\" }";
        LiProfile profileFull = Utils.getObjectFromString(linkedInProfileStr, LiProfile.class);
        user.setLinkedInProfile(profileFull);
        user.setSource(UserSource.linkedIn);

        try {
            User createdUser = userService.createUser(user);
            assert createdUser != null;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void delete() {
        Query query = new Query();
        operations.findAllAndRemove(query, User.class);
        operations.findAllAndRemove(query, com.tapwisdom.core.daos.documents.Company.class);
    }
    
}
