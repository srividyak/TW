package com.tapwisdom.service.entity;

import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserView;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by srividyak on 19/04/15.
 */
public class TwUser implements Serializable {
    
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
