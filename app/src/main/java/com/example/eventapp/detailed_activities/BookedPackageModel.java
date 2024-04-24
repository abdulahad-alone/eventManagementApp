package com.example.eventapp.detailed_activities;

import com.example.eventapp.utils.FirebaseUtil;
import com.example.eventapp.utils.OrderNumberGenerator;
import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class BookedPackageModel {
    String PackageRef;
    String description;
    String nameOfPackage;
    String priceOfPackage;
    String documentId;
    ArrayList<String> serviceList;
    String CurrentUserId;
    String ProductId;
    String ProductUserId;
    String BookingDate;
    String BookingTime;
    String phone;
    String FullName;
    String address;
    Timestamp timeStamp;
    String Type_of_ad;
    String imageUrl;
    String orderNumber;

    public BookedPackageModel(String packageRef, String description, String nameOfPackage,
                              String priceOfPackage, String documentId, ArrayList<String> serviceList,
                              String currentUserId, String productId, String productUserId, String bookingDate,
                              String bookingTime, String phone, String fullName, String address,
                              Timestamp timeStamp, String type_of_ad, String imageUrl, String orderNumber) {
        this.PackageRef = packageRef;
        this.description = description;
        this.nameOfPackage = nameOfPackage;
        this.priceOfPackage = priceOfPackage;
        this.documentId = documentId;
        this.serviceList = serviceList;
        this. CurrentUserId = currentUserId;
        this. ProductId = productId;
        this. ProductUserId = productUserId;
        this. BookingDate = bookingDate;
        this.  BookingTime = bookingTime;
        this.phone = phone;
        this. FullName = fullName;
        this.address = address;
        this.timeStamp = timeStamp;
        this.Type_of_ad = type_of_ad;
        this.imageUrl = imageUrl;
        this.orderNumber = orderNumber;
    }

    public String getPackageRef() {
        return PackageRef;
    }

    public void setPackageRef(String packageRef) {
        PackageRef = packageRef;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameOfPackage() {
        return nameOfPackage;
    }

    public void setNameOfPackage(String nameOfPackage) {
        this.nameOfPackage = nameOfPackage;
    }

    public String getPriceOfPackage() {
        return priceOfPackage;
    }

    public void setPriceOfPackage(String priceOfPackage) {
        this.priceOfPackage = priceOfPackage;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ArrayList<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<String> serviceList) {
        this.serviceList = serviceList;
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
