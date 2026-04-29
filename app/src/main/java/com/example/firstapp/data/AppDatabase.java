package com.example.firstapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.firstapp.models.FocusSession;
import com.example.firstapp.models.Habit;

@Database(entities = {Habit.class, FocusSession.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract HabitDao habitDao();
    public abstract FocusSessionDao focusSessionDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "habit_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() // Note: For simplicity in this transition, allowing main thread. In production, use background threads.
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
