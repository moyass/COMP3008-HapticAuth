package com.a3008project.test.a3008_haptic;

import java.util.HashMap;

/**
 * Created by mohamad on 26/03/18.
 */

public class User {
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private String  username = null;

    // Using hashmap to store "shopping" and it's pattern
    public HashMap<String,Pattern> sequences = new HashMap();

    private Pattern sequence = new Pattern(); // generate a password

    private int usernameLength = 7;

    public User () {
        generateUserName();
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
    public void generateUserName() {
        if (username != null) {
            System.out.println("DEBUG: User already has a username. (" + username + ").");
            return;
        }
        StringBuilder builder = new StringBuilder();
        while (usernameLength-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        username = builder.toString();
        return;
        //return builder.toString();
    }

}
