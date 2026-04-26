package com.example.firstapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

@Entity(tableName = "habits")
public class Habit implements Serializable {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String category;
    private String difficulty; // "Easy", "Medium", "Hard"
    private int iconRes;
    private String color; // Hex color string
    private boolean isCompleted;
    private boolean isArchived;
    private long completedDate;

    public Habit(@NonNull String id, String title, String category, String difficulty, int iconRes, String color) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.iconRes = iconRes;
        this.color = color;
        this.isCompleted = false;
        this.isArchived = false;
        this.completedDate = 0;
    }

    @Ignore
    public Habit(String title, String category, String difficulty, String color, int iconRes) {
        this(UUID.randomUUID().toString(), title, category, difficulty, iconRes, color);
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
    
    public int getIconRes() { return iconRes; }
    public void setIconRes(int iconRes) { this.iconRes = iconRes; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { 
        isCompleted = completed;
        if (completed) {
            this.completedDate = System.currentTimeMillis();
        }
    }
    
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean archived) { isArchived = archived; }

    public long getCompletedDate() { return completedDate; }
    public void setCompletedDate(long completedDate) { this.completedDate = completedDate; }
}
