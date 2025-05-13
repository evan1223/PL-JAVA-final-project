package com.demo.service;

import org.springframework.stereotype.Service;
import com.demo.util.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class AuthService {

    // 登入驗證
    public boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM userAccount WHERE username = ? AND password = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 有資料表示帳號密碼正確

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 註冊帳號
    public boolean register(String username, String password) {
        String checkSql = "SELECT * FROM userAccount WHERE username = ?";
        String insertSql = "INSERT INTO userAccount (username, password) VALUES (?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // 帳號已存在
            }

            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
