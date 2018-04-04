package com.a3008project.test.a3008_haptic;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by mohamad on 26/03/18.
 */

public class User {

    final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String  username = null;

    // Using hashmap to store "shopping" and it's pattern
    public HashMap<String,Pattern> sequences = new HashMap();

    private Pattern sequence = new Pattern(); // generate a password
    RandomString gen;

    private static int usernameLength = 7;

    public User () {
        generateUserName(false);
    }

    public void setPattern(Pattern input){
        sequence  = input;
        System.out.println("DEBUG: User '" + username + "' sequence has been stored.");
    }

    public void setUsername(String input){
        username  = input;
        System.out.println("DEBUG: Users username has been set to " + username);
    }

    public String getUsername(){
        return username;
    }


    // Generate a username that is 7 characters long
    public void generateUserName(boolean force) {
        if(!force) {
            if (username != null) {
                System.out.println("DEBUG: User already has a username. (" + username + ").");
                return;
            }
        }

        gen = new RandomString(usernameLength, ThreadLocalRandom.current());

        username = gen.nextString();

        return;
    }

}
