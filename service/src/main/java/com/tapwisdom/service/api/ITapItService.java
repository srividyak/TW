package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UberEntity;

import java.util.List;

public interface ITapItService<T extends UberEntity> {

    List<T> getTaps(String userId, String folderName, int page) throws TapWisdomException;

    List<T> getTaps(String userId, int page) throws TapWisdomException;
    
    void addTap(T entity, String userId, String folderName) throws TapWisdomException;

    void removeTap(T entity, String userId, String folderName) throws TapWisdomException;
}
