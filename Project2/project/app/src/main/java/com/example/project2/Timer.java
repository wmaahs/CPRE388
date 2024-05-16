package com.example.project2;

public class Timer {

    private long StartTime;
    private long TimerDuration;

    private long TimerRemaningTime;

    private boolean Running;
    private boolean Finished;
    // default constructor
    public Timer() {
        StartTime = 0;
        TimerDuration = 0;
        TimerRemaningTime = 0;
        Running = false;
        Finished = false;
    }

    // constructor that also starts the timer
    public Timer(long TimeDuration) {
        StartTime = 0;
        this.TimerDuration = TimeDuration;
        TimerRemaningTime = TimeDuration;
        Running = false;
        Finished = false;
    }

    // Starts timer with new duration
    public void Start(long TimeDuration) {
        StartTime = System.currentTimeMillis();
        this.TimerDuration = TimeDuration;
        TimerRemaningTime = TimeDuration;
        Running = true;
        Finished = false;
    }

    public void Start() {
        if(this.TimerDuration > 0) {
            Running = true;
            Finished = false;
            TimerRemaningTime = this.TimerDuration;
            StartTime = System.currentTimeMillis();
        }else{
            throw new IllegalArgumentException("Timer needs to have time greater then 0");
        }
    }

    public long getTimerDuration() {
        return TimerDuration;
    }

    public long getRemaningTime() {
        TimerEnded();
        return TimerRemaningTime;
    }
    public void setTimerDuration(long timerDuration) {
        this.TimerDuration = timerDuration;
    }

    // Resumes the timer after it was started
    public void Resume() {
        if (!Running && TimerRemaningTime < 0) {
            StartTime = System.currentTimeMillis();
            Running = true;
        }

    }
    // Restarts the Timer
    public void restart() {
        Start();

    }
    // Stops the timer
    public void stop() {
        if (Running && TimerRemaningTime < 0) {
            CheckTimer();
            Running = false;
        }

    }
    // checks if the timer is done
    private void CheckTimer() {
        TimerRemaningTime -= (System.currentTimeMillis() - StartTime);
        StartTime = System.currentTimeMillis();
        if (TimerRemaningTime <= 0) {
            Running = false;
            Finished = true;
        }
    }
    // returns if the timer ended
    public boolean TimerEnded() {
        if (!Finished && Running) {
            CheckTimer();
        }
        return Finished;
    }
    // to reset the timer to default sate
    public void resetTimer() {
        StartTime = 0;
        Running = false;
        Finished = false;
        TimerDuration = 0;
        TimerRemaningTime = 0;

    }

}
