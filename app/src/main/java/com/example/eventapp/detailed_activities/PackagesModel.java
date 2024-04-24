package com.example.eventapp.detailed_activities;

import java.util.ArrayList;

public class PackagesModel {
    String PackageRef;
    String description;
    String nameOfPackage;
    String priceOfPackage;
    ArrayList<String> servicesList;

    public PackagesModel(String packageRef, String description, String nameOfPackage, String priceOfPackage, ArrayList<String> servicesList) {
        this.PackageRef = packageRef;
        this.description = description;
        this.nameOfPackage = nameOfPackage;
        this.priceOfPackage = priceOfPackage;
        this.servicesList = servicesList;
    }

    public PackagesModel() {
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

    public ArrayList<String> getServicesList() {
        return servicesList;
    }

    public void setServicesList(ArrayList<String> servicesList) {
        this.servicesList = servicesList;
    }
}
