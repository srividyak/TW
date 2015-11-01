package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.service.api.IConversationService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.ConversationDao;
import com.tapwisdom.core.daos.documents.Conversation;
import com.tapwisdom.core.daos.documents.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by srividyak on 26/04/15.
 */
@Component
public class ConversationServiceImpl implements IConversationService {
    
    @Autowired
    private ConversationDao conversationDao;
    
    private String generateConversationId() {
        return Constants.CONVERSATION_ID_PREFIX + UUID.randomUUID().toString();
    }
    
    private String generateMessageId() {
        return Constants.MESSAGE_ID_PREFIX + UUID.randomUUID().toString();
    }
    
    @Override
    public void createConversation(Conversation conversation) throws TapWisdomException {
        // If new conversation initiated, conversationId will be null. Else create new message for same conversation
        if (conversation.getConversationId() == null) {
            conversation.setConversationId(generateConversationId());    
        }
        Message message = conversation.getMessage();
        message.setId(generateMessageId());
        conversationDao.save(conversation);
    }

    @Override
    public boolean updateConversation(Conversation conversation) throws TapWisdomException {
        return conversationDao.updateConversation(conversation);
    }

    @Override
    public List<Conversation> getConversation(String conversationId) throws TapWisdomException {
        return conversationDao.getConversation(conversationId, 0);
    }

    @Override
    public List<Conversation> getConversation(String conversationId, int page) throws TapWisdomException {
        if (page >= 1) {
            return conversationDao.getConversation(conversationId, page - 1);
        } else {
            throw new TapWisdomException(1, "page number has to be >= 1");
        }

    }
}
