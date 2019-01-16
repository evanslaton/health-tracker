package com.evanslaton.health_tracker;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.util.List;

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

        // Improves performance if changes in content do not change the layout size of the RecyclerView
        // mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter (see also next example)
        mAdapter = new MyAdapter(exerciseDatabase.exerciseDao().getAll());
        mRecyclerView.setAdapter(mAdapter);

        //
        if (exerciseDatabase.exerciseDao().getAll().isEmpty()) {
            Exercise newExercise = new Exercise("Bench Press", 10, "Push heavy things against gravity");
            exerciseDatabase.exerciseDao().insertExercise(newExercise);
        }
    }

//    public void showExercises() {
//        List<Exercise> exercises = exerciseDatabase.exerciseDao().getAll();
//    }

    public void addExercise(View v) {
        EditText editViewTitle = findViewById(R.id.exerciseTitle);
        EditText editViewReps = (findViewById(R.id.reps));
        EditText editViewDescription = findViewById(R.id.description);

        String title = editViewTitle.getText().toString();
        int reps = Integer.parseInt(editViewReps.getText().toString());
        String description = editViewDescription.getText().toString();

        Exercise newExercise = new Exercise(title, reps, description);
        exerciseDatabase.exerciseDao().insertExercise(newExercise);

        // https://stackoverflow.com/questions/3053761/reload-activity-in-android - from Jessica Lovell
        finish();
        startActivity(getIntent());
    }
}
