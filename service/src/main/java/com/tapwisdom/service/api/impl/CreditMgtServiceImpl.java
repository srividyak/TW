package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.apis.CreditMgtDao;
import com.tapwisdom.service.api.ICreditMgtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sachin on 23/8/15.
 */
@Component
public class CreditMgtServiceImpl implements ICreditMgtService {

    @Autowired
    private CreditMgtDao creditMgtDao;

    @Override
    public int getCredits(String userId) throws TapWisdomException {
        return creditMgtDao.getCredits(userId);
    }

    @Override
    public void addCredits(String userId, int credits) throws TapWisdomException {
        creditMgtDao.addCredits(userId,credits);
    }

    @Override
    public void redeemCredits(String userId, int credits) throws TapWisdomException {
        creditMgtDao.redeemCredits(userId,credits);
    }
}
