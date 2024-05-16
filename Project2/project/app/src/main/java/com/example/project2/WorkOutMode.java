package com.example.project2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.data.model.Exercise;
import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;


public class WorkOutMode extends AppCompatActivity {
    private String USERNAME;
    private UserClass user;

    private String WorkoutID;
    private TextView tv_workoutName;

    private TextView tv_workoutPrimary;   //textview for displaying main workout atribute (weight or time on)
    private TextView tv_workoutSecondary; //textview for displaying secondary workout atribute (reps or rest time)
    private TextView tv_workoutPrimaryLabel; //lable for mid workout adjustment of primary atribute
    private TextView tv_workoutSecondaryLabel;//lable for mid workout adjustment of secondary atribute
    private Button btn_workoutmode_foward;
    private Button btn_workoutmode_back;
    private Button btn_workoutmode_primary_up;
    private Button btn_workoutmode_primary_down;
    private Button btn_workoutmode_secondary_up;
    private Button btn_workoutmode_secondary_down;
    public Exercise tempExercise;
    private int exerciseCount;
    private boolean flag=false;
    private FirebaseFirestore WorkoutDB;
    public CollectionReference mWorkoutRef;
    private Timer timer;
    private Timer timer2;
    private Runnable runnable;
    private Handler handler;
    private Runnable runnable2;
    private Handler handler2;
    final int delay = 1;

    //private DocumentReference mWorkoutRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_mode);
        timer = new Timer();
        // Get uername from login activity
        Bundle loginBundle = getIntent().getExtras();
        if (loginBundle.getString("USERNAME") != null) {
            USERNAME = loginBundle.getString("USERNAME");
        }

        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        WorkoutID = fromListIntent.getStringExtra("WorkoutID");

        tv_workoutName = findViewById(R.id.tv_workout_mode_name);
        //tv_workoutName.setText(WorkoutID);
        tv_workoutPrimary = findViewById(R.id.tv_workout_mode_PrimaryView);
        tv_workoutSecondary = findViewById(R.id.tv_workout_mode_SecondaryView);
        tv_workoutPrimaryLabel = findViewById(R.id.tv_workout_mode_primary);
        tv_workoutSecondaryLabel = findViewById(R.id.tv_workout_mode_secondary);


        btn_workoutmode_foward = findViewById(R.id.bt_workout_mode_foward); //foward button btn
        btn_workoutmode_back = findViewById(R.id.bt_workout_mode_back); // back button btn
        btn_workoutmode_primary_up = findViewById(R.id.bt_workout_mode_primary_up);//primary stat up  btn
        btn_workoutmode_primary_down = findViewById(R.id.bt_workout_mode_primary_down);//primary stat down btn
        btn_workoutmode_secondary_up = findViewById(R.id.bt_workout_mode_secondary_up);//secondary stat up btn
        btn_workoutmode_secondary_down = findViewById(R.id.bt_workout_mode_secondary_down);//secondary stat down btn

        WorkoutDB = FirebaseUtil.getFirestore();
        //mWorkoutRef = WorkoutDB.collection("__" + user.getUserName() + "__Workouts").document(WorkoutID);
        mWorkoutRef = WorkoutDB.collection("__" + user.getUserName() + "__Workouts");


        exerciseCount =0;
        getExercise(exerciseCount);

        // Timer time = clock.runMian();


        btn_workoutmode_foward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exerciseCount++;
                handler.removeCallbacks(runnable);
                handler2.removeCallbacks(runnable2);
                flag = false;

                    Query query = mWorkoutRef.document(WorkoutID).collection("Exercises");
                    query.count().get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()){
                                AggregateQuerySnapshot snapshot = task.getResult();
                                Log.d("count", String.valueOf(snapshot.getCount()));
                                if(exerciseCount < snapshot.getCount()){
                                    getExercise(exerciseCount);
                                }
                                else{
                                    Toast.makeText(view.getContext(), "Hey bud, You are already at the end of the workout", Toast.LENGTH_LONG).show();
                                    exerciseCount--; //makes sure we move this back
                                }
                            }
                        }
                    });



            }
        });
         handler = new Handler();

         runnable = new Runnable() {
            @Override
            public void run() {
//                        if(!timer.TimerEnded()){
//                            timer.setTimerDuration(tempExercise.getTime());
//                            timer.Start();
//                        }

                if(!flag){
                    timer.setTimerDuration(tempExercise.getTime()*1000);
                    timer.Start();
                    flag = true;
                }
                if(!(tempExercise.getType().equals("Strength")&&timer.TimerEnded())){
                    handler.postDelayed(this, delay);
                }
                if(timer.TimerEnded()&&flag){
                    timer.stop();
                    tv_workoutPrimary.setText("0.00");
                    handler.removeCallbacks(this);
                    flag = false;
                    handler2.postDelayed(runnable2, delay);
                    //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
                }
                tv_workoutPrimary.setText(String.valueOf(timer.getRemaningTime()/1000));


            }


        };
         ///////////////second handler//////////////////////
        handler2 = new Handler();

        runnable2 = new Runnable() {
            @Override
            public void run() {
//                        if(!timer.TimerEnded()){
//                            timer.setTimerDuration(tempExercise.getTime());
//                            timer.Start();
//                        }

                if(!flag){
                    timer2.setTimerDuration(tempExercise.getRest()*1000);
                    timer2.Start();
                    flag = true;
                }
                if(!(tempExercise.getType().equals("Strength")&&timer2.TimerEnded())){
                    handler2.postDelayed(this, delay);
                }
                if(timer2.TimerEnded()&&flag){
                    timer2.stop();
                    tv_workoutSecondary.setText("0.00");
                    handler2.removeCallbacks(this);
                    flag = false;
                    Toast.makeText(getApplicationContext(), "Your rest is over. Get your ass moving", Toast.LENGTH_SHORT).show();
                }
                tv_workoutSecondary.setText(String.valueOf(timer2.getRemaningTime()/1000));

            }


        };
        /////////////////////////
        btn_workoutmode_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exerciseCount > 0){
                    exerciseCount--;
                    getExercise(exerciseCount);
                    handler.removeCallbacks(runnable);
                    handler2.removeCallbacks(runnable2);
                    flag = false;
                }else {Toast.makeText(getApplicationContext(), "You are at the beginning", Toast.LENGTH_LONG).show();}

            }
        });

        //Increase Primary exercise attr
        btn_workoutmode_primary_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increasePrimary();
            }
        });
        //Decrease Primary exercise attr
        btn_workoutmode_primary_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreasePrimary();
            }
        });
        //Increase Secondary exercise attr
        btn_workoutmode_secondary_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseSecondary();
            }
        });
        //Decrease Secondary exercise attr
        btn_workoutmode_secondary_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseSecondary();
            }
        });

    }



    public void getExercise(int index){
        timer = new Timer();
        timer2 = new Timer();

        //handler.removeCallbacks(runnable);


        Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()  {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tempExercise = queryDocumentSnapshots.getDocuments().get(index).toObject(Exercise.class);
                tv_workoutName.setText(tempExercise.getName());


                if (tempExercise.getType().equals("Strength")) {
                    tv_workoutPrimary.setText(Integer.toString(tempExercise.getWeight()));
                    tv_workoutSecondary.setText(Integer.toString(tempExercise.getReps()));
                    tv_workoutPrimaryLabel.setText("Weight");
                    tv_workoutSecondaryLabel.setText("Reps");
                } else {
                    tv_workoutPrimary.setText(String.valueOf(tempExercise.getTime()));
                    tv_workoutSecondary.setText(String.valueOf(tempExercise.getRest().toString()));
                    tv_workoutPrimaryLabel.setText("Time");
                    tv_workoutSecondaryLabel.setText("Rest");
                }





//                };
                if(!(tempExercise.getType().equals("Strength"))){
                    handler.postDelayed(runnable, delay);

                }

                //handler.postDelayed(runnable, delay);

                Log.d("gunnars thingy", "great sucess");

            }
        });
    }

    /**
     * Increase the primary attribute. Start by getting the current
     * weight or time. If weight, add 5. If time, add 1.
     * Then update the textView.
     * Then query FireBase for matching exercise, update field.
     */
    private void increasePrimary() {
        if(tempExercise.getType().equals("Strength")) {
            int newPrimary = tempExercise.getWeight() + 5;
            tempExercise.setWeight(newPrimary);
            tv_workoutPrimary.setText(String.valueOf(newPrimary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot docSnap : queryDocumentSnapshots) {
                                mWorkoutRef.document(WorkoutID).collection("Exercises")
                                        .document(docSnap.getId())
                                        .update("weight", tempExercise.getWeight());
                            }
                        }
                    });
        } else {
            long newPrimary = tempExercise.getTime() + 1;
            tempExercise.setTime(newPrimary);
            tv_workoutPrimary.setText(String.valueOf(newPrimary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mWorkoutRef.document(WorkoutID).collection("Exercises")
                                    .document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                    .update("time", tempExercise.getTime());
                        }
                    });
        }
    }

    /**
     * Increase the Secondary attribute. Start by getting the current
     * reps or rest. If reps, add 1. If rest, add 1.
     * Then update the textView.
     * Then query FireBase for matching exercise, update field.
     */
    private void increaseSecondary(){

        if(tempExercise.getType().equals("Strength")) {
            int newSecondary = tempExercise.getReps() + 1;
            tempExercise.setReps(newSecondary);
            tv_workoutSecondary.setText(String.valueOf(newSecondary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot docSnap : queryDocumentSnapshots) {
                                mWorkoutRef.document(WorkoutID).collection("Exercises")
                                        .document(docSnap.getId())
                                        .update("reps", tempExercise.getReps());
                            }
                        }
                    });
        } else {
            long newSecondary = tempExercise.getRest() + 1;
            tempExercise.setRest(newSecondary);
            tv_workoutSecondary.setText(String.valueOf(newSecondary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mWorkoutRef.document(WorkoutID).collection("Exercises")
                                    .document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                    .update("rest", tempExercise.getRest());
                        }
                    });
        }

    }

    /**
     * Decrease the Primary attribute. Start by getting the current
     * weight or time. If weight, sub 5. If time, sub 1.
     * Then update the textView.
     * Then query FireBase for matching exercise, update field.
     */
    private void decreasePrimary() {

        if(tempExercise.getType().equals("Strength")) {
            int newPrimary = tempExercise.getWeight() - 5;
            tempExercise.setWeight(newPrimary);
            tv_workoutPrimary.setText(String.valueOf(newPrimary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot docSnap : queryDocumentSnapshots) {
                                mWorkoutRef.document(WorkoutID).collection("Exercises")
                                        .document(docSnap.getId())
                                        .update("weight", tempExercise.getWeight());
                            }
                        }
                    });
        } else {
            long newPrimary = tempExercise.getTime() - 1;
            tempExercise.setTime(newPrimary);
            tv_workoutPrimary.setText(String.valueOf(newPrimary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mWorkoutRef.document(WorkoutID).collection("Exercises")
                                    .document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                    .update("time", tempExercise.getTime());
                        }
                    });
        }

    }

    /**
     * Decrease the Secondary attribute. Start by getting the current
     * reps or rest. If reps, sub 1. If rest, sub 1.
     * Then update the textView.
     * Then query FireBase for matching exercise, update field.
     */
    private void decreaseSecondary() {

        if(tempExercise.getType().equals("Strength")) {
            int newSecondary = tempExercise.getReps() - 1;
            tempExercise.setReps(newSecondary);
            tv_workoutSecondary.setText(String.valueOf(newSecondary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot docSnap : queryDocumentSnapshots) {
                                mWorkoutRef.document(WorkoutID).collection("Exercises")
                                        .document(docSnap.getId())
                                        .update("reps", tempExercise.getReps());
                            }
                        }
                    });
        } else {
            long newSecondary = tempExercise.getRest() - 1;
            tempExercise.setRest(newSecondary);
            tv_workoutSecondary.setText(String.valueOf(newSecondary));
            Task<QuerySnapshot> query = mWorkoutRef.document(WorkoutID).collection("Exercises")
                    .whereEqualTo("name", tempExercise.getName())
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            mWorkoutRef.document(WorkoutID).collection("Exercises")
                                    .document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                    .update("rest", tempExercise.getRest());
                        }
                    });
        }

    }


}
