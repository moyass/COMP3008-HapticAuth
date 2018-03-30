package com.a3008project.test.a3008_haptic;

import android.icu.util.DateInterval;

import java.util.ArrayList;

/**
 * Created by mohamad on 26/03/18.
 */

public class Pattern {

    private ArrayList<Long> RatioList = new ArrayList<>();
    int numberOfTaps = 0;


    Pattern (ArrayList<Long> inputRatios, int inputNumberOfTaps){
        RatioList = inputRatios;
        numberOfTaps = inputNumberOfTaps;
    }

    Pattern (){
        Generate();
    }

    public void Generate() {
        // In the case a ratio list isn't included, we should by default generate a new pattern


        return;
    }

    public ArrayList<Long> getRatioList() {
        return RatioList;
    }
    public boolean Compare(Pattern input) {
        // Compare two patterns and see if they are similar
        // In the case someone types in their pattern slower
        // sample: 940,1287,3312,3671,4797,5698,5999




        if(input.getRatioList().size() != RatioList.size()){
            System.out.println("DEBUG: The input ratio list is not the same. [DIFF SIZES]");
            return false;
        }

        // Store it locally so we don't need to access multiple methods to get the value
        int size = RatioList.size();

        // Store values for differences between local and comparative values
        long differencesBetweenintervals[] = new long [size];
        long result = -1;

        // Compare local values with input.
        for (int i = 0; i < size; i++){
            differencesBetweenintervals[i] = (input.getRatioList().get(i) - RatioList.get(i));
        }

        // Subtract all the values
        for (int i = 0; i < size; i++){
            result = differencesBetweenintervals[0] - differencesBetweenintervals[i];
        }


        // To be removed
        System.out.println("DEBUG: Result of comparing the patterns " + result);


        return false;
    }
}
