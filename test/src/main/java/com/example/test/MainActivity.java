package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.logging.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IM.initializeInProcess(this);
        setContentView(R.layout.activity_main);
        jj j = jj.newInstance(jj.class);
        Log.a("aa");
        j.getActions().pp();
        Log.a("aa");
    }
}
