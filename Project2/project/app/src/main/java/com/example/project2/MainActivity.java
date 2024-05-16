package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.data.model.Exercise;
import com.example.project2.data.model.Workout;
import com.example.project2.util.FirebaseUtil;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.annotation.Nullable;
/**
* Main Activity. Acts as the dashboard for our app.
*/
public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore strengthLabDB;
    private Query userQuery;
    private final String TAG = "MAIN_ACTIVITY_TAG";
    private CollectionReference userCollection;
    private CollectionReference workoutCollection;
    private Button workout_list_btn;
    private Button my_account_btn;
    private Button Sign_Out_button;
    private Button calendar_button;
    private TextView welcome_tv;
    private String USERNAME;
    private UserClass user;

    private FirebaseFirestore workoutFirestore;
    CollectionReference routine;

    private Workout workout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get username from login activity
        user = new UserClass();
        Bundle MainBundle = getIntent().getExtras();

        // Init Firebase
        strengthLabDB = FirebaseUtil.getFirestore();
        userCollection = strengthLabDB.collection("Users");

        if (MainBundle != null) {
            USERNAME = MainBundle.getString("USERNAME");
            Log.d(TAG, "Username: " + USERNAME);
            Log.d(TAG, "User: " + user.toString());
        }
        Intent intent = getIntent();
        if(intent.getParcelableExtra("UserClass") != null) {
            user = intent.getParcelableExtra("UserClass");
        }
        else{
            userInit();
        }
        // Set welcome textview
        welcome_tv = findViewById(R.id.tv_main_username);
        String msg = getString(R.string.welcome, user.getUserName());
        welcome_tv.setText(msg);
        // Init Buttons
        my_account_btn = findViewById(R.id.bt_main_account);
        workout_list_btn = findViewById(R.id.bt_main_workouts);
        calendar_button = findViewById(R.id.btn_calendar);
        Sign_Out_button = findViewById(R.id.bt_main_SighOut);

        // On click listeners for buttons
        //Sign Out
        Sign_Out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                USERNAME = null;
                // Create intent
                Intent myAccountIntent = new Intent(view.getContext(), LoginActivity.class);
                // Start Activity
                view.getContext().startActivity(myAccountIntent);
            }
        });
        // My Account
        my_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent
                Intent myAccountIntent = new Intent(view.getContext(), Account.class);
                // Add put extras with UserClass
                myAccountIntent.putExtra("UserClass", user);
                // Start Activity
                view.getContext().startActivity(myAccountIntent);

            }
        });

        // Workout List
        workout_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent
                Intent workoutListIntent = new Intent(view.getContext(), WorkoutList.class);
                // Add put extras with UserClass
                workoutListIntent.putExtra("UserClass", user);
                // Start Activity
                view.getContext().startActivity(workoutListIntent);
            }
        });


        //to calendar
        calendar_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent
                Intent calendarIntent = new Intent(view.getContext(), Calendar.class);
                // Add put extras with UserClas
                calendarIntent.putExtra("UserClass", user);
                // Start Activity
                view.getContext().startActivity(calendarIntent);
            }
        });


    }

    private void userInit() {

        user.setUserName(USERNAME);
        Log.d(TAG, "User.getUser: " + user.getUserName());
        userQuery = userCollection.whereEqualTo("userName", user.getUserName());
        userQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Nullable
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                DocumentSnapshot snapshot = value.getDocuments().get(0);

                user.setEmail(snapshot.get("email").toString());
                user.setFirstName(snapshot.get("firstName").toString());
                user.setLastName(snapshot.get("lastName").toString());
                user.setPassword(snapshot.get("password").toString());
                user.setMaxWeightBench(Integer.parseInt(snapshot.get("maxWeightBench").toString()));
                user.setMaxWeightSquant(Integer.parseInt(snapshot.get("maxWeightSquant").toString()));
                user.setMaxWeightDeadlift(Integer.parseInt(snapshot.get("maxWeightDeadlift").toString()));
                user.setBestTimeMile(Integer.parseInt(snapshot.get("bestTimeMile").toString()));
            }
        });
    }

    public void addData() {
    routine.add(new Exercise("test"));


    }
}
