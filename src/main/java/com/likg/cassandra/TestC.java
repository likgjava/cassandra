
package com.likg.cassandra;


import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class TestC {



    public static void main(String[] args) {

        //addUser();
        getUser();

        //getMsginfo();

    }

    public static void getMsginfo(){
        Cluster cluster = Cluster.builder().addContactPoint("172.17.0.1").build();

        Session session = cluster.connect("xmmsg");

        String cql = "select msgid from xmmsg.msginfo;";
        ResultSet resultSet = session.execute(cql);
        Iterator<Row> iterator = resultSet.iterator();
        while(iterator.hasNext()) {
            Row row = iterator.next();
            System.out.println("from_uid====="+row.getLong("msgid"));
        }

        session.close();
        cluster.close();
    }

    public static void addUser(){
        Cluster cluster = Cluster.builder().addContactPoint("172.17.0.1").build();

        Session session = cluster.connect("demo");

        String sql = "INSERT INTO demo.users (key,full_name) VALUES (?,?);";
        PreparedStatement insertStatement = session.prepare(sql);
        BoundStatement boundStatement = new BoundStatement(insertStatement);
        session.execute(boundStatement.bind("1", "likg"));


        session.close();
        cluster.close();
    }
    public static void getUser(){
        Cluster cluster = Cluster.builder().addContactPoint("172.17.0.1").build();

        Session session = cluster.connect("demo");

        String cql = "select full_name from demo.users;";
        ResultSet resultSet = session.execute(cql);
        Iterator<Row> iterator = resultSet.iterator();
        while(iterator.hasNext()) {
            Row row = iterator.next();
            System.out.println("from_uid====="+row.getString("full_name"));
        }

        session.close();
        cluster.close();
    }
}