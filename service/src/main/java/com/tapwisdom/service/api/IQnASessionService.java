package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.QnA;
import com.tapwisdom.core.daos.documents.QnAEntity;
import com.tapwisdom.core.daos.documents.QnASession;

import java.util.List;

public interface IQnASessionService {
    
    public List<QnASession> getQuestionsAskedBySeeker(String seeker, int page) throws TapWisdomException;
    
    public List<QnASession> getQuestionsDirectedToAdvisor(String advisor, int page) throws TapWisdomException;
    
    public List<QnASession> getQuestionsAskedAboutCompany(String companyId, int page) throws TapWisdomException;

    public void sendQuestionsToAdvisors(String qnaSessionId, String[] advisorIds) throws TapWisdomException;
    
    public String answerQuestion(String qnaSessionId, String advisorId, List<QnA> qnAList) throws TapWisdomException;
    
    public List<QnASession> getQuestionsByFilter(QnASession qnASession, int page) throws TapWisdomException;
    
    public QnASession addQuestionsBySeeker(List<QnA> qnAList, String seekerId, String companyId,
                                           String designation) throws TapWisdomException;

}
