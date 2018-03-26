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

public class MainActivity extends AppCompatActivity {

    Button one,two,three,four;
    Vibrator vibrator;

    // Store the input into this sequence like so
    List<String> input_sequence;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        one = findViewById(R.id.button1);
        two = findViewById(R.id.button2);
        three = findViewById(R.id.button3);
        four = findViewById(R.id.button4);

        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        //vibrator.hasAmplitudeControl();

        Parcelable.Creator<VibrationEffect> CREATOR;


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