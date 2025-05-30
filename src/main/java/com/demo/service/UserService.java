package com.demo.service;

import com.demo.util.MapMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.util.DBConnector;
import com.demo.util.UserSession;

import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private DBConnector dbConnector;
    @Autowired
    private UserSession userSession;
    // fetch markers by username
    public List<MapMarker> getCurrentUserMarkers() {
        // not logged in, return empty list (can use Spring Security to handle this)
        if (!userSession.isLoggedIn()) {
            return Collections.emptyList();
        }
        String sql = "SELECT latitude, longitude, popupText, isOpenPopup "
                + "FROM markers WHERE username = ?";
        List<MapMarker> result = new ArrayList<>();

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userSession.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double latitude  = rs.getDouble("latitude");
                    double longitude = rs.getDouble("longitude");
                    String popupText = rs.getString("popupText");
                    boolean openPop  = rs.getBoolean("isOpenPopup");
                    String[] parts = popupText.split("\n", 2);
                    String name = parts.length > 0 ? parts[0] : "";
                    String description = parts.length > 1 ? parts[1] : "";

                    result.add(new MapMarker(latitude, longitude, name, description, openPop));

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 保存用户地图标记到数据库
     * @param marker 地图标记对象
     * @param description 标记描述
     * @return 是否保存成功
     */
    public boolean saveUserMarker(MapMarker marker, String description) {
        // 检查用户是否已登录
        if (!userSession.isLoggedIn()) {
            return false;
        }

        String sql = "INSERT INTO markers (username, latitude, longitude, popupText, isOpenPopup, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userSession.getUsername());
            ps.setDouble(2, marker.getLatitude());
            ps.setDouble(3, marker.getLongitude());
            ps.setString(4, marker.getPopupText());
            ps.setBoolean(5, marker.getIsOpenPopup());
            ps.setString(6, description);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
