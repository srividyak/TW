package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UberEntity;

import java.util.List;

public interface IUpVoteService<T extends UberEntity> {
    
    public void upVoteEntity(T entity, String userId) throws TapWisdomException;
    
    public List<T> getUpVotedEntities(String userId, int page) throws TapWisdomException;
    
}
