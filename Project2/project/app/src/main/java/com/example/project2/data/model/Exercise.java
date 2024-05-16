package com.example.project2.data.model;

public class Exercise {

    /*
    Users of this app should be able to create individual workouts of type cardio and lifitng weights
    Cardio workouts will have a name and duration (minutes), and rest time possibly add more here (speed intensity etc0
    Lifting weights workouts will have a name, weight, reps, rest time
     */
    private String name;
    private String type;

    private int weight;

    private int reps;

    private Long time;

    private Long rest; // seconds

    public Exercise(String workoutType){
        type = workoutType;

        if(type.equals("Cardio")){
            name = "";  //FINISH


        }
        if(type.equals("Weight Lifting")){
            name = ""; //Finish
        }
    }
    public Exercise() {
        type = "";
        name = "";
        reps =0;
        time = 0L;
        rest = 0L;
        weight = 0;

    }


    /////
    ///// getters and setters for cardio
    ////
    public void setTime(Long seconds){
        time = seconds;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){return type;}

    //returns rest time in seconds
    public Long getTime(){
        return time;
    }


    /////
    ///// getters and setters for weight lifting
    ////
    //set the amount of rest time for the exercise
    public void setRest(Long seconds){
        rest = seconds;
    }

    //returns rest time in seconds
    public Long getRest(){
        return rest;
    }

    //sets weight
    public void setWeight(int lbs){
        weight = lbs;
    }

    //returns weight
    public int getWeight(){
        return weight;
    }


    //sets reps
    public void setReps(int repetitions){
        reps = repetitions;
    }

    //returns reps
    public int getReps(){
        return reps;
    }
}
