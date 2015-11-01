package com.tapwisdom.admin.api;

import com.tapwisdom.core.daos.documents.AdminUser;
import com.tapwisdom.core.daos.documents.AdminUserViewable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by srividyak on 03/07/15.
 */
public class TwAdminUser implements Serializable {
    
    private String id;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
