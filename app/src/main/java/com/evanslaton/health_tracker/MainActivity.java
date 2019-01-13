package com.evanslaton.health_tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    }

    public void goToStopwatch(View v) {
        Intent stopwatchIntent = new Intent(this, Stopwatch.class);
        startActivity(stopwatchIntent);
    }

    public void goToFingerExercise(View v) {
        Intent fingerExerciseIntent = new Intent(this, FingerExercise.class);
        startActivity(fingerExerciseIntent);
    }

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

    // Image carousel
    int imageCounter = 0;
    Image[] images = {new Image(R.drawable.forearm1, "Be the best version of yourself"),
            new Image(R.drawable.forearm2, "Only 7 minutes a day"),
            new Image(R.drawable.forearm3, "Two arms with amazing forearms")};

    // Changes image and caption to the next image
    public void nextImage(View v) {
        imageCounter++;
        if (imageCounter > 2) {
            imageCounter = 0;
        }
        ImageView image = findViewById(R.id.imageCarousel);
        TextView caption = findViewById(R.id.imageCaption);

        image.setImageResource(images[imageCounter].source);
        caption.setText(images[imageCounter].caption + " (" + (imageCounter + 1) + "/3)");
    }

    // Changes image and caption to the previous image
    public void previousImage(View v) {
        imageCounter--;
        if (imageCounter < 0) {
            imageCounter = 2;
        }
        ImageView image = findViewById(R.id.imageCarousel);
        TextView caption = findViewById(R.id.imageCaption);

        image.setImageResource(images[imageCounter].source);
        caption.setText(images[imageCounter].caption + " (" + (imageCounter + 1) + "/3)");
    }
}
