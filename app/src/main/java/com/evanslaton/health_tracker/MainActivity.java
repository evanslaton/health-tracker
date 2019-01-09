package com.evanslaton.health_tracker;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // Updates the finger count exercise counter
    public void increaseCount(View v) {
        TextView counterText = findViewById(R.id.counter);
        int counter = Integer.parseInt(counterText.getText().toString());
        counter++;
        counterText.setText(String.valueOf(counter));
    }

    // Stopwatch code by Amit Kumar Singh - https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    TextView timer;
    Button startStop, reset;
    long milliSecondTime, startTime, timeBuffer, updateTime = 0L;
    Handler handler;
    int hours, minutes, seconds, milliSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stopwatch variables
        timer = (TextView)findViewById(R.id.timer);
        startStop = (Button)findViewById(R.id.startStopButton);
        reset = (Button)findViewById(R.id.resetButton);
        handler = new Handler() ;

        // Starts the stopwatch
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reset.isEnabled()) {
                    startTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    reset.setEnabled(false);
                    startStop.setText("Stop");
                } else {
                    timeBuffer += milliSecondTime;
                    handler.removeCallbacks(runnable);
                    reset.setEnabled(true);
                    startStop.setText("Start");
                }
            }
        });

        // Resets the stopwatch
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                milliSecondTime = 0L;
                startTime = 0L;
                timeBuffer = 0L;
                updateTime = 0L;
                hours = 0;
                minutes = 0;
                seconds = 0;
                milliSeconds = 0;
                timer.setText("0:00:00.000");
            }
        });
    }

    // Runs the stopwatch in a new thread
    public Runnable runnable = new Runnable() {
        public void run() {
            milliSecondTime = SystemClock.uptimeMillis() - startTime;
            updateTime = timeBuffer + milliSecondTime;
            hours = seconds / 360;
            minutes = seconds / 60;
            seconds = (int) (updateTime / 1000);
            seconds = seconds % 60;
            milliSeconds = (int) (updateTime % 1000);
            timer.setText("" + hours + ":"
                    + String.format("%02d", minutes) + ":"
                    + String.format("%02d", seconds) + "."
                    + String.format("%03d", milliSeconds));
            handler.postDelayed(this, 0);
        }
    };

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
