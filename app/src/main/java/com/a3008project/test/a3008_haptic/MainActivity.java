package com.a3008project.test.a3008_haptic;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
//import android.widget.Toolbar;

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
    Button bigTapTap, createSequence, loginButton, generatePassword, aboutButton;
    ImageButton ProfileButton;
    Boolean START_TIMER = false, CREATE_NEW = false;
    Pattern currentInputPattern = new Pattern();
    Pattern currentGeneratedPattern = new Pattern();
    String selectedCategory = "Nothing";
    User currentUser = new User();
    long oldTime, currentTime;
    long currentInterval;
    long maxTime = (5*1000);
    Logger logger = new Logger();
    TextView statusText, userName;
    ArrayList<Long> intervalsBetweenTaps = new ArrayList<>();
    Vibrator vibrator;


    private View.OnClickListener createButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CreateButtonClicked();
        }
    };

    private View.OnClickListener BigTapTapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BigTapTapTapped();
        }
    };

    private View.OnClickListener GenerateButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GenerateButtonClicked();
        }
    };

    private View.OnClickListener AboutButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AboutButtonClick();
        }
    };



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

        // Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // About button
        aboutButton = findViewById(R.id.about_button);

        // Generate a username every time the app is launched
        currentUser.generateUserName();
        currentInputPattern.numberOfTaps = 0;

        // Initialize log file
        logger.writeToFile(false);

        // Set the initial username since the app has started
        userName.setText(currentUser.getUsername());
        System.out.println("DEBUG: User name is "+  currentUser.getUsername());

        // Initialize the vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // DropDownMenu
        PopulateDropDownMenu();

        createSequence.setOnClickListener(createButtonListener);
        generatePassword.setOnClickListener(GenerateButtonListener);
        bigTapTap.setOnClickListener(BigTapTapListener);
        aboutButton.setOnClickListener(AboutButtonListener);


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


    }

    private void GenerateButtonClicked() {
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
        logger.Set(DataEnum.USER_ID, currentUser.getUsername());

    }

    private void BigTapTapTapped() {
        // Vibrate the phone for some haptic feedback for the user
        vibrator.vibrate(20);

        if (!START_TIMER) {
            START_TIMER = true;
            oldTime = System.currentTimeMillis();
            cT.start();
            currentInputPattern.numberOfTaps++;
            System.out.println("DEBUG: Intial Tap. NOT = " + currentInputPattern.numberOfTaps);

        } else {
            currentInputPattern.numberOfTaps++;

            // It has been tapped at least once so far
            currentTime = System.currentTimeMillis();
            currentInterval = currentTime - oldTime;
            Log.d("TIME_DEBUG", "Current time " + currentTime + " old time "+ oldTime + " = " + currentInterval);
            intervalsBetweenTaps.add(currentInterval);
            oldTime = currentTime;
            System.out.println("DEBUG: Current Interval " + (currentInterval) + " NOT = "+ currentInputPattern.numberOfTaps);

        }
    }

    private void CreateButtonClicked (){
        Log.d ("BOI", "BOI");
    }


    private CountDownTimer cT =  new CountDownTimer(maxTime, 10) {

        public void onTick(long millisUntilFinished) {
            String v = String.format("%02d", millisUntilFinished / 60000);
            int va = (int) ((millisUntilFinished % 60000) / 1000);
            statusText.setText("seconds remaining: " + v + ":" + String.format("%02d", va));
        }

        public void onFinish() {
            statusText.setText("Press one more time to confirm input");
            EndOfTime();

        }
    };

    private void EndOfTime(){
        String result = "failed";
        oldTime = 0;
        currentTime = 0;
        START_TIMER = false;
        CREATE_NEW = false;

        /**
         * Assuming the user has completed his input
         * We need to now store a Pattern based on his input
         */

        // Create a pattern based on the current input
        currentInputPattern = new Pattern(intervalsBetweenTaps,currentInputPattern.numberOfTaps);

        Log.d("DEBUG", "Generated number of taps is " + currentGeneratedPattern.numberOfTaps
                + " User input number of taps is " + currentInputPattern.numberOfTaps + " \n");

        if(currentInputPattern.Compare(currentGeneratedPattern)) {
            result = "success";
        }

        currentUser.sequences.put(selectedCategory,currentInputPattern);

        Log.d("DEBUG", "PUT into hash map " + selectedCategory
                + ", current interval "+ currentInputPattern);

        logger.Set(DataEnum.RESULT, result);
        logger.Set(DataEnum.SEQUENCE, currentInputPattern.getRatioList().toString());

        logger.writeToFile(true);

        currentInputPattern.numberOfTaps = 0;

        // Add
        users.add(currentUser);

        // Clear
        intervalsBetweenTaps.clear();
    }

    private void PopulateDropDownMenu(){
        //------------------------------------------------------------------------------------------
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        //------------------------------------------------------------------------------------------
    }

    private void AboutButtonClick() {
        Toast.makeText(MainActivity.this, "Have not done that yet lol", Toast.LENGTH_SHORT).show();
        //setContentView(R.layout.about_layout);
    }

}