package com.a3008project.test.a3008_haptic;


import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mohamad on 29/03/18.
 */

// DATE     "2017-07-05 14:49:36",
// USER ID  "ast103",
// SITE     "HS-2017-103",
// SCHEME   "text",
// MODE     "N/A",
// EVENT    "register",
// RESULT   "success",
// COOKIE   ""


public class Logger {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat FileNameDataFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private Date date = new Date(); //dateFormat.format(date)

    private String fileName = null;

    private File directory = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DOWNLOADS).getPath() + "/3008/");

    private File newFile = new File(directory, "userdata-"+ FileNameDataFormat.format(date) +".csv");

    private String  userID      = "JONDOE",
                    environment = "Android Application",
                    scheme      = "Rhythm matching",
                    mode        = "N/A",
                    event       = "N/A",
                    result      = "N/A",
                    cookie      = "N/A";


    public Logger (){
        InitializeFile();
        writeToFile("",false);
    }

    // Default constructor to specify only the file name.
    public Logger (String input){
        fileName = input;
        InitializeFile();
        writeToFile("",false);
    }

    public void Set (DataEnum dataEnum, String data){
        switch(dataEnum) {
            case USER_ID:
                userID = data;
                break;
            case ENVIRONMENT:
                environment = data;
                break;
            case SCHEME:
                scheme = data;
                break;
            case MODE:
                mode = data;
                break;
            case EVENT:
                event = data;
                break;
            case RESULT:
                result = data;
                break;
            case COOKIE:
                cookie = data;
                break;
        }
    }
    private void InitializeFile(){
        int count = 0;
        boolean something = false;

        if(!directory.exists()){
            Log.d("DEBUG","Directory does not exist. Attempting to directory file");
            directory.mkdir();
        }

        while (!something) {
            if (!newFile.exists()) {
                Log.d("DEBUG", "File does not exist. Attempting to create file");
                try {
                    newFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                something = true;
            } else {
                Log.d("DEBUG", "File exists. Attempting to create a new file");
                newFile = new File(directory, "userdata-" + count + ".csv");
                something = false;
                count++;
            }
        }
    }

    public void writeToFile (String data, boolean append){

        //userID = data;
        date = new Date();
        data = dateFormat.format(date) + ","
                + userID + ","
                + environment + ","
                + scheme + ","
                + mode + ","
                + event + ","
                + result + ","
                + cookie + "\n";

        try  {
            FileOutputStream fOut = new FileOutputStream(newFile, append);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(data);
            outputWriter.close();

            //display file saved message
            Toast.makeText(App.getContext(), "File saved successfully!",
                    Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
