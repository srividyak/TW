package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUserCompanyConnectionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/company")
public class UserCompanyController extends BaseController {
    
    @Autowired
    private IUserCompanyConnectionService service;
    
    @RequestMapping(value = "/watchList", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map addToWatchList(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        if (request.containsKey("companyIds")) {
            String companyIdJson = (String) request.get("companyIds");
            String[] companyIds = Utils.getObjectFromString(companyIdJson, String[].class);
            service.addToWatchList(twUser.getId(), companyIds);
            return getResponse(0);
        } else {
            throw new TapWisdomException(1, "companyIds is a mandatory field & should be an array of strings");
        }
    }
    
    @RequestMapping(value = "/watchList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getWatchedCompanies(HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        List<Company> companies = service.getWatchedCompanies(twUser.getId());
        Map map = new HashMap();
        map.put("companies", companies);
        return getResponse(0, map);
    }
    
    @RequestMapping(value = "/inWatchList", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map isInWatchList(@RequestParam("companies") String[] companies, HttpSession session) 
            throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        Map isWatchList = service.isInWatchList(twUser.getId(), companies);
        return getResponse(0, isWatchList);
    }
    
}
