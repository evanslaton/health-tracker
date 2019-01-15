package com.evanslaton.health_tracker;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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

        // Specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        if (exerciseDatabase.exerciseDao().getAll().isEmpty()) {
            Exercise newExercise = new Exercise("Bench Press", 10, "Push heavy things against gravity");
            exerciseDatabase.exerciseDao().insertExercise(newExercise);
        }



    }

    public void showExercises() {
        List<Exercise> exercises = exerciseDatabase.exerciseDao().getAll();

    }


    public void addExercise(View v) {
        TextView textViewTitle = findViewById(R.id.exerciseTitle);
        TextView textViewReps = (findViewById(R.id.reps));
        TextView textViewDescription = findViewById(R.id.description);

        String title = textViewTitle.getText().toString();
        int reps = Integer.parseInt(textViewReps.getText().toString());
        String description = textViewDescription.getText().toString();

        Exercise newExercise = new Exercise(title, reps, description);
        exerciseDatabase.exerciseDao().insertExercise(newExercise);
    }
}
