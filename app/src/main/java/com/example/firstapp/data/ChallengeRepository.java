package com.example.firstapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.firstapp.models.Challenge;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChallengeRepository {
    private static final String PREFS_NAME = "arena_prefs";
    private static final String CHALLENGES_KEY = "custom_challenges";
    private final SharedPreferences prefs;
    private final Gson gson;

    public ChallengeRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void addChallenge(Challenge challenge) {
        List<Challenge> challenges = getCustomChallenges();
        challenges.add(challenge);
        String json = gson.toJson(challenges);
        prefs.edit().putString(CHALLENGES_KEY, json).apply();
    }

    public List<Challenge> getCustomChallenges() {
        String json = prefs.getString(CHALLENGES_KEY, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<Challenge>>() {}.getType();
        return gson.fromJson(json, type);
    }
}