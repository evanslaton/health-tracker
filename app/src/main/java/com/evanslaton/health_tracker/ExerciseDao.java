package com.evanslaton.health_tracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    void insertExercise(Exercise exercise);

    @Query("SELECT * FROM exercise WHERE id=:id")
    Exercise getById(long id);

    @Query("SELECT * FROM exercise")
    List<Exercise> getAll();
}
