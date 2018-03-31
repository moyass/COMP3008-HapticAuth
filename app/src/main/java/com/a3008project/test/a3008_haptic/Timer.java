package com.a3008project.test.a3008_haptic;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by mohamad on 27/03/18.
 */

public class Timer {

    private TextView statusText ;
    private long maxTime;
    private boolean hasTimerStarted = false;

    public Timer (long inputMaxTime){
        maxTime = inputMaxTime;
    }

    public Timer (TextView text, long inputMaxTime){
        maxTime = inputMaxTime;
        statusText = text;

    }

    // Count down to limit the space to 10 seconds per sequence
    private CountDownTimer cT =  new CountDownTimer(maxTime, 1000) {

        public void onTick(long millisUntilFinished) {
            hasTimerStarted = true;
            String v = String.format("%02d", millisUntilFinished / 60000);
            int va = (int) ((millisUntilFinished % 60000) / 1000);
            statusText.setText("seconds remaining: " + v + ":" + String.format("%02d", va));
        }

        public void onFinish() {
            hasTimerStarted = false;
            statusText.setText("Press one more time to confirm input");
        }
    };

    public int start(){
        cT.start();
        return 0;
    }

    public boolean getRunningStatus() {
        return hasTimerStarted;
    }



}
