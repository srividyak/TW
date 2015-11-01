package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUserConnectionService;
import com.tapwisdom.api.util.CommonUtils;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/connection")
public class UserConnectionController extends BaseController {

    @Autowired
    private IUserConnectionService service;
    
    @RequestMapping(value = "/view", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map view(@RequestParam("userId") String[] userIds, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        service.viewUsers(twUser, userIds);
        return getResponse(0);
    }
    
    @RequestMapping(value = "/views", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getAllViewedProfiles(HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        List<User> users = service.getAllConnections(twUser, 0);
        List<UserView> userViews = new ArrayList<UserView>();
        for (User user : users) {
            userViews.add(CommonUtils.filterSensitiveData(user));
        }
        Map response = new HashMap();
        response.put("views", userViews);
        return getResponse(0, response);
    }
}
