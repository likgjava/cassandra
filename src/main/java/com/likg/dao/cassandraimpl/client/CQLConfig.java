package com.likg.dao.cassandraimpl.client;


/**
 * Created by lixusign on 15-1-23.
 * CQL-Constants
 */
public interface CQLConfig {

    /**
     * Hot data CQL
     */
    interface HOT {

        String hotdata = "msgbox";


        /*从消息实体表中获取一条消息*/
        public static final String SELECT_ONE_MESSAGE = "SELECT * FROM "
                + hotdata + " WHERE to_uid = ? and to_appid = ? and msgid = ?";

        /*根据msgid获取from_uid和to_uid*/
        public static final String SELECT_MSG_INFO = "select * from msginfo "
                + " where msgid = ?";

        /*根据msgid获取单聊消息历史记录*/
        public static final String SELECT_MSG_INDEX_MSGID_HISTORY = "select msgid from msgindexbymsgid where from_uid = ? and to_uid = ? and "
                + " app_id = ? and to_appid = ? and svid = ? and msgid < ? limit ? allow filtering";

        /*根据cts获取单聊消息历史记录*/
        public static final String SELECT_MSG_INDEX_CTS_HISTORY = "select msgid from msgindexbycts where from_uid = ? and to_uid = ? and "
                + " app_id = ? and to_appid = ? and svid = ? and cts < ? limit ? allow filtering";

        /*根据cts获取群聊消息历史记录*/
        public static final String SELECT_GROUPMSG_INDEX_CTS_HISTORY = "select msgid from groupmsgindexbycts where to_uid = ? and "
                + " app_id = ? and svid = ? and cts < ? limit ? allow filtering";

        /*根据msgid获取群聊消息历史记录*/
        public static final String SELECT_GROUPMSG_INDEX_MSGID_HISTORY = "select msgid from groupmsgindexbymsgid where to_uid = ? and "
                + " app_id = ? and svid = ? and msgid < ? limit ? allow filtering";

        /**
         * --------------撤销消息----------------
         */
        /*根据msgid获取消息信息
        public static final String SELECT_MSGBOX_INFO = "select * from msgbox where msgid = ? ";*/

        /*删除单聊消息msgindexbycts*/
        public static final String DEL_MSGINDEXBYCTS = "delete from msgindexbycts where from_uid = ? and to_uid= ? "
                + "and app_id= ? and to_appid = ? and svid = ? and cts = ?";

        /*删除群聊消息groupmsgindexbycts*/
        public static final String DEL_GROUPMSGINDEXBYCTS = "delete from groupmsgindexbycts where to_uid= ? "
                + " and app_id= ? and svid = ? and cts = ?";

        /*删除群聊消息groupmsgindexbymsgid*/
        public static final String DEL_GROUPMSGINDEXBYMSGID = "delete from groupmsgindexbymsgid where to_uid= ? "
                + " and app_id= ? and svid = ? and msgid = ?";

        /*删除单聊消息msgindexbymsgid*/
        public static final String DEL_MSGINDEXBYMSGID = "delete from msgindexbymsgid where from_uid = ? "
                + "and to_uid= ? and app_id= ? and to_appid = ? and svid = ? and msgid= ?";
        /*删除消息信息 msginfo*/
        public static final String DEL_MSGINFO = "delete from msginfo where msgid = ?";

        /*删除消息信息 MSGBOX*/
        public static final String DEL_MSGBOX = "delete from msgbox where msgid = ? and to_uid = ? and to_appid = ?";

    }




}
