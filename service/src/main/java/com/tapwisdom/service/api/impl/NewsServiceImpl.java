package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.UserCompanyConnectionDao;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.service.api.INewsService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.apis.NewsDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class NewsServiceImpl implements INewsService {
    
    private static final Logger LOG = Logger.getLogger(NewsServiceImpl.class);
    
    @Autowired
    private NewsDao newsDao;
    
    @Autowired
    private EntityDao<News> entityDao;
    
    @Autowired
    private UserCompanyConnectionDao userCompanyConnectionDao;
    
    @Autowired
    private CompanyDao companyDao;
    
    @Override
    public void create(News news) throws TapWisdomException {
        newsDao.save(news);
    }

    @Override
    public boolean update(News news) throws TapWisdomException {
        news.setUpdatedAt(new Date().getTime());
        return newsDao.updateNews(news);
    }

    @Override
    public List<News> getNews(String company) throws TapWisdomException {
        return getNews(company, 0);
    }

    @Override
    public List<News> getNews(String company, int page) throws TapWisdomException {
        return newsDao.getNewsList(company, page);
    }

    @Override
    public void upVote(News news, String userId) {
        entityDao.upVoteEntity(news, EntityType.NEWS, userId);
    }

    @Override
    public List<News> getNewsForUser(String userId, int page) throws TapWisdomException {
        List<UserCompanyConnection> userCompanyConnections = userCompanyConnectionDao.getWatchedCompanies(userId);
        List<String> companyIds = new ArrayList<>();
        for (UserCompanyConnection connection : userCompanyConnections) {
            companyIds.add(connection.getCompanyId());
        }
        List<News> newsList = newsDao.getNewsList(companyIds, page);
        List<String> newsIds = new ArrayList<>();
        for (News news : newsList) {
            newsIds.add(news.getId());
        }
        Map<String, Company> map = companyDao.getIdsToObjectMap(companyIds, Company.class);
        for (News news : newsList) {
            Company[] companies = news.getCompanies();
            for (int i = 0; i < companies.length; i++) {
                Company company = companies[i];
                String companyId = company.getId();
                if (map.containsKey(companyId)) {
                    company = map.get(companyId);
                    LOG.debug(company.getName() + " <= company name");
                }
                companies[i] = company;
            }
            news.setCompanies(companies);
        }
        return newsList;
    }
}
