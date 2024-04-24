package com.example.eventapp.Models;

public class GetDaigs {
    private String DaigName;
    private String DaigPrice;
    private DaigModel daigModels;
    private String description;
    private String id;
    private String imageUrl;
    private String minimumOrder;

    public GetDaigs() {
    }

    public String getDaigName() {
        return DaigName;
    }

    public void setDaigName(String daigName) {
        DaigName = daigName;
    }

    public String getDaigPrice() {
        return DaigPrice;
    }

    public void setDaigPrice(String daigPrice) {
        DaigPrice = daigPrice;
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

    public String getMinimumOrder() {
        return minimumOrder;
    }

    public void setMinimumOrder(String minimumOrder) {
        this.minimumOrder = minimumOrder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DaigModel getDaigModels() {
        return daigModels;
    }

    public void setDaigModels(DaigModel daigModels) {
        this.daigModels = daigModels;
    }
}
