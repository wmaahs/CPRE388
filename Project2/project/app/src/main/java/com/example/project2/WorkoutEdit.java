package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WorkoutEdit extends AppCompatActivity {
    private String USERNAME;
    private UserClass user;

    private String WorkoutID;

    private Button addExercise;

    private Button deleteExercise;
    private Button cancelButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_edit);

        addExercise = findViewById(R.id.add_exercise_btn);
        deleteExercise = findViewById(R.id.delete_exercise_btn);
        cancelButtons = findViewById(R.id.btn_wrktout_edit_cancel);

        // Get uername from login activity
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        WorkoutID = fromListIntent.getStringExtra("WorkoutID");


        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWrkout  = new Intent(view.getContext(), ExerciseAdd.class);
                // Add put extra
                addWrkout.putExtra("WorkoutID", WorkoutID);
                addWrkout.putExtra("UserClass", user);
                view.getContext().startActivity(addWrkout);
            }
        });

        deleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ExerciseDelete.class);
                intent.putExtra("WorkoutID", WorkoutID);
                intent.putExtra("UserClass", user);
                startActivity(intent);

            }
        });
        cancelButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WorkoutDetails.class);
                intent.putExtra("WorkoutID", WorkoutID);
                intent.putExtra("UserClass", user);
                startActivity(intent);
            }
        });



    }
}