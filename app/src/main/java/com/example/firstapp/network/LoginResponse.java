package com.example.firstapp.network;

public class LoginResponse {
    public String token;
    public User user;

    public static class User {
        public String id;
        public String name;
        public String email;
        public int xp;
        public int streak;
    }
}
