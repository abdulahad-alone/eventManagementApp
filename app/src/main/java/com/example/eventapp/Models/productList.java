package com.example.eventapp.Models;

import java.util.List;

public class productList {
    private String id;
    private String location;
    private String price;
    private String description;
    private String shortDescription;
    private List<String> images;
    private String userId;

    public productList(String id, String location, String price, String description, String shortDescription, List<String> images, String userId) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.description = description;
        this.shortDescription = shortDescription;
        this.images = images;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}