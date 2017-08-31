package com.likg.dao.cassandraimpl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

/**
 * Created by lixusign on 15-2-3.
 * 集群链接会话构造器:原则上为一个KeySpace 一个Session,我们这个程序暂时只涉及到一个Space
 */
public class SessionBuilder {

    private static Session session;

    public static Session buildSession(Cluster cluster, final String keySpace) {

        if (session == null) {
            synchronized (SessionBuilder.class) {
                if (session == null) {
                    session = cluster.connect(keySpace);
                }
            }
        }
        return session;
    }

    protected static void closeSession() {

        if (session != null) {
            session.close();
        } else {
            throw new IllegalStateException("session is not init!");
        }
    }
}
