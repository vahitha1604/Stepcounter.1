package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager mSensorManger = null;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previewsTotalStep = 0;
    private ProgressBar ProgressBar;
    private TextView steps;





    protected void onResume(){
        super.onResume();

        if (stepSensor == null){
            Toast.makeText(this, "This device has no sensor", Toast.LENGTH_LONG).show();
        }
        else {
            mSensorManger.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    protected void onPause(){
        super.onPause();
        mSensorManger.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()==Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) event.values[0];
            int currentSteps = totalSteps-previewsTotalStep;
            steps.setText(String.valueOf(currentSteps));

            ProgressBar.setProgress(currentSteps);
        }
    }

    private void resetSteps() {
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Toast.makeText(MainActivity.this, "Long press to reset steps", Toast.LENGTH_SHORT).show();

            }
        });

        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View V) {
                previewsTotalStep = totalSteps;
                steps.setText("0");
                ProgressBar.setProgress(0);
                saveData();
                return true;
            }
        });
    }
    private void saveData(){
        SharedPreferences sharedPref = getSharedPreferences(  "myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("key",String.valueOf(previewsTotalStep));


    }
    private void loadData(){
        SharedPreferences sharedPref = getSharedPreferences(  "myPref", Context.MODE_PRIVATE);
        float Of = 0;
        int savedNumber = (int) sharedPref.getFloat("key1", Of);
        previewsTotalStep = savedNumber;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){

}
}