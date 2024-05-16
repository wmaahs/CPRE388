package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project2.data.model.Workout;
import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class WorkoutAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private UserClass user;
    private Spinner dayOfWeek;

    private EditText workoutName;

    private EditText workoutTime;
    private Button btn_continue;

    private Workout tempWorkout;
    private String tempDay = "";

    public String workoutID;

    private FirebaseFirestore workoutdb;
    public CollectionReference firebaseWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);
        workoutdb = FirebaseUtil.getFirestore();

        user = new UserClass();
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        firebaseWorkout = workoutdb.collection("__" + user.getUserName() + "__Workouts");

        // find widgets
        btn_continue = findViewById((R.id.btn_to_exerciseAdd));
        dayOfWeek = findViewById(R.id.dropdown_dow);
        workoutName = findViewById(R.id.etv_workout_create_name);
        workoutTime = findViewById(R.id.etv_workout_create_time);

        //initializes the dropdown box
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.DaysOfWeek, android.R.layout.simple_spinner_item);
        dayOfWeek.setAdapter(adapter);
        dayOfWeek.setOnItemSelectedListener(this);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time;
                try {
                    time = Integer.parseInt(workoutTime.getText().toString());
                }
                catch (NumberFormatException e) {
                    time = 0;
                }
                tempWorkout = new Workout(workoutName.getText().toString(), tempDay, time);
                firebaseWorkout.add(tempWorkout);
                Task<QuerySnapshot> query = firebaseWorkout.whereEqualTo("workoutName", tempWorkout.getWorkoutName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        workoutID = queryDocumentSnapshots.getDocuments().get(0).getId();
                        Log.d("wkoutID", workoutID);
                        toExerciseAdd(view);
                    }
                });

                //Clear Text Views
                workoutName.setText("");
                workoutTime.setText("");

            }
        });

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String day = adapterView.getItemAtPosition(i).toString();
        if(day.equals("Sunday")){
            tempDay = "Sunday";
        }
        if(day.equals("Monday")){
            tempDay = "Sunday";
        }
        if(day.equals("Tuesday")){
            tempDay = "Sunday";
        }
        if(day.equals("Wendesday")){
            tempDay = "Sunday";
        }
        if(day.equals("Thursday")){
            tempDay = "Sunday";
        }
        if(day.equals("Friday")){
            tempDay = "Sunday";
        }
        if(day.equals("Saturday")){
            tempDay = "Sunday";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        tempDay = "Sunday";
    }

    public void toExerciseAdd(View v){
        final Handler handler = new Handler();
        final int delay = 500;
        Log.d("test", workoutID);

        // Make runnable to delay the password check
        // Otherwise read from database may not be fst enough
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Password matches firebase
                if (workoutID.equals(null)) {

                } else {
                    Intent addWrkout  = new Intent(v.getContext(), ExerciseAdd.class);
                    // Add put extra
                    addWrkout.putExtra("WorkoutID", workoutID);
                    addWrkout.putExtra("UserClass", user);
                    v.getContext().startActivity(addWrkout);
                }

            }
        };
        // Post runnable after 1/2 sec to allow time to read firebase
        handler.postDelayed(runnable, delay);

    }

}

