package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Conversation;

import java.util.List;

/**
 * Created by srividyak on 26/04/15.
 */
public interface IConversationService {
    
    public void createConversation(Conversation conversation) throws TapWisdomException;
    
    public boolean updateConversation(Conversation conversation) throws TapWisdomException;
    
    public List<Conversation> getConversation(String conversationId) throws TapWisdomException;

    public List<Conversation> getConversation(String conversationId, int page) throws TapWisdomException;
}
