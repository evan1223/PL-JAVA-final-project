package com.demo.service;

import com.demo.util.MapMarker;
import com.demo.util.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MapMarkerService implements InitializingBean {

    private final List<MapMarker> markers = new ArrayList<>();

    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() {
        refreshUserMarkers();
    }

    public List<MapMarker> getAllMarkers() {
        return Collections.unmodifiableList(markers);
    }

    public void addMarker(MapMarker marker) {
        if (marker != null) {
            markers.add(marker);
        }
    }

    public void refreshUserMarkers() {
        markers.clear();

        List<MapMarker> userMarkers = userService.getCurrentUserMarkers();
        if (userMarkers != null && !userMarkers.isEmpty()) {
            markers.addAll(userMarkers);
        }

        markers.add(new MapMarker(24.9866487, 121.5765365, "政大商學院", "教室與圖書館", false));
    }

    public void saveMarkerToDatabase(MapMarker marker, String description) {
        userService.saveUserMarker(marker, description); // 假設你資料庫也會存 description
        markers.add(marker);
        refreshUserMarkers();
    }

    // Escape HTML/JS for safety
    private String escapeJavaScript(String text) {
        return text.replace("'", "\\'")
                .replace("\"", "&quot;")
                .replace("\n", "<br>");
    }

    public String generateMarkersJavaScript() {
        StringBuilder script = new StringBuilder();

        for (MapMarker m : markers) {
            String popup = escapeJavaScript(m.getPopupText());
            script.append(String.format(
                    "L.marker([%f, %f], {icon: customIcon}).addTo(map).bindPopup('%s')%s;\n",
                    m.getLatitude(),
                    m.getLongitude(),
                    popup,
                    m.getIsOpenPopup() ? ".openPopup()" : ""
            ));
        }

        return script.toString();
    }
}

