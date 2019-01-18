package com.evanslaton.health_tracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public int quantity;
    public String description;
    public String timestamp;

    // Default constructor
    public Exercise() {};

    // Constructor
    public Exercise(String title, int quantity, String description) {
        this.title = title;
        this.quantity = quantity;
        this.description = description;
        this.timestamp = new Date().toString();
    }
}
