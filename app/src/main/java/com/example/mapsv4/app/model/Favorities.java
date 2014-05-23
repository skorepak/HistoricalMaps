package com.example.mapsv4.app.model;

/**
 * Created by peta on 23.5.14.
 */
public class Favorities {

    int id;
    int place_id;
    float lat;
    float lng;
    String title;

    // constructors
    public Favorities() {

    }

    public Favorities(int place_id, String title, float lat, float lng) {
        this.place_id = place_id;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    public Favorities(int id, int place_id, String title, float lat, float lng) {
        this.id = id;
        this.place_id = place_id;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
    }

    // getter
    public int getId() {
        return this.id;
    }

    // setter
    public void setId(int id) {
        this.id = id;
    }

    public int getPlaceId() {
        return this.place_id;
    }

    public void setPlaceId(int place_id) {
        this.place_id = place_id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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
