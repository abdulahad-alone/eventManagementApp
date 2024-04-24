package com.example.eventapp.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class PropertyDetails {
    private String id;
    private String userId;
    private String purpose;
    private String type;
    private String contactNo;
    private String city;
    private String latitude;
    private String longitude;
    private String areaSize;
    private String areaUnit;
    private String price;
    private String bedrooms;
    private String bathrooms;
    private String title;
    private String description;
    private List<String> images;
    private ArrayList<String> features;
    private Timestamp timestamp;
    private String parsePrice;



    public PropertyDetails(String id, String userId, String purpose, String type, String contactNo, String city, String latitude, String longitude, String areaSize, String areaUnit, String price, String bedrooms, String bathrooms, String title, String description, List<String> images, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.purpose = purpose;
        this.type = type;
        this.contactNo = contactNo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.areaSize = areaSize;
        this.areaUnit = areaUnit;
        this.price = price;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.title = title;
        this.description = description;
        this.images = images;
        this.timestamp = timestamp;
    }

    public String getParsePrice() {
        return parsePrice;
    }

    public void setParsePrice(String parsePrice) {
        this.parsePrice = parsePrice;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAreaSize() {
        return areaSize;
    }

    public void setAreaSize(String areaSize) {
        this.areaSize = areaSize;
    }

    public String getAreaUnit() {
        return areaUnit;
    }

    public void setAreaUnit(String areaUnit) {
        this.areaUnit = areaUnit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(String bedrooms) {
        this.bedrooms = bedrooms;
    }

    public String getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(String bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
