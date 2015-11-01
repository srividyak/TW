package com.tapwisdom.admin.api.service;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.AdminUser;
import com.tapwisdom.core.daos.documents.AdminUserViewable;

/**
 * Created by srividyak on 03/07/15.
 */
public interface IAdminUserService {
    
    public AdminUser createAdminUser(AdminUser user, String password) throws TapWisdomException;
    
    public AdminUser getAdminUser(String id) throws TapWisdomException;
    
    public AdminUser updateAdminUser(AdminUser user) throws TapWisdomException;

    public AdminUser updateAdminUser(AdminUser user, String password) throws TapWisdomException;
    
    public AdminUser validatePassword(String email, String password) throws TapWisdomException;
}
