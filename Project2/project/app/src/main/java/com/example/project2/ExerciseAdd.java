package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project2.data.model.Exercise;
import com.example.project2.data.model.Workout;
import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.TimeUnit;

public class ExerciseAdd extends AppCompatActivity {
    int bt_flag = 0;

    public EditText tv_Primary_Attribute;

    private FirebaseFirestore exercisedb;
    public EditText tv_Secondary_Attribute;
    public EditText tv_Name;

    private EditText tv_Routine;
    public Button btn_exercise_type;
    public Button btn_exercise_create;

    public Button btn_toMain;

    private Spinner dayOfWeek;


    public CollectionReference firebaseExercise;
    public Exercise TempExercise;



    UserClass user;

    private String WorkoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_add);

        //gets user/workout id from previous screen
        user = new UserClass();
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        WorkoutID = fromListIntent.getStringExtra("WorkoutID");

        tv_Primary_Attribute = findViewById(R.id.tv_exercise_edit_primary);
        tv_Secondary_Attribute = findViewById(R.id.tv_exercise_edit_secondary);
        tv_Name = findViewById(R.id.tv_exercise_edit_name);
        btn_exercise_type = (Button) findViewById(R.id.sw_exercise_edit_type);
        btn_exercise_create = findViewById(R.id.bt_workout_edit_create);
        btn_toMain = findViewById(R.id.bt_workout_edit_main);


        exercisedb = FirebaseUtil.getFirestore();
        firebaseExercise = exercisedb.collection("__" + user.getUserName() + "__Workouts");




        //exercise that will be added to firebase
        TempExercise = new Exercise();


//        List<exercise> exercises = new ArrayList<>();
//        exercises.add(exercise1);
//        Log.d(TAG, "Added exercise to workout.");
//        Workout workout1 = new Workout("test", exercises);
//        firebaseWorkout.add(workout1);
//        Log.d(TAG, "Added to firebase.");


        //switch that wil allow user to select weight lifting/ cardio
        btn_exercise_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bt_flag % 2 == 0) {
                    btn_exercise_type.setText("Cardio");
                    bt_flag++;
                } else {
                    btn_exercise_type.setText("Weight Lifting");
                    bt_flag++;
                }

            }
        });

        btn_exercise_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this is here so people can enter in the name of the workout and passes different routines to the backend


                if (bt_flag % 2 == 0) {
                    TempExercise.setType("Strength");
                    TempExercise.setName(tv_Name.getText().toString());
                    TempExercise.setWeight(Integer.parseInt(tv_Primary_Attribute.getText().toString()));
                    TempExercise.setReps(Integer.parseInt(tv_Secondary_Attribute.getText().toString()));
                } else {
                    TempExercise.setType("Cardio");
                    TempExercise.setName(tv_Name.getText().toString());
                    TempExercise.setTime((long) Long.valueOf(tv_Primary_Attribute.getText().toString()));
                    TempExercise.setRest((long) Long.valueOf(tv_Secondary_Attribute.getText().toString()));
                }

                //adds to firebase
                Log.d("workoutEdit", "Temp " + TempExercise.getName());
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Query query = firebaseExercise.document(WorkoutID).collection("Exercises").whereEqualTo("name", TempExercise.getName());
                query.count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()){
                            AggregateQuerySnapshot snapshot = task.getResult();
                            Log.d("count", String.valueOf(snapshot.getCount()));
                            if(snapshot.getCount() == 0){
                                firebaseExercise.document(WorkoutID).collection("Exercises").add(TempExercise);

                            }
                            else{
                                Task<QuerySnapshot> query = firebaseExercise.document(WorkoutID).collection("Exercises").whereEqualTo("name", TempExercise.getName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()){
                                            if(docSnap.get("name").equals(null)){
                                                //add to firebase if not already there
                                                //
                                            }
                                            else{
                                                //updates existing value in firestore
                                                firebaseExercise.document(WorkoutID).collection("Exercises").document(docSnap.getId()).update("name", TempExercise.getName(), "reps", TempExercise.getReps(), "rest", TempExercise.getRest(), "time", TempExercise.getTime(), "type", TempExercise.getType(), "weight", TempExercise.getWeight());

                                            }

                                            //Log.d("workoutEdit", "Temp " + tempWorkout.getWorkoutName());
                                        }


                                    }
                                });
                            }
                        }
                    }
                });



                //waits just to make sure everything posts correctly
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }



                /*
                adds corresponding exercise to collection specified at top and the workout specified by the
                workout id in the subcollection Exercises
                 */

                tv_Primary_Attribute.setText("");
                tv_Secondary_Attribute.setText("");
                tv_Name.setText("");

            }
        });


        //sends you back to main
        btn_toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMainIntent = new Intent(view.getContext(), MainActivity.class);
                // Add put extras with UserClas
                toMainIntent.putExtra("USERNAME", user.getUserName());
                // Start Activity
                view.getContext().startActivity(toMainIntent);
            }
        });


    }
}

