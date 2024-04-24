package com.example.eventapp.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class VenueDetails {
    private String Category;
    private String userId;
    private String contactNo;
    private String city;
    private String latitude;
    private String longitude;
    private String price;
    private String Id;
    private String VenueTitle;
    private String description;
    private List<String> images;
    private Timestamp timestamp;
    private ArrayList<String> features;


    public VenueDetails(String Id,String category, String userId, String contactNo, String city, String latitude, String longitude, String price, String venueTitle, String description, List<String> images, Timestamp timestamp) {
        this.Category = category;
        this.Id = Id;
        this.userId = userId;
        this.contactNo = contactNo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.VenueTitle = venueTitle;
        this.description = description;
        this.images = images;
        this.timestamp = timestamp;
    }


    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }

    public java.lang.String getId() {
        return Id;
    }

    public void setId(java.lang.String id) {
        Id = id;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVenueTitle() {
        return VenueTitle;
    }

    public void setVenueTitle(String venueTitle) {
        VenueTitle = venueTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


}
