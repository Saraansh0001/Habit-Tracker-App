package com.example.firstapp.models;

import java.util.List;

public class ArenaUiState {
    private final UserRank userRank;
    private final List<Challenge> ongoingChallenges;
    private final List<Challenge> availableChallenges;

    public ArenaUiState(UserRank userRank, List<Challenge> ongoingChallenges, List<Challenge> availableChallenges) {
        this.userRank = userRank;
        this.ongoingChallenges = ongoingChallenges;
        this.availableChallenges = availableChallenges;
    }

    public UserRank getUserRank() { return userRank; }
    public List<Challenge> getOngoingChallenges() { return ongoingChallenges; }
    public List<Challenge> getAvailableChallenges() { return availableChallenges; }
}