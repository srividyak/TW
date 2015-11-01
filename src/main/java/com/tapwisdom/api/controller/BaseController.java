package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.core.common.exception.TapWisdomException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srividyak on 18/04/15.
 */
public abstract class BaseController {
    
    private static final Logger LOG = Logger.getLogger(BaseController.class);
    
    public Boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("user") != null;
    }
    
    public TwUser getTwUser(HttpSession session) throws TapWisdomException {
        if (session.getAttribute("user") != null) {
            TwUser twUser = (TwUser) session.getAttribute("user");
            return twUser;
        } else {
            throw new TapWisdomException(1, "Unable to get session details of logged in user");
        }
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map handleException(Exception e) {
        LOG.error("ERROR: " + e.getMessage(), e);
        return getResponse(1, "Error occurred in this API");
    }
    
    @ExceptionHandler(TapWisdomException.class)
    @ResponseBody
    public Map handleTapWisdomException(TapWisdomException e) {
        LOG.error("ERROR: " + e.getMessage(), e);
        return getResponse(1, e.getMessage());
    }

    protected Map getResponse(int statusCode, String message) {
        Map map = new HashMap();
        map.put("statusCode", statusCode);
        map.put("message", message);
        return map;
    }

    protected Map getResponse(int statusCode, Map<String, Object> fields) {
        fields.put("statusCode", statusCode);
        return fields;
    }
    
    protected Map getResponse(int statusCode) {
        Map response = new HashMap();
        response.put("statusCode", statusCode);
        return response;
    }
}
