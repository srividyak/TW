package com.tapwisdom.api.util;

import com.tapwisdom.admin.api.TwAdminUser;
import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.service.entity.TwUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {
    
    public static UserView filterSensitiveData(User user) {
        user.setPasswordSalt(null);
        user.setPasswordSha1(null);
        user.setLastWrittenToIndexedStore(null);
        return (UserView) user;
    }
    
    public static AdminUserViewable filterSensitiveData(AdminUser user) {
        user.setPasswordSalt(null);
        user.setPasswordSha1(null);
        return (AdminUserViewable) user;
    }
    
    public static TwUser getTwUser(User user) {
        return getTwUser((UserView) user);
    }
    
    public static TwUser getTwUser(UserView userView) {
        TwUser twUser = new TwUser();
        twUser.setId(userView.getId());
        twUser.setEmail(userView.getEmail());
        return twUser;   
    }
    
    public static TwAdminUser getTwAdminUser(AdminUserViewable adminUser) {
        TwAdminUser twAdminUser = new TwAdminUser();
        twAdminUser.setEmail(adminUser.getEmail());
        twAdminUser.setId(adminUser.getId());
        return twAdminUser;
    }
    
    public static TwAdminUser getTwAdminUser(AdminUser adminUser) {
        return getTwAdminUser((AdminUserViewable) adminUser);
    }
    
}
