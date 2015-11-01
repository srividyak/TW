package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Call;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by srividyak on 09/05/15.
 */
@Component
public class CallServiceImpl extends AbstractCallServiceImpl {
    
    @Override
    public List<Call> getCallsAsGiverWithUser(TwUser twUser, String userId) throws TapWisdomException {
        List<Call> calls = callDao.getCalls(twUser.getId(), userId);
        return calls;
    }

    @Override
    public List<Call> getCallsAsGiverWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException {
        List<Call> calls = callDao.getCalls(twUser.getId(), userId, from);
        return calls;
    }

    @Override
    public List<Call> getCallsAsSeekerWithUser(TwUser twUser, String userId) throws TapWisdomException {
        List<Call> calls = callDao.getCalls(userId, twUser.getId());
        return calls;
    }

    @Override
    public List<Call> getCallsAsSeekerWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException {
        List<Call> calls = callDao.getCalls(userId, twUser.getId(), from);
        return calls;
    }

    @Override
    public void createCall(Call call) throws TapWisdomException {
        callDao.save(call);
    }

    @Override
    public Boolean updateCall(Call call) throws TapWisdomException {
        return callDao.updateCall(call);
    }

}
