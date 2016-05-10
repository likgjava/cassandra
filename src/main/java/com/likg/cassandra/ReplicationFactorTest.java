package com.likg.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;

import java.util.Iterator;

/**
 * Created by likg on 2016-05-09.
 */
public class ReplicationFactorTest {

    public static void main(String[] args) {

        //addUser();
        //getUser();

        getMsginfo();

    }

    public static void getMsginfo(){
        //QueryOptions queryOptions = new QueryOptions().setConsistencyLevel(ConsistencyLevel.QUORUM);
        QueryOptions queryOptions = new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_ONE);
        Cluster cluster = Cluster.builder().addContactPoints("172.17.0.38", "172.17.0.44", "172.17.0.45")
                .withQueryOptions(queryOptions)
                .build();

        Session session = cluster.connect("shop");

        String cql = "select id from shop.msgbox;";
        ResultSet resultSet = session.execute(cql);
        Iterator<Row> iterator = resultSet.iterator();
        int i = 1;
        while(iterator.hasNext()) {
            Row row = iterator.next();
            System.out.println(i++ + "======="+row.getLong("id"));
        }

        session.close();
        cluster.close();
    }



}
