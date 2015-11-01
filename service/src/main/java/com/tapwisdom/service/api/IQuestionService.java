package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Question;

import java.util.List;
import java.util.Map;

public interface IQuestionService {
    
    public void createQuestion(Question question) throws TapWisdomException;

    public Question[] getQuestionsByCompany(Company company) throws TapWisdomException;
    
    public boolean updateQuestion(Question question) throws TapWisdomException;

    Map<String, List<Question>> getQuestionsByCompanyWithTags(Company company) throws TapWisdomException;
    
}
