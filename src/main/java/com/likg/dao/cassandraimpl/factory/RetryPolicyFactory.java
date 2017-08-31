package com.likg.dao.cassandraimpl.factory;

import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.FallthroughRetryPolicy;
import com.datastax.driver.core.policies.RetryPolicy;

/**
 * Created by lixusign on 15-2-3.
 * 重链接策略工厂
 */
public class RetryPolicyFactory extends AbstractPolicyFactory<RetryPolicy> implements PolicyFactory {

    public RetryPolicyFactory() {

        map.put("DowngradingConsistency", DowngradingConsistencyRetryPolicy.INSTANCE);
        map.put("Fallthrough", FallthroughRetryPolicy.INSTANCE);
        map.put("Default", DefaultRetryPolicy.INSTANCE);
    }

    @Override
    public RetryPolicy getPolicy(String policyName) {
        return map.get(policyName);
    }
}
