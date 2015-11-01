package com.tapwisdom.service.api.impl;

import com.tapwisdom.service.api.ISearchService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.UserView;
import com.tapwisdom.core.es.UserSearchCriteria;
import com.tapwisdom.core.es.repositories.IUserSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchServiceImpl implements ISearchService {
    
    @Autowired
    private IUserSearchRepository userSearchRepository;
    
    @Override
    public List<UserView> search(UserSearchCriteria criteria, int page) throws TapWisdomException {
        List<com.tapwisdom.core.es.documents.User> esUsers = userSearchRepository.getUsers(criteria, page);
        List<UserView> users = new ArrayList<UserView>();
        for (com.tapwisdom.core.es.documents.User esUser : esUsers) {
            users.add(esUser.getUserView());
        }
        return users;
    }
}
