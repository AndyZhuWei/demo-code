package com.example.demo.spi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author HP
 * @Description TODO
 * @date 2021/1/13-14:32
 */
public class Main {

    public static void main(String[] args) throws Exception {

        // 加载 Mysql 驱动
        Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        // 数据库连接地址
        String dbConnStr = "jdbc:mysql://117.78.41.246:3306/shanhu_manager?user=root&password=He@lthlink";
        // 创建数据库连接
        Connection conn = DriverManager.getConnection(dbConnStr);
        System.out.println(conn);


    }









    //SPI需要参考spi-demo
    private void spiDemo() {
        ServiceLoader<ISayHelloService> load = ServiceLoader.load(ISayHelloService.class);
        Iterator<ISayHelloService> iterator = load.iterator();
        while(iterator.hasNext()) {
            ISayHelloService next = iterator.next();
            System.out.println(next.say());
        }
    }
}
