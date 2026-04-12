package com.example.firstapp.models;

public class Habit {
    private String id;
    private String title;
    private String category;
    private String difficulty; // "Easy", "Medium", "Hard"
    private int iconRes;
    private String color; // Hex color string

    public Habit(String id, String title, String category, String difficulty, int iconRes, String color) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.iconRes = iconRes;
        this.color = color;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getDifficulty() { return difficulty; }
    public int getIconRes() { return iconRes; }
    public String getColor() { return color; }
}