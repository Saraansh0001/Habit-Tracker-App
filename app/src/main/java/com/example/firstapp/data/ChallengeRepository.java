package com.example.firstapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.firstapp.models.Challenge;
import com.example.firstapp.network.ApiClient;
import com.example.firstapp.network.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeRepository {
    private final ApiService apiService;
    private final SharedPreferences prefs;
    private final Gson gson;
    private static final String PREFS_NAME = "local_challenges_prefs";
    private static final String CHALLENGES_KEY = "local_challenges";

    public ChallengeRepository(Context context) {
        apiService = ApiClient.getService(context);
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public LiveData<List<Challenge>> getAllChallenges() {
        MutableLiveData<List<Challenge>> data = new MutableLiveData<>();
        apiService.getChallenges().enqueue(new Callback<List<Challenge>>() {
            @Override
            public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    data.postValue(getLocalChallenges());
                }
            }

            @Override
            public void onFailure(Call<List<Challenge>> call, Throwable t) {
                data.postValue(getLocalChallenges());
            }
        });
        return data;
    }

    public List<Challenge> getLocalChallenges() {
        String json = prefs.getString(CHALLENGES_KEY, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<ArrayList<Challenge>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveLocalChallenge(Challenge challenge) {
        List<Challenge> challenges = getLocalChallenges();
        challenges.add(0, challenge);
        String json = gson.toJson(challenges);
        prefs.edit().putString(CHALLENGES_KEY, json).apply();
    }

    public void updateLocalChallenge(Challenge updatedChallenge) {
        List<Challenge> challenges = getLocalChallenges();
        for (int i = 0; i < challenges.size(); i++) {
            if (challenges.get(i).getId().equals(updatedChallenge.getId())) {
                challenges.set(i, updatedChallenge);
                break;
            }
        }
        String json = gson.toJson(challenges);
        prefs.edit().putString(CHALLENGES_KEY, json).apply();
    }

    public void addChallenge(Challenge challenge, Callback<Challenge> callback) {
        apiService.createChallenge(challenge).enqueue(callback);
    }
}
