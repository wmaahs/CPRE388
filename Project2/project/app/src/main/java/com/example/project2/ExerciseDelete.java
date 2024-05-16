package com.example.project2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project2.MainActivity;
import com.example.project2.R;
import com.example.project2.UserClass;
import com.example.project2.util.FirebaseUtil;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ExerciseDelete extends AppCompatActivity {

    private EditText toDel;
    private Button del;
    private Button back_to_home;


    private FirebaseFirestore workoutdb;
    public CollectionReference firebaseWorkout;

    private UserClass user;

    private String WorkoutID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_delete);

        //Find widgets
        toDel = findViewById(R.id.name_to_delete);
        del = findViewById(R.id.del_btn);
        back_to_home = findViewById(R.id.back_to_home_btn);

        user = new UserClass();
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");
        WorkoutID = fromListIntent.getStringExtra("WorkoutID");
        workoutdb = FirebaseUtil.getFirestore();
        firebaseWorkout = workoutdb.collection("__" + user.getUserName() + "__Workouts");

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the home screen
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("UserClass", user);
                view.getContext().startActivity(intent);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //gets name of exercise to be deleted
                String exerciseName = toDel.getText().toString();


                //see if exercise with specified name exists
                Task<QuerySnapshot> query = firebaseWorkout.document(WorkoutID).collection("Exercises").whereEqualTo("name", exerciseName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()){
                            if(docSnap.equals(null)){
                                //if it does not find the document the user specified, there is nothing to do
                                //hence nothing shall be done here
                            }
                            else{
                                //deletes specified document
                                firebaseWorkout.document(WorkoutID).collection("Exercises").document(docSnap.getId()).delete();
                            }

                        }

                    }
                });

                //Clear text view
                toDel.setText("");

            }
        });


    }
}
