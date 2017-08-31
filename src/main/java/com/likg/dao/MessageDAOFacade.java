package com.likg.dao;

import com.likg.dao.cassandraimpl.MessageCassandraDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lixu
 * @version 1.0
 * @created 15-6-18
 */
public abstract class MessageDAOFacade {

    protected static final Logger logger = LoggerFactory.getLogger(MessageDAOFacade.class);

    private static int isSwitch;

   // private static RedisMessageCacheService cacheService;

    public static MessageCassandraDAO getMessageDAO() {

        try {
            //MessageDAOFacade.cacheService = cacheService;
            return MessageCassandraDAO.getInstance();
        } catch (Exception e) {
            isSwitch = -1;
            throw new RuntimeException("illegal messageDao config, please check store.switch in config.properties");
        } finally {
            String engine;
            if (1 == isSwitch) {
                engine = "Hbase";
            } else if (0 == isSwitch) {
                engine = "Cassandra";
            } else {
                engine = "Error";
            }
            logger.info("now msgreader is on store engine-{}", engine);
        }
    }

    public static void switchStoreEngine(String storeCategory, String storeKeyspace) {

//        if (MessageDAOFacade.cacheService != null) {
//            int category = Integer.parseInt(storeCategory);
//            MessageDAO dao;
//            dao = MessageCassandraDAO.getInstance();
//
//            StoreEngineCache engine = StoreEngineCache.getInstance();
//            engine.setStoreKeyspace(storeKeyspace);
//            engine.setStoreCategory(storeCategory);
//            cacheService.setMessageDAO(dao);
//            logger.info("engine.getkeyspace = {}, engine.getStoreCategory = {}",engine.getStoreKeyspace(),engine.getStoreCategory());
//        } else {
//            logger.error("can't switch store engine, because of not init Facade complete.");
//        }
    }


}
