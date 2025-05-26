package com.demo.util;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Component
public class DBConnector {
    // 遠端資料庫連線設定
    private static final String server = "jdbc:mysql://140.119.19.73:3315/";
    private static final String database = "TG01"; // change to your own database
    private static final String URL = server + database + "?useSSL=false";
    private static final String USER = "TG01"; // change to your own username
    private static final String PASSWORD = "yEuBAm"; // change to your own password

    // 提供外部取得資料庫連線的方法
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}



