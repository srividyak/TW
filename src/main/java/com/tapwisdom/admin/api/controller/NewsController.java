package com.tapwisdom.admin.api.controller;

import com.tapwisdom.service.api.INewsService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseAdminController {
    
    @Autowired
    private INewsService service;
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map add(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("news")) {
            String newsJson = (String) request.get("news");
            News news = Utils.getObjectFromString(newsJson, News.class);
            service.create(news);
        } else {
            throw new TapWisdomException(1, "news field is mandatory");
        }
        return getResponse(0);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map edit(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("news")) {
            String newsJson = (String) request.get("news");
            News news = Utils.getObjectFromString(newsJson, News.class);
            service.update(news);
        } else {
            throw new TapWisdomException(1, "news field is mandatory");
        }
        return getResponse(0);
    }
    
    @RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getNewsForCompany(@PathVariable("companyId") String companyId) throws TapWisdomException {
        List<News> newsList = service.getNews(companyId);
        Map response = new HashMap();
        response.put("news", newsList);
        return getResponse(0, response);
    }
    
}
