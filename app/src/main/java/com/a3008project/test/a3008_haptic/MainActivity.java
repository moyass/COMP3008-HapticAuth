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
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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

    // Our primary database to store users
    Database users = new Database();

    Button bigTapTap, createSequence, loginButton, generatePassword;

    ImageButton ProfileButton;

    Boolean START_TIMER = false, CREATE_NEW = false;
    Pattern currentInputPattern = new Pattern();
    Pattern currentGeneratedPattern = new Pattern();

    String selectedCategory = "Nothing";

    User currentUser = new User();

    long oldTime, currentTime;
    long currentInterval;
    long maxTime = (5*1000);
    int  numberOfTaps = 0;


    Logger logger = new Logger();


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

        // Generate a sequence
        generatePassword = findViewById(R.id.generateButton);

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
        currentInputPattern.numberOfTaps = 0;

        // Initialize log file
        logger.writeToFile("",false);

        // Set the initial username since the app has started
        userName.setText(currentUser.getUsername());
        System.out.println("DEBUG: User name is "+  currentUser.getUsername());

        // Initialize the vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        stopWatch = new Timer(statusText, maxTime);

        final List<String> list = new ArrayList<String>();
        list.add("Bank");
        list.add("Email");
        list.add("Shopping");
        list.add("Social");
        list.add("Other");

        final Spinner sp1 = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adp1);

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getBaseContext(), list.get(position), Toast.LENGTH_SHORT).show();
                selectedCategory = list.get(position);
                Toast.makeText(getBaseContext(), "Press Generate to generate new password", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Generate Rhythmic Password and Vibrate
        generatePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(selectedCategory == "Nothing") {
                    Toast.makeText(getBaseContext(), "NO CATEGORY IS SELECTED", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getBaseContext(), "Generating new password.", Toast.LENGTH_SHORT).show();
                bigTapTap.setEnabled(true);
                userName.setText(currentUser.getUsername());

                Pattern testPattern = new Pattern(0);
                ArrayList<Long> temp = testPattern.getRatioList();

                Handler handler = new Handler();
                for (long element: temp){
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            vibrator.vibrate(50);
                        }
                    }, element);
                }

                currentGeneratedPattern = testPattern;

            }
        });

        // Create button
        createSequence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusText.setText("Creating a new sequence!");

                /*
                CREATE_NEW = true;
                START_TIMER = true;

                userName.setText(currentUser.getUsername());
                bigTapTap.setEnabled(true);
                logger.Set(DataEnum.USER_ID, currentUser.getUsername());
                logger.writeToFile(currentUser.getUsername(),true);
                */

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
                            logger.Set(DataEnum.USER_ID, currentUser.getUsername());
                            stopWatch.start();
                            //cT.cancel();
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
                 *
                 */
                long startTime = System.currentTimeMillis();
                if (!START_TIMER) {
                    START_TIMER = true;
                    oldTime = System.currentTimeMillis();

                    //stopWatch.cT.start();
                    stopWatch.start();
                    //cT.start();
                    currentInputPattern.numberOfTaps++;
                    System.out.println("DEBUG: Intial Tap. NOT = " + currentInputPattern.numberOfTaps);


                } else if (System.currentTimeMillis() - startTime >= maxTime) {
                    oldTime = 0;
                    currentTime = 0;
                    START_TIMER = false;
                    CREATE_NEW = false;
                    //numberOfTaps = 0;

                    /**
                     * Assuming the user has completed his input
                     * We need to now store a Pattern based on his input
                     */

                    // Create a pattern based on the current input
                    currentInputPattern = new Pattern(intervalsBetweenTaps,numberOfTaps);

                    Log.d("DEBUG", "Generated number of taps is " + currentGeneratedPattern.numberOfTaps
                    + " User input number of taps is " + currentInputPattern.numberOfTaps + " \n");

                    currentInputPattern.Compare(currentGeneratedPattern);

                    currentUser.sequences.put(selectedCategory,currentInputPattern);

                    Log.d("DEBUG", "PUT into hashmap " + selectedCategory + ", current interval "+ currentInputPattern);

                    logger.writeToFile(currentUser.getUsername(),true);

                    currentInputPattern.numberOfTaps = 0;

                    // Add
                    users.add(currentUser);

                    // Clear
                    intervalsBetweenTaps.clear();

                } else {
                    currentInputPattern.numberOfTaps++;

                    // It has been tapped at least once so far
                    currentTime = System.currentTimeMillis();
                    currentInterval = currentTime - oldTime;
                    Log.d("TIME_DEBUG", "Current time " + currentTime + " old time "+ oldTime + " = " + currentInterval);
                    intervalsBetweenTaps.add(currentInterval);
                    oldTime = currentTime;
                    System.out.println("DEBUG: Current Interval " + (currentInterval) + " NOT = "+currentInputPattern.numberOfTaps);

                }

            }
        });
    }

}