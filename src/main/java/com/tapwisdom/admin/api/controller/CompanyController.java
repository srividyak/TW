package com.tapwisdom.admin.api.controller;

import com.tapwisdom.service.api.ICompanyService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/companies")
public class CompanyController extends BaseAdminController {
    
    @Autowired
    private ICompanyService service;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompanies() throws TapWisdomException {
        List<Company> companies = service.getAllCompanies();
        Map map = new HashMap();
        map.put("companies", companies);
        return getResponse(0, map);
    }
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map addCompanies(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("companies")) {
            String companiesJson = (String) request.get("companies");
            Company[] companies = Utils.getObjectFromString(companiesJson, Company[].class);
            service.createCompanies(Arrays.asList(companies));
            return getResponse(0);
        } else {
            throw new TapWisdomException(1, "companies field is mandatory");
        }
    }

}
