package com.a3008project.test.a3008_haptic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

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
 *
 *
 * TODO: - Implement User and remove the listOfPatterns arraylist
 *       - Implement a category choice to set a sequence for each category (shop, email, bank..etc)
 *
 * */



public class MainActivity extends AppCompatActivity {
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    ArrayList<Pattern> listOfPatterns = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    Button button, createSequence, loginButton;
    Boolean START_TIMER = false, CREATE_NEW = false;
    Pattern currentInputPattern;
    User currentUser = new User();

    //long startTime = System.currentTimeMillis();
    long oldTime, currentTime;
    long currentInterval;

    long maxTime = (10*1000);


    TextView statusText, userName;


    ArrayList<Long> intervalsBetweenTaps = new ArrayList<>();
    ArrayList<Integer> ints = new ArrayList<>();

    Vibrator vibrator;

    /**
    * Store the input as follows:
    *   1. A,B,C,D for the selected box
    *   2. Record time for each tap and convert it to dots '.'
    *   3. Append them to input_sequence
    *   4. dis is the earf
    **/


    // Count down to limit the space to 10 seconds per sequence
    CountDownTimer cT =  new CountDownTimer(maxTime, 1000) {

        public void onTick(long millisUntilFinished) {

            String v = String.format("%02d", millisUntilFinished / 60000);
            int va = (int) ((millisUntilFinished % 60000) / 1000);
            statusText.setText("seconds remaining: " + v + ":" + String.format("%02d", va));
        }

        public void onFinish() {
            statusText.setText("Stop!!");
        }
    };

    // Generate a username that is 7 characters long
    public static String generateUserName() {
        int count = 7;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load an activity from res/layouts
        setContentView(R.layout.activity_main);

        // Collect the elements from the activity and store them into objects
        button = findViewById(R.id.button1);

        // Create a sequence
        createSequence = findViewById(R.id.createButton);

        // Login using a sequence you created earlier
        loginButton = findViewById(R.id.loginButton);

        // The text between the big button and the profile picture
        statusText = findViewById(R.id.countDown);

        // Username
        userName = findViewById(R.id.usernameText);
        //userName.setText(generateUserName());

        // Initialize the vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        createSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Creating a new sequence!");

                CREATE_NEW = true;
                START_TIMER = true;

                currentUser.setUsername((generateUserName()));

                userName.setText(currentUser.getUsername());
                //cT.start();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Vibrate the phone for some haptic feedback for the user
                vibrator.vibrate(20);

                /**
                 * LOGIC
                 * if timer is not started
                 * start timer and set flag
                 *
                 * if timer is started
                 * set current interval to old - new
                 * ... repeat until either time is up
                 * or user has not tapped the the button in 10 seconds
                 *
                 */

                if (!START_TIMER) {
                    START_TIMER = true;
                    oldTime = System.currentTimeMillis();
                    cT.start();

                } else if (System.currentTimeMillis() - oldTime >= maxTime) {
                    oldTime = 0;
                    currentTime = 0;
                    START_TIMER = false;
                    CREATE_NEW = false;
                    /**
                     * Assuming the user has completed his input
                     * We need to now store a Pattern based on his input
                     */

                    // Create a pattern based on the current input
                    currentInputPattern = new Pattern(intervalsBetweenTaps,0);


                    currentUser.setPattern(currentInputPattern);

                    // Total list of patterns
                    listOfPatterns.add(currentInputPattern);

                    // Clear
                    intervalsBetweenTaps.clear();

                } else {
                    // It has been tapped at least once so far
                    currentTime = System.currentTimeMillis();
                    currentInterval = currentTime - oldTime;
                    intervalsBetweenTaps.add(currentInterval);
                    System.out.println(" Current Interval " + (currentInterval));
                }



            }
        });
    }

}