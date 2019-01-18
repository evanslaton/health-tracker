package com.evanslaton.health_tracker;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {Exercise.class}, exportSchema = false)
public abstract class ExerciseDatabase extends RoomDatabase {
    abstract public ExerciseDao exerciseDao();
}
