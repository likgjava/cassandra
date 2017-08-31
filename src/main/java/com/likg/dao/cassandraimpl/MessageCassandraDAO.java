package com.likg.dao.cassandraimpl;

import com.likg.dao.cassandraimpl.client.BoundClient;
import com.likg.dao.cassandraimpl.client.CQLConfig;
import com.likg.dao.domain.Message;
import com.likg.dao.domain.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lixusign on 15-1-20.
 */
public class MessageCassandraDAO {

    private static final Logger log = LoggerFactory.getLogger(MessageCassandraDAO.class);

    private static volatile MessageCassandraDAO instance = new MessageCassandraDAO();

    private MessageCassandraDAO() {
    }

    public static MessageCassandraDAO getInstance() {
        return instance;
    }

    private BoundClient boundClient = new BoundClient();

    private static String hotdata = "msgbox";

    private static final long MAX_TIME_MILS = 60 * 24 * 60 * 60 * 1000L;

    private final static long twepoch = 1361753741828L;


    public Message getMsgBoxMessage(long msgid) {
        System.out.println("getMsgBoxMessage msgid:"+msgid);
        Object[] value = new Object[]{msgid};
        Message box = null;
        MessageInfo msgInfo = null;
        try {
            msgInfo = boundClient.fetchMsgInfo(BoundClient.getHotMap().get(CQLConfig.HOT.SELECT_MSG_INFO), value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            value = new Object[]{msgInfo.getToUID(), msgInfo.getToAppId(), msgInfo.getMsgid()};
            box = boundClient.fetchOneMsg(BoundClient.getHotMap().get(CQLConfig.HOT.SELECT_ONE_MESSAGE), value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return box;
    }

    public MessageInfo getMsgInfo(long msgid) throws Exception {
        System.out.println("getMsgBoxMessage msgid:"+msgid);
        Object[] value = new Object[]{msgid};
        Message box = null;
        MessageInfo msgInfo = boundClient.fetchMsgInfo(BoundClient.getHotMap().get(CQLConfig.HOT.SELECT_MSG_INFO), value);

        return msgInfo;
    }
}
