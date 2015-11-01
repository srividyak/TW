package com.tapwisdom.service.api;

import com.tapwisdom.core.daos.documents.UserTimeLine;

import java.util.List;

public interface IUserFeedService {
    
    public List<UserTimeLine> getUserTimeLine(String userId, int page);
    
}
