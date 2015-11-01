package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.api.ITapItService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.UberEntity;
import org.apache.log4j.Logger;

import java.util.List;

public abstract class TapItServiceImpl<T extends UberEntity> implements ITapItService<T> {

    private static final Logger LOG = Logger.getLogger(TapItServiceImpl.class);
    
    protected abstract EntityDao getEntityDao();
    
    protected abstract EntityType getEntityType();

    @Override
    public List<T> getTaps(String userId, String folderName, int page) throws TapWisdomException {
        EntityDao entityDao = getEntityDao();
        EntityType type = getEntityType();
        return entityDao.getTappedEntities(userId, type, folderName, page);
    }

    @Override
    public List<T> getTaps(String userId, int page) throws TapWisdomException {
        EntityDao entityDao = getEntityDao();
        EntityType type = getEntityType();
        return entityDao.getTappedEntities(userId, type, page);
    }
    
    @Override
    public void addTap(T entity, String userId, String folderName) throws TapWisdomException {
        getEntityDao().tapEntity(entity, getEntityType(), userId, folderName);
    }

    @Override
    public void removeTap(T entity, String userId, String folderName) throws TapWisdomException {
        getEntityDao().unTapEntity(entity, getEntityType(), userId, folderName);
    }
}
