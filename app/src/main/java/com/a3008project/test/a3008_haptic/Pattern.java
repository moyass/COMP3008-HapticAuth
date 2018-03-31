package com.a3008project.test.a3008_haptic;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mohamad on 26/03/18.
 */

public class Pattern {

    private ArrayList<Long> RatioList = new ArrayList<>();
    int numberOfTaps = 0;
    private Timer timer = new Timer(5);

    // DISCUS WITH THE REST OF THE GROUP
    private int UpperGenerateBound = 3;
    private int LowerGenerateBound = 1;
    private int UpperIntervalBound      = 2000;
    private int LowerIntervalBound      = 500;



    Pattern (ArrayList<Long> inputRatios, int inputNumberOfTaps){
        RatioList = inputRatios;
        numberOfTaps = inputNumberOfTaps;
    }
    Pattern(){

    }
    Pattern (int donothing){
        Generate();
    }

    public void Generate() {
        // In the case a ratio list isn't included, we should by default generate a new pattern
        Random rand = new Random();

        long interval;
        int NOT = rand.nextInt(UpperGenerateBound) + LowerGenerateBound;
        numberOfTaps = NOT;
        for(int i = 1; i <= NOT; i++){
            interval = rand.nextInt(UpperIntervalBound) + LowerIntervalBound;
            Log.d ("DEBUG"," interval at " + i + " is " + interval);


            RatioList.add(interval);
        }

        return;
    }

    public ArrayList<Long> getRatioList() {
        return RatioList;
    }


    public boolean Compare(Pattern input) {
        // TODO: FIX ALGO
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
            differencesBetweenintervals[i] = (input.getRatioList().get(i) / RatioList.get(i));
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
