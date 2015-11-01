package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.core.common.util.Utils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    private static final Logger LOG = Logger.getLogger(LoginInterceptor.class);

    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        LOG.debug("session id: " + session.getId());
        TwUser twUser = (TwUser) session.getAttribute("user");
        if(twUser == null) {
            LOG.debug("user not logged in");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            Map result = new HashMap();
            result.put("statusCode", 2);
            result.put("message", "Not authenticated to perform this action");
            response.getWriter().print(Utils.getStringFromObject(result));
            return false;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("user value: " + Utils.getStringFromObject(twUser));
            }
            LOG.debug("user logged in with id: " + twUser.getId());
            return true;
        }
    }

    @Override
    public void postHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
