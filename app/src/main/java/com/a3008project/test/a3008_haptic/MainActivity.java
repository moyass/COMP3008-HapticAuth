package com.a3008project.test.a3008_haptic;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
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
import java.util.Date;
import java.util.List;

/**
 * DOCUMENTATION FOR VIBRATION
 * https://developer.android.com/reference/android/os/VibrationEffect.html
 *
 *
 * LOGIC
 * User taps the square -> get current time
 *                                           ] -> firstInterval
 * User taps the square -> get current time
 *                                           ] -> firstInterval
 * User taps the square -> get current time
 * */

public class MainActivity extends AppCompatActivity {


    ArrayList<Pattern> listOfPatterns;

    Button button;
    Boolean START_TIMER = false;
    Pattern currentInputPattern;

    //long startTime = System.currentTimeMillis();
    long firstIterval;
    long maxTime = (10*1000);
    long elapsedTime = 0L;

    ArrayList<Long> intervalsBetweenTaps = new ArrayList<>();
    ArrayList<Integer> ints = new ArrayList<>();

    Vibrator vibrator;
    // TODO: Implement touch and hold input to include the time component.

    /**
    * Store the input as follows:
    *   1. A,B,C,D for the selected box
    *   2. Record time for each tap and convert it to dots '.'
    *   3. Append them to input_sequence
    *   4. dis is the earf
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load an activity from res/layouts
        setContentView(R.layout.activity_main);

        // Collect the elements from the activity and store them into objects
        button = findViewById(R.id.button1);

        // Initialize the vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Vibrate the phone for some haptic feedback for the user
                vibrator.vibrate(20);
                // Change the background color


                // Record the tap after the vibrator, since the vibrator has a slight delay
                // between tapping the actual screen and actually vibrating.
                // this will help get a more accurate recording

                if (!START_TIMER) {
                    START_TIMER = true;
                    firstIterval = System.currentTimeMillis();
                } else {
                    // It has been tapped at least once so far
                }

            }
        });

        // TODO: Fix dis
        button.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 10);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        System.out.println("OMG FINALLY!!!");
                        break;
                }
                return false;
            }

            Runnable mCountMili = new Runnable(){
                @Override public void run() {
                    // Record the time
                }
            };

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    System.out.println("HOLDING!!!");
                    mHandler.postDelayed(this, 10);
                }
            };

        });
    }
}