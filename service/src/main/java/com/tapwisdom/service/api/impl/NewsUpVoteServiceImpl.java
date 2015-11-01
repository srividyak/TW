package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.apis.NewsDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("NewsUpVoteService")
public class NewsUpVoteServiceImpl extends UpVoteServiceImpl<News> {
    
    @Autowired
    private EntityDao<News> newsEntityDao;
    
    @Autowired
    private NewsDao newsDao;
    
    @Override
    protected EntityDao getEntityDao() {
        return newsEntityDao;
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.NEWS;
    }
    
    @Override
    public void upVoteEntity(News entity, String userId) throws TapWisdomException {
        News news = newsDao.getById(entity.getId(), News.class);
        newsEntityDao.upVoteEntity(news, EntityType.NEWS, userId);
    }
}
