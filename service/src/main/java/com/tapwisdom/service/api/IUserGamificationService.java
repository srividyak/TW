package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.QnAEntity;
import com.tapwisdom.core.daos.documents.QnASession;

/**
 * Created by shravan on 25/8/15.
 */
public interface IUserGamificationService {
    public void updateUserUpVoteBadge(QnAEntity qnaEntity) throws TapWisdomException;

    public void updateUserEvaluateBadge(QnASession qnASession) throws TapWisdomException;

    public void updateUserEvaluateBadge(String qnASessionId) throws TapWisdomException;

    public void updateUserTapBadge(QnASession qnASession) throws TapWisdomException;

    public void updateUserTapBadge(String qnASessionId) throws  TapWisdomException;

}
