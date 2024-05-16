package com.example.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project2.adapter.ExerciseAdapter;
import com.example.project2.adapter.WorkoutAdapter;
import com.example.project2.data.model.Workout;
import com.example.project2.util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class WorkoutDetails extends AppCompatActivity implements
        View.OnClickListener,
        EventListener<DocumentSnapshot> {

    private Button edit_workout_btn;
    private Button workout_mode_btn;
    private String USERNAME;
    private Query mQuery;
    private FirebaseFirestore WorkoutDB;
    private DocumentReference mWorkoutRef;
    private String WorkoutID;
    private UserClass user;
    private ViewGroup mEmptyView;
    private String TAG = "WORKOUT_DETAILS";
    private RecyclerView exerciseRecycler;
    private TextView tv_workoutName;
    private ExerciseAdapter mExerciseAdapter;
    private ListenerRegistration mExerciseReg;

    private Workout workout;
    private List<DocumentSnapshot> documentSnapshots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_details);

        edit_workout_btn = findViewById(R.id.add_workout_btn);
        workout_mode_btn = findViewById(R.id.btn_start_workout);
        tv_workoutName = findViewById(R.id.tv_workoutname_details);
        mEmptyView = findViewById(R.id.view_empty);
        exerciseRecycler = findViewById(R.id.exercise_recycler);
        // Get uername from login activity
        user = new UserClass();
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        WorkoutID = fromListIntent.getStringExtra("WorkoutID");

        Log.d(TAG, "workoutID: " + WorkoutID);

        // Start workout mode Activity
        workout_mode_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WorkOutModeIntent  = new Intent(view.getContext(), WorkOutMode.class);
                // Add put extra
                WorkOutModeIntent.putExtra("WorkoutID", WorkoutID);
                WorkOutModeIntent.putExtra("UserClass", user);
                view.getContext().startActivity(WorkOutModeIntent);
            }
        });

        // Start Edit Workout Activity
        edit_workout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editWrkOutIntent  = new Intent(view.getContext(), WorkoutEdit.class);
                // Add put extra
                editWrkOutIntent.putExtra("WorkoutID", WorkoutID);
                editWrkOutIntent.putExtra("UserClass", user);
                view.getContext().startActivity(editWrkOutIntent);
            }
        });
        workout = new Workout();
        WorkoutDB = FirebaseUtil.getFirestore();
        mWorkoutRef = WorkoutDB.collection("__" + user.getUserName() + "__Workouts").document(WorkoutID);


        mWorkoutRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                workout.setWorkoutName(value.get("workoutName").toString());
                tv_workoutName.setText(workout.getWorkoutName());
                Log.d(TAG, "workoutName " + workout.getWorkoutName());
            }
        });

        mQuery = mWorkoutRef.collection("Exercises")
                        .limit(50);
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                documentSnapshots = value.getDocuments();
                Log.d(TAG, "Query result: " + documentSnapshots);

            }
        });
        initRecyclerView();

    }

    /**
     * This function is used to initialize the recycler view
     * It uses the query from mQuery and uses those snapshots
     * to populate the RecyclerView
     */
    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mExerciseAdapter = new ExerciseAdapter(mQuery) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    exerciseRecycler.setVisibility(View.GONE);
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    exerciseRecycler.setVisibility(View.VISIBLE);
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        exerciseRecycler.setLayoutManager(new LinearLayoutManager(this));
        exerciseRecycler.setAdapter(mExerciseAdapter);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ExerciseAdd.class);
    }


    @Override
    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

        Log.d(TAG, "workout loaded");
    }

    @Override
    public void onStart() {
        super.onStart();

        mExerciseAdapter.startListening();
        mExerciseReg = mWorkoutRef.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        mExerciseAdapter.stopListening();

        if (mExerciseReg != null) {
            mExerciseReg.remove();
            mExerciseReg = null;
        }
    }
}