package com.likg.dao;

import com.likg.dao.cassandraimpl.MessageCassandraDAO;
import com.likg.dao.domain.Message;
import com.likg.dao.domain.MessageInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by likg on 2016-11-25.
 */
public class ClientTest {
    private static final Logger log = LoggerFactory.getLogger(ClientTest.class);


    @Test
    public void queryTest() {
        log.error("1111");
//        MessageCassandraDAO dao = MessageCassandraDAO.getInstance();
//
//        //Message boxMessage = dao.getMsgBoxMessage(495048769654493184L);
//        //System.out.println(boxMessage);
//
//
//        log.info("=====================================================1============================================");
//        log.info("=====================================================2============================================");
//        try {
//            long msgId = 497248304837431296L;
//            MessageInfo msgInfo = dao.getMsgInfo(msgId);
//            System.out.println(msgInfo);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

}
