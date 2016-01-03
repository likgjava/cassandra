package com.likg.cassandra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;


public class CassandraTest {

    private Cluster cluster;

    private Session session;

    /**
     * 连接节点
     * @param node
     */
    public void connect(String node) {
        cluster = Cluster.builder().addContactPoint(node).build();
        Metadata metadata = cluster.getMetadata();
        System.out.printf("Connected to cluster: %s\n", metadata.getClusterName());
        for (Host host : metadata.getAllHosts()) {
            System.out.printf("Datatacenter: %s; Host: %s; Rack: %s\n",
                    host.getDatacenter(), host.getAddress(), host.getRack());
        }

        this.session = cluster.connect();
    }

    public void createTable() {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS myspace.users (");
        sql.append("id uuid,");
        sql.append("first_name varchar,");
        sql.append("last_name varchar,");
        sql.append("age int,");
        sql.append("emails set<text>,");
        sql.append("avatar blob,");
        sql.append("PRIMARY KEY (id)");
        sql.append(");");
        session.execute(sql.toString());
    }

    public void insertSimpleData() {
        String sql = "INSERT INTO myspace.users (id, first_name, last_name, age) VALUES (?, ?, ?, ?);";
        PreparedStatement insertStatement = session.prepare(sql);

        BoundStatement boundStatement = new BoundStatement(insertStatement);


        session.execute(
                boundStatement.bind(UUID.randomUUID(),"likg", "中国", 10));

    }

    public void insertData() {
        String sql = "INSERT INTO myspace.users (id, first_name, last_name, age, emails,avatar) "
                + "VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement insertStatement = session.prepare(sql);

        BoundStatement boundStatement = new BoundStatement(insertStatement);
        Set<String> emails = new HashSet<String>();
        emails.add("xxx@qq.com");
        emails.add("xxx@163.com");

        ByteBuffer avatar = null;
        try {
            avatar = toByteBuffer("e:\\user.png");
            avatar.flip();
            System.out.println("头像大小：" + avatar.capacity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.execute(
                boundStatement.bind(
                        UUID.fromString("756716f7-2e54-4715-9f00-91dcbea6cf50"),
                        "pi", "min", 10, emails, avatar));

    }

    public void loadData() {
        ResultSet resultSet = session.execute(
                "SELECT first_name,last_name,age,avatar FROM myspace.users;");
        System.out
                .println(String
                        .format("%-30s\t%-20s\t%-20s\n%s", "first_name",
                                "last_name", "age",
                                "-------------------------------+-----------------------+--------------------"));
        for (Row row : resultSet) {
            System.out.println(String.format("%-30s\t%-20s\t%-20s",
                    row.getString("first_name"), row.getString("last_name"),
                    row.getInt("age")));

            ByteBuffer byteBuffer = row.getBytes("avatar");
            System.out.println("头像大小："
                    + (byteBuffer.limit() - byteBuffer.position()));

            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream("e:\\2.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.write(byteBuffer.array(),
                        byteBuffer.position(),
                        byteBuffer.limit() - byteBuffer.position());
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        System.out.println();

    }

    public void close() {
        cluster.close();
    }

    /**
     * 读取文件
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static ByteBuffer toByteBuffer(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CassandraTest client = new CassandraTest();
        client.connect("127.0.0.1");
        client.createTable();
        //client.insertData();
        client.insertSimpleData();
        client.loadData();
        client.session.close();
        client.close();
    }
}
