package com.example.firstapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.firstapp.models.FocusSession;

import java.util.List;

@Dao
public interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    List<FocusSession> getAllSessions();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSession(FocusSession session);

    @Query("DELETE FROM focus_sessions")
    void deleteAll();
}
