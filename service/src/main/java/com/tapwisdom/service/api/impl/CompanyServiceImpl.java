package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.documents.Location;
import com.tapwisdom.service.api.ICompanyService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.documents.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CompanyServiceImpl implements ICompanyService {
    
    @Autowired
    private CompanyDao companyDao;
    
    @Override
    public List<Company> getCompanies(int page) throws TapWisdomException {
        return companyDao.getAllCompanies(page);
    }

    @Override
    public List<Company> getAllCompanies() throws TapWisdomException {
        return companyDao.getAllCompanies();
    }

    /**
     * *
     * @param companies
     * @return count of companies created
     * @throws TapWisdomException
     */
    @Override
    public void createCompanies(List<Company> companies) throws TapWisdomException {
        for (Company company : companies) {
            Company existingCompany = companyDao.getCompanyByName(company.getName());
            if (existingCompany == null) {
                companyDao.save(company);
            } else {
                Location[] locations = existingCompany.getLocations();
                Set<Location> locationSet = new HashSet<Location>();
                if (locations != null) {
                    for (Location location : locations) {
                        locationSet.add(location);
                    }
                }
                for (Location location : company.getLocations()) {
                    locationSet.add(location);
                }
                existingCompany.setLocations((Location[]) locationSet.toArray(new Location[locationSet.size()]));
                companyDao.updateCompany(existingCompany);
            }
        }
    }

    @Override
    public List<Company> getCompaniesByLocation(String location, int page) throws TapWisdomException {
        return companyDao.getCompaniesByLocation(location, page);
    }

    @Override
    public List<Company> getCompaniesByIndustry(String industry, int page) throws TapWisdomException {
        return companyDao.getCompaniesByIndustry(industry, page);
    }

    @Override
    public Company getCompanyById(String id) throws TapWisdomException {
        return companyDao.getById(id, Company.class);
    }

    @Override
    public Company getCompanyByName(String name) throws TapWisdomException {
        return companyDao.getCompanyByName(name);
    }

}
