package com.likg.dao.cassandraimpl;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Configuration;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.policies.DowngradingConsistencyRetryPolicy;
import com.datastax.driver.core.policies.LoadBalancingPolicy;
import com.datastax.driver.core.policies.Policies;
import com.datastax.driver.core.policies.ReconnectionPolicy;
import com.datastax.driver.core.policies.RetryPolicy;
import com.likg.dao.cassandraimpl.factory.LoadBalancePolicyFactory;
import com.likg.dao.cassandraimpl.factory.ReconnectionPolicyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by lixusign on 15-2-3.
 * 集群构造器
 */
public class ClusterBuilder {

    private static final Logger log = LoggerFactory.getLogger(ClusterBuilder.class);

    private static final Long DefaultBaseDelayMs = 300L;
    private static final Long DefaultMaxDelayMs = 3000L;

    private static Cluster cluster;

    private static final LoadBalancingPolicy defaultBalancingPolicy =
            new LoadBalancePolicyFactory().getPolicy("DCAwareRoundRobin");

    private static final ReconnectionPolicy defaultReconnectionPolicy =
            new ReconnectionPolicyFactory(DefaultBaseDelayMs, DefaultMaxDelayMs).getPolicy("Exponential");

    private static final RetryPolicy defaultRetryPolicy = DowngradingConsistencyRetryPolicy.INSTANCE;


    public static Cluster buildCluster(String... iplist) {
        return buildCluster(defaultBalancingPolicy, defaultReconnectionPolicy, defaultRetryPolicy, iplist);
    }

    public static Cluster buildCluster(final String username, final String password, String... iplist) {
        return buildCluster(defaultBalancingPolicy, defaultReconnectionPolicy, defaultRetryPolicy, username, password, iplist);
    }


    private static Cluster buildCluster(LoadBalancingPolicy defaultBalancingPolicy,
                                        ReconnectionPolicy defaultReconnectionPolicy,
                                        RetryPolicy defaultRetryPolicy,
                                        String... iplist
    ) {
        if (cluster == null) {
            synchronized (ClusterBuilder.class) {
                if (cluster == null) {
                    cluster = Cluster.builder().addContactPoints(iplist)
                            .withLoadBalancingPolicy(defaultBalancingPolicy)
                            .withReconnectionPolicy(defaultReconnectionPolicy)
                            .withRetryPolicy(defaultRetryPolicy)
                            .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))
                            .build();
                }
            }
        }
        return cluster;
    }

    private static Cluster buildCluster(LoadBalancingPolicy defaultBalancingPolicy,
                                        ReconnectionPolicy defaultReconnectionPolicy,
                                        RetryPolicy defaultRetryPolicy,
                                        String username, String password,
                                        String... iplist
    ) {
        if (cluster == null) {
            synchronized (ClusterBuilder.class) {
                if (cluster == null) {
                    cluster = Cluster.builder().addContactPoints(iplist)
                            .withCredentials(username, password)
                            .withLoadBalancingPolicy(defaultBalancingPolicy)
                            .withReconnectionPolicy(defaultReconnectionPolicy)
                            .withRetryPolicy(defaultRetryPolicy)
                            .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE))
                            .build();
                }
            }
        }
        return cluster;
    }

    public static String getClusterName() {
        if (cluster != null) {
            return cluster.getClusterName();
        } else {
            throw new IllegalStateException("cluster is not init!");
        }
    }

    public static Metadata getClusterMeta() {
        if (cluster != null) {
            return cluster.getMetadata();
        } else {
            throw new IllegalStateException("cluster is not init!");
        }
    }

    public static Configuration getConfiguration() {
        if (cluster != null) {
            return cluster.getConfiguration();
        } else {
            throw new IllegalStateException("cluster is not init!");
        }
    }

    protected static void closeCluster() {
        if (cluster != null) {
            cluster.close();
        } else {
            throw new IllegalStateException("cluster is not init!");
        }
    }

    public static void printClusterInfo() {
        printClusterInfo(false);
    }

    public static void printClusterInfo(boolean isPrintConf) {
        if (cluster != null) {
            Metadata metadata = cluster.getMetadata();
            Configuration conf = cluster.getConfiguration();
            String clusterName = metadata.getClusterName();
            Set<Host> sethosts = metadata.getAllHosts();
            log.info("Connected to cluster: {}", clusterName);
            for (Host host : sethosts) {
                log.info("Datatacenter: {} Host: {} Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
            }
            if (isPrintConf) {
                printConfiguration(conf);
            }
        } else {
            throw new IllegalStateException("cluster is not init!");
        }
    }

    private static void printConfiguration(Configuration conf) {

        if (conf != null) {
            Policies p = conf.getPolicies();
            String balanceName = p.getLoadBalancingPolicy().getClass().getName();
            String reconnectionName = p.getReconnectionPolicy().getClass().getName();
            String retryName = p.getRetryPolicy().getClass().getName();
            log.info("BalancePolicy: {} ReconnectionPolicy: {} RetryPolicy: {}", balanceName, reconnectionName, retryName);
        }
    }
}
