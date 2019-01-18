package com.evanslaton.health_tracker;

import android.os.Handler;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Stopwatch extends AppCompatActivity {
    // Stopwatch code by Amit Kumar Singh - https://www.c-sharpcorner.com/article/creating-stop-watch-android-application-tutorial/
    TextView timer;
    Button startStop, reset;
    long milliSecondTime, startTime, timeBuffer, updateTime = 0L;
    Handler handler;
    int hours, minutes, seconds, milliSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

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
}
