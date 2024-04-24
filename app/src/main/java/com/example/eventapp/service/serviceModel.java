package com.example.eventapp.service;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class serviceModel {
    String Id;
    String Totalprice;
    String CurrentUserId;
    String ProductId;
    String ProductUserId;
    ArrayList<String> BookingDates;
    String Title;
    String description;
    String ServiceRef;
    String phone;
    String FullName;
    String address;
    Timestamp timeStamp;
    String Type_of_ad;
    String imageUrl;
    String orderNumber;
    String manydays;
    String totalNoOf;

    public serviceModel(String id, String totalprice, String currentUserId, String productId,
                        String productUserId,ArrayList<String> BookingDates, String title,
                        String description, String ServiceRef, String phone, String fullName,
                        String address, Timestamp timeStamp,
                        String type_of_ad, String imageUrl, String orderNumber,
                        String manydays, String totalNoOf) {
        this.Id = id;
        this.Totalprice = totalprice;
        this.CurrentUserId = currentUserId;
        this. ProductId = productId;
        this.ProductUserId = productUserId;
        this.BookingDates = BookingDates;
        this.Title = title;
        this.description = description;
        this.ServiceRef = ServiceRef;
        this.phone = phone;
        this.FullName = fullName;
        this.address = address;
        this.timeStamp = timeStamp;
        this. Type_of_ad = type_of_ad;
        this.imageUrl = imageUrl;
        this.orderNumber = orderNumber;
        this.manydays = manydays;
        this.totalNoOf = totalNoOf;
    }

    public ArrayList<String> getBookingDates() {
        return BookingDates;
    }

    public void setBookingDates(ArrayList<String> bookingDates) {
        BookingDates = bookingDates;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTotalprice() {
        return Totalprice;
    }

    public void setTotalprice(String totalprice) {
        Totalprice = totalprice;
    }

    public String getCurrentUserId() {
        return CurrentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        CurrentUserId = currentUserId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductUserId() {
        return ProductUserId;
    }

    public void setProductUserId(String productUserId) {
        ProductUserId = productUserId;
    }



    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceRef() {
        return ServiceRef;
    }

    public void setServiceRef(String serviceRef) {
        ServiceRef = serviceRef;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType_of_ad() {
        return Type_of_ad;
    }

    public void setType_of_ad(String type_of_ad) {
        Type_of_ad = type_of_ad;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getManydays() {
        return manydays;
    }

    public void setManydays(String manydays) {
        this.manydays = manydays;
    }

    public String getTotalNoOf() {
        return totalNoOf;
    }

    public void setTotalNoOf(String totalNoOf) {
        this.totalNoOf = totalNoOf;
    }
}
