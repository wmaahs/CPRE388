package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.Sensor;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private TextView step_amnts;

    private SensorManager sensorManager;
    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;

    private Integer steps = 0;

    private float[] gravAccel;
    private float[] accel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        step_amnts = findViewById(R.id.TV_step_amnts);
        step_amnts.setText(Integer.toString(steps));

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensorManager.registerListener((SensorEventListener) this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 0.8F;

            gravAccel[0] = alpha + gravAccel[0] + (1-alpha) * sensorEvent.values[0];
            gravAccel[1] = alpha + gravAccel[1] + (1-alpha) * sensorEvent.values[1];
            gravAccel[2] = alpha + gravAccel[2] + (1-alpha) * sensorEvent.values[3];

            accel[0] = sensorEvent.values[0] - gravAccel[0];
            accel[1] = sensorEvent.values[1] - gravAccel[1];
            accel[2] = sensorEvent.values[2] - gravAccel[2];

            if((accel[0] > 0.1) && (accel[0] < 0.7) && (accel[1] > 0.1) && (accel[1] < 0.7) && (accel[2] > 0.1) && (accel[2] < 0.7)){
                incrementStep();

            }

        }
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void incrementStep() {
        steps++;
        step_amnts.setText(Integer.toString(steps));

    }
}