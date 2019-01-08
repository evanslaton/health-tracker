package com.evanslaton.health_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void increaseCount(View v) {
        TextView counterText = findViewById(R.id.counter);
        int counter = Integer.parseInt(counterText.getText().toString());
        counter++;
        counterText.setText(String.valueOf(counter));
    }
}
