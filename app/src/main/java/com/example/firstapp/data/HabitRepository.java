package com.example.firstapp.data;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.firstapp.models.Habit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    private static final String PREF_NAME = "habit_prefs";
    private static final String KEY_USER_HABITS = "user_habits";
    private final SharedPreferences prefs;
    private final Gson gson;

    public HabitRepository(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public List<Habit> getUserHabits() {
        String json = prefs.getString(KEY_USER_HABITS, null);
        if (json == null) {
            List<Habit> defaults = getDefaultHabits();
            saveHabits(defaults);
            return defaults;
        }
        Type type = new TypeToken<List<Habit>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void addHabit(Habit habit) {
        List<Habit> habits = getUserHabits();
        habits.add(habit);
        saveHabits(habits);
    }

    public void updateHabitCompletion(String id, boolean completed) {
        List<Habit> habits = getUserHabits();
        for (Habit h : habits) {
            if (h.getId().equals(id)) {
                h.setCompleted(completed);
                break;
            }
        }
        saveHabits(habits);
    }

    private void saveHabits(List<Habit> habits) {
        String json = gson.toJson(habits);
        prefs.edit().putString(KEY_USER_HABITS, json).apply();
    }

    private List<Habit> getDefaultHabits() {
        List<Habit> defaults = new ArrayList<>();
        defaults.add(new Habit("Morning Meditation", "Mindfulness", "Easy", "#6B3FD4", 0)); // Res handles separately
        defaults.add(new Habit("Read 30 Minutes", "Productivity", "Medium", "#38BDF8", 0));
        defaults.add(new Habit("Drink 2L Water", "Health", "Easy", "#10B981", 0));
        return defaults;
    }
}