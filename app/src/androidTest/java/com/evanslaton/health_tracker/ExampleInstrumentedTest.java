package com.evanslaton.health_tracker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
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

    @Test
    public void writeUserAndReadInList() throws Exception {
//        User user = TestUtil.createUser(3);
//        user.setName("george");
//        mUserDao.insert(user);
//        List<User> byName = mUserDao.findUsersByName("george");
//        assertThat(byName.get(0), equalTo(user));

        Exercise newExercise = new Exercise("Test", 5000, "This is a test description");
        exerciseDao.insertExercise(newExercise);
        List<Exercise> exercises = exerciseDao.getAll();
        assertFalse(exercises.isEmpty());
    }


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.evanslaton.health_tracker", appContext.getPackageName());
    }
}
