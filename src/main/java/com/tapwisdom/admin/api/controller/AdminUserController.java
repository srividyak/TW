package com.tapwisdom.admin.api.controller;

import com.tapwisdom.admin.api.TwAdminUser;
import com.tapwisdom.admin.api.service.IAdminUserService;
import com.tapwisdom.api.util.CommonUtils;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.AdminUser;
import com.tapwisdom.core.daos.documents.AdminUserViewable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srividyak on 03/07/15.
 */
@Controller
@RequestMapping("/adminuser")
public class AdminUserController extends BaseAdminController {
    
    @Autowired
    private IAdminUserService service;
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map login(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        if (isUserLoggedIn(session)) {
            throw new TapWisdomException(1, "Invalid access to API, user already logged in");
        } else {
            if (request.containsKey("email") && request.containsKey("password")) {
                String email = (String) request.get("email");
                String password = (String) request.get("password");
                AdminUser adminUser = service.validatePassword(email, password);
                // filter out sensitive data
                AdminUserViewable userViewable = CommonUtils.filterSensitiveData(adminUser);
                if (adminUser != null) {
                    TwAdminUser twAdminUser = CommonUtils.getTwAdminUser(adminUser);
                    session.setAttribute("admin_user", twAdminUser);
                    Map map = new HashMap();
                    map.put("admin_user", userViewable);
                    return getResponse(0, map);
                } else {
                    throw new TapWisdomException(1, "Invalid email or password passed");
                }
            } else {
                throw new TapWisdomException(1, "email and password are mandatory");
            }
        }
    }
    
    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map register(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        if (isUserLoggedIn(session)) {
            throw new TapWisdomException(1, "Invalid access to API, user already logged in");
        } else {
            if (request.containsKey("user") && request.containsKey("password")) {
                String userJson = (String) request.get("user");
                AdminUser adminUser = Utils.getObjectFromString(userJson, AdminUser.class);
                String password = (String) request.get("password");
                service.createAdminUser(adminUser, password);
                return getResponse(0);
            } else {
                throw new TapWisdomException(1, "user and password fields are mandatory");
            }
        }
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map logout(HttpSession session)  throws TapWisdomException {
        session.invalidate();
        return getResponse(0);
    }
    
    @RequestMapping(value = "/isLoggedIn", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map isLoggedIn(HttpSession session) {
        Boolean isLoggedIn = isUserLoggedIn(session);
        return getResponse(isLoggedIn ? 0 : 1);
    }
    
}
