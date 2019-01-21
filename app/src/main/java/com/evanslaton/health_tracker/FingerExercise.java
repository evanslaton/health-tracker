package com.evanslaton.health_tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FingerExercise extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_exercise);

        showUsername();
    }

    // Updates the finger count exercise counter
    public void increaseCount(View v) {
        TextView counterText = findViewById(R.id.counter);
        int counter = Integer.parseInt(counterText.getText().toString());
        counter++;
        counterText.setText(String.valueOf(counter));
    }

    // Shows the user's username at the top of the page
    public void showUsername() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.username), "please enter a username on the homepage");
        TextView userData = findViewById(R.id.username3);
        userData.setText("Hi, " + username);
    }
}
