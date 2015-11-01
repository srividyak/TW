package com.tapwisdom.service.api;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;

import java.util.List;

/**
 * Created by srividyak on 27/04/15.
 */
public interface IUserConnectionService {
    
    public void viewUser(TwUser twUser, String userId) throws TapWisdomException;
    
    public void viewUsers(TwUser twUser, String[] userIds) throws TapWisdomException;

    public List<User> getAllConnections(TwUser twUser, int page) throws TapWisdomException;
    
}
