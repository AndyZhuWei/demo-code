package com.example.demo.lpj.pool.dbcp;


import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author HP
 * @Description DBCP连接池示例
 * @date 2020/12/7-14:53
 */
public class DBCPTest {

    public static void main(String[] args) {
        //数据库的连接池资源，在之后需要操作的时候从池中获取即可
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://192.168.80.100:3306/shanhu-manager-test");
        dataSource.setUsername("root");
        dataSource.setPassword("root");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = dataSource.getConnection();
            String sql = "select * from role ";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

       /* try {
            dataSource.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/


    }
}
