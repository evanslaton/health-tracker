package com.evanslaton.health_tracker;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(version = 1, entities = {Exercise.class}, exportSchema = false)
public abstract class ExerciseDatabase extends RoomDatabase {
    abstract public ExerciseDao exerciseDao();
}
