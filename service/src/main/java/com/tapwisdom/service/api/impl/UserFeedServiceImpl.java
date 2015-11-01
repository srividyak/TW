package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.apis.UserTimeLineDao;
import com.tapwisdom.core.daos.documents.UserTimeLine;
import com.tapwisdom.service.api.IUserFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserFeedServiceImpl implements IUserFeedService {
    @Autowired
    private UserTimeLineDao timeLineDao;
    
    @Override
    public List<UserTimeLine> getUserTimeLine(String userId, int page) {
        List<UserTimeLine> timeLine = timeLineDao.getUserTimeLine(userId, page);
        return timeLine;
    }
}
