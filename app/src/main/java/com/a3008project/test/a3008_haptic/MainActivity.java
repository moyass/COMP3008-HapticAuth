package com.a3008project.test.a3008_haptic;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
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
 * Usage
 * User opens the app
 *  - Option to Generate new password or manual create
 *   - Manually Create
 *      - Prompted to "Create password"
 *      - Prompted to "Enter the password again"
 *      - Prompted to "Create another password for (Shopping, Email, Bank)"
 *      - Prompted to "Enter the password for (Shopping, Email, Bank)"
 *
 *   - Generate Password
 *      - User is given a timer before the password is generated
 *      - User gets feedback from both a blinking box and haptic feedback
 *      - User is then asked to re-enter the password
 *
 * TODO: - Implement User and remove the listOfPatterns arraylist
 *       - Implement a category choice to set a sequence for each category (shop, email, bank..etc)
 *
 * */



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEBUG";
    //ArrayList<Pattern> listOfPatterns = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();

    Button bigTapTap, createSequence, loginButton;
    ImageButton ProfileButton;
    Boolean START_TIMER = false, CREATE_NEW = false;
    Pattern currentInputPattern;
    User currentUser = new User();
    long oldTime, currentTime;
    long currentInterval;
    int numberOfTaps = 0;

    Logger logger = new Logger("userdata.csv");

    long maxTime = (10*1000);


    TextView statusText, userName;


    ArrayList<Long> intervalsBetweenTaps = new ArrayList<>();

    Vibrator vibrator;

    // Timer to make sure that the user can only use up to 10 seconds or whatever maxTime is
    Timer stopWatch;

    /**
    * Store the input as follows:
    *   1. A,B,C,D for the selected box
    *   2. Record time for each tap and convert it to dots '.'
    *   3. Append them to input_sequence
    *   4. dis is the earf
    **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load an activity from res/layouts
        setContentView(R.layout.activity_main);

        // Collect the elements from the activity and store them into objects
        bigTapTap = findViewById(R.id.button1);
        bigTapTap.setEnabled(false);

        // Create a sequence
        createSequence = findViewById(R.id.createButton);

        // Login using a sequence you created earlier
        loginButton = findViewById(R.id.loginButton);

        // The text between the big bigTapTap and the profile picture
        statusText = findViewById(R.id.countDown);
        statusText.setText("");

        // Username text object
        userName = findViewById(R.id.usernameText);

        // Change username
        ProfileButton = findViewById(R.id.imageButton);

        // Generate a username every time the app is launched
        currentUser.generateUserName();

        // Set the initial username since the app has started
        userName.setText(currentUser.getUsername());
        System.out.println("DEBUG: User name is "+  currentUser.getUsername());

        logger.writeToFile("SUCK A DICK", true);

        // Initialize the vibrator
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        stopWatch = new Timer(statusText, maxTime);

        final CountDownTimer cT =  new CountDownTimer(maxTime, 1000) {

            public void onTick(long millisUntilFinished) {
                //hasTimerStarted = true;
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished % 60000) / 1000);
                statusText.setText("seconds remaining: " + v + ":" + String.format("%02d", va));
            }

            public void onFinish() {
                //hasTimerStarted = false;
                statusText.setText("Press one more time to confirm input");

            }
        };



        // Create button
        createSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Creating a new sequence!");

                CREATE_NEW = true;
                START_TIMER = true;

                userName.setText(currentUser.getUsername());
                bigTapTap.setEnabled(true);


            }
        });


        // Create a new user
        // TODO: Show dialog saying are you sure
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            System.out.println("DEBUG: Creating a new user. Resetting all fields");
                            CREATE_NEW = true;
                            START_TIMER = true;
                            currentUser = new User();
                            userName.setText(currentUser.getUsername());
                            //stopWatch.cT.cancel();
                            cT.cancel();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            System.out.println("DEBUG: Canceled the making of a new user.");
                            break;
                    }
                }
            };

            @Override
            public void onClick(View v) {
                Context context  = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Create new user")
                        .setMessage("This will store the previous user in the database and make a new user.")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();

            }
        });

        // Main Button aka the tap tap
        bigTapTap.setOnClickListener(new View.OnClickListener() {

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
                 * or user has not tapped the the bigTapTap in 10 seconds
                 */

                if (!START_TIMER) {
                    START_TIMER = true;
                    oldTime = System.currentTimeMillis();

                    //stopWatch.cT.start();
                    //stopWatch.start();
                    cT.start();
                    numberOfTaps++;
                    System.out.println("DEBUG: Intial Tap. NOT = " + numberOfTaps);


                } else if (System.currentTimeMillis() - oldTime >= maxTime) {
                    oldTime = 0;
                    currentTime = 0;
                    START_TIMER = false;
                    CREATE_NEW = false;
                    numberOfTaps = 0;

                    /**
                     * Assuming the user has completed his input
                     * We need to now store a Pattern based on his input
                     */

                    // Create a pattern based on the current input
                    currentInputPattern = new Pattern(intervalsBetweenTaps,numberOfTaps);
                    currentUser.setPattern(currentInputPattern);

                    // Add
                    users.add(currentUser);

                    // Clear
                    intervalsBetweenTaps.clear();

                } else {
                    numberOfTaps++;
                    // It has been tapped at least once so far
                    currentTime = System.currentTimeMillis();
                    currentInterval = currentTime - oldTime;
                    intervalsBetweenTaps.add(currentInterval);
                    System.out.println("DEBUG: Current Interval " + (currentInterval) + " NOT = "+numberOfTaps);
                }



            }
        });
    }

}