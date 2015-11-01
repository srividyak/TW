package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CompanyDao;
import com.tapwisdom.core.daos.apis.QuestionDao;
import com.tapwisdom.core.daos.documents.Company;
import com.tapwisdom.core.daos.documents.Location;
import com.tapwisdom.core.daos.documents.Question;
import com.tapwisdom.core.daos.documents.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 07/07/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class QuestionServiceTest {

    @Autowired
    private IQuestionService service;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private MongoOperations operations;

    private Company company;

    private List<Question> questions;

    @Before
    public void createQuestion() {
        questions = new ArrayList<Question>();

        company = new Company();
        company.setIndustry("IT");
        Location location = new Location();
        location.setName("bangalore");
        company.setLocations(new Location[]{location});
        company.setName("tapwisdom");
        try {
            companyDao.save(company);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }

        Question question = new Question();
        question.setCompanyId(company.getId());
        question.setContent("What is the question?");
        question.setTags("culture");
        question.setType(QuestionType.TEXT);
        questions.add(question);

        question = new Question();
        question.setCompanyId("fakeCompany");
        question.setContent("What is the question?");
        question.setTags("culture,technology");
        question.setIndustries(new String[]{"IT"});
        question.setType(QuestionType.TEXT);
        questions.add(question);
        try {
            for (Question q : questions) {
                questionDao.save(q);
            }
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetQuestionsByCompanyWithExisting() {
        Company company = new Company();
        company.setId(this.company.getId());
        company.setIndustry(this.company.getIndustry());
        try {
            Question[] questions = service.getQuestionsByCompany(company);
            assert questions != null;
            assert questions.length == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test(expected = TapWisdomException.class)
    public void testGetQuestionsByCompanyWithNonExisting() throws TapWisdomException {
        Company company = new Company();
        company.setId("company2");
        Question[] questions = service.getQuestionsByCompany(company);
    }

    @Test
    public void testGetQuestionsByCompanyWithOnlyCompanyName() {
        Company company = new Company();
        company.setName(this.company.getName());
        try {
            Question[] questions = service.getQuestionsByCompany(company);
            assert questions != null;
            assert questions.length == 2;
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test(expected = TapWisdomException.class)
    public void testGetQuestionsByCompanyWithInvalidCompany() throws TapWisdomException {
        Company company = new Company();
        company.setName("invalidName");
        service.getQuestionsByCompany(company);
    }

    @Test
    public void testUpdateQuestion() {
        Question question = questions.get(0);
        Question newQuestion = new Question();
        String newQStr = "What is the culture like?";
        newQuestion.setId(question.getId());
        newQuestion.setContent(newQStr);
        try {
            boolean updated = service.updateQuestion(newQuestion);
            assert updated == true;
            assert ((String) newQuestion.getContent()).equals(newQStr);
        } catch (TapWisdomException e) {
            e.printStackTrace();
            assert false;
        }
    }

    @Test
    public void testGetQuestionsByCompanyWithTags() {
        Company company = new Company();
        company.setId(this.company.getId());
        company.setIndustry(this.company.getIndustry());
        try {
            Map<String, List<Question>> questions = service.getQuestionsByCompanyWithTags(company);
            assert questions != null;
            assert questions.get("technology").size() == 1;
            assert questions.get("culture").size() == 2;
        } catch (TapWisdomException e) {
            assert false;
        }
    }

    @After
    public void deleteAllQuestions() {
        Query query = new Query();
        operations.findAllAndRemove(query, Question.class);
        operations.findAllAndRemove(query, Company.class);
    }

}
