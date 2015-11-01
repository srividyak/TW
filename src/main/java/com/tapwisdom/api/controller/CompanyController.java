package com.tapwisdom.api.controller;

import com.tapwisdom.core.daos.documents.QnASession;
import com.tapwisdom.service.api.ICompanyService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/companies")
public class CompanyController extends BaseController {
    
    @Autowired
    private ICompanyService service;
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompanies(@RequestParam(value = "page", required = false) Integer page) throws TapWisdomException {
        List<Company> companies;
        if (page != null) {
            companies = service.getCompanies(page);
        }else {
            companies = service.getCompanies(0);
        }

        Map map = new HashMap();
        map.put("companies", companies);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/location/{location}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompaniesByLocation(@RequestParam(value = "page", required = false) Integer page,
                                     @PathVariable("location") String location) throws TapWisdomException {
        List<Company> companies;
        if (page != null) {
            companies = service.getCompaniesByLocation(location, page - 1);
        } else {
            companies = service.getCompaniesByLocation(location, 0);
        }
        Map map = new HashMap();
        map.put("companies", companies);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/industry/{industry}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompaniesByIndustry(@RequestParam(value = "page", required = false) Integer page,
                                      @PathVariable("industry") String industry) throws TapWisdomException {
        List<Company> companies;
        if (page != null) {
            companies = service.getCompaniesByIndustry(industry, page - 1);
        } else {
            companies = service.getCompaniesByIndustry(industry, 0);
        }
        Map map = new HashMap();
        map.put("companies", companies);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/name/{name}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompaniesByName(@PathVariable("name") String name) throws TapWisdomException {
        Map map = new HashMap();
        Company company = service.getCompanyByName(name);
        map.put("company", company);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCompanyById(@PathVariable("id") String id) throws TapWisdomException {
        Map map = new HashMap();
        Company company = service.getCompanyById(id);
        map.put("company", company);
        return getResponse(0, map);
    }
    
}
