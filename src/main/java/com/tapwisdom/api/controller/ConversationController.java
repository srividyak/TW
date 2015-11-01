package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IConversationService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 26/04/15.
 */
@Controller
@RequestMapping("/conversation")
public class ConversationController extends BaseController {
    
    @Autowired
    private IConversationService conversationService;
    
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map createConversation(@RequestBody String request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        Conversation conversation = Utils.getObjectFromString(request, Conversation.class);
        String id = twUser.getId();
        conversation.setSenderId(id);
        if (conversation.getReceiverId() == null || conversation.getMessage() == null) {
            throw new TapWisdomException(1, "receiverId and message should not be null");
        } else {
            conversationService.createConversation(conversation);
        }
        Map response = new HashMap();
        response.put("conversationId", conversation.getConversationId());
        response.put("messageId", conversation.getMessage().getId());
        return getResponse(0, response);
    }
    
    @RequestMapping(value = "{conversationId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getConversation(@PathVariable String conversationId, 
                               @RequestParam("page") Integer page,
                               HttpSession session)
            throws TapWisdomException {
        Map map = new HashMap();
        List<Conversation> conversations;
        if (page == null) {
            conversations = conversationService.getConversation(conversationId);
        } else {
            conversations = conversationService.getConversation(conversationId, page);
        }
        map.put("conversations", conversations);
        return getResponse(0, map);
    }
    
    @RequestMapping(value = "{conversationId}/message/{messageId}", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public Map updateConversation(@PathVariable String conversationId, @PathVariable String messageId, @RequestBody String request) throws TapWisdomException {
        Map map = new HashMap();
        Conversation conversation = new Conversation();
        conversation.setConversationId(conversationId);
        Message message = Utils.getObjectFromString(request, Message.class);
        message.setId(messageId);
        conversation.setMessage(message);
        boolean isUpdated = conversationService.updateConversation(conversation);
        int statusCode = isUpdated ? 0 : 1;
        return getResponse(statusCode, map);
    }
    
}
