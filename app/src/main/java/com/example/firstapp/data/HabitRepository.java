package com.example.firstapp.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.firstapp.models.Habit;
import com.example.firstapp.network.ApiClient;
import com.example.firstapp.network.ApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HabitRepository {
    private final HabitDao habitDao;
    private final ApiService apiService;

    public HabitRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        habitDao = db.habitDao();
        apiService = ApiClient.getService(context);
        
        // Fetch from backend
        syncHabitsWithBackend();
    }

    private void syncHabitsWithBackend() {
        apiService.getHabits().enqueue(new Callback<List<Habit>>() {
            @Override
            public void onResponse(Call<List<Habit>> call, Response<List<Habit>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        habitDao.insertAll(response.body());
                    }).start();
                }
            }

            @Override
            public void onFailure(Call<List<Habit>> call, Throwable t) {
                // Keep local data if network fails
            }
        });
    }

    public List<Habit> getUserHabits() {
        return habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getUserHabitsLive() {
        syncHabitsWithBackend();
        return habitDao.getAllHabitsLive();
    }

    public boolean existsByTitle(String title) {
        return habitDao.countByTitle(title) > 0;
    }

    public void addHabit(Habit habit) {
        // Optimistic UI update - insert with temporary ID
        new Thread(() -> habitDao.insert(habit)).start();
        
        // Network call
        apiService.createHabit(habit).enqueue(new Callback<Habit>() {
            @Override
            public void onResponse(Call<Habit> call, Response<Habit> response) {
                if (response.isSuccessful() && response.body() != null) {
                    new Thread(() -> {
                        // Backend returned the habit with its official ID (e.g. MongoDB _id)
                        // Remove the temporary client-side habit and insert the official one
                        habitDao.delete(habit);
                        habitDao.insert(response.body());
                    }).start();
                }
            }
            @Override
            public void onFailure(Call<Habit> call, Throwable t) {
                // In a real app, you might want to retry or mark for sync
            }
        });
    }

    public void updateHabitCompletion(String id, boolean completed) {
        new Thread(() -> habitDao.updateCompletion(id, completed, completed ? System.currentTimeMillis() : 0)).start();
        
        if (completed) {
            apiService.completeHabit(id).enqueue(new Callback<Habit>() {
                @Override
                public void onResponse(Call<Habit> call, Response<Habit> response) {}
                @Override
                public void onFailure(Call<Habit> call, Throwable t) {}
            });
        } else {
            // If they uncomplete, we'll just let the backend know it's updated (assuming updateHabit endpoint handles completion status too if passed)
            // But for now, complete is the main thing.
        }
    }

    public List<Habit> getArchivedHabits() {
        return habitDao.getArchivedHabits();
    }

    public void archiveHabit(String id) {
        habitDao.archiveHabit(id);
    }

    public void deleteHabit(Habit habit) {
        new Thread(() -> habitDao.delete(habit)).start();
        apiService.deleteHabit(habit.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {}
            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    private List<Habit> getDefaultHabits() {
        List<Habit> defaults = new ArrayList<>();
        defaults.add(new Habit("Morning Meditation", "Mindfulness", "Easy", "#6B3FD4", 0));
        defaults.add(new Habit("Read 30 Minutes", "Productivity", "Medium", "#38BDF8", 0));
        defaults.add(new Habit("Drink 2L Water", "Health", "Easy", "#10B981", 0));
        return defaults;
    }
}
