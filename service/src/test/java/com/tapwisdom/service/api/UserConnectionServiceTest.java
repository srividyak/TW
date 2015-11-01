package com.tapwisdom.service.api;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUserConnectionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.EntityCharacteristics;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserEntityRelation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by srividyak on 04/08/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserConnectionServiceTest {
    
    @Autowired
    private IUserConnectionService service;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private MongoOperations operations;
    
    private User targetUser;
    
    @Before
    public void createUser() {
        targetUser = new User();
        targetUser.setEmail("email");
        try {
            userDao.save(targetUser);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void viewUserTest() {
        TwUser twUser = new TwUser();
        twUser.setId("id");
        try {
            service.viewUser(twUser, targetUser.getId());
            List<User> users = service.getAllConnections(twUser, 0);
            assert users != null;
            assert users.size() == 1;
            assert users.get(0).getEmail().equals(targetUser.getEmail());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), User.class);
        operations.findAllAndRemove(new Query(), EntityCharacteristics.class);
        operations.findAllAndRemove(new Query(), UserEntityRelation.class);
    }
    
}
