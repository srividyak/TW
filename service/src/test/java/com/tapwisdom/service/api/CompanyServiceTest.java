package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Location;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class CompanyServiceTest {
    
    @Autowired
    private ICompanyService service;
    
    @Autowired
    private CompanyDao companyDao;

    private List<Company> companies;
    
    @Autowired
    private MongoOperations operations;

    @Before
    public void createCompanies() {
        companies = new ArrayList<Company>();
        
        // company 1
        Company company = new Company();
        company.setIndustry("software");
        Location location = new Location();
        location.setName("bangalore");
        company.setLocations(new Location[]{location});
        company.setName("tapwisdom");
        company.setNumEmployees(4L);
        company.setWebsite("http://tapwisdom.com");
        companies.add(company);
        
        // company 2
        company = new Company();
        company.setIndustry("IT");
        location = new Location();
        location.setName("mumbai");
        company.setLocations(new Location[]{location});
        company.setName("swerve");
        company.setNumEmployees(2L);
        company.setWebsite("http://theswerve.net");
        companies.add(company);
        
        for (Company c : companies) {
            try {
                companyDao.save(c);
            } catch (TapWisdomException e) {
                e.printStackTrace();
                assert false;
            }
        }
    }
    
    @Test
    public void testCreateCompaniesWithDifferentName() {
        Company company = new Company();
        Location location = new Location();
        location.setName("Mountain View");
        company.setLocations(new Location[]{location});
        company.setIndustry("software");
        company.setName("google");
        company.setNumEmployees(30000L);
        company.setWebsite("http://google.com");
        List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        try {
            service.createCompanies(companies);
            List<Company> companyList = companyDao.getAllCompanies();
            assert companyList.size() == 3;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testCreateCompaniesWithDifferentIndustry() {
        Company company = new Company();
        company.setIndustry("IT");
        company.setName("tapwisdom");
        Location location = new Location();
        location.setName("bangalore");
        company.setLocations(new Location[]{location});
        company.setNumEmployees(30000L);
        company.setWebsite("http://tapwisdom.com");
        List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        try {
            service.createCompanies(companies);
            List<Company> companyList = companyDao.getAllCompanies();
            assert companyList.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testCreateCompaniesWithSameName() {
        Company company = new Company();
        company.setIndustry("software");
        company.setName("tapwisdom");
        Location location = new Location();
        location.setName("Mountain View");
        company.setLocations(new Location[]{location});
        company.setNumEmployees(4L);
        company.setWebsite("http://tapwisdom.com");
        List<Company> companies = new ArrayList<Company>();
        companies.add(company);
        try {
            service.createCompanies(companies);
            List<Company> companyList = companyDao.getAllCompanies();
            assert companyList.size() == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }
    
    @Test
    public void testGetAllCompanies() {
        try {
            List<Company> companyList = service.getCompanies(0);
            assert companyList != null;
            assert companyList.size() == 2;
            assert companyList.get(0).getName().equals("tapwisdom");
            assert companyList.get(1).getName().equals("swerve");
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }

    }
    
    @After
    public void deleteCompanies() {
        Query query = new Query();
        operations.findAllAndRemove(query, Company.class);
    }
    
}
