package com.evanslaton.health_tracker;

import androidx.room.Room;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// From Google's Android docs and http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
public class Diary extends AppCompatActivity {
    protected ExerciseDatabase exerciseDatabase;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Exercise> exercises = new ArrayList<>();

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

        exercises = exerciseDatabase.exerciseDao().getAll();
        getExercisesFromHerokuDB();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Specify an adapter (see also next example)
        mAdapter = new MyAdapter(exercises);
        mRecyclerView.setAdapter(mAdapter);

        // Adds an exercise to the database if the database is empty to avoid a NullPointerException
        if (exerciseDatabase.exerciseDao().getAll().isEmpty()) {
            Exercise newExercise = new Exercise("Bench Press", 10, "Push heavy things against gravity");
            exerciseDatabase.exerciseDao().insertExercise(newExercise);
        }
    }

    // Gets exercises from health-tracker-backend database
    public void getExercisesFromHerokuDB() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://health-tracker-backend.herokuapp.com/exercises";

        // Request a string response from the provided URL.
        StringRequest stringRequest =
                new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                Exercise[] exercisesFromHerokuDB = gson.fromJson(response, Exercise[].class);

                                // Combine Heroku DB and local DB
                                if (exercisesFromHerokuDB.length > 0) {
                                    for (Exercise e : exercisesFromHerokuDB) {
//                                        System.out.println("This is e: " + e);
                                        exercises.add(e);
                                    }
                                }
                                System.out.println(exercises);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // mTextView.setText("That didn't work!");
                        Log.e("Error", "That didn't work!");
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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

        // Adds to Heroku DB
        addExercisesToHerokuDB(title, String.valueOf(reps), description);

        // https://stackoverflow.com/questions/3053761/reload-activity-in-android
        finish();
        startActivity(getIntent());
    }

    // http://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
    public void addExercisesToHerokuDB(final String title, final String reps, final String description) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://health-tracker-backend.herokuapp.com/exercises";

        // Request a string response from the provided URL.
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // Response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Error
                        Log.d("Error.Response", "It didn't work");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("title", title);
                params.put("quantity", reps);
                params.put("description", description);
                return params;
            }
        };
        queue.add(postRequest);
    }
}
