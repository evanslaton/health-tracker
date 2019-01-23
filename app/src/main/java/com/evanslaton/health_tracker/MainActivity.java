package com.evanslaton.health_tracker;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "channelId";
    private int notificationId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showUsername();
        updateHomepageVisitCounter();
        loadCounter();
    }

    // Takes the user to their stopwatch view
    public void goToStopwatch(View v) {
        Intent stopwatchIntent = new Intent(this, Stopwatch.class);
        startActivity(stopwatchIntent);
    }

    // Takes the user to their finger exercise view
    public void goToFingerExercise(View v) {
        Intent fingerExerciseIntent = new Intent(this, FingerExercise.class);
        startActivity(fingerExerciseIntent);
    }

    // Takes the user to their exercise diary view
    public void goToDiary(View v) {
        Intent diaryIntent = new Intent(this, Diary.class);
        startActivity(diaryIntent);
    }

    // Takes the user to the image carousel
    public void goToImageCarousel(View v) {
        Intent imageCarouselIntent = new Intent(this, ImageCarousel.class);
        startActivity(imageCarouselIntent);
    }

    // Takes the user to the profile page
    public void goToProfile(View v) {
        Intent ProfileIntent = new Intent(this, Profile.class);
        startActivity(ProfileIntent);
    }

    // Notifies the user to drink water every 2 hours, swap in line 65 for 67 to test every 5 seconds
    public void notifyUserToDrinkWater(View v) {
        // from https://stackoverflow.com/questions/9406523/android-want-app-to-perform-tasks-every-second
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Drink water")
                        .setContentText("Drink water please!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Drink water please!"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
                notificationManager.notify(notificationId++, mBuilder.build());
            }
        }, 3000, 3000);
//          }, 7200000, 7200000);
    }

    // Shows the user's username at the top of the page
    public void showUsername() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);
        String username = sharedPref.getString(getString(R.string.username), "please enter a username on the homepage");
        TextView userData = findViewById(R.id.username);
        userData.setText("Hi, " + username);
    }

    // Save user's username
    public void saveUsername(View v) {
        EditText usernameText = findViewById(R.id.usernameInput);
        String username = usernameText.getText().toString();
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.username), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.username), username);
        editor.commit();

        TextView userData = findViewById(R.id.username);
        userData.setText("Hi, " + username);
        usernameText.setText("");
    }

    // Keeps track of how many times the user has visited the homepage
    public void updateHomepageVisitCounter() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.visited), Context.MODE_PRIVATE);
        int visited = sharedPref.getInt(getString(R.string.visited), 0);
        visited++;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.visited), visited);
        editor.commit();

        TextView visitedField = findViewById(R.id.homepageVisitCounter);
        visitedField.setText(String.valueOf(visited));
    }

    // Keeps track of the finger exerciser count
    public void loadCounter() {
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.counter), Context.MODE_PRIVATE);
        int counter = sharedPref.getInt(getString(R.string.counter), 0);

        TextView fingerExerciseField = findViewById(R.id.fingerExerciseCounter);
        fingerExerciseField .setText(String.valueOf(counter));
    }
}
