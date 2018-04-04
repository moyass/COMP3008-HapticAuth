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

    // DISCUS WITH THE REST OF THE GROUP
    public int UpperGenerateBound = 6;
    public int LowerGenerateBound = 4;
    public int UpperIntervalBound = 1000;
    public int LowerIntervalBound = 200;

    double marginOfError = 0.35;

    Pattern (ArrayList<Long> inputRatios, int inputNumberOfTaps){
        RatioList = inputRatios;
        numberOfTaps = inputNumberOfTaps;
    }

    Pattern(){}

    Pattern (int donothing){
        Generate();
    }

    public void Generate() {
        // In the case a ratio list isn't included, we should by default generate a new pattern
        Random rand = new Random();

        long interval;
        int NOT = rand.nextInt(UpperGenerateBound - LowerGenerateBound) + LowerGenerateBound;

        numberOfTaps = NOT;
        for(int i = 2; i <= NOT; i++){
            interval = rand.nextInt(UpperIntervalBound - LowerIntervalBound) + LowerIntervalBound;
            Log.d ("DEBUG"," interval at " + (i - 1) + " is " + interval);


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

        // Store it locally so we don't need to access multiple methods to get the value

        int size = RatioList.size();

        if(input.getRatioList().size() != RatioList.size()){
            System.out.println("DEBUG: The input ratio list is not the same. [DIFF SIZES]");
            return false;
        }



        // Store values for differences between local and comparative values
        double ratiosBetweenintervals[] = new double [size];
        double result = -1;

        // Compare local values with input.
        for (int i = 0; i < size; i++){
            ratiosBetweenintervals[i] = (double) input.getRatioList().get(i) / (double) RatioList.get(i);
            Log.d("DEBUG","\nGenerated : " + input.getRatioList().get(i) + " \nUser " + RatioList.get(i) + " Diff: "+ ratiosBetweenintervals[i]);
        }

        // Compare all the values
        double controlRatio = ratiosBetweenintervals[0];

        for (int i = 0; i < size; i++){
            if ((Math.abs(ratiosBetweenintervals[i] - controlRatio) > marginOfError)) {
                Log.d("COMPARE RESULT", "Failed");
                return false;
            }
        }
        Log.d("COMPARE RESULT", "Success");
        return true;
    }
}
