package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;

/**
 * Created by sachin on 23/8/15.
 */
public interface ICreditMgtService {

    public int getCredits(String userId) throws TapWisdomException;

    public void addCredits(String userId, int credits) throws TapWisdomException;

    public void redeemCredits(String userId, int credits) throws TapWisdomException;

}
