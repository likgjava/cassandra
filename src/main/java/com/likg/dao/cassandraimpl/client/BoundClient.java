package com.likg.dao.cassandraimpl.client;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;
import com.likg.dao.domain.Message;
import com.likg.dao.domain.MessageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lixusign on 15-2-3.
 * 处理PrepareStatement
 */
public class BoundClient extends AbstractClient {

    private static final Logger log = LoggerFactory.getLogger(BoundClient.class);

    private static final int QUERYTIMEOUT = 1000;

    private static final String PROJECT_PREFIX = "msg.reader";


    public boolean execute(PreparedStatement statement, Object[] value) throws Exception {

        boolean isException = false;
        try {
            BoundStatement boundStatement = new BoundStatement(statement);
            getSessionHot().executeAsync(boundStatement.bind(value));
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
            isException = true;
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
            isException = true;
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
            isException = true;
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
            isException = true;
        } catch (Exception e) {
            throw e;
        } finally {
            if (isException) {
                log.error("lost message , parameter = {}", value);
                //重试也挂了，我应该存储下来，稍后完善
            }
        }
        return true;
    }

    public Message fetchOneMsg(PreparedStatement statement, Object[] value) throws Exception {

        try {
            BoundStatement boundStatement = new BoundStatement(statement);
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            Message msgbox = new Message();
            for (Row row : r) {
                msgbox.setFromUID(row.getLong("from_uid"));
                msgbox.setToUID(row.getLong("to_uid"));
                msgbox.setGroupUID(row.getLong("group_uid"));
                msgbox.setAppId(row.getInt("app_id"));
                msgbox.setToAppId(row.getInt("to_appid"));
                msgbox.setSvid(row.getInt("svid"));
                msgbox.setMessageId(row.getLong("msgid"));
                msgbox.setCts(row.getLong("cts"));
                msgbox.setMessageBody(row.getBytes("msg"));
                break;
            }
            return msgbox;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    /**
     * 从msginfo表中获取from_uid和
     * @param statement
     * @param value
     * @return
     * @throws Exception
     */
    public MessageInfo fetchMsgInfo(PreparedStatement statement, Object[] value) throws Exception {

       try {
            BoundStatement boundStatement = new BoundStatement(statement);
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            MessageInfo msgInfo = new MessageInfo();
            for (Row row : r) {
                msgInfo.setMsgid(row.getLong("msgid"));
                msgInfo.setToAppId(row.getInt("to_appid"));
                msgInfo.setToUID(row.getLong("to_uid"));
                break;
            }
            return msgInfo;
        } catch (NoHostAvailableException e) {
           log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
           log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
           log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
        } catch (IllegalStateException e) {
           log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
           throw e;
        }
        return null;
    }

    public List<Message> fetchMsgList(PreparedStatement statement, Object[] value) throws Exception {

        try {
            BoundStatement boundStatement = new BoundStatement(statement);
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            List<Message> result = new ArrayList<Message>();
            for (Row row : r) {
                Message msgbox = new Message();
                msgbox.setMessageBody(row.getBytes("msg"));
                msgbox.setCts(row.getLong("cts"));
                msgbox.setAppId(row.getInt("app_id"));
                msgbox.setToAppId(row.getInt("to_appid"));
                msgbox.setFromUID(row.getLong("from_uid"));
                msgbox.setToUID(row.getLong("to_uid"));
                msgbox.setAcked(row.getInt("acked") == 1 ? true : false);
                msgbox.setRead(row.getInt("read") == 1 ? true : false);
                msgbox.setGroupUID(row.getLong("group_uid"));
                msgbox.setMessageId(row.getLong("msgid"));
                msgbox.setWebAcked(row.getInt("web_ack") == 1 ? true : false);
                msgbox.setAppAcked(row.getInt("app_ack") == 1 ? true : false);
                msgbox.setDeviceType((byte)row.getInt("device_type"));
                msgbox.setSvid(row.getInt("svid"));
                result.add(msgbox);
            }
            return result;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public List<Message> fetchMsgListUseIn(String CQL, Object[] value) throws Exception {

        try {
            BoundStatement boundStatement = new BoundStatement(getSessionHot().prepare(CQL));
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            List<Message> result = new ArrayList<Message>();
            for (Row row : r) {
                Message msgbox = new Message();
                msgbox.setMessageBody(row.getBytes("msg"));
                msgbox.setCts(row.getLong("cts"));
                msgbox.setAppId(row.getInt("app_id"));
                msgbox.setToAppId(row.getInt("to_appid"));
                msgbox.setFromUID(row.getLong("from_uid"));
                msgbox.setToUID(row.getLong("to_uid"));
                msgbox.setAcked(row.getInt("acked") == 1 ? true : false);
                msgbox.setRead(row.getInt("read") == 1 ? true : false);
                msgbox.setGroupUID(row.getLong("group_uid"));
                msgbox.setMessageId(row.getLong("msgid"));
                msgbox.setWebAcked(row.getInt("web_ack") == 1 ? true : false);
                msgbox.setAppAcked(row.getInt("app_ack") == 1 ? true : false);
                msgbox.setDeviceType((byte)row.getInt("device_type"));
                msgbox.setSvid(row.getInt("svid"));
                result.add(msgbox);
            }
            return result;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", CQL);
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public List<Message> fetchMsgList(String CQL) throws Exception {

        try {
            SimpleStatement statement = new SimpleStatement(CQL);
            ResultSetFuture future = getSessionHot().executeAsync(statement);
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            List<Message> result = new ArrayList<Message>();
            for (Row row : r) {
                Message msgbox = new Message();
                msgbox.setMessageBody(row.getBytes("msg"));
                msgbox.setCts(row.getLong("cts"));
                msgbox.setAppId(row.getInt("app_id"));
                msgbox.setToAppId(row.getInt("to_appid"));
                msgbox.setFromUID(row.getLong("from_uid"));
                msgbox.setToUID(row.getLong("to_uid"));
                msgbox.setGroupUID(row.getLong("group_uid"));
                msgbox.setMessageId(row.getLong("msgid"));
                msgbox.setSvid(row.getInt("svid"));
                result.add(msgbox);
            }
            return result;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", CQL);
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public List<Message> fetchMsgListUseInNew(String CQL, Object[] value) throws Exception {

        try {
            BoundStatement boundStatement = new BoundStatement(getSessionHot().prepare(CQL));
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            List<Message> result = new ArrayList<Message>();
            for (Row row : r) {
                Message msgbox = new Message();
                msgbox.setMessageBody(row.getBytes("msg"));
                msgbox.setCts(row.getLong("cts"));
                msgbox.setAppId(row.getInt("app_id"));
                msgbox.setToAppId(row.getInt("to_appid"));
                msgbox.setFromUID(row.getLong("from_uid"));
                msgbox.setToUID(row.getLong("to_uid"));
                msgbox.setGroupUID(row.getLong("group_uid"));
                msgbox.setMessageId(row.getLong("msgid"));
                msgbox.setSvid(row.getInt("svid"));
                result.add(msgbox);
            }
            return result;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", CQL);
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }


    public List<Long> fetchMsgidList(PreparedStatement statement, Object[] value) throws Exception {

        try {
            List<Long> list = new ArrayList<Long>();
            BoundStatement boundStatement = new BoundStatement(statement);
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
            for (Row row : r) {
                Long msgid = row.getLong("msgid");
                list.add(msgid);
            }
            return list;
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
        return null;
    }

    public void delMsg(PreparedStatement statement, Object[] value) throws Exception {
        try {
            BoundStatement boundStatement = new BoundStatement(statement);
            ResultSetFuture future = getSessionHot().executeAsync(boundStatement.bind(value));
            ResultSet r = future.getUninterruptibly(QUERYTIMEOUT, TimeUnit.MILLISECONDS);
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
        } catch (QueryValidationException e) {
            log.error("The query {} is not valid, for example, incorrect syntax.", statement.getQueryString());
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
        } catch (Exception e) {
            throw e;
        }
    }

    private String getMetricsName(String name) {
        return new StringBuilder().append(PROJECT_PREFIX).append(".dao.").append(name).toString();
    }
}
