package com.a3008project.test.a3008_haptic;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mohamad on 26/03/18.
 */

public class Database {

    private int USER_ID_COUNT = 0;

    // To be used in a bit
    HashMap<Integer,User> USER_DB = new HashMap<Integer, User>();


    public void add(User input){
        USER_DB.put(USER_ID_COUNT, input);
        Log.d("DEBUG", "USER ADDED "+ USER_ID_COUNT +" "+ input.getUsername());
        USER_ID_COUNT++;
    }

}
