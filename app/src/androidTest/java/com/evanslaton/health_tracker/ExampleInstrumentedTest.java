package com.evanslaton.health_tracker;

import androidx.room.Room;
import android.content.Context;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private ExerciseDao exerciseDao;
    private ExerciseDatabase exerciseDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        exerciseDatabase = Room.inMemoryDatabaseBuilder(context, ExerciseDatabase.class).build();
        exerciseDao = exerciseDatabase.exerciseDao();
    }

    @After
    public void closeDb() throws IOException {
        exerciseDatabase.close();
    }

    // Tests adding and retrieving an exercise and turning an exercise into its String representation
    @Test
    public void writeUserAndReadInList() throws Exception {
        Exercise newExercise = new Exercise("Test", 5000, "This is a test description", "-1", "1");
        exerciseDao.insertExercise(newExercise);
        List<Exercise> exercises = exerciseDao.getAll();
        assertFalse(exercises.isEmpty());
        assertEquals(1, exercises.size());
        assertEquals(newExercise.title, exercises.get(0).title);
        assertEquals(newExercise.quantity, exercises.get(0).quantity);
        assertEquals(newExercise.description, exercises.get(0).description);
        assertEquals(newExercise.timestamp, exercises.get(0).timestamp);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.evanslaton.health_tracker", appContext.getPackageName());
    }
}
