package com.evanslaton.health_tracker;

//import android.arch.persistence.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;

import androidx.core.app.ActivityCompat;
import androidx.room.Room;
import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private FusedLocationProviderClient mFusedLocationClient;
    private List<Exercise> exercises = new ArrayList<>();
    private final int PERMISSION_ID = 0;
    private String latitude;
    private String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary2);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        showUsername();

        // Fused Location Provider Client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getUserLocation();

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
//        if (exerciseDatabase.exerciseDao().getAll().isEmpty()) {
//            Exercise newExercise = new Exercise("Bench Press", 10, "Push heavy things against gravity");
//            exerciseDatabase.exerciseDao().insertExercise(newExercise);
//        }
    }

    // Gets exercises from health-tracker-backend database
    public void getExercisesFromHerokuDB() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://health-tracker-backend.herokuapp.com/exercises";

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

    // Gets the users location
    public void getUserLocation() {
        // Checks to see if the app has received permission from the user to get location data
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permission hasn't been granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ID);
        } else {
            // If permission has been granted, get location data
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("location", "Location is: " + location.getLatitude());
                                Log.d("location", "Location is: " + location.getLatitude());
                                latitude = String.valueOf(location.getLatitude());
                                longitude = String.valueOf(location.getLongitude());
                            } else {
                                // Logic to handle location object
                                latitude = "Unknown";
                                longitude = "Unknown";
                            }
                        }
                    });
        }
    }

    // Checks to see if the user granted location permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getUserLocation();
                } else {
                    // permission denied
                    latitude = "Unknown";
                    longitude = "Unknown";
                }
                return;
            }
        }
    }

    // Saves user input to the database
    public void addExercise(View v) {
        // Get user input
        EditText editViewTitle = findViewById(R.id.exerciseTitle);
        EditText editViewReps = (findViewById(R.id.reps));
        EditText editViewDescription = findViewById(R.id.description);
        String title = editViewTitle.getText().toString();
        int reps = Integer.parseInt(editViewReps.getText().toString());
        String description = editViewDescription.getText().toString();

        // Adds the exercise to the database
        Exercise newExercise = new Exercise(title, reps, description, latitude, longitude);
        exerciseDatabase.exerciseDao().insertExercise(newExercise);

        // Adds the exercise to Heroku Database
        addExercisesToHerokuDB(title, String.valueOf(reps), description, latitude, longitude);

        // https://stackoverflow.com/questions/3053761/reload-activity-in-android
        finish();
        startActivity(getIntent());
    }

    // http://www.itsalif.info/content/android-volley-tutorial-http-get-post-put
    // Adds exercises to the Heroku Database
    public void addExercisesToHerokuDB(final String title, final String reps, final String description, final String latitude, final String longitude) {
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
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                return params;
            }
        };
        queue.add(postRequest);
    }

    // Shows the user's username at the top of the page
    public void showUsername() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.username), "please enter a username on the homepage");
        TextView userData = findViewById(R.id.username2);
        userData.setText("Hi, " + username);
    }
}
