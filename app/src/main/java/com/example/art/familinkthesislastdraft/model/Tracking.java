package com.example.art.familinkthesislastdraft.model;

/**
 * Created by Johannes Agustin on 18/02/2018.
 */

public class Tracking {
    private String email,uid,lat,lng;


    public Tracking(String email, String uid, String lat, String lng) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLat() {
        return lat;
    }
    public String getLng() {
        return lng;
    }

    public Tracking (){ }
    /*
    public void setLng(String lng) {
        this.lng = lng;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    */
}
