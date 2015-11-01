package com.tapwisdom.service.api;

import com.tapwisdom.service.api.IUserCompanyConnectionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.UserCompanyConnection;
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
 * Created by srividyak on 14/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class UserCompanyConnectionServiceTest {
    
    @Autowired
    private IUserCompanyConnectionService service;
    
    @Autowired
    private UserCompanyConnectionDao dao;
    
    @Autowired
    private CompanyDao companyDao;
    
    @Autowired
    private MongoOperations operations;
    
    private UserCompanyConnection connection;
    
    @Before
    public void create() {
        String userId = "user";
        String companyId = "company";
        connection = new UserCompanyConnection();
        connection.setCompanyId(companyId);
        connection.setUserId(userId);
        Company company = new Company();
        company.setId(companyId);
        try {
            dao.save(connection);
            companyDao.save(company);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testAddToWatchList() {
        try {
            service.addToWatchList(connection.getUserId(), connection.getCompanyId());
            assert service.isInWatchList(connection.getUserId(), connection.getCompanyId());
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetWatchedCompanies() {
        try {
            connection.setInWatchList(true);
            dao.save(connection);
            List<Company> companies = service.getWatchedCompanies(connection.getUserId());
            assert companies != null;
            assert companies.size() == 1;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @After
    public void delete() {
        operations.findAllAndRemove(new Query(), UserCompanyConnection.class);
        operations.findAllAndRemove(new Query(), Company.class);
    }
    
}
