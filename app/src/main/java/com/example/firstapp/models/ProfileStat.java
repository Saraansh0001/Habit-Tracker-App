package com.example.firstapp.models;

public class ProfileStat {
    private String label;
    private String value;
    private int iconRes;

    public ProfileStat(String label, String value, int iconRes) {
        this.label = label;
        this.value = value;
        this.iconRes = iconRes;
    }

    public String getLabel() { return label; }
    public String getValue() { return value; }
    public int getIconRes() { return iconRes; }
}