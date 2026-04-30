package com.example.firstapp.network;

public class UpdateProfileRequest {
    public String name;
    public String avatarUrl;
    public String rank;

    public UpdateProfileRequest(String name, String avatarUrl, String rank) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.rank = rank;
    }
}
