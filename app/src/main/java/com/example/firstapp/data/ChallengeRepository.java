package com.example.firstapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.firstapp.R;
import com.example.firstapp.models.Challenge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChallengeRepository {
    private static final String PREF_NAME = "challenge_prefs";
    private static final String KEY_ALL_CHALLENGES = "all_challenges";
    private final SharedPreferences prefs;
    private final Gson gson;

    public ChallengeRepository(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public List<Challenge> getAllChallenges() {
        String json = prefs.getString(KEY_ALL_CHALLENGES, null);
        if (json == null) {
            List<Challenge> defaults = getDefaultChallenges();
            saveChallenges(defaults);
            return defaults;
        }
        Type type = new TypeToken<List<Challenge>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void addChallenge(Challenge challenge) {
        List<Challenge> challenges = getAllChallenges();
        challenges.add(challenge);
        saveChallenges(challenges);
    }

    private void saveChallenges(List<Challenge> challenges) {
        String json = gson.toJson(challenges);
        prefs.edit().putString(KEY_ALL_CHALLENGES, json).apply();
    }

    private List<Challenge> getDefaultChallenges() {
        List<Challenge> defaults = new ArrayList<>();
        defaults.add(new Challenge("1", "30-Day Meditation", 128, "18d left", 40, false, R.drawable.ic_meditation, "Meditation", "#6B3FD4"));
        defaults.add(new Challenge("2", "Morning Workout", 85, "6d left", 60, false, R.drawable.ic_workout, "Workout", "#6B3FD4"));
        defaults.add(new Challenge("3", "No Social Media", 234, "7 days", 0, false, R.drawable.ic_social, "Social", "#38BDF8"));
        defaults.add(new Challenge("4", "Reading Marathon", 156, "14 days", 0, false, R.drawable.ic_reading, "Reading", "#34D399"));
        defaults.add(new Challenge("5", "Hydration Challenge", 312, "30 days", 0, false, R.drawable.ic_health, "Health", "#818CF8"));
        defaults.add(new Challenge("6", "Sleep by 10 PM", 189, "21 days", 0, false, R.drawable.ic_sleep, "Sleep", "#F59E0B"));
        return defaults;
    }
}
