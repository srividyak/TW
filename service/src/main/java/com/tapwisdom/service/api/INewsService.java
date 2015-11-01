package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.News;

import java.util.List;

public interface INewsService {
    
    public void create(News news) throws TapWisdomException;
    
    public boolean update(News news) throws TapWisdomException;
    
    public List<News> getNews(String company) throws TapWisdomException;

    public List<News> getNews(String company, int page) throws TapWisdomException;

    public void upVote(News news, String userId);
    
    public List<News> getNewsForUser(String userId, int page) throws TapWisdomException;
}
