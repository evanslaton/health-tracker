package com.evanslaton.health_tracker;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// From Google's Android docs and http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Exercise> exercises;

    // Provides a reference to the views for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView title;
        public TextView description;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            title = (TextView) v.findViewById(R.id.firstLine);
            description = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    // MyAdapter constructor
    public MyAdapter(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_layout, parent, false);

        // set the view's size, margins, padding and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get elements from the dataset and replaces the contents of the view with the element
        holder.title.setText(exercises.get(position).quantity + " " + exercises.get(position).title + " " + exercises.get(position).timestamp);
        holder.description.setText(exercises.get(position).description  + " lat: " + exercises.get(position).latitude + " lon: " + exercises.get(position).longitude);
    }

    // Returns the size of the dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return exercises.size();
    }
}
