package com.a3008project.test.a3008_haptic;

import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENTATION FOR VIBRATION
 * https://developer.android.com/reference/android/os/VibrationEffect.html
 * */

public class MainActivity extends AppCompatActivity {

    Button one,two,three,four;
    Vibrator vibrator;
    // TODO: Implement touch and hold input to include the time component.

    /**
    * Store the input as follows:
    *   1. A,B,C,D for the selected box
    *   2. Record time for each tap and convert it to dots '.'
    *   3. Append them to input_sequence
    *   4. dis is the earf
    * */

    List<String> input_sequence; // KEY1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load an activity from res/layouts
        setContentView(R.layout.activity_main);

        // Collect the elements from the activity and store them into objects
        one = findViewById(R.id.button1);
        two = findViewById(R.id.button2);
        three = findViewById(R.id.button3);
        four = findViewById(R.id.button4);

        // Initialize the vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // THE FOLLOWING ONLY WORKS FOR API 26 (Android 8.0)
        // The testing device does not have API 26 and I don't think many do either
        // I'll keep this here until one of ya'll deletes it xD
        //vibrator.hasAmplitudeControl();



        one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                long [] test = {10,77,700};
                long[] mVibratePattern = new long[]{0, 400, 200, 400};
                vibrator.vibrate(mVibratePattern, -1);

            }
        });

        two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                long [] test = {10,37,200};
                vibrator.vibrate(test,2);

            }
        });


        three.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                long[] mVibratePattern = new long[]{0, 400, 800, 600, 800, 800, 800, 1000};
                vibrator.vibrate(mVibratePattern, -1);

            }
        });


        four.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                long [] test = {200,100,150,200};
                vibrator.vibrate(test, -1);
            }
        });

    }
}