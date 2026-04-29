package com.example.firstapp.network;

public class AuthRequest {
    public String name;
    public String email;
    public String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthRequest(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
