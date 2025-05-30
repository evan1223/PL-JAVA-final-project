package com.demo.util;

public class MapMarker {
    private double latitude;
    private double longitude;
    private String name;
    private String description;
    private boolean isOpenPopup;

    public MapMarker(double latitude, double longitude, String name, String description, boolean isOpenPopup) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.description = description;
        this.isOpenPopup = isOpenPopup;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean getIsOpenPopup() { return isOpenPopup; }

    public String getPopupText() {
        return name + "<br>" + description;
    }
}
