package com.example.firstapp.network;

public class UserProfileResponse {
    public String _id;
    public String name;
    public String email;
    public int xp;
    public int streak;
    public String avatarUrl;
    public String rank;
    public UserSettings settings;

    public static class UserSettings {
        public boolean notificationsEnabled;
        public boolean darkMode;
        public String reminderTime;
    }
}
