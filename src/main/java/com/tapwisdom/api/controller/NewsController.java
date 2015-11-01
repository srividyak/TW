package com.tapwisdom.api.controller;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.News;
import com.tapwisdom.service.api.INewsService;
import com.tapwisdom.service.entity.TwUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController {
    
    @Autowired
    private INewsService service;
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getNews(@RequestParam(value = "page", required = false) Integer page, HttpSession session) throws TapWisdomException {
        if (page == null) {
            page = 0;
        } else {
            page = page - 1;
        }
        TwUser twUser = getTwUser(session);
        List<News> newsList = service.getNewsForUser(twUser.getId(), page);
        Map map = new HashMap();
        map.put("news", newsList);
        return getResponse(0, map);
    }
}
