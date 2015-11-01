package com.tapwisdom.api.controller;

import com.tapwisdom.service.api.ISearchService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UserView;
import com.tapwisdom.core.es.UserSearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 23/07/15.
 */
@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {
    
    @Autowired
    private ISearchService searchService;
    
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map searchUsers(@RequestParam(value = "companyName") String companyName,
                           @RequestParam(value = "companyLocation") String companyLocation,
                           @RequestParam(value = "designation") String designation,
                           @RequestParam(value = "industry") String industry,
                           @RequestParam(value = "page", required = false) Integer page) throws TapWisdomException {
        UserSearchCriteria criteria = new UserSearchCriteria().setCompanyName(companyName)
                .setDesignation(designation)
                .setIndustry(industry)
                .setLocation(companyLocation);
        page = (page == null) ? 0 : page;
        List<UserView> users = searchService.search(criteria, page);
        Map map = new HashMap();
        map.put("users", users);
        return getResponse(0, map);
    }
    
}
