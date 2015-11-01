package com.tapwisdom.api.controller;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.ICallService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.Call;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by srividyak on 09/05/15.
 */
@Controller
@RequestMapping("/call")
public class CallController extends BaseController {
    
    @Autowired
    private ICallService service;
    
    private static final Logger LOG = Logger.getLogger(CallController.class);
    
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map create(@RequestBody String request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        Call call = Utils.getObjectFromString(request, Call.class);
        //assuming seeker creates a call always
        LOG.debug("logged in user's id: " + twUser.getId());
        call.setSeekerId(twUser.getId());
        service.createCall(call);
        Map response = new HashMap();
        response.put("callId", call.getId());
        response.put("conferenceId", call.getConferenceId());
        return getResponse(0, response);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map update(@RequestBody String request) throws TapWisdomException {
        Call call = Utils.getObjectFromString(request, Call.class);
        Boolean updated = service.updateCall(call);
        int statusCode = updated ? 0 : 1;
        return getResponse(statusCode);
    }
    
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getMyCalls(@RequestParam(required = false, value = "from") Long from, HttpSession session)
            throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        List<Call> myCalls;
        if (from != null) {
            myCalls = service.getMyCalls(twUser, from);
        } else {
            myCalls = service.getMyCalls(twUser);
        }
        Map response = new HashMap();
        response.put("calls", myCalls);
        return getResponse(0, response);
    }
    
    @RequestMapping(value = "{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map getAllCallsWithUser(@PathVariable("userId") String userId, 
                                   @RequestParam(required = false, value = "from") Long from,
                                   HttpSession session) 
            throws TapWisdomException {
        List<Call> calls;
        TwUser twUser = getTwUser(session);
        if (from != null) {
            calls = service.getAllCallsWithUser(twUser, userId, from);
        } else {
            calls = service.getAllCallsWithUser(twUser, userId);
        }
        Map response = new HashMap();
        response.put("calls", calls);
        return getResponse(0, response);
    }

}
