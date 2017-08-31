package com.likg.dao.domain;

/**
 * @author zhanghao
 * @version 1.0
 * @created 15/5/28
 */
public class MessageInfo {

    private long msgid;
    private long toUID;
    private int toAppId;

    public long getMsgid() {
        return msgid;
    }

    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }

    public long getToUID() {
        return toUID;
    }

    public void setToUID(long toUID) {
        this.toUID = toUID;
    }

    public int getToAppId() {
        return toAppId;
    }

    public void setToAppId(int toAppId) {
        this.toAppId = toAppId;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MessageInfo{");
        sb.append("msgid=").append(msgid);
        sb.append(", toUID=").append(toUID);
        sb.append(", toAppId=").append(toAppId);
        sb.append('}');
        return sb.toString();
    }
}
