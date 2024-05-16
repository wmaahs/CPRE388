package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project2.adapter.WorkoutAdapter;
import com.example.project2.util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

public class WorkoutList extends AppCompatActivity implements
        View.OnClickListener,
        WorkoutAdapter.OnWorkoutSelectedListener {
    private static final String TAG = "WORKOUT_LIST";
    private static final int LIMIT = 50;
    private String USERNAME;
    private Toolbar mToolbar;
    private TextView mCurrentSearchView;
    private TextView mCurrentSortByView;
    private FirebaseFirestore workoutFirestore;
    private WorkoutAdapter workoutAdapter;
    private RecyclerView workoutRecycler;

    private Query mQuery;

    private ViewGroup emptyList;
    private Button addWorkoutBtn;
    private UserClass user;
    private String test_admin; // delete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_list);

        user = new UserClass();

        // Get username from login activity
        Intent fromMainIntent = getIntent();
        user = (UserClass) fromMainIntent.getParcelableExtra("UserClass");
        Log.d(TAG, "UserClass: " + user);
        mToolbar = findViewById(R.id.toolbar);
        mCurrentSearchView = findViewById(R.id.text_current_search);
        mCurrentSortByView = findViewById(R.id.text_current_sort_by);
        workoutRecycler = findViewById(R.id.workout_recycler);
        emptyList = findViewById(R.id.view_empty);
        addWorkoutBtn = findViewById(R.id.add_workout_btn);

        findViewById(R.id.filter_bar).setOnClickListener(this);
        findViewById(R.id.button_clear_filter).setOnClickListener(this);

        // Enable firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        workoutFirestore = FirebaseUtil.getFirestore();
        Log.d(TAG, "username: " + user.getUserName());
        mQuery = workoutFirestore.collection("__" + user.getUserName() + "__Workouts")
                .orderBy("time", Query.Direction.ASCENDING)
                .limit(LIMIT);
        initRecyclerView();

        addWorkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addWorkoutIntent = new Intent(view.getContext(), WorkoutAdd.class);
                addWorkoutIntent.putExtra("UserClass", user);
                startActivity(addWorkoutIntent);
            }
        });
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

        workoutAdapter = new WorkoutAdapter(mQuery, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    workoutRecycler.setVisibility(View.GONE);
                    emptyList.setVisibility(View.VISIBLE);
                } else {
                    workoutRecycler.setVisibility(View.VISIBLE);
                    emptyList.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        // Set the Adapter and layout manager
        workoutRecycler.setLayoutManager(new LinearLayoutManager(this));
        workoutRecycler.setAdapter(workoutAdapter);
    }

    /**
     * On Start
     * Start listening to firestore
     */
    @Override
    public void onStart() {
        super.onStart();
        // Start listening for Firestore updates
        if (workoutAdapter != null) {
            workoutAdapter.startListening();
        }
    }

    /**
     * On Stop
     * Stop listening on the adapter
     */
    @Override
    public void onStop() {
        super.onStop();
        if (workoutAdapter != null) {
            workoutAdapter.stopListening();
        }
    }

    /**
     * This method starts an intent that passes the
     * DocumentSnapshot id and the UserClass instance to the
     * WorkoutDetails Screen
     * @param workout the snapshot of the workout that was clicked on
     */
    @Override
    public void onWorkoutSelected(DocumentSnapshot workout) {
        // Go to the details page for the selected workout
        Intent intent = new Intent(this, WorkoutDetails.class);
        intent.putExtra("WorkoutID", workout.getId());
        intent.putExtra("UserClass", user);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(this, WorkoutDetails.class);

    }


}