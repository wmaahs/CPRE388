package com.example.project2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.util.FirebaseUtil;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore userDB;
    private final String TAG = "LOGIN_TAG";
    private CollectionReference firebaseUsers;
    private Query mQuery;
    private TextView TV_Password;
    private TextView TV_Username;

    private Button btn_login;
    private Button btn_createAcc;
    private UserClass test;
    private String uname;
    private String pass;
    private String temp_pass;
    private int incorrect_pass_toast = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        test = new UserClass();

        btn_login = (Button) findViewById(R.id.btn_login);
        TV_Password = findViewById(R.id.tv_login_password);
        TV_Username = findViewById(R.id.tv_login_unsername);
        btn_createAcc = (Button) findViewById(R.id.btn_login_creatAcc);

        userDB = FirebaseUtil.getFirestore();
        firebaseUsers = userDB.collection("Users");
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uname = TV_Username.getText().toString();
                pass = TV_Password.getText().toString();
                verifyUser();
                startApp(v);

            }
        });

        btn_createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccount.class));
            }
        });

    }

    private void startApp(View v) {

        // Init Handler and delay
        final Handler handler = new Handler();
        final int delay = 500;

        // Make runnable to delay the password check
        // Otherwise read from database may not be fst enough
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Password matches firebase
                if (pass.equals(temp_pass)) {
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("USERNAME", uname);
                    v.getContext().startActivity(intent);
                } else {
                    //Toast password doesn't match
                    Toast.makeText(v.getContext(), "Incorrect Password. Try Again!", Toast.LENGTH_LONG).show();
                }

            }
        };
        // Post runnable after 1/2 sec to allow time to read firebase
        handler.postDelayed(runnable, delay);
    }

    private void verifyUser() {

        mQuery = userDB.collection("Users")
                .whereEqualTo("userName", uname);
        mQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    DocumentSnapshot snapshot = value.getDocuments().get(0);
                    temp_pass = snapshot.get("password").toString();
                } catch (Exception e) {

                }
                // Password Matches
            }
        });


        // Clear TextViews
        TV_Username.setText("");
        TV_Password.setText("");

    }
}