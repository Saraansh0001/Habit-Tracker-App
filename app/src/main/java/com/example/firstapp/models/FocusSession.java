package com.example.firstapp.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "focus_sessions")
public class FocusSession {
    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private long timestamp;
    private int durationMinutes;
    private int xpEarned;

    public FocusSession(@NonNull String id, String title, long timestamp, int durationMinutes, int xpEarned) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.durationMinutes = durationMinutes;
        this.xpEarned = xpEarned;
    }

    @Ignore
    public FocusSession(String title, int durationMinutes, int xpEarned) {
        this(UUID.randomUUID().toString(), title, System.currentTimeMillis(), durationMinutes, xpEarned);
    }

    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public int getXpEarned() { return xpEarned; }
    public void setXpEarned(int xpEarned) { this.xpEarned = xpEarned; }
}
