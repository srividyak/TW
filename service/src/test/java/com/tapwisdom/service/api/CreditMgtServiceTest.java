package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.QnA;
import com.tapwisdom.core.daos.documents.QnASession;
import com.tapwisdom.core.daos.documents.Question;
import com.tapwisdom.core.daos.documents.QuestionType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sachin on 26/8/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:daosAppContext_test.xml"
})
public class CreditMgtServiceTest {

        @Autowired
        private ICreditMgtService creditMgtService;

        @Autowired
        private MongoOperations operations;


        @Test
        public void testAddCredits(){
                try {
                        creditMgtService.addCredits("advisor",50);
                        int credits = creditMgtService.getCredits("advisor");
                        assert credits == 50;

                } catch (TapWisdomException e) {
                        e.printStackTrace();
                        assert false;
                }
        }

        @Test
        //test for less than 300 and multiples of 50
        public void testInvalidRedeemCredits(){
                try {
                        creditMgtService.redeemCredits("advisor",100);
                        assert false;
                }catch (TapWisdomException e) {
                        assert true;
                }
                try {
                        creditMgtService.redeemCredits("advisor", 350);
                        assert false;
                }catch (TapWisdomException e) {
                        assert true;
                }
        }

        @Test

        public void testValidRedeemCredits(){
                try {
                        creditMgtService.addCredits("advisor",50);
                        creditMgtService.redeemCredits("advisor",100);
                        int currentCredits = creditMgtService.getCredits("advisor");
                        assert currentCredits == 0;
                }catch (TapWisdomException e) {
                        e.printStackTrace();
                        assert false;
                }
        }


}
