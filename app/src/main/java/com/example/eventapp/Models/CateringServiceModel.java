package com.example.eventapp.Models;

public class CateringServiceModel {
    String ServiceName;
    String ServicePrice;
    String description;
    String imageUrl;
    String minimum_orderSt;
    String id;
    String ServiceRef;

    public String getServiceRef() {
        return ServiceRef;
    }

    public void setServiceRef(String serviceRef) {
        ServiceRef = serviceRef;
    }

    public String getMinimum_orderSt() {
        return minimum_orderSt;
    }

    public void setMinimum_orderSt(String minimum_orderSt) {
        this.minimum_orderSt = minimum_orderSt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(String servicePrice) {
        ServicePrice = servicePrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CateringServiceModel() {
    }
}
