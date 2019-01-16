package com.evanslaton.health_tracker;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

// From Google's Android docs and http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
public class Diary extends AppCompatActivity {
    protected ExerciseDatabase exerciseDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary2);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // Creates a singleton of the database
        exerciseDatabase = Room.databaseBuilder(getApplicationContext(),
                ExerciseDatabase.class, "exerciseDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter (see also next example)
        mAdapter = new MyAdapter(exerciseDatabase.exerciseDao().getAll());
        mRecyclerView.setAdapter(mAdapter);

        // Adds an exercise to the database if the database is empty to avoid a NullPointerException
        if (exerciseDatabase.exerciseDao().getAll().isEmpty()) {
            Exercise newExercise = new Exercise("Bench Press", 10, "Push heavy things against gravity");
            exerciseDatabase.exerciseDao().insertExercise(newExercise);
        }
    }

    // Saves user input to the database
    public void addExercise(View v) {
        EditText editViewTitle = findViewById(R.id.exerciseTitle);
        EditText editViewReps = (findViewById(R.id.reps));
        EditText editViewDescription = findViewById(R.id.description);

        String title = editViewTitle.getText().toString();
        int reps = Integer.parseInt(editViewReps.getText().toString());
        String description = editViewDescription.getText().toString();

        Exercise newExercise = new Exercise(title, reps, description);
        exerciseDatabase.exerciseDao().insertExercise(newExercise);

        // https://stackoverflow.com/questions/3053761/reload-activity-in-android
        finish();
        startActivity(getIntent());
    }
}
