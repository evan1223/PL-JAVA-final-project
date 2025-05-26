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

    @Autowired
    private UserSession userSession;

    // excute once after all dependencies are injected
    @Override
    public void afterPropertiesSet() {
        refreshUserMarkers();
    }
    // returns an read-only list of markers
    public List<MapMarker> getAllMarkers() {
        return Collections.unmodifiableList(markers);
    }

    // manual add a marker
    public void addMarker(MapMarker marker) {
        if (marker != null) {
            markers.add(marker);
        }
    }

    // refresh user markers from UserService, can be called when user logs in or switches
    public void refreshUserMarkers() {
        markers.clear();

        List<MapMarker> userMarkers = userService.getCurrentUserMarkers();
        if (userMarkers != null && !userMarkers.isEmpty()) {
            markers.addAll(userMarkers);
        }
        // default Markers
        markers.add(new MapMarker(24.9866487, 121.5765365, "政大商學院", true));
    }

    // static method to generate JavaScript for markers
    public String generateMarkersJavaScript() {
        StringBuilder script = new StringBuilder();

        for (MapMarker m : markers) {
            script.append(String.format(
                    "L.marker([%f, %f], {icon: customIcon}).addTo(map).bindPopup('%s')%s;\n",
                    m.getLatitude(),
                    m.getLongitude(),
                    m.getPopupText(),
                    m.getIsOpenPopup() ? ".openPopup()" : ""
            ));
        }

        return script.toString();
    }
}
