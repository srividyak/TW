package com.tapwisdom.service.api;

import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.Call;

import java.util.List;

/**
 * Created by srividyak on 09/05/15.
 */
public interface ICallService {
    
    public List<Call> getCallsAsGiverWithUser(TwUser twUser, String userId) throws TapWisdomException;

    public List<Call> getCallsAsGiverWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException;
    
    public List<Call> getCallsAsSeekerWithUser(TwUser twUser, String userId) throws TapWisdomException;

    public List<Call> getCallsAsSeekerWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException;
    
    public List<Call> getAllCallsWithUser(TwUser twUser, String userId) throws TapWisdomException;

    public List<Call> getAllCallsWithUser(TwUser twUser, String userId, Long from) throws TapWisdomException;
    
    public List<Call> getMyCalls(TwUser twUser) throws TapWisdomException;
    
    public List<Call> getMyCalls(TwUser twUser, Long to) throws TapWisdomException;
    
    public void createCall(Call call) throws TapWisdomException;
    
    public Boolean updateCall(Call call) throws TapWisdomException;
}
