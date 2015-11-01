package com.tapwisdom.service.api.impl;

import com.tapwisdom.core.daos.apis.EntityDao;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.QnAEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("QnAUpVoteService")
public class QnAUpVoteServiceImpl extends UpVoteServiceImpl<QnAEntity> {
    
    @Autowired
    private EntityDao<QnAEntity> entityDao;
    
    @Override
    protected EntityDao getEntityDao() {
        return entityDao;
    }

    @Override
    protected EntityType getEntityType() {
        return EntityType.QNA;
    }
}
