package com.evanslaton.health_tracker;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
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
