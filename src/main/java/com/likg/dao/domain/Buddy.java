package com.likg.dao.domain;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;

/**
 * Buddy in a conversation
 */
public class Buddy {

    private static final int byteLen = 12;

    private long uid;

    private short appId;

    private long jts; // Only be useful when uid is a group


    public Buddy(long uid, short appId) {
        this.uid = uid;
        this.appId = appId;
    }

    public long getUID() {
        return uid;
    }

    public void setUID(long uid) {
        this.uid = uid;
    }

    public short getAppId() {
        return appId;
    }

    public void setAppId(short appId) {
        this.appId = appId;
    }

    public void setJts(long jts) {
        this.jts = jts;
    }

    public long getJts() {
        return jts;
    }

    public static byte[] toByteArr(Buddy buddy) {
        Preconditions.checkNotNull(buddy);

        ByteBuffer buf = ByteBuffer.allocate(byteLen);
        buf.putLong(buddy.uid);
        buf.putShort(buddy.appId);
        return buf.array();
    }

    public static Buddy fromByteArr(byte[] arr) {
        Preconditions.checkNotNull(arr);
        Preconditions.checkArgument(arr.length == byteLen, "The length of provided byte[] is not " + byteLen);

        ByteBuffer buf = ByteBuffer.wrap(arr);
        return new Buddy(buf.getLong(), buf.getShort());
    }

    @Override
    public String toString() {
        return "Conversation{"
                + ", uid=" + uid
                + ", appId=" + appId
                + '}';
    }

    public static void main(String[] args) {
        Buddy expectedBuddy = new Buddy(1234567654321L, (short)2);
        byte[] arr = toByteArr(expectedBuddy);
        Buddy actualBuddy = fromByteArr(arr);

        System.out.println("Expected: " + expectedBuddy.toString());
        System.out.println("Actual: " + actualBuddy.toString());
    }
}
