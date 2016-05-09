
package com.likg.cassandra;


import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class TestC {


    private static ThreadPoolExecutor servicepool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);



    public static void main(String[] args) {

        addUser();
        //getUser();

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
        SocketOptions socketOptions = new SocketOptions();
        socketOptions.setConnectTimeoutMillis(5000);
        socketOptions.setKeepAlive(true);

        NettyOptions nettyOptions = new NettyOptions();
        //nettyOptions.


        Cluster cluster = Cluster.builder().addContactPoint("172.17.0.12")
                .withSocketOptions(socketOptions)
                //.withNettyOptions(nettyOptions)
                .build();
        Session session = null;




//        servicepool.execute(new Runnable() {
//            public void run() {
//
//            }
//        });

        try {
            long time = System.currentTimeMillis();
            session = cluster.connect("xmmsg");

            long id = System.currentTimeMillis();

            String sql = "INSERT INTO xmmsg.msgbox2 (msgid,from_uid,to_uid,to_appid,msg) VALUES (?,?,?,1,?);";
            PreparedStatement insertStatement = session.prepare(sql);
            for(int i=0; i<100000; i++){

                String msg = "msg"+i;
                ByteBuffer b = ByteBuffer.wrap(msg.getBytes());

                BoundStatement boundStatement = new BoundStatement(insertStatement);
                //session.execute(boundStatement.bind(id++, 1L, 1L, b));
                session.executeAsync(boundStatement.bind(id++, 1L, 1L, b));
            }

            time = System.currentTimeMillis() - time;
            System.out.println("time===" + time);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            session.close();
            cluster.close();
        }

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