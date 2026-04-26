package com.example.firstapp.data;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.firstapp.models.Habit;
import java.util.ArrayList;
import java.util.List;

public class HabitRepository {
    private final HabitDao habitDao;

    public HabitRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        habitDao = db.habitDao();
        
        // Initialize with default habits if empty
        if (habitDao.getAllHabits().isEmpty()) {
            habitDao.insertAll(getDefaultHabits());
        }
    }

    public List<Habit> getUserHabits() {
        return habitDao.getAllHabits();
    }

    public LiveData<List<Habit>> getUserHabitsLive() {
        return habitDao.getAllHabitsLive();
    }

    public void addHabit(Habit habit) {
        habitDao.insert(habit);
    }

    public void updateHabitCompletion(String id, boolean completed) {
        habitDao.updateCompletion(id, completed, completed ? System.currentTimeMillis() : 0);
    }

    public List<Habit> getArchivedHabits() {
        return habitDao.getArchivedHabits();
    }

    public void archiveHabit(String id) {
        habitDao.archiveHabit(id);
    }

    private List<Habit> getDefaultHabits() {
        List<Habit> defaults = new ArrayList<>();
        defaults.add(new Habit("Morning Meditation", "Mindfulness", "Easy", "#6B3FD4", 0));
        defaults.add(new Habit("Read 30 Minutes", "Productivity", "Medium", "#38BDF8", 0));
        defaults.add(new Habit("Drink 2L Water", "Health", "Easy", "#10B981", 0));
        return defaults;
    }
}
