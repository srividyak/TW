package com.tapwisdom.api.controller;

import com.tapwisdom.api.util.CommonUtils;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.EntityViewable;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserTimeLine;
import com.tapwisdom.core.daos.documents.UserTimeLineEntityType;
import com.tapwisdom.service.api.IUserFeedService;
import com.tapwisdom.service.entity.TwUser;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller("/user/feed")
public class UserFeedController extends BaseController {
    private static final Logger LOG = Logger.getLogger(UserFeedController.class);
    
    @Autowired
    private IUserFeedService service;
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getFeed(@RequestParam(value = "page", required = false) Integer page, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        Map map = new HashMap();
        if (page == null) {
            page = 0;
        }
        List<UserTimeLine> timeLines = service.getUserTimeLine(twUser.getId(), page);
        for (UserTimeLine timeLine : timeLines) {
            // try catch for each feed since error in one feed should not affect other feed items
            try {
                if (timeLine.getEntityType() == UserTimeLineEntityType.USERS) {
                    UserTimeLine<EntityViewable<User>> userEntity = timeLine;
                    CommonUtils.filterSensitiveData(userEntity.getEntity().getEntityCharacteristics().getEntity());
                }
                String entityType = timeLine.getEntityType().toString();
                if (map.containsKey(entityType)) {
                    List<UserTimeLine> value = (List<UserTimeLine>) map.get(entityType);
                    value.add(timeLine);
                } else {
                    List<UserTimeLine> list = new ArrayList<UserTimeLine>();
                    list.add(timeLine);
                    map.put(entityType, list);
                }
            } catch (Exception e) {
                LOG.error("Error while filtering sensitive data", e);
            }
        }
        return getResponse(0, map);
    }
    
}
