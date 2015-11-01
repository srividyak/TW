package com.tapwisdom.api.controller;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.QnAEntity;
import com.tapwisdom.service.api.ICreditMgtService;
import com.tapwisdom.service.entity.TwUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sachin on 23/8/15.
 */

@Controller
@RequestMapping("/credit")
public class CreditMgtController extends BaseController {

    @Autowired
    private ICreditMgtService creditMgtService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getCredits(HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        Map response = new HashMap();
        int credits = creditMgtService.getCredits(userId);
        response.put("credits",credits);
        return getResponse(0, response);
    }

    @RequestMapping(value = "/redeem/{credits}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map redeemCredits(@PathVariable("credits") int credits,HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        Map response = new HashMap();
        creditMgtService.redeemCredits(userId,credits);
        return getResponse(0, response);
    }

}
