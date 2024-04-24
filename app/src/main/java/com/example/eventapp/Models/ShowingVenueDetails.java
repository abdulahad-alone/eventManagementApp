package com.example.eventapp.Models;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class ShowingVenueDetails {
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
    private String rating;
    private String numberOfRatings;

    public ShowingVenueDetails(String Id,String category, String userId, String contactNo, String city, String latitude, String longitude, String price, String venueTitle, String description, List<String> images, Timestamp timestamp, ArrayList<String> features) {
        this.Category = category;
        this.userId = userId;
        this.contactNo = contactNo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.Id = Id;
        this.VenueTitle = venueTitle;
        this.description = description;
        this.images = images;
        this.timestamp = timestamp;
        this.features = features;
    }

    public ShowingVenueDetails(String Id,String category, String userId, String contactNo, String city, String latitude, String longitude, String price, String venueTitle, String description, List<String> images, Timestamp timestamp, ArrayList<String> features,String rating,String numberOfRatings) {
        this.Category = category;
        this.userId = userId;
        this.contactNo = contactNo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.Id = Id;
        this.VenueTitle = venueTitle;
        this.description = description;
        this.images = images;
        this.timestamp = timestamp;
        this.features = features;
        this.rating = rating;
        this.numberOfRatings = numberOfRatings;
    }


    public ShowingVenueDetails(String category, String userId, String contactNo, String city, String latitude, String longitude, String price, String id, String venueTitle, String description, List<String> images, Timestamp timestamp, ArrayList<String> features, String rating) {
        this.  Category = category;
        this.userId = userId;
        this.contactNo = contactNo;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.price = price;
        this.Id = id;
        this. VenueTitle = venueTitle;
        this.description = description;
        this.images = images;
        this.timestamp = timestamp;
        this.features = features;
        this.rating = rating;
    }

    public String getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(String numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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

    public ArrayList<String> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<String> features) {
        this.features = features;
    }
}
