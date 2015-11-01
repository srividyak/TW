package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Company;

import java.util.List;

public interface ICompanyService {
    
    public List<Company> getCompanies(int page) throws TapWisdomException;
    
    public List<Company> getAllCompanies() throws TapWisdomException;
    
    public void createCompanies(List<Company> companies) throws TapWisdomException;

    public List<Company> getCompaniesByLocation(String location , int page) throws TapWisdomException;

    public List<Company> getCompaniesByIndustry(String industry, int page) throws TapWisdomException;

    public Company getCompanyById(String id) throws TapWisdomException;
    
    public Company getCompanyByName(String name) throws TapWisdomException;

}
