package com.example.eventapp.Models;

public class keys {
    private static keys instance;
    private String name;

    public keys() {
    }

    public static synchronized keys getInstance() {
        if (instance == null) {
            instance = new keys();
        }
        return instance;
    }
    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}
