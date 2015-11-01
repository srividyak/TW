package com.tapwisdom.admin.api.controller;

import com.tapwisdom.core.daos.documents.*;
import com.tapwisdom.service.api.IUserService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by srividyak on 03/07/15.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseAdminController {
    
    private static final Logger LOG = Logger.getLogger(UserController.class);

    @Autowired
    private IUserService service;

    /**
     * Assuming this is to add users as advisors only
     * @param request
     * @return
     * @throws TapWisdomException
     */
    @RequestMapping(value = "/add", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map addUser(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("linkedInUser")) {
            String userJson = (String) request.get("linkedInUser");
            LiProfile linkedInProfile = Utils.getObjectFromString(userJson, LiProfile.class);
            User user = new User();
            user.setStatus(UserStatus.unsubscribed);
            user.setSource(UserSource.linkedIn);
            user.setIsVerifiedAdvisor(true);
            user.setLinkedInProfile(linkedInProfile);
            user.setRole(UserRole.adviser);
            if (request.containsKey("phoneNumber")) {
                user.setPhoneNumber((String) request.get("phoneNumber"));
            }
            service.createUser(user);
            return getResponse(0);
        } else {
            throw new TapWisdomException(1, "linkedInUser field is mandatory");
        }
    }
}
