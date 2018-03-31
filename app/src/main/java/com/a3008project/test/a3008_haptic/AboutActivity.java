package com.a3008project.test.a3008_haptic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

    }
    @Override
    public void finish() {
        // do something on back.
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "WOOOOOO", Toast.LENGTH_SHORT).show();
        return;
    }

}
