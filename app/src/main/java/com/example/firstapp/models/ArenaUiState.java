package com.example.firstapp.models;

import java.util.List;

public class ArenaUiState {
    private final UserRank userRank;
    private final List<Challenge> ongoingChallenges;
    private final List<Challenge> availableChallenges;
    private final List<Habit> habitHistory;

    public ArenaUiState(UserRank userRank, List<Challenge> ongoingChallenges, List<Challenge> availableChallenges, List<Habit> habitHistory) {
        this.userRank = userRank;
        this.ongoingChallenges = ongoingChallenges;
        this.availableChallenges = availableChallenges;
        this.habitHistory = habitHistory;
    }

    public UserRank getUserRank() { return userRank; }
    public List<Challenge> getOngoingChallenges() { return ongoingChallenges; }
    public List<Challenge> getAvailableChallenges() { return availableChallenges; }
    public List<Habit> getHabitHistory() { return habitHistory; }
}