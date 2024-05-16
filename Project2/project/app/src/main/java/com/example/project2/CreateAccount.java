package com.example.project2;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project2.data.model.Exercise;
import com.example.project2.data.model.Workout;
import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CreateAccount extends AppCompatActivity {

    private FirebaseFirestore userdb;


    private final String TAG = "DEBUG_TAG";
    private CollectionReference firebaseUser;

    private Query userQuery; //to check if username is taken

    private Workout workout;
    private Button btn_createAcc;

    private Button btn_BackToLogin;

    public UserClass TempUser;

    private FirebaseFirestore defaultdb;
    public CollectionReference firebaseDefault;
    private FirebaseFirestore workoutdb;
    public CollectionReference firebaseWorkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        TempUser = new UserClass();
        workoutdb = FirebaseUtil.getFirestore();
        defaultdb = FirebaseUtil.getFirestore();
        firebaseDefault = defaultdb.collection("DefaultCollection");


        TextView UserPassword = findViewById(R.id.tv_createAcc_pw1);
        TextView UserUsername = findViewById(R.id.tv_createAcc_username);
        TextView UserEmail = findViewById(R.id.tv_createAcc_email);
        TextView UserFirstName = findViewById(R.id.tv_createAcc_firstName);
        TextView UserLastName = findViewById(R.id.tv_createAcc_lastName);

        TextView pwCheck = findViewById(R.id.tv_createAcc_pw2);

        userdb=FirebaseUtil.getFirestore(); //initializes to firebase
        firebaseUser = userdb.collection("Users");

        btn_createAcc = (Button)findViewById(R.id.btn_createAcc_create);

        btn_BackToLogin = (Button)findViewById(R.id.btn_createAcc_ToLogin);
        btn_BackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("working", "");
                // Create intent
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // userQuery = userdb.collection("Users")
                 //       .whereEqualTo("username", UserUsername);

               // userQuery = userdb.collection("Users")
               //         .whereEqualTo("userName", UserUsername.getText().toString());
                //userQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                   // @Override
                   // public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     //   DocumentSnapshot snapshot = value.getDocuments().get(0);
                       // if (snapshot.get("userName").toString().equals(UserUsername.getText().toString())){
                         //   Log.d("already exists", "");
                        //}
                        //else{

                            if (UserPassword.getText().toString().equals(pwCheck.getText().toString())) {

                                TempUser.setPassword(UserPassword.getText().toString());
                                TempUser.setUserName(UserUsername.getText().toString());
                                TempUser.setEmail(UserEmail.getText().toString());
                                TempUser.setLastName(UserFirstName.getText().toString());
                                TempUser.setFirstName(UserLastName.getText().toString());
                                firebaseUser.add(TempUser); //adds user to firebase

                                firebaseWorkout = workoutdb.collection("__" + TempUser.getUserName() + "__Workouts");


                                //For default workouts... Dont touch
                                Task<QuerySnapshot> query = firebaseDefault.whereNotEqualTo("workoutName", null).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                //creates workouts corresponding to those in DefaultCollection
                                                Workout workout1 = queryDocumentSnapshots.getDocuments().get(0).toObject(Workout.class);
                                                Workout workout2 = queryDocumentSnapshots.getDocuments().get(1).toObject(Workout.class);

                                                firebaseWorkout.add(workout1);
                                                firebaseWorkout.add(workout2);

                                                //When it finds the workout with the specified name, it will add the following
                                                //exercises to the correct one`
                                                Task<QuerySnapshot> query = firebaseWorkout.whereEqualTo("workoutName", workout1.getWorkoutName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        Exercise exercise = new Exercise();
                                                        exercise.setName("Preacher Curls");
                                                        exercise.setReps(8);
                                                        exercise.setWeight(20);
                                                        exercise.setType("Strength");
                                                        Exercise exercise1 = new Exercise();
                                                        firebaseWorkout.document(queryDocumentSnapshots.getDocuments().get(0).getId()).collection("Exercises").add(exercise);
                                                        exercise1.setName("Tricep Pushdown");
                                                        exercise1.setReps(20);
                                                        exercise1.setWeight(10);
                                                        exercise1.setType("Strength");
                                                        firebaseWorkout.document(queryDocumentSnapshots.getDocuments().get(0).getId()).collection("Exercises").add(exercise1);
                                                    }
                                                });
                                            Task<QuerySnapshot> query1 = firebaseWorkout.whereEqualTo("workoutName", workout2.getWorkoutName()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {


                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    Exercise exercise2 = new Exercise();
                                                    exercise2.setName("run");
                                                    exercise2.setRest(10L);
                                                    exercise2.setTime(300L);
                                                    exercise2.setType("Cardio");
                                                    firebaseWorkout.document(queryDocumentSnapshots.getDocuments().get(0).getId()).collection("Exercises").add(exercise2);
                                                }
                                            });




                                        }


                                });


                                Log.d("working", "");
                                // Create intent
                                Intent intent = new Intent(v.getContext(), MainActivity.class);
                                intent.putExtra("USERNAME", TempUser.getUserName());
                                v.getContext().startActivity(intent);
                            }
                        //}




                Log.d("create account", "pw1 =  " + UserPassword.getText().toString());
                Log.d("create account", "pw2 =  " + pwCheck.getText().toString());

                    }
                });




                   // startActivity(new Intent(CreateAccount.this, MainActivity.class));
                }

            }
      //  });




