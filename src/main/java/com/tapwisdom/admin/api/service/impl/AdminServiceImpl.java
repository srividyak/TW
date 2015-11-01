package com.tapwisdom.admin.api.service.impl;

import com.tapwisdom.admin.api.service.IAdminUserService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.AdminUserDao;
import com.tapwisdom.core.daos.documents.AdminUser;
import com.tapwisdom.core.daos.documents.AdminUserViewable;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Created by srividyak on 03/07/15.
 */
@Component
public class AdminServiceImpl implements IAdminUserService {
    
    @Autowired
    private AdminUserDao adminUserDao;

    private String getPasswordSalt(String email, String password) {
        int random = new Random().nextInt(999999);
        random = (random < 100000) ? random + 100000 : random;
        return DigestUtils.md5Hex(random + email);
    }

    private String getPasswordSha1(String salt, String password) {
        return DigestUtils.sha1Hex(salt + password);
    }
    
    @Override
    public AdminUser createAdminUser(AdminUser user, String password) throws TapWisdomException {
        if (user.getEmail() != null) {
            String passwordSalt = getPasswordSalt(user.getEmail(), password);
            String passwordSha1 = getPasswordSha1(passwordSalt, password);
            user.setPasswordSalt(passwordSalt);
            user.setPasswordSha1(passwordSha1);
            adminUserDao.save(user);
        } else {
            throw new TapWisdomException(1, "email mandatory for creating an admin user");
        }
        return user;
    }

    @Override
    public AdminUser getAdminUser(String id) throws TapWisdomException {
        AdminUser adminUser = adminUserDao.getAdminUser(id);
        return adminUser;
    }

    @Override
    public AdminUser updateAdminUser(AdminUser user) throws TapWisdomException {
        adminUserDao.updateAdminUser(user);
        return getAdminUser(user.getId());
    }

    @Override
    public AdminUser updateAdminUser(AdminUser user, String password) throws TapWisdomException {
        String passwordSalt = getPasswordSalt(user.getEmail(), password);
        String passwordSha1 = getPasswordSha1(passwordSalt, password);
        user.setPasswordSalt(passwordSalt);
        user.setPasswordSha1(passwordSha1);
        adminUserDao.updateAdminUser(user);
        return getAdminUser(user.getId());
    }

    @Override
    public AdminUser validatePassword(String email, String password) throws TapWisdomException {
        AdminUser adminUser = adminUserDao.getAdminUserByEmail(email);
        if (adminUser != null) {
            String passwordSalt = adminUser.getPasswordSalt();
            String passwordSha1 = adminUser.getPasswordSha1();
            boolean isValid = passwordSha1 != null ?
                    passwordSha1.equals(DigestUtils.sha1Hex(passwordSalt + password)) : false;
            return isValid ? adminUser : null;
        } else {
            throw new TapWisdomException(1, "No user exists matching email");
        }
    }
}
