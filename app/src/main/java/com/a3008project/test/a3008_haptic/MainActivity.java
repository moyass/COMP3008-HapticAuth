package com.a3008project.test.a3008_haptic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
 *
 * */



public class MainActivity extends AppCompatActivity {

    // Our primary database to store users
    Database users = new Database();
    Button bigTapTap, repeatSequence, loginButton, generatePassword, aboutButton;
    CheckBox satView, satView2;
    boolean checkBoxChecked = false;
    ImageButton ProfileButton;
    Boolean START_TIMER = false, CREATE_NEW = false;
    Pattern currentInputPattern = new Pattern();
    Pattern currentGeneratedPattern = new Pattern();
    String selectedCategory = "Nothing";
    User currentUser = new User();
    long oldTime, currentTime;
    long currentInterval;
    long maxTime = (7*1000);
    Logger logger = new Logger();
    TextView statusText, userName, attemptText;
    ArrayList<Long> intervalsBetweenTaps = new ArrayList<>();
    Vibrator vibrator;

    Pattern testPattern;
    int numberOfAttempts = 0;
    boolean attemptGoingOn = false;


    private View.OnClickListener repeatButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RepeatButtonClicked();
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
        bigTapTap.setBackgroundColor(Color.LTGRAY);

        // Create a sequence
        repeatSequence = findViewById(R.id.createButton);

        // Generate a sequence
        generatePassword = findViewById(R.id.generateButton);

        // Login using a sequence you created earlier
        loginButton = findViewById(R.id.loginButton);

        // The text between the big bigTapTap and the profile picture
        statusText = findViewById(R.id.countDown);
        statusText.setText("");

        // Attempt Text
        attemptText = findViewById(R.id.attemptText);
        attemptText.setText("");

        // Username text object
        userName = findViewById(R.id.usernameText);

        // Change username
        ProfileButton = findViewById(R.id.imageButton);

        // Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // About button
        aboutButton = findViewById(R.id.about_button);

        // Checkbox
        satView = findViewById(R.id.checkBox);
        //satView2 = findViewById(R.id.checkBox2);


        // Generate a username every time the app is launched
        //currentUser.generateUserName(false);



        currentInputPattern.numberOfTaps = 0;

        // Initialize log file
        //logger.writeToFile(false);
        logger.Reset();
        logger.Set(DataEnum.USER_ID, currentUser.getUsername());
        logger.Set(DataEnum.SOUND,"false");
        logger.Set(DataEnum.ENVIRONMENT, "android");
        logger.Set(DataEnum.SCHEME, "rhythm");
        logger.Set(DataEnum.EVENT, "register");

        // Set the initial username since the app has started
        userName.setText(currentUser.getUsername());
        System.out.println("DEBUG: User name is "+  currentUser.getUsername());

        // Initialize the vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // DropDownMenu
        PopulateDropDownMenu();

        repeatSequence.setOnClickListener(repeatButtonListener);
        generatePassword.setOnClickListener(GenerateButtonListener);
        bigTapTap.setOnClickListener(BigTapTapListener);
        aboutButton.setOnClickListener(AboutButtonListener);


        satView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(satView.isChecked()){
                    checkBoxChecked = true;
                    logger.Set(DataEnum.SOUND,"true");
                }else{
                    checkBoxChecked = false;
                    logger.Set(DataEnum.SOUND,"false");
                }
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

                            // Check if the user is currently attempting to register / login or not
                            /** TODO:
                             * Currently just changes the username in text without actually creating
                             * a new user object. The problem is making sure the user can't create
                             * a new user while attempting to login...
                             * For now and for the sake of logs we can just change the username
                             * while we do the testing
                             */
                            /* UNCOMMENT ME TO FIX PREVIOUS TODO
                            if(!attemptGoingOn) {
                                statusText.setText("");   // Clear status text
                                currentUser = new User(); // Default Constructor generates username
                                userName.setText(currentUser.getUsername());
                                logger.Set(DataEnum.USER_ID, currentUser.getUsername());
                            } else {
                                Toast.makeText(MainActivity.this, "Cannot create user while attempting!", Toast.LENGTH_SHORT).show();
                            }
                             */
                            currentUser.generateUserName(true);
                            userName.setText(currentUser.getUsername());
                            logger.Set(DataEnum.USER_ID, currentUser.getUsername());


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

        statusText.setText("");
        Toast.makeText(getBaseContext(), "Generating new password.", Toast.LENGTH_SHORT).show();
        bigTapTap.setEnabled(true);
        bigTapTap.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        numberOfAttempts = 0;

        attemptText.setText("");


        testPattern = new Pattern(0);

        ArrayList<Long> temp = testPattern.getRatioList();

        for (int i = 0; i < temp.size(); i++) {
            vibrator.vibrate(20);
            if(checkBoxChecked) playTone(500, 0.20);
            try {
                Thread.sleep(temp.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        vibrator.vibrate(20);
        if(checkBoxChecked) playTone(500, 0.20);

        currentGeneratedPattern = testPattern;


        // Attempt is currently begun
        attemptGoingOn = true;


        // Logger CSV File
        logger.Set(DataEnum.MODE, "generate");
        logger.Set(DataEnum.CATEGORY, selectedCategory);

    }

    private void BigTapTapTapped() {
        // Vibrate the phone for some haptic feedback for the user

        {
            if (currentInputPattern.numberOfTaps == 0){
                cT.start();
            }
            //oldTime = System.currentTimeMillis();
            if (checkBoxChecked) playTone(500,0.20);
            vibrator.vibrate(20);


            currentInputPattern.numberOfTaps++;

            // It has been tapped at least once so far
            currentTime = System.currentTimeMillis();


            if(currentInputPattern.numberOfTaps > 1) {
                currentInterval = currentTime - oldTime;
                intervalsBetweenTaps.add(currentInterval);
                Log.d("NOOO", "Size of the interval list " + intervalsBetweenTaps.size()
                        +" current time " + currentTime + "  old " + oldTime
                        + " currentInterval " + currentInterval);
            }

            Log.d("TIME_DEBUG", "Current time " + currentTime + " old time "+ oldTime
                    + " = " + currentInterval);


            oldTime = currentTime;
            System.out.println("DEBUG: Current Interval " + (currentInterval) + " NOT = "
                    + currentInputPattern.numberOfTaps);

        }
    }

    private void RepeatButtonClicked(){

        ArrayList<Long> temp = testPattern.getRatioList();

        for (int i = 0; i < temp.size(); i++) {
            vibrator.vibrate(20);
            if(checkBoxChecked) playTone(500, 0.20);
            try {
                Thread.sleep(temp.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        vibrator.vibrate(20);
        if(checkBoxChecked) playTone(500, 0.20);

    }


    private CountDownTimer cT =  new CountDownTimer(maxTime, 10) {

        public void onTick(long millisUntilFinished) {
            String v = String.format("%02d", millisUntilFinished / 60000);
            int va = (int) ((millisUntilFinished % 60000) / 1000);
            statusText.setText("seconds remaining: " + v + ":" + String.format("%02d", va));
        }

        public void onFinish() {
            statusText.setText("");
            bigTapTap.setEnabled(false);
            bigTapTap.setBackgroundColor(Color.LTGRAY);
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
            statusText.setText("Success!!");
            attemptText.setText("Failed Attempts " + numberOfAttempts);
        } else {
            result = "failed";
            statusText.setText("Failed!!");
            attemptText.setText("Failed Attempts " + ++numberOfAttempts);
        }

        logger.Set(DataEnum.SEQUENCE, currentInputPattern.getRatioList().toString());
        logger.Set(DataEnum.GENSEQUENCE, currentGeneratedPattern.getRatioList().toString());
        logger.Set(DataEnum.USER_ID, currentUser.getUsername());
        logger.Set(DataEnum.RESULT, result);
        logger.Set(DataEnum.ATTEMPTS, String.valueOf(numberOfAttempts ));
        logger.Set(DataEnum.USERTAPS, String.valueOf(currentInputPattern.numberOfTaps));
        logger.Set(DataEnum.GENTAPS, String.valueOf(currentGeneratedPattern.numberOfTaps));

        bigTapTap.setEnabled(true);
        bigTapTap.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        currentInputPattern = new Pattern();

        currentUser.sequences.put(selectedCategory,currentInputPattern);

        Log.d("DEBUG", "PUT into hash map " + selectedCategory
                + ", current interval "+ currentInputPattern);



        logger.writeToFile(true);


        // Add
        users.add(currentUser);

        // Clear
        intervalsBetweenTaps.clear();

        if (result.compareTo("success") == 0){
            statusText.setText("Success!!");
            currentInputPattern.numberOfTaps = 0;

            bigTapTap.setEnabled(false);
            bigTapTap.setBackgroundColor(Color.LTGRAY);
            attemptText.setText("");
            return;
        }

        if(numberOfAttempts > 2) {
            numberOfAttempts = 0;
            statusText.setText("You have failed 3 attempts. Generate a new password");
            currentInputPattern.numberOfTaps = 0;

            bigTapTap.setEnabled(false);
            bigTapTap.setBackgroundColor(Color.LTGRAY);
            attemptText.setText("");
            attemptGoingOn = false;

            return;
        }

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

    public void playTone(double freqOfTone, double duration) {
        //double duration = 1000;                // seconds
        //   double freqOfTone = 1000;           // hz
        int sampleRate = 4800;              // a number

        double dnumSamples = duration * sampleRate;
        dnumSamples = Math.ceil(dnumSamples);
        int numSamples = (int) dnumSamples;
        double sample[] = new double[numSamples];
        byte generatedSnd[] = new byte[2 * numSamples];


        for (int i = 0; i < numSamples; ++i) {      // Fill the sample array
            sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalized.
        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        int i = 0 ;

        int ramp = numSamples / 20 ;                                    // Amplitude ramp as a percent of sample count


        for (i = 0; i< ramp; ++i) {                                     // Ramp amplitude up (to avoid clicks)
            double dVal = sample[i];
            // Ramp up to maximum
            final short val = (short) ((dVal * 32767 * i/ramp));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }


        for (i = i; i< numSamples - ramp; ++i) {                        // Max amplitude for most of the samples
            double dVal = sample[i];
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        for (i = i; i< numSamples; ++i) {                               // Ramp amplitude down
            double dVal = sample[i];
            // Ramp down to zero
            final short val = (short) ((dVal * 32767 * (numSamples-i)/ramp ));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }

        AudioTrack audioTrack = null;                                   // Get audio track
        try {
            int bufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                    sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT, bufferSize,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();                                          // Play the track
            audioTrack.write(generatedSnd, 0, generatedSnd.length);     // Load the track
        }
        catch (Exception e){
        }
        if (audioTrack != null) audioTrack.release();           // Track play done. Release track.
    }

}