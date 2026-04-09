package com.example.firstapp.models;

public class UserRank {
    private int rank;
    private String percentile;

    public UserRank(int rank, String percentile) {
        this.rank = rank;
        this.percentile = percentile;
    }

    public int getRank() { return rank; }
    public String getPercentile() { return percentile; }
}