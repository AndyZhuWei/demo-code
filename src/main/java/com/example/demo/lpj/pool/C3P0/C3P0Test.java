package com.example.demo.lpj.pool.C3P0;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HP
 * @Description 第一种使用方式
 * @date 2020/12/7-16:54
 */
public class C3P0Test {

    public static void main(String[] args) throws PropertyVetoException, SQLException {

        //1) directly instantiate and configure a ComboPooledDataSource bean;
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
       // dataSource.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        dataSource.setJdbcUrl( "jdbc:mysql://192.168.80.100:3306/shanhu-manager-test" );
        dataSource.setUser("root");
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
    }
}
