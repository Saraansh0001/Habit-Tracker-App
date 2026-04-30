package com.example.firstapp.models;

public class Challenge {
    private String id;
    private String title;
    private int participants;
    private String duration; // e.g. "18d left" or "7 days"
    private int progress;    // 0-100
    private boolean isActive;
    private int iconRes;     // Drawable resource ID
    private String type;     // e.g. "Meditation", "Workout"
    private String color;    // Hex color string, e.g. "#FF0000"

    public Challenge(String id, String title, int participants, String duration, int progress, boolean isActive, int iconRes, String type, String color) {
        this.id = id;
        this.title = title;
        this.participants = participants;
        this.duration = duration;
        this.progress = progress;
        this.isActive = isActive;
        this.iconRes = iconRes;
        this.type = type;
        this.color = color;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getParticipants() { return participants; }
    public String getDuration() { return duration; }
    public int getProgress() { return progress; }
    public boolean isActive() { return isActive; }
    public int getIconRes() { return iconRes; }
    public String getType() { return type; }
    public String getColor() { return color; }

    public void setParticipants(int participants) { this.participants = participants; }
    public void setActive(boolean active) { isActive = active; }
    public void setColor(String color) { this.color = color; }
}