package com.tapwisdom.api.controller;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUserService;
import com.tapwisdom.api.util.CommonUtils;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.*;
import org.apache.log4j.Logger;
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
 * Created by srividyak on 18/04/15.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;
    
    private static final Logger LOG = Logger.getLogger(UserController.class);
    
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map login(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        if (isUserLoggedIn(session)) {
            throw new TapWisdomException(1, "Invalid access to API, user already logged in");
        } else {
            if (request.containsKey(Constants.SOURCE)) {
                String source = (String) request.get(Constants.SOURCE);
                UserSource userSource = Enum.valueOf(UserSource.class, source);
                User user = new User();
                user.setSource(userSource);
                user.setStatus(UserStatus.active);
                if (userSource == UserSource.facebook) {
                    String fbProfileJson = (String) request.get("facebookProfile");
                    FacebookProfile facebookProfile = Utils.getObjectFromString(fbProfileJson, FacebookProfile.class);
                    user.setFacebookProfile(facebookProfile);
                } else if (userSource == UserSource.google) {
                    String gProfileJson = (String) request.get("googleProfile");
                    GoogleProfile googleProfile = Utils.getObjectFromString(gProfileJson, GoogleProfile.class);
                    user.setGoogleProfile(googleProfile);
                } else if (userSource == UserSource.linkedIn) {
                    String lProfileJson = (String) request.get("linkedInProfile");
                    LiProfile linkedInProfile = Utils.getObjectFromString(lProfileJson, LiProfile.class);
                    user.setLinkedInProfile(linkedInProfile);
                } else {
                    throw new TapWisdomException(1, "Invalid user source passed");
                }
                user = userService.createUser(user);
                UserView userView = CommonUtils.filterSensitiveData(user);
                // store minimal info in session
                session.setAttribute("user", CommonUtils.getTwUser(userView));
                Map map = new HashMap();
                map.put("user", userView);
                return getResponse(0, map);
            } else {
                throw new TapWisdomException(1, "No source passed");
            }
        }
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map logout(HttpSession session) throws TapWisdomException {
        session.invalidate();
        return getResponse(0);
    }
    
    @RequestMapping(value = "/edit", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map update(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        Map response = new HashMap();
        TwUser twUser = getTwUser(session);
        User user = new User();
        user.setId(twUser.getId());
        if (request.containsKey("user")) {
            String userJson = (String) request.get("user");
            user = Utils.getObjectFromString(userJson, User.class);
            user.setId(twUser.getId());
            Boolean update = userService.updateUser(user);
            user = userService.getUser(user.getId());
            UserView userView = CommonUtils.filterSensitiveData(user);
            session.setAttribute("user", twUser);
            response.put("success", update);
            response.put("user", userView);
            return getResponse(update ? 0 : 1, response);
        } else {
            throw new TapWisdomException(1, "user field is mandatory");
        }
    }
    
    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public Map delete(HttpSession session) throws TapWisdomException {
        Map response = new HashMap();
        TwUser twUser = getTwUser(session);
        int statusCode = 0;
        User user = new User();
        user.setId(twUser.getId());
        user.setStatus(UserStatus.del);
        if (userService.updateUser(user)) {
            response.put("success", true);
        } else {
            response.put("success", false);
            statusCode = 1;
        }
        return getResponse(statusCode, response);
    }

}
