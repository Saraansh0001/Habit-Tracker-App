package com.example.firstapp.models;

import java.io.Serializable;
import java.util.UUID;

public class Habit implements Serializable {
    private String id;
    private String title;
    private String category;
    private String difficulty; // "Easy", "Medium", "Hard"
    private int iconRes;
    private String color; // Hex color string
    private boolean isCompleted;

    public Habit(String title, String category, String difficulty, String color, int iconRes) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.iconRes = iconRes;
        this.color = color;
        this.isCompleted = false;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getIconRes() { return iconRes; }
    public String getColor() { return color; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}