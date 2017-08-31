package com.likg.dao.cassandraimpl.factory;

/**
 * Created by lixusign on 15-2-3.
 */
public interface PolicyFactory {

    public <T> T getPolicy(String policyName);
}
