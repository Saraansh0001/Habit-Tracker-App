package com.example.firstapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firstapp.models.ArenaUiState;
import com.example.firstapp.models.Challenge;
import com.example.firstapp.models.UserRank;

import java.util.ArrayList;
import java.util.List;

public class ArenaViewModel extends ViewModel {

    private final MutableLiveData<ArenaUiState> _uiState = new MutableLiveData<>();
    public LiveData<ArenaUiState> uiState = _uiState;

    public ArenaViewModel() {
        loadMockData();
    }

    private void loadMockData() {
        UserRank rank = new UserRank(42, "Top 15% of all players");

        List<Challenge> ongoing = new ArrayList<>();
        ongoing.add(new Challenge("1", "30-Day Meditation", 128, "18d left", 40, true, 0, "Meditation", "#6B3FD4"));
        ongoing.add(new Challenge("2", "Morning Workout", 85, "6d left", 60, true, 0, "Workout", "#6B3FD4"));

        List<Challenge> available = new ArrayList<>();
        // Colors from design: Light Blue, Green, Purple/Lavender, Orange
        available.add(new Challenge("3", "No Social Media", 234, "7 days", 0, false, android.R.drawable.ic_menu_search, "Social", "#38BDF8"));
        available.add(new Challenge("4", "Reading Marathon", 156, "14 days", 0, false, android.R.drawable.ic_menu_search, "Reading", "#34D399"));
        available.add(new Challenge("5", "Hydration Challenge", 312, "30 days", 0, false, android.R.drawable.ic_menu_search, "Health", "#818CF8"));
        available.add(new Challenge("6", "Sleep by 10 PM", 189, "21 days", 0, false, android.R.drawable.ic_menu_search, "Sleep", "#F59E0B"));

        _uiState.setValue(new ArenaUiState(rank, ongoing, available));
    }

    public void joinChallenge(Challenge challenge) {
        ArenaUiState currentState = _uiState.getValue();
        if (currentState != null) {
            List<Challenge> available = new ArrayList<>(currentState.getAvailableChallenges());
            List<Challenge> ongoing = new ArrayList<>(currentState.getOngoingChallenges());

            if (available.remove(challenge)) {
                challenge.setActive(true);
                ongoing.add(challenge);
                _uiState.setValue(new ArenaUiState(currentState.getUserRank(), ongoing, available));
            }
        }
    }
}