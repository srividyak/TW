package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.api.IUpVoteService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.UberEntity;

import java.util.List;

public abstract class UpVoteServiceImpl<T extends UberEntity> implements IUpVoteService<T> {

    protected abstract EntityDao getEntityDao();

    protected abstract EntityType getEntityType();
    
    @Override
    public void upVoteEntity(T entity, String userId) throws TapWisdomException {
        EntityDao entityDao = getEntityDao();
        entityDao.upVoteEntity(entity, getEntityType(), userId);
    }

    @Override
    public List getUpVotedEntities(String userId, int page) throws TapWisdomException {
        EntityDao entityDao = getEntityDao();
        EntityType type = getEntityType();
        return entityDao.getUpVotedEntities(userId, type, page);
    }
}
