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
                    result.add(new MapMarker(latitude, longitude, popupText, openPop));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
