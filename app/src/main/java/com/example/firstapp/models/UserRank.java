package com.example.firstapp.models;

public class UserRank {
    private int rank;
    private int percentile;

    public UserRank(int rank, int percentile) {
        this.rank = rank;
        this.percentile = percentile;
    }

    public int getRank() { return rank; }
    public int getPercentile() { return percentile; }
}