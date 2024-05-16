package com.example.project2.data.model;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Workout {

    public String workoutName;

    public int time;
    public String dayOfWeek;


    public Workout() {}

    public Workout(String workoutName, String dayOfWeek){
        this.workoutName = workoutName; // may get rid of later
        this.dayOfWeek = dayOfWeek;

    }
    public Workout(String workoutName, String dayOfWeek, int time){
        this.workoutName = workoutName; // may get rid of later
        this.time = time;
        this.dayOfWeek = dayOfWeek;

    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String Name) {
        this.workoutName = Name;
    }
    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }


   // public void setType(String Type) {this.Type = Type;}
}
