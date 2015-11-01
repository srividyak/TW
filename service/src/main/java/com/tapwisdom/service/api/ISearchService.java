package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UserView;
import com.tapwisdom.core.es.UserSearchCriteria;

import java.util.List;

public interface ISearchService {

    public List<UserView> search(UserSearchCriteria criteria, int page) throws TapWisdomException;
}
