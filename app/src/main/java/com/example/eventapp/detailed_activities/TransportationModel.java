package com.example.eventapp.detailed_activities;

import java.util.ArrayList;

public class TransportationModel {
    String PackageRef;
    String RideName;
    String ServicePrice;
    String capacity;
    String description;
    String id;
    String imageUrl;
    ArrayList<String> servicesList;
    String termsCondition;

    public String getPackageRef() {
        return PackageRef;
    }

    public void setPackageRef(String packageRef) {
        PackageRef = packageRef;
    }

    public String getRideName() {
        return RideName;
    }

    public void setRideName(String rideName) {
        RideName = rideName;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<String> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<String> servicesList) {
        this.servicesList = servicesList;
    }

    public String getTermsCondition() {
        return termsCondition;
    }

    public void setTermsCondition(String termsCondition) {
        this.termsCondition = termsCondition;
    }

    public TransportationModel(String packageRef, String rideName, String servicePrice, String capacity, String description,
                               String id, String imageUrl, ArrayList<String> servicesList,
                               String termsCondition) {
        this.  PackageRef = packageRef;
        this.  RideName = rideName;
        this. ServicePrice = servicePrice;
        this.capacity = capacity;
        this.description = description;
        this.id = id;
        this.imageUrl = imageUrl;
        this.servicesList = servicesList;
        this.termsCondition = termsCondition;
    }
}
