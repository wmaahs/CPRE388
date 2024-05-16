package com.example.project2.adapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project2.R;
import com.example.project2.data.model.Workout;
//import com.example.project2.util.RestaurantUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * RecyclerView adapter for a list of Workouts.
 */
public class WorkoutAdapter extends FirestoreAdapter<WorkoutAdapter.ViewHolder> {

    private static final String TAG = "WorkoutAdapter";

    public interface OnWorkoutSelectedListener {

        void onWorkoutSelected(DocumentSnapshot workout);

    }

    private OnWorkoutSelectedListener mListener;

    public WorkoutAdapter(Query query, OnWorkoutSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_workout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView dayView;
        TextView timeView;


        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_workout_name);
            dayView = itemView.findViewById(R.id.tv_workout_day);
            timeView = itemView.findViewById(R.id.tv_workout_time);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnWorkoutSelectedListener listener) {

            Log.d(TAG, "workout name:" + (snapshot.toString()));
            Workout workout = snapshot.toObject(Workout.class);
            Resources resources = itemView.getResources();


            nameView.setText(workout.getWorkoutName());
            dayView.setText(workout.getDayOfWeek().toString());
            timeView.setText(String.valueOf(workout.getTime()));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onWorkoutSelected(snapshot);
                    }
                }
            });
        }

    }
}
