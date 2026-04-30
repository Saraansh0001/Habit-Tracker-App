package com.example.firstapp.network;

import com.example.firstapp.models.Challenge;
import com.example.firstapp.models.FocusSession;
import com.example.firstapp.models.Habit;
import com.example.firstapp.models.JournalEntry; // Assume we create this
import com.example.firstapp.models.UserRank; // We might need to adjust this to backend Leaderboard model
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Auth
    @POST("auth/register")
    Call<LoginResponse> register(@Body AuthRequest request);

    @POST("auth/login")
    Call<LoginResponse> login(@Body AuthRequest request);

    // Habits
    @GET("habits")
    Call<List<Habit>> getHabits();

    @POST("habits")
    Call<Habit> createHabit(@Body Habit habit);

    @PUT("habits/{id}")
    Call<Habit> updateHabit(@Path("id") String id, @Body Habit habit);

    @DELETE("habits/{id}")
    Call<Void> deleteHabit(@Path("id") String id);

    @PUT("habits/{id}/complete")
    Call<Habit> completeHabit(@Path("id") String id);

    // Focus
    @GET("focus")
    Call<List<FocusSession>> getFocusSessions();

    @POST("focus")
    Call<FocusSession> createFocusSession(@Body FocusSession session);

    // Challenges
    @GET("challenges")
    Call<List<Challenge>> getChallenges();

    @POST("challenges")
    Call<Challenge> createChallenge(@Body Challenge challenge);

    @POST("challenges/{id}/join")
    Call<Challenge> joinChallenge(@Path("id") String id);

    // Journal
    @GET("journal")
    Call<List<JournalEntry>> getJournalEntries();

    @POST("journal")
    Call<JournalEntry> createJournalEntry(@Body JournalEntry entry);

    // Leaderboard & Profile
    @GET("leaderboard")
    Call<List<UserProfileResponse>> getLeaderboard();

    @GET("profile")
    Call<UserProfileResponse> getProfile();

    @PUT("profile")
    Call<UserProfileResponse> updateProfile(@Body UpdateProfileRequest request);

    @GET("stats")
    Call<StatsResponse> getStats();
}
