
package com.likg.cassandra;


import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.util.Iterator;

public class TestC {



    public static void main(String[] args) {

        Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .build();
       // Session session = cluster.connect();

        Session session = cluster.connect("myspace");

        session.execute(
                QueryBuilder.insertInto("myspace", "student").values(new String[]{"a","b"}, new Object[]{1,2}));

        String cql = "select * from myspace.student;";
        ResultSet resultSet = session.execute(cql);
        Iterator<Row> iterator = resultSet.iterator();
        while(iterator.hasNext())
        {
            Row row = iterator.next();
            System.out.println(row.getString(0));
            System.out.println(row.getString(1));
            System.out.println(row.getString(2));
        }

    }
}