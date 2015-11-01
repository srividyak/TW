package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Company;

import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 14/07/15.
 */
public interface IUserCompanyConnectionService {
    
    public void addToWatchList(String userId, String companyId) throws TapWisdomException;

    public void addToWatchList(String userId, String[] companyId) throws TapWisdomException;
    
    public List<Company> getWatchedCompanies(String userId) throws TapWisdomException;
    
    public Boolean isInWatchList(String userId, String companyId) throws TapWisdomException;

    public Map<String, Boolean> isInWatchList(String userId, String[] companyId) throws TapWisdomException;
    
}
