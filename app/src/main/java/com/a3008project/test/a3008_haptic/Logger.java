package com.a3008project.test.a3008_haptic;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by mohamad on 29/03/18.
 */

public class Logger {
    private static final String TAG = "FILEIO";
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date(); //dateFormat.format(date)

    String path = "/sdcard/";
    String filename = null;

    private String username;

    // "2017-07-05 14:49:36",
    // "ast103",
    // "HS-2017-103",
    // "text",
    // "N/A",
    // "register",
    // "success",
    // ""


    public Logger (String input){
        filename = path + input;
        writeToFile("", false);
    }


    // For some fucking reason does not work for other people
    public void writeToFile (String content, boolean append){
        if (filename == null) return;
        File sdCard = Environment.getExternalStorageDirectory();
        filename = filename.replace("/sdcard/", sdCard.getAbsolutePath());
        File tempFile = new File(filename);
        try
        {
            FileOutputStream fOut = new FileOutputStream(tempFile, append);
            content = content + "\n";
            byte[] contentInBytes = content.getBytes();

            fOut.write(contentInBytes);
            fOut.flush();
            fOut.close();
        }catch (Exception e)
        {
            Log.w(TAG, "FileOutputStream exception: - " + e.toString());
        }
        return;
    }

}
