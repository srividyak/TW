package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.ICallService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CallDao;
import com.tapwisdom.core.daos.documents.Call;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by srividyak on 09/05/15.
 */
public abstract class AbstractCallServiceImpl implements ICallService {
    
    @Autowired
    protected CallDao callDao;
    
    @Override
    public List<Call> getAllCallsWithUser(TwUser twUser, String userId) throws TapWisdomException {
        List<Call> callsAsGiver = getCallsAsGiverWithUser(twUser, userId);
        List<Call> calls = getCallsAsSeekerWithUser(twUser, userId);
        calls.addAll(callsAsGiver);
        return calls;
    }

    @Override
    public List<Call> getAllCallsWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException {
        List<Call> callsAsGiver = getCallsAsGiverWithUser(twUser, userId, from);
        List<Call> calls = getCallsAsSeekerWithUser(twUser, userId, from);
        calls.addAll(callsAsGiver);
        return calls;
    }

    @Override
    public List<Call> getMyCalls(TwUser twUser) throws TapWisdomException {
        List<Call> calls = callDao.getMyCalls(twUser.getId());
        return calls;
    }

    @Override
    public List<Call> getMyCalls(TwUser twUser, Long from) throws TapWisdomException {
        List<Call> calls = callDao.getMyCalls(twUser.getId(), from);
        return calls;
    }
}
