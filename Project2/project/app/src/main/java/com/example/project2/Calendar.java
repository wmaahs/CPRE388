package com.example.project2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Calendar extends AppCompatActivity {


    // Define the variable of CalendarView type
    // and TextView type;
    CalendarView calendar;
    TextView date_view;

    TextView wkout_date;

    Button toMain;

    private String USERNAME;

    UserClass user;

    private FirebaseFirestore workoutdb;
    public CollectionReference firebaseWorkout;

    String dow;// day of week for query

    String output;// output string for workout Textbox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //initializes firestore, gets intent data from previous screen
        workoutdb = FirebaseUtil.getFirestore();
        user = new UserClass();
        Intent fromListIntent = getIntent();
        user = (UserClass) fromListIntent.getParcelableExtra("UserClass");

        firebaseWorkout = workoutdb.collection("__" + user.getUserName() + "__Workouts");
        USERNAME = user.getUserName();
        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        toMain = findViewById(R.id.btn_cal_toMain);
        calendar = (CalendarView)
                findViewById(R.id.calendar);

        calendar.setDate(System.currentTimeMillis(),false,true);
        date_view = (TextView)
                findViewById(R.id.date_view);
        wkout_date = findViewById(R.id.workout_for_date);

        // Add Listener in calendar
        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String Date
                                        = dayOfMonth + "-"
                                        + (month + 1) + "-" + year;

                                // set this date in TextView for Display
                                date_view.setText(Date);

                                //sets format of date so it can be parsed correctly
                                DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-uuuu");

                                //parse as date, clear output text, and call printWorkout
                                LocalDate date = LocalDate.parse(Date,df);
                                DayOfWeek Tempday = date.getDayOfWeek();
                                output = "";
                                printWorkout(Tempday);
                            }
                        });
        toMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toMainIntent = new Intent(view.getContext(), MainActivity.class);
                // Add put extras with UserClas
                toMainIntent.putExtra("UserClass", user);
                // Start Activity
                view.getContext().startActivity(toMainIntent);
            }
        });
    }


    //print workout based on day of the week from success listener
    public void printWorkout(DayOfWeek Day){
        if(DayOfWeek.SUNDAY.equals(Day)){
            dow = "Sunday";
        }
        if(DayOfWeek.MONDAY.equals(Day)){
            dow = "Monday";
        }
        if(DayOfWeek.TUESDAY.equals(Day)){
            dow = "Tuesday";
        }
        if(DayOfWeek.WEDNESDAY.equals(Day)){
            dow = "Wednesday";
        }
        if(DayOfWeek.THURSDAY.equals(Day)){
            dow = "Thursday";
        }
        if(DayOfWeek.FRIDAY.equals(Day)){
            dow = "Friday";
        }
        if(DayOfWeek.SATURDAY.equals(Day)){
            dow = "Saturday";
        }
        wkout_date.setText("");
        Task<QuerySnapshot> query = firebaseWorkout.whereEqualTo("dayOfWeek", dow).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot docSnap : queryDocumentSnapshots.getDocuments()){
                    if(docSnap.get("workoutName").equals(null)){
                        //If there is no workouts that are assigned to a day
                        //then nothing needs to happen
                    }
                    else{
                        //make sure to display all workouts from same day, on newlines
                        output = output + docSnap.get("workoutName") + "\n";
                    }

                }
                //actually prints
                wkout_date.setText(output);
            }
        });
    }
}


