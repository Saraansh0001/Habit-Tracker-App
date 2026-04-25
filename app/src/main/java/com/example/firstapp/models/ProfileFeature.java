package com.example.firstapp.models;

public class ProfileFeature {
    private String title;
    private String description;
    private int iconRes;
    private String backgroundColor; // Hex string for the icon container

    public ProfileFeature(String title, String description, int iconRes, String backgroundColor) {
        this.title = title;
        this.description = description;
        this.iconRes = iconRes;
        this.backgroundColor = backgroundColor;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getIconRes() { return iconRes; }
    public String getBackgroundColor() { return backgroundColor; }
}