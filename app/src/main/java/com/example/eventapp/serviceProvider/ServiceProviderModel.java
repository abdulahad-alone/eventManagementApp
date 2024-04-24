package com.example.eventapp.serviceProvider;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class ServiceProviderModel {
    String documentId ;
    String Price ;
    String Budget ;
    String CurrentUserId ;
    String ProductId ;
    String this_product_userId ;
    ArrayList<String> ServicesEditList;
    ArrayList<String> selectedDates;
    String phone ;
    String FullName ;
    String address ;
    Timestamp timeStamp ;
    String Type_of_ad ;
    String imageUrl ;
    String orderNumber ;
    boolean Notification ;
    String Daigs ;
    String Type ;
    String event ;
    String noOfPersons ;
    String  imageUri;



    public ServiceProviderModel(String documentId, String price, String budget, String currentUserId, String productId, String this_product_userId, ArrayList<String> servicesEditList, ArrayList<String> selectedDates, String phone, String fullName,
                                String address, Timestamp timeStamp,
                                String type_of_ad, String imageUrl,
                                String orderNumber, boolean Notification, String Daigs,
                                String Type, String event, String noOfPersons, String imageUri) {
        this.documentId = documentId;
        this.  Price = price;
        this.  Budget = budget;
        this.  CurrentUserId = currentUserId;
        this.  ProductId = productId;
        this.this_product_userId = this_product_userId;
        this.  ServicesEditList = servicesEditList;
        this.selectedDates = selectedDates;
        this.phone = phone;
        this.  FullName = fullName;
        this.address = address;
        this.timeStamp = timeStamp;
        this. Type_of_ad = type_of_ad;
        this.imageUrl = imageUrl;
        this.orderNumber = orderNumber;
        this.Notification = Notification;
        this.Daigs = Daigs;
        this.Type = Type;
        this.event = event;
        this.noOfPersons = noOfPersons;
        this.imageUri = imageUri;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isNotification() {
        return Notification;
    }

    public void setNotification(boolean notification) {
        Notification = notification;
    }

    public String getDaigs() {
        return Daigs;
    }

    public void setDaigs(String daigs) {
        Daigs = daigs;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getNoOfPersons() {
        return noOfPersons;
    }

    public void setNoOfPersons(String noOfPersons) {
        this.noOfPersons = noOfPersons;
    }
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
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

    public String getThis_product_userId() {
        return this_product_userId;
    }

    public void setThis_product_userId(String this_product_userId) {
        this.this_product_userId = this_product_userId;
    }

    public ArrayList<String> getServicesEditList() {
        return ServicesEditList;
    }

    public void setServicesEditList(ArrayList<String> servicesEditList) {
        ServicesEditList = servicesEditList;
    }

    public ArrayList<String> getSelectedDates() {
        return selectedDates;
    }

    public void setSelectedDates(ArrayList<String> selectedDates) {
        this.selectedDates = selectedDates;
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
