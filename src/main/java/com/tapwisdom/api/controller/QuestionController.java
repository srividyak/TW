package com.tapwisdom.api.controller;

import com.tapwisdom.service.api.IQuestionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by srividyak on 06/07/15.
 */
@Controller
@RequestMapping("/questions")
public class QuestionController extends BaseController {
    
    @Autowired
    private IQuestionService service;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map getQuestions(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("company")) {
            String companyJson = (String) request.get("company");
            Company company = Utils.getObjectFromString(companyJson, Company.class);
            Question[] questions = service.getQuestionsByCompany(company);
            Map map = new HashMap();
            map.put("questions", questions);
            return getResponse(0, map);
        } else {
            throw new TapWisdomException(1, "company is mandatory field to get questions");
        }
    }
    
}
