package com.example.project2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Account extends AppCompatActivity {
    private FirebaseFirestore strengthLabDB;
    private CollectionReference userCollection;
    private Task<QuerySnapshot> userQuery;
    private UserClass user;
    private Button BackToMain;
    private Button btn_UpdateMax;

    private TextView UserName;
    private TextView MaxMile;
    private TextView MaxBench;
    private TextView MaxSquat;
    private TextView MaxDeadlift;
    private TextView Email;
    private TextView Password;
    private TextView FirstName;
    private TextView LastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user = new UserClass();

        // Get username from login activity
        Intent fromIntent = getIntent();
        user = fromIntent.getParcelableExtra("UserClass");

        // Init Firebase
        strengthLabDB = FirebaseUtil.getFirestore();
        userCollection = strengthLabDB.collection("Users");
        UserName = findViewById(R.id.tv_account_username);

        UserName.setText(user.getUserName());
        Email= findViewById(R.id.tv_account_Current_email);
        Password= findViewById(R.id.tv_account_Current_password);
        FirstName= findViewById(R.id.tv_account_FirstName);
        LastName= findViewById(R.id.tv_account_LastName);

         MaxMile = findViewById(R.id.tv_account_mileTime);
         MaxBench = findViewById(R.id.tv_account_maxBench);
         MaxSquat = findViewById(R.id.tv_account_maxSquat);
         MaxDeadlift = findViewById(R.id.tv_account_maxDeadlift);
        // User init
        userInit();

        BackToMain = (Button) findViewById(R.id.bt_account_ToMain);

        BackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("UserClass", user);
                v.getContext().startActivity(intent);
            }
        });


        btn_UpdateMax = (Button) findViewById(R.id.bt_account_maxUpdate);

        btn_UpdateMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!Email.getText().toString().equals("")) {
                        user.setEmail(Email.getText().toString());
                    }
                    if (!Password.getText().toString().equals("")) {
                        user.setPassword(Password.getText().toString());
                    }
                    if (!FirstName.getText().toString().equals("")) {
                        user.setFirstName(FirstName.getText().toString());
                    }
                    if (!LastName.getText().toString().equals("")) {
                        user.setLastName(LastName.getText().toString());
                    }

                    if (!MaxBench.getText().toString().equals("")) {
                        user.setMaxWeightBench(Integer.parseInt(MaxBench.getText().toString()));
                    }
                    if (!MaxDeadlift.getText().toString().equals("")) {
                        user.setMaxWeightDeadlift(Integer.parseInt(MaxDeadlift.getText().toString()));
                    }
                    if (!MaxSquat.getText().toString().equals("")) {
                        user.setMaxWeightSquant(Integer.parseInt(MaxSquat.getText().toString()));
                    }
                    if (!MaxMile.getText().toString().equals("")) {
                        user.setBestTimeMile(Integer.parseInt(MaxMile.getText().toString()));
                    }
                    Map<String,Object> updates = new HashMap<>();
//
                    updates.put("email", user.getEmail());
                    updates.put("firstName",user.getFirstName());
                    updates.put("lastName", user.getLastName());
                    updates.put("password", user.getPassword());
                    updates.put("maxWeightBench", user.getMaxWeightBench());
                    updates.put("maxWeightSquant", user.getMaxWeightSquant());
                    updates.put("maxWeightDeadlift", user.getMaxWeightDeadlift());
                    updates.put("bestTimeMile", user.getBestTimeMile());

                    userQuery = userCollection.whereEqualTo("userName", user.getUserName()).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0);
                                    DocumentReference docRef = userCollection.document(snapshot.getId());
                                    docRef.update(updates);
                                    //Toast if there is an issue
                                    Toast.makeText(getApplicationContext(), "Update successful", Toast.LENGTH_LONG).show();
                                }
                            });

                }catch (Exception e){
                    String[] parts = e.toString().split(":");

                    //Toast if there is an issue
                    Toast.makeText(v.getContext(), "Because " + parts[1].trim()+" Update was not possible!", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
    private void userInit() {

        MaxMile.setText(""+ user.getBestTimeMile());
        MaxBench.setText(""+ user.getMaxWeightBench());
        MaxSquat.setText(""+ user.getMaxWeightSquant());
        MaxDeadlift.setText(""+ user.getMaxWeightDeadlift());
        Email.setText(user.getEmail());
        Password.setText(user.getPassword());
        FirstName.setText(user.getFirstName());
        LastName.setText(user.getLastName());

    }
}