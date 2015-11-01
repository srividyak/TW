package com.tapwisdom.api.controller;

import com.tapwisdom.service.api.IUserGamificationService;
import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUpVoteService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.News;
import com.tapwisdom.core.daos.documents.QnAEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/upvote")
public class UpVoteController extends BaseController {
    
    @Autowired
    @Qualifier("NewsUpVoteService")
    private IUpVoteService newsUpVoteService;
    
    @Autowired
    @Qualifier("QnAUpVoteService")
    private IUpVoteService qnAUpVoteService;

    @Autowired
    private IUserGamificationService gamificationService;
    
    private void upVoteQnA(Map request, String userId) throws TapWisdomException {
        if (request.containsKey("questionId") && request.containsKey("qnaSessionId")) {
            Integer questionId = (Integer) request.get("questionId");
            String qnaSessionId = (String) request.get("qnaSessionId");
            QnAEntity qnAEntity = new QnAEntity(qnaSessionId, questionId);
            qnAUpVoteService.upVoteEntity(qnAEntity, userId);
            //Gamification : call upvoteBadgeUpdate service
            gamificationService.updateUserUpVoteBadge(qnAEntity);
        } else {
            throw new TapWisdomException(1, "questionId & qnaSessionId are mandatory fields");
        }
    }
    
    private void upVoteNews(Map request, String userId) throws TapWisdomException {
        if (request.containsKey("newsId")) {
            String newsId = (String) request.get("newsId");
            News news = new News();
            news.setId(newsId);
            newsUpVoteService.upVoteEntity(news, userId);
        } else {
            throw new TapWisdomException(1, "newsId is mandatory field");
        }
    }
    
    @RequestMapping(value = "{type}", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map upVoteEntity(@PathVariable("type") EntityType type, @RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        if (type == EntityType.NEWS) {
            upVoteNews(request, userId);
        } else if (type == EntityType.QNA) {
            upVoteQnA(request, userId);
        } else {
            throw new TapWisdomException(1, "Invalid entity to tap");
        }
        return getResponse(0);
    }
    
}
