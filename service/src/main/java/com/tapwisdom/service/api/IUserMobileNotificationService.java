package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;

public interface IUserMobileNotificationService {
    void requestAnswer(String seekerId, String advisorId) throws TapWisdomException;
    
    void questionAnswered(String seekerId, String advisorId) throws TapWisdomException;
    
    void qnaUpVoted(String userId, String qnaSessionId, String questionId) throws TapWisdomException;
    
    void creditsAdded(String userId, Double credits) throws TapWisdomException;
    
    void creditsDeducted(String userId, Double credits) throws TapWisdomException;
}
