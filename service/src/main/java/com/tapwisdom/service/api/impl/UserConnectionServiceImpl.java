package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.common.util.Constants;
import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.IUserConnectionService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.PropertyReader;
import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.apis.UserDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.UberEntity;
import com.tapwisdom.core.daos.documents.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srividyak on 27/04/15.
 */
@Component
public class UserConnectionServiceImpl implements IUserConnectionService {

    @Autowired
    private EntityDao<UberEntity> entityDao;

    @Autowired
    private UserDao userDao;

    private static final PropertyReader reader = PropertyReader.getInstance();
    private static final Boolean shouldUpdateUserViewCount = Boolean.parseBoolean(reader.getProperty(Constants.UPDATE_USER_COUNT, "false"));

    @Override
    public void viewUser(TwUser twUser, String toUserId) throws TapWisdomException {
        User targetUser = new User();
        targetUser.setId(toUserId);
        entityDao.viewEntity(targetUser, EntityType.USER, twUser.getId());
    }

    @Override
    public void viewUsers(TwUser twUser, String[] toUserIds) throws TapWisdomException {
        for (String toUserId : toUserIds) {
            User targetUser = new User();
            targetUser.setId(toUserId);
            entityDao.viewEntity(targetUser, EntityType.USER, twUser.getId());
        }
    }

    @Override
    public List<User> getAllConnections(TwUser twUser, int page) throws TapWisdomException {
        List<UberEntity> viewees = entityDao.getViewedEntities(twUser.getId(), EntityType.USER, page);
        List<String> vieweeIds = new ArrayList<String>();
        for (UberEntity viewee : viewees) {
            vieweeIds.add(viewee.getId());
        }
        return userDao.getUsersById(vieweeIds);
    }
}
