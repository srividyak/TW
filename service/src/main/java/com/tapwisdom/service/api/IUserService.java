package com.tapwisdom.service.api;

import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.daos.documents.FacebookProfile;
import com.tapwisdom.core.daos.documents.GoogleProfile;
import com.tapwisdom.core.daos.documents.User;
import com.tapwisdom.core.daos.documents.UserSource;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.stereotype.Component;

import java.util.List;

public interface IUserService {
    
    public User createUser(User user) throws TapWisdomException;
    
    public Boolean updateUser(User user) throws TapWisdomException;
    
    public User getUser(String id) throws TapWisdomException;
    
}
