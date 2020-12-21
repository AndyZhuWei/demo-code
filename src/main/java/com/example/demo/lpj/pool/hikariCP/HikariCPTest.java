package com.example.demo.lpj.pool.hikariCP;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

/**
 * @author HP
 * @Description TODO
 * @date 2020/12/7-18:22
 */
public class HikariCPTest {
    public static void main(String[] args) throws Exception {
//        HikariConfig config = new HikariConfig();
//        config.setJdbcUrl("jdbc:mysql://192.168.80.100:3306/shanhu-manager-test");
//        config.setUsername("root");
//        config.setPassword("root");
//        config.addDataSourceProperty("cachePrepStmts", "true");
//        config.addDataSourceProperty("prepStmtCacheSize", "250");
//        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
//
//        HikariDataSource ds = new HikariDataSource(config);



//        HikariDataSource ds = new HikariDataSource();
//        ds.setJdbcUrl("jdbc:mysql://192.168.80.100:3306/shanhu-manager-test");
//        ds.setUsername("root");
//        ds.setPassword("root");



        HikariConfig config = new HikariConfig("D:\\IT\\demo-code\\src\\main\\java\\com\\example\\demo\\lpj\\pool\\hikariCP\\hikariCP.properties");
        HikariDataSource ds = new HikariDataSource(config);



        Connection connection = ds.getConnection();
        System.out.println(connection);
        connection.close();
    }
}
