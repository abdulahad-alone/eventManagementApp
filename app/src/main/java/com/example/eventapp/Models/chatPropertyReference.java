package com.example.eventapp.Models;

public class chatPropertyReference {
    private String propertyImage;
    private String propertyTitle;
    private String PropertyPrice;
    private String PhoneNumber;

    public chatPropertyReference() {
    }

    public chatPropertyReference(String propertyImage, String propertyTitle, String propertyPrice,String PhoneNumber) {
        this.propertyImage = propertyImage;
        this.propertyTitle = propertyTitle;
        this.PropertyPrice = propertyPrice;
        this.PhoneNumber = PhoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getPropertyImage() {
        return propertyImage;
    }

    public void setPropertyImage(String propertyImage) {
        this.propertyImage = propertyImage;
    }

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    public String getPropertyPrice() {
        return PropertyPrice;
    }

    public void setPropertyPrice(String propertyPrice) {
        PropertyPrice = propertyPrice;
    }
}
