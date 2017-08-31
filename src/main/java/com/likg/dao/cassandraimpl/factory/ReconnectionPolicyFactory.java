package com.likg.dao.cassandraimpl.factory;

import com.datastax.driver.core.policies.ConstantReconnectionPolicy;
import com.datastax.driver.core.policies.ExponentialReconnectionPolicy;
import com.datastax.driver.core.policies.ReconnectionPolicy;

/**
 * Created by lixusign on 15-2-3.
 * 重连策略工厂
 */
public class ReconnectionPolicyFactory extends AbstractPolicyFactory<ReconnectionPolicy> implements PolicyFactory {

    private static long COMM_INTERVAL_TIME = 300L;
    private static long MAXX_INTERVAL_TIME = 3000L;

    public ReconnectionPolicyFactory(long c, long m) {

        if (c > m || c <= 0L || m <= 0L) {
            throw new IllegalArgumentException("c or m can't <= 0 c mast <= m");
        }
        this.COMM_INTERVAL_TIME = c;
        this.MAXX_INTERVAL_TIME = m;
        map.put("Constant", new ConstantReconnectionPolicy(COMM_INTERVAL_TIME));
        map.put("Exponential", new ExponentialReconnectionPolicy(COMM_INTERVAL_TIME, MAXX_INTERVAL_TIME));
    }

    @Override
    public ReconnectionPolicy getPolicy(String policyName) {
        return map.get(policyName);
    }
}
