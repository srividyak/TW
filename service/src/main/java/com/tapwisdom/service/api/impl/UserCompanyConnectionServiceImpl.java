package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.api.IUserCompanyConnectionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.UserCompanyConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 14/07/15.
 */
@Component
public class UserCompanyConnectionServiceImpl implements IUserCompanyConnectionService {
    
    @Autowired
    private UserCompanyConnectionDao dao;
    
    @Autowired
    private CompanyDao companyDao;
    
    @Override
    public void addToWatchList(String userId, String companyId) throws TapWisdomException {
        dao.addToWatchList(userId, companyId);
    }

    @Override
    public void addToWatchList(String userId, String[] companyIds) throws TapWisdomException {
        for (String companyId : companyIds) {
            dao.addToWatchList(userId, companyId);
        }
    }

    @Override
    public List<Company> getWatchedCompanies(String userId) throws TapWisdomException {
        List<UserCompanyConnection> userCompanyConnections = dao.getWatchedCompanies(userId);
        List<String> ids = new ArrayList<String>();
        for (UserCompanyConnection connection : userCompanyConnections) {
            ids.add(connection.getCompanyId());
        }
        List<Company> companies = companyDao.getByIds(ids, Company.class);
        return companies;
    }

    @Override
    public Boolean isInWatchList(String userId, String companyId) throws TapWisdomException {
        return dao.isInWatchList(userId, companyId);
    }

    @Override
    public Map<String, Boolean> isInWatchList(String userId, String[] companyIds) throws TapWisdomException {
        Map<String, Boolean> inWatchListMap = new HashMap<String, Boolean>();
        for (String companyId : companyIds) {
            inWatchListMap.put(companyId, dao.isInWatchList(userId, companyId));
        }
        return inWatchListMap;
    }
}
