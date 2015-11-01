package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("NewsTapService")
public class NewsTapServiceImpl extends TapItServiceImpl<News>{
    @Autowired
    private EntityDao<News> entityDao;

    @Override
    protected EntityDao getEntityDao() {
        return entityDao;
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.NEWS;
    }
}
