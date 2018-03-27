package com.a3008project.test.a3008_haptic;

import android.icu.util.DateInterval;

import java.util.ArrayList;

/**
 * Created by mohamad on 26/03/18.
 */

public class Pattern {

    ArrayList<Long> RatioList = new ArrayList<>();
    int numberOfTaps = 0;


    Pattern (ArrayList<Long> inputRatios, int inputNumberOfTaps){
        RatioList = inputRatios;
        numberOfTaps = inputNumberOfTaps;
    }

    Pattern (){
        Generate();
    }

    public void Generate() {
        // In charge of generating a pattern

        return;
    }

    public boolean Compare(Pattern input1, Pattern input2) {
        // Compare two patterns and see if they are similar
        // In the case someone types in their pattern slower

        return true;
    }
}
