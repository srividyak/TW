package com.tapwisdom.admin.api.controller;

import com.tapwisdom.admin.api.TwAdminUser;
import com.tapwisdom.api.controller.BaseController;
import com.tapwisdom.core.common.exception.TapWisdomException;

import javax.servlet.http.HttpSession;

/**
 * Created by srividyak on 03/07/15.
 */
public class BaseAdminController extends BaseController {

    public Boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("admin_user") != null;
    }
    
    public TwAdminUser getTwAdminUser(HttpSession session) throws TapWisdomException {
        if (session.getAttribute("admin_user") != null) {
            return (TwAdminUser) session.getAttribute("admin_user");
        } else {
            throw new TapWisdomException(1, "Unable to get session details of logged in admin user");
        }
    }
}
