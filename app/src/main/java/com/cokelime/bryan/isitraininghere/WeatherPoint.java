package com.cokelime.bryan.isitraininghere;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by Bryan on 6/8/2015.
 */
public class WeatherPoint {


    private boolean isRain;

    private double lat;
    private double lng;

    private String condition;


    private Marker marker;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }




    public boolean isRain() {
        return isRain;
    }

    public void setIsRain(boolean isRain) {
        this.isRain = isRain;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() { return lat; }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
