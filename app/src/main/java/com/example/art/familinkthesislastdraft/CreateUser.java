package com.example.art.familinkthesislastdraft;

/**
 * Created by Art on 2/25/2018.
 */

public class CreateUser
{
    public CreateUser()
    {

    }
    public String name;
    public String email;

    public CreateUser(String name, String email, String password, String code, String issharing, String lat, String lng, String imageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.issharing = issharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
    }

    public String password;
    public String code;
    public String issharing;
    public String lat;
    public String lng;
    public String imageUrl;

}
