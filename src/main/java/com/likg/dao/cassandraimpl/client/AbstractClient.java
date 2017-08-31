package com.likg.dao.cassandraimpl.client;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.likg.dao.cassandraimpl.ClusterBuilder;
import com.likg.dao.cassandraimpl.SessionBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lixusign on 15-2-3.
 * Client抽象
 */
public abstract class AbstractClient implements CQLConfig {

    /**
     * Cassandra config
     */
    private static class ClientConfig {
        public static final String HotKeySpaceName = "xmmsg";
        public static final String UserName = "";
        public static final String Password = "";
        public static final String[] address = "192.168.1.212,192.168.1.213,192.168.1.215".split(","); //
    }

    private static Cluster cluster = ClusterBuilder.buildCluster(ClientConfig.UserName, ClientConfig.Password, ClientConfig.address);

    private static Session sessionHot = SessionBuilder.buildSession(cluster, ClientConfig.HotKeySpaceName);

    public static Session getSessionHot() {
        return sessionHot;
    }

    protected static Map<String, PreparedStatement> mapHot = new ConcurrentHashMap<String, PreparedStatement>();

    protected static Map<String, PreparedStatement> mapCold = new ConcurrentHashMap<String, PreparedStatement>();

    static {
//        mapHot.put(HOT.SELECT_ONE_MESSAGE, getSessionHot().prepare(HOT.SELECT_ONE_MESSAGE));
        mapHot.put(HOT.SELECT_MSG_INFO, getSessionHot().prepare(HOT.SELECT_MSG_INFO));
//        mapHot.put(HOT.SELECT_MSG_INDEX_CTS_HISTORY, getSessionHot().prepare(HOT.SELECT_MSG_INDEX_CTS_HISTORY));
//        mapHot.put(HOT.SELECT_MSG_INDEX_MSGID_HISTORY, getSessionHot().prepare(HOT.SELECT_MSG_INDEX_MSGID_HISTORY));
//        mapHot.put(HOT.SELECT_GROUPMSG_INDEX_CTS_HISTORY, getSessionHot().prepare(HOT.SELECT_GROUPMSG_INDEX_CTS_HISTORY));
//        mapHot.put(HOT.SELECT_GROUPMSG_INDEX_MSGID_HISTORY, getSessionHot().prepare(HOT.SELECT_GROUPMSG_INDEX_MSGID_HISTORY));
//        mapHot.put(HOT.DEL_MSGINDEXBYCTS, getSessionHot().prepare(HOT.DEL_MSGINDEXBYCTS));
//        mapHot.put(HOT.DEL_GROUPMSGINDEXBYCTS, getSessionHot().prepare(HOT.DEL_GROUPMSGINDEXBYCTS));
//        mapHot.put(HOT.DEL_GROUPMSGINDEXBYMSGID, getSessionHot().prepare(HOT.DEL_GROUPMSGINDEXBYMSGID));
//        mapHot.put(HOT.DEL_MSGINDEXBYMSGID, getSessionHot().prepare(HOT.DEL_MSGINDEXBYMSGID));
//        mapHot.put(HOT.DEL_MSGINFO, getSessionHot().prepare(HOT.DEL_MSGINFO));
//        mapHot.put(HOT.DEL_MSGBOX, getSessionHot().prepare(HOT.DEL_MSGBOX));
        //mapCold.put(COLD.SELECT_HIS,getSessionHot().prepare(COLD.SELECT_HIS));
    }

    public static Map<String, PreparedStatement> getHotMap() {
        return mapHot;
    }

}


