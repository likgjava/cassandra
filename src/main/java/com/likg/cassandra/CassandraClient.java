package com.likg.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

/**
 * Created by likg on 2016/1/18.
 */
public class CassandraClient {

    private Cluster cluster;

    private Session session;

    public void connect() {
        String node = "172.17.0.1";
        cluster = Cluster.builder().addContactPoint(node)
                //.withPort(9160)
                .build();

        Session kpSession = cluster.connect();

        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n", host.getDatacenter(), host.getAddress(), host.getRack());
        }

        this.session = cluster.connect();
    }

    public void close(){
        this.session.close();
        this.cluster.close();
    }

    public Session getSession() {
        return session;
    }
}
