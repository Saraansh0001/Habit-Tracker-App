package com.example.firstapp.api;

import com.example.firstapp.models.Habit;
import com.example.firstapp.models.FocusSession;

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
    class AuthRequest {
        public String name;
        public String email;
        public String password;
        public AuthRequest(String n, String e, String p) { name = n; email = e; password = p; }
        public AuthRequest(String e, String p) { email = e; password = p; }
    }

    class AuthResponse {
        public String token;
        public UserData user;
    }

    class UserData {
        public String id;
        public String name;
        public String email;
        public int xp;
        public int streak;
        public String avatarUrl;
        public String rank;
        public Settings settings;
    }

    class Settings {
        public boolean notificationsEnabled;
        public boolean darkMode;
        public String reminderTime;
    }

    @GET("profile")
    Call<UserData> getProfile();

    @PUT("profile")
    Call<UserData> updateProfile(@Body UserData user);

    @POST("auth/register")
    Call<AuthResponse> register(@Body AuthRequest req);

    @POST("auth/login")
    Call<AuthResponse> login(@Body AuthRequest req);

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

}
