
package com.tapwisdom.api.controller;

import com.tapwisdom.core.daos.documents.News;
import com.tapwisdom.service.entity.TwUser;
import com.tapwisdom.service.api.ITapItService;
import com.tapwisdom.core.common.exception.TapWisdomException;
import com.tapwisdom.core.common.util.Utils;
import com.tapwisdom.core.daos.documents.EntityType;
import com.tapwisdom.core.daos.documents.QnAEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/tap")
public class TapItController extends BaseController {

    @Autowired
    @Qualifier("QnATapService")
    private ITapItService qnaTapService;
    
    @Autowired
    @Qualifier("NewsTapService")
    private ITapItService newsTapService;

    private static final Logger LOG = Logger.getLogger(QnASessionController.class);

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map tapQna(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        if (request.containsKey("type") && request.containsKey("entity") && request.containsKey("folder")) {
            EntityType type = EntityType.valueOf((String) request.get("type"));
            String entityJson = (String) request.get("entity");
            String folder = (String) request.get("folder");
            if (type == EntityType.QNA) {
                QnAEntity qnAEntity = Utils.getObjectFromString(entityJson, QnAEntity.class);
                qnaTapService.addTap(qnAEntity, userId, folder);
            } else if (type == EntityType.NEWS) {
                News news = Utils.getObjectFromString(entityJson, News.class);
                newsTapService.addTap(news, userId, folder);
            } else {
                throw new TapWisdomException(1, "Invalid entity to tap");
            }
        } else {
            throw new TapWisdomException(1, "type, entity and folder are mandatory parameters");
        }
        return getResponse(0);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map unTapQna(@RequestBody Map request, HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        if (request.containsKey("type") && request.containsKey("entity") && request.containsKey("folder")) {
            EntityType type = EntityType.valueOf((String) request.get("type"));
            String entityJson = (String) request.get("entity");
            String folder = (String) request.get("folder");
            if (type == EntityType.QNA) {
                QnAEntity qnAEntity = Utils.getObjectFromString(entityJson, QnAEntity.class);
                qnaTapService.removeTap(qnAEntity, userId, folder);
            } else {
                throw new TapWisdomException(1, "Invalid entity to tap");
            }
        } else {
            throw new TapWisdomException(1, "type, entity and folder are mandatory parameters");
        }
        return getResponse(0);
    }

    @RequestMapping(value = "/{type}/folder/{folderName}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public Map getQnaTaps(@PathVariable("type") EntityType type, @PathVariable("folderName") String folder,
                          HttpSession session) throws TapWisdomException {
        TwUser twUser = getTwUser(session);
        String userId = twUser.getId();
        Map response = new HashMap();
        if (type == EntityType.QNA) {
            List<QnAEntity> qnAEntities = qnaTapService.getTaps(userId, folder, 0);
            response.put("entities", qnAEntities);
            return getResponse(0, response);
        } else if (type == EntityType.NEWS) {
            List<News> newsEntities = newsTapService.getTaps(userId, folder, 0);
            response.put("entities", newsEntities);
            return getResponse(0, response);
        } else {
            throw new TapWisdomException(1, "Invalid entity type passed");
        }
    }
}

