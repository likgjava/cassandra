package com.likg.dao.domain;

import java.nio.ByteBuffer;

/**
 * A wrapper for all kinds of messages
 */
public class Message {

    private long messageId;
    private int appId;



    private int toAppId;

    private long fromUID;
    private long toUID;
    private long groupUID;

    private boolean isRead;
    private boolean isAcked;

    public int getSvid() {
        return svid;
    }

    public void setSvid(int svid) {
        this.svid = svid;
    }

    private boolean isWebAcked;

    public void setAppAcked(boolean isAppAcked) {
        this.isAppAcked = isAppAcked;
    }

    private boolean isAppAcked;

    private int svid;
    private ByteBuffer messageBody;

    private long cts; // message create timestamp
    private byte deviceType;

    public byte getDeviceType() { return deviceType; }

    public void setDeviceType(byte deviceType) { this.deviceType = deviceType; }

    public int getToAppId() { return toAppId;}

    public void setToAppId(int toAppId) { this.toAppId = toAppId;}

    public boolean isAcked() {
        return isAcked;
    }

    public void setAcked(boolean acked) {
        this.isAcked = acked;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getToUID() {
        return toUID;
    }

    public void setToUID(long toUID) {
        this.toUID = toUID;
    }

    public long getFromUID() {
        return fromUID;
    }

    public void setFromUID(long fromUID) {
        this.fromUID = fromUID;
    }

    public long getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(long groupUID) {
        this.groupUID = groupUID;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public boolean isWebAcked() {
        return isWebAcked;
    }

    public void setWebAcked(boolean webAck) {
        this.isWebAcked = webAck;
    }

    public boolean isAppAcked() {
        return isAppAcked;
    }

    public void isAppAcked(boolean isAppAcked) {
        this.isAppAcked = isAppAcked;
    }

    public ByteBuffer getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(ByteBuffer messageBody) {
        this.messageBody = messageBody;
    }

    public long getCts() {
        return cts;
    }

    public void setCts(long cts) {
        this.cts = cts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Message{");
        sb.append("messageId=").append(messageId);
        sb.append(", appId=").append(appId);
        sb.append(", toAppId=").append(toAppId);
        sb.append(", fromUID=").append(fromUID);
        sb.append(", toUID=").append(toUID);
        sb.append(", groupUID=").append(groupUID);
        sb.append(", isRead=").append(isRead);
        sb.append(", isAcked=").append(isAcked);
        sb.append(", isWebAcked=").append(isWebAcked);
        sb.append(", isAppAcked=").append(isAppAcked);
        sb.append(", svid=").append(svid);
        sb.append(", messageBody=").append(messageBody);
        sb.append(", cts=").append(cts);
        sb.append(", deviceType=").append(deviceType);
        sb.append('}');
        return sb.toString();
    }
}
