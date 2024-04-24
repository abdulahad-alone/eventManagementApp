package com.example.eventapp.Models;

import com.google.firebase.Timestamp;

public class UserModel {
    private String name;
    private String email;
    private Timestamp createdTimestamp;
    private String userId;
    private String image;
    private  String Type;

    public UserModel() {
    }

   /* public UserModel(String name, String email, Timestamp createdTimestamp, String userId, String image) {
        this.name = name;
        this.email = email;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.image = image;
    }*/

    public UserModel(String name, String email, Timestamp createdTimestamp, String userId,String Type) {
        this.name = name;
        this.email = email;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.Type = Type;

    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
