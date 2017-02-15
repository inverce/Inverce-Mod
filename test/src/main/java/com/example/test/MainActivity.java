package com.example.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IMInitializer.initialize(this);
        setContentView(R.layout.activity_main);



        final Handler handler = new Handler(Looper.getMainLooper());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("d");
                for (int i=0;i<1000; i++) {
                    IM.onBg().schedule(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 1500, TimeUnit.MILLISECONDS);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }
}
