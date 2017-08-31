package com.likg.dao.cassandraimpl.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lixusign on 15-2-3.
 * fuck father class
 *
 * @param <T> policy type
 */
public abstract class AbstractPolicyFactory<T> implements PolicyFactory {

    protected final Map<String, T> map = new ConcurrentHashMap<String, T>();

    public Map<String, T> getMap() {
        return map;
    }
}