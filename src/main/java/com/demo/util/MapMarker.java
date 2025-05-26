package com.demo.util;

public class MapMarker {
    private double latitude;
    private double longitude;
    private String popupText;
    private boolean isOpenPopup;

    public MapMarker(double latitude, double longitude, String popupText, boolean isOpenPopup) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.popupText = popupText;
        this.isOpenPopup = isOpenPopup;
    }

    // getter
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getPopupText() { return popupText; }
    public boolean getIsOpenPopup() { return isOpenPopup; }
}