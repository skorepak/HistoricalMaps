package com.example.mapsv4.app.model;

/**
 * Created by peta on 23.5.14.
 */
public class Places {

    int id;
    float lat;
    float lng;
    String placecz;
    String placede;
    String placeen;

    // constructors
    public Places() {
    }

    public Places(float lat, float lng, String placecz, String placede, String placeen) {
        this.lat = lat;
        this.lng = lng;
        this.placecz = placecz;
        this.placede = placede;
        this.placeen = placeen;
    }

    public Places(int id, float lat, float lng, String placecz, String placede, String placeen) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
        this.placecz = placecz;
        this.placede = placede;
        this.placeen = placeen;
    }

    public void setPlaces(String placecz, String placede, String placeen) {
        this.placecz = placecz;
        this.placede = placede;
        this.placeen = placeen;
    }

    // getters
    public long getId() {
        return this.id;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceCZ() {
        return this.placecz;
    }

    public void setPlaceCZ(String placecz) {
        this.placecz = placecz;
    }

    public String getPlaceDE() {
        return this.placede;
    }

    public void setPlaceDE(String placede) {
        this.placede = placede;
    }

    public String getPlaceEN() {
        return this.placeen;
    }

    public void setPlaceEN(String placeen) {
        this.placeen = placeen;
    }

    public float getLatitude() {
        return this.lat;
    }

    public void setLatitude(float lat) {
        this.lat = lat;
    }

    public float getLongitude() {
        return this.lng;
    }

    public void setLongitude(float lng) {
        this.lng = lng;
    }
}

