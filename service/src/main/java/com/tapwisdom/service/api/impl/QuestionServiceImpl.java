package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.service.api.IQuestionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.QuestionDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Question;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionServiceImpl implements IQuestionService {

    private static final Logger LOG = Logger.getLogger(QuestionServiceImpl.class);
    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final HashSet<String> configTags = new HashSet<String>(Arrays.asList(reader.getProperty(Constants.QUESTION_TAGS, " ").split(",")));


    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CompanyDao companyDao;

    @Override
    public void createQuestion(Question question) throws TapWisdomException {
        questionDao.save(question);
    }

    @Override
    public Question[] getQuestionsByCompany(Company company) throws TapWisdomException {
        if (company.getId() == null && company.getName() != null) {
            company = companyDao.getCompanyByName(company.getName());
        } else if (company.getIndustry() == null) {
            company = companyDao.getById(company.getId(), Company.class);
        }
        if (company == null || company.getId() == null) {
            throw new TapWisdomException(1, "Invalid company passed");
        }
        Set<Question> questions = new HashSet<Question>();
        List<Question> questionsByCompany = questionDao.getQuestionsByCompany(company.getId());
        questions.addAll(questionsByCompany);
        if (company.getIndustry() != null) {
            String[] industries = {company.getIndustry()};
            List<Question> questionsByIndustry = questionDao.getQuestionsByIndustries(industries);
            questions.addAll(questionsByIndustry);
        }
        return questions.toArray(new Question[]{});
    }

    @Override
    public boolean updateQuestion(Question question) throws TapWisdomException {
        return questionDao.updateQuestion(question);
    }

    @Override
    public Map<String, List<Question>> getQuestionsByCompanyWithTags(Company company) throws TapWisdomException {
        Question[] questions = this.getQuestionsByCompany(company);
        Map<String, List<Question>> questionTagMap = new HashMap<String, List<Question>>();
        for (Question question : questions) {
            String questionTags[] = question.getTags().split(",");
            for (String questionTag : questionTags) {
                questionTag = questionTag.trim();
                if (configTags.contains(questionTag)) {
                    if (questionTagMap.containsKey(questionTag)) {
                        List<Question> tempQuestionsList = new ArrayList<Question>(questionTagMap.get(questionTag));
                        tempQuestionsList.add(question);
                        questionTagMap.put(questionTag, tempQuestionsList);
                    } else {
                        questionTagMap.put(questionTag, Arrays.asList(question));
                    }
                }
            }
        }
        return questionTagMap;
    }
}
