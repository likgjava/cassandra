package com.likg.dao.cassandraimpl.factory;

import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.RoundRobinPolicy;

/**
 * Created by lixusign on 15-2-3.
 * 负载策略工厂 还有好多负载策略5种 不写了。
 */
public class LoadBalancePolicyFactory extends AbstractPolicyFactory<LoadBalancingPolicy> implements PolicyFactory {

    public LoadBalancePolicyFactory() {
        map.put("RoundRobin", new RoundRobinPolicy());
        map.put("DCAwareRoundRobin", new DCAwareRoundRobinPolicy());
    }

    @Override
    public LoadBalancingPolicy getPolicy(String policyName) {
        return map.get(policyName);
    }
}
