package com.example.eventapp.activities;

import com.google.firebase.Timestamp;

public class DaigModelForBooked {
    String Id;
    String Totalprice;
    String CurrentUserId;
    String ProductId;
    String ProductUserId;
    String BookingDate;
    String BookingTime;
    String Title;
    String description;
    String DaigRef;
    String phone;
    String FullName;
    String address;
    Timestamp timeStamp;
    String Type_of_ad;
    String imageUrl;
    String orderNumber;
    String TotalKgs;
    String totalNoOfDaigs;

    public DaigModelForBooked(String id, String totalprice, String currentUserId, String productId, String ProductUserId, String bookingDate, String bookingTime, String title, String description
            , String daigRef, String phone, String fullName,
                              String address, Timestamp timeStamp, String type_of_ad, String imageUrl,
                              String orderNumber,String TotalKgs,String totalNoOfDaigs) {
        this.Id = id;
        this.Totalprice = totalprice;
        this.CurrentUserId = currentUserId;
        this.ProductId = productId;
        this.ProductUserId = ProductUserId;
        this.BookingDate = bookingDate;
        this.BookingTime = bookingTime;
        this.Title = title;
        this.description = description;
        this.DaigRef = daigRef;
        this.phone = phone;
        this.FullName = fullName;
        this.address = address;
        this.timeStamp = timeStamp;
        this.Type_of_ad = type_of_ad;
        this.imageUrl = imageUrl;
        this.orderNumber = orderNumber;
        this.TotalKgs = TotalKgs;
        this.totalNoOfDaigs = totalNoOfDaigs;
    }

    public String getId() {
        return Id;
    }

    public String getTotalKgs() {
        return TotalKgs;
    }

    public String getTotalNoOfDaigs() {
        return totalNoOfDaigs;
    }

    public void setTotalNoOfDaigs(String totalNoOfDaigs) {
        this.totalNoOfDaigs = totalNoOfDaigs;
    }

    public void setTotalKgs(String totalKgs) {
        TotalKgs = totalKgs;
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

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
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

    public String getDaigRef() {
        return DaigRef;
    }

    public void setDaigRef(String daigRef) {
        DaigRef = daigRef;
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
}
