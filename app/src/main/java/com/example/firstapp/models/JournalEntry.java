package com.example.firstapp.models;

import java.io.Serializable;

public class JournalEntry implements Serializable {
    private String _id;
    private String date;
    private String mood;
    private String content;

    public JournalEntry(String date, String mood, String content) {
        this.date = date;
        this.mood = mood;
        this.content = content;
    }

    public String getId() { return _id; }
    public String getDate() { return date; }
    public String getMood() { return mood; }
    public String getContent() { return content; }
}
