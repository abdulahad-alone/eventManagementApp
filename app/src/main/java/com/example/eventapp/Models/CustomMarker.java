package com.example.eventapp.Models;

public class CustomMarker {
    private String imageUrl;
    private String propertyId;

    public CustomMarker(String imageUrl, String propertyId) {
        this.imageUrl = imageUrl;
        this.propertyId = propertyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPropertyId() {
        return propertyId;
    }
}
