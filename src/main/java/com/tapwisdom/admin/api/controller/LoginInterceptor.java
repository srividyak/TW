package com.tapwisdom.admin.api.controller;

import com.tapwisdom.admin.api.TwAdminUser;
import com.tapwisdom.api.util.CommonUtils;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.AdminUser;
import com.tapwisdom.core.daos.documents.AdminUserViewable;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by srividyak on 03/07/15.
 */
public class LoginInterceptor implements HandlerInterceptor {
    
    private static final Logger LOG = Logger.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        LOG.debug("session id: " + session.getId());
        TwAdminUser twAdminUser = (TwAdminUser) session.getAttribute("admin_user");
        if (LOG.isDebugEnabled()) {
            LOG.debug("user value: " + Utils.getStringFromObject(twAdminUser));
        }
        if(twAdminUser == null) {
            LOG.debug("user not logged in");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map result = new HashMap();
            result.put("statusCode", 2);
            result.put("message", "Not authenticated to perform this action");
            response.getWriter().print(Utils.getStringFromObject(result));
            return false;
        } else {
            LOG.debug("user logged in with id: " + twAdminUser.getId());
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
