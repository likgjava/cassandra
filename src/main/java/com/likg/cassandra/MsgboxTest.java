package com.likg.cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by likg on 2016/1/18.
 */
public class MsgboxTest {

    public static void main(String[] args) {
        MsgboxTest test = new MsgboxTest();
        test.getAllMsg();
    }


    public void getAllMsg(){
        CassandraClient client = new CassandraClient();
        client.connect();

        String sql = "SELECT from_uid,to_uid FROM xmmsg.msgbox;";
        ResultSet resultSet = client.getSession().execute(sql);
        System.out.println(String.format("%-30s\t%-20s\t%-20s\n%s", "first_name","last_name", "age","-------------------------------+-----------------------+--------------------"));
        for (Row row : resultSet) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s",
                    row.getString("from_uid"), row.getString("to_uid")));


        }
        System.out.println();

        client.close();
    }




}
