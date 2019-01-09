package com.evanslaton.health_tracker;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;

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
                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    reset.setEnabled(false);
                } else {
                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    reset.setEnabled(true);
                }
            }
        });

        // Resets the stopwatch
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                timer.setText("0:00.000");
            }
        });
    }

    // Runs the stopwatch in a new thread
    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timer.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + "."
                    + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };
}
