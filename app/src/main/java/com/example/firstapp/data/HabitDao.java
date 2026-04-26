package com.example.firstapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.firstapp.models.Habit;

import java.util.List;

@Dao
public interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0")
    List<Habit> getAllHabits();

    @Query("SELECT * FROM habits WHERE isArchived = 0")
    LiveData<List<Habit>> getAllHabitsLive();

    @Query("SELECT * FROM habits WHERE isArchived = 1 OR isCompleted = 1")
    List<Habit> getArchivedHabits();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Habit habit);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Habit> habits);

    @Update
    void update(Habit habit);

    @Query("UPDATE habits SET isCompleted = :completed, completedDate = :date WHERE id = :id")
    void updateCompletion(String id, boolean completed, long date);

    @Query("UPDATE habits SET isArchived = 1 WHERE id = :id")
    void archiveHabit(String id);

    @Delete
    void delete(Habit habit);
}
