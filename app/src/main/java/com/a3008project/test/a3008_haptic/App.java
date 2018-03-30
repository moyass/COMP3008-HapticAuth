package com.a3008project.test.a3008_haptic;

import android.app.Application;
import android.content.Context;

/**
 * Created by mohamad on 29/03/18.
 */

public class App extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
