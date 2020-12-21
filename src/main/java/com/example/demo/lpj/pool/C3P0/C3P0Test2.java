package com.example.demo.lpj.pool.C3P0;

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
 * @Description 第二种使用方式
 * @date 2020/12/7-16:54
 */
public class C3P0Test2 {

    public static void main(String[] args) throws PropertyVetoException, SQLException {
        //2）use the DataSources factory class;
//        DataSource ds_unpooled = DataSources.unpooledDataSource("jdbc:mysql://192.168.80.100:3306/shanhu-manager-test",
//                "root",
//                "root");
//        DataSource dataSource = DataSources.pooledDataSource( ds_unpooled );



        //2.1）带map参数
        DataSource ds_unpooled = DataSources.unpooledDataSource("jdbc:mysql://192.168.80.100:3306/shanhu-manager-test",
                "root",
                "root");
        Map overrides = new HashMap();
        overrides.put("maxStatements", "200");         //Stringified property values work
        overrides.put("maxPoolSize", new Integer(50)); //"boxed primitives" also work
        DataSource dataSource = DataSources.pooledDataSource( ds_unpooled, overrides );



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
