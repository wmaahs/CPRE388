package com.example.project2.adapter;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;
import com.example.project2.data.model.Exercise;
import com.example.project2.data.model.Workout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

public class ExerciseAdapter extends FirestoreAdapter<ExerciseAdapter.ViewHolder>{

    private static String TAG = "EXERCISE_ADAPTER";

    public ExerciseAdapter(Query query) {
        super(query);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Exercise exercise = getSnapshot(position).toObject(Exercise.class);
        Log.d(TAG, "exercise name: " + exercise.getName());
        holder.bind(exercise);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView typeView;
        TextView option1View;
        TextView option2View;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.tv_exercise_details_name);
            typeView = itemView.findViewById(R.id.tV_exercise_details_type);
            option1View = itemView.findViewById(R.id.tv_exercise_details_weight_or_time);
            option2View = itemView.findViewById(R.id.tv_exercise_details_reps_or_rest);
        }

        public void bind(Exercise exercise) {
            nameView.setText(exercise.getName());
            typeView.setText(exercise.getType());
            if(exercise.getType().equals("Strength")) {
                option1View.setText(String.valueOf(exercise.getWeight()));
                option2View.setText(String.valueOf(exercise.getReps()));

            } else {
                option1View.setText(String.valueOf(exercise.getTime()));
                option2View.setText(String.valueOf(exercise.getRest()));
            }

        }
    }


}
