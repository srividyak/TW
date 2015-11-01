package com.tapwisdom.admin.api.controller;

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
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 06/07/15.
 */
@Controller
@RequestMapping("/questions")
public class QuestionController extends BaseAdminController {

    @Autowired
    private IQuestionService service;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map createQuestion(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("questions")) {
            String questionsJson = (String) request.get("questions");
            Question[] questions = Utils.getObjectFromString(questionsJson, Question[].class);
            for (Question question : questions) {
                service.createQuestion(question);
            }
        } else {
            throw new TapWisdomException(1, "questions is a mandatory field");
        }
        return getResponse(0);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map updateQuestion(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("question")) {
            String questionJson = (String) request.get("question");
            Question question = Utils.getObjectFromString(questionJson, Question.class);
            boolean updated = service.updateQuestion(question);
            return getResponse(updated ? 0 : 1);
        } else {
            throw new TapWisdomException(1, "question is a mandatory field");
        }
    }

    @RequestMapping(value = "/company", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Map getQuestions(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("company")) {
            String companyJson = (String) request.get("company");
            Company company = Utils.getObjectFromString(companyJson, Company.class);
            //Map<String, List<Question>> questions=service.getQuestionsByCompanyWithTags(company);
            Question[] questions = service.getQuestionsByCompany(company);
            Map map = new HashMap();
            map.put("questions", questions);
            return getResponse(0, map);
        } else {
            throw new TapWisdomException(1, "company is mandatory field to get questions");
        }
    }

}
