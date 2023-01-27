package com.example.art.familinkthesislastdraft.model;
/**
 * Created by Art on 2/2/2018.
 */

public class User {
    private String email,status,username,password;

    public User(String email, String status) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return password;
    }

    public String getUser() {
        return username;
    }

    public User (){ }


    public void setStatus(String status) {
        this.status = status;
    }
}
