package com.tapwisdom.api.controller;

import com.google.gson.reflect.TypeToken;
import com.tapwisdom.core.daos.apis.QnASessionDao;
import com.tapwisdom.service.api.IUserGamificationService;
import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IQnASessionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/qnaSession")
public class QnASessionController extends BaseController {

    @Autowired
    private IQnASessionService service;

    @Autowired
    private IUserGamificationService userGamificationService;

    @Autowired
    private QnASessionDao qnASessionDao;

    private static final Logger LOG = Logger.getLogger(QnASessionController.class);

    @RequestMapping(value = "/addQuestions", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map addQuestions(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String seekerId = twUser.getId();
        if (request.containsKey("questions") && request.containsKey("companyId")
                && request.containsKey("designation")) {
            String questionsJson = (String) request.get("questions");
            String companyId = (String) request.get("companyId");
            String designation = (String) request.get("designation");
            Question[] questions = Utils.getObjectFromString(questionsJson, Question[].class);
            List<QnA> qnAs = new ArrayList<QnA>();
            int i = 0;
            for (Question q : questions) {
                QnA qnA = new QnA();
                qnA.setQuestion(q);
                qnA.setQuestionId(i++);
                qnAs.add(qnA);
            }
            QnASession qnASession = service.addQuestionsBySeeker(qnAs, seekerId, companyId, designation);
            Map map = new HashMap();
            map.put("qnaSession", qnASession);
            return getResponse(0, map);
        } else {
            throw new TapWisdomException(1, "questions, companyId and designation fields are mandatory");
        }
    }

    @RequestMapping(value = "/submitToAdvisor", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map submitQuestionsToAdvisor(@RequestBody Map request) throws TapWisdomException {
        if (request.containsKey("advisors") && request.containsKey("qnaSessionId")) {
            String advisorsJson = (String) request.get("advisors");
            String[] advisors = Utils.getObjectFromString(advisorsJson, String[].class);
            String qnaSessionId = (String) request.get("qnaSessionId");
            service.sendQuestionsToAdvisors(qnaSessionId, advisors);
            //Gamification : call the tapBadgeupdate service
            userGamificationService.updateUserTapBadge(qnaSessionId);
            return getResponse(0);
        } else {
            throw new TapWisdomException(1, "advisors and qnaSessionId are mandatory fields");
        }
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map answerQuestions(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        if (request.containsKey("qnaList") && request.containsKey("qnaSessionId")) {
            Type type = new TypeToken<ArrayList<QnA>>() {
            }.getType();
            String qnaListJson = (String) request.get("qnaList");
            List<QnA> qnAList = Utils.getObjectFromString(qnaListJson, type);
            String qnaSessionId = (String) request.get("qnaSessionId");
            String advisorId = twUser.getId();
            qnaSessionId = service.answerQuestion(qnaSessionId, advisorId, qnAList);
            //Gamification : call the evaluateBadgeUpdate service
            userGamificationService.updateUserEvaluateBadge(qnaSessionId);
            return getResponse(0);
        } else {
            throw new TapWisdomException(1, "qnaList and qnaSessionId are mandatory fields");
        }
    }

    @RequestMapping(value = "/seeker/questions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getQuestionsBySeeker(@RequestParam(value = "page", required = false) Integer page,
                                    HttpSession session)
            throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        List<QnASession> qnASessions;
        String seekerId = twUser.getId();
        if (page != null) {
            qnASessions = service.getQuestionsAskedBySeeker(seekerId, page - 1);
        } else {
            qnASessions = service.getQuestionsAskedBySeeker(seekerId, 0);
        }
        Map map = new HashMap();
        map.put("qnaSessions", qnASessions);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/advisor/questions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getQuestionsByAdvisor(@RequestParam(value = "page", required = false) Integer page,
                                     HttpSession session)
            throws TapWisdomException {
        List<QnASession> qnASessions;
        TwUser twUser = getTwUser(session);
        String advisorId = twUser.getId();
        if (page != null) {
            qnASessions = service.getQuestionsDirectedToAdvisor(advisorId, page - 1);
        } else {
            qnASessions = service.getQuestionsDirectedToAdvisor(advisorId, 0);
        }
        Map map = new HashMap();
        map.put("qnaSessions", qnASessions);
        return getResponse(0, map);
    }

    @RequestMapping(value = "/{companyId}/questions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getQuestionsByCompany(@RequestParam(value = "page", required = false) Integer page,
                                     @RequestParam(value = "location", required = false) String location,
                                     @RequestParam(value = "designation", required = false) String designation,
                                     @PathVariable("companyId") String companyId) throws TapWisdomException {
        List<QnASession> qnASessions;
        if (page == null) {
            page = 0;
        } else {
            page--;
        }
        QnASession qnASession = new QnASession();
        qnASession.setCompanyId(companyId);
        qnASession.setCompanyLocation(location);
        qnASession.setDesignation(designation);
        qnASessions = service.getQuestionsByFilter(qnASession, page);
        Map map = new HashMap();
        map.put("qnaSessions", qnASessions);
        return getResponse(0, map);
    }

}
