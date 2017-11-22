package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.Log;


public class MainActivity extends AppCompatActivity implements Intre {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void pp() {
        Log.w("PP");
    }
}
