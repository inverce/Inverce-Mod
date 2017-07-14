package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.Log;
import com.inverce.mod.processing.ProcessingQueue;

import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ProcessingQueue queue = ProcessingQueue.create()
                .setAsynchronous(true)
                .setFailureAction(ProcessingQueue.FailureAction.ABORT)
                .process(p -> {
                    Log.w("Job started: " + p);
                    Thread.sleep(3000);
                    Log.w("Job finishing: " + p);
                    return Integer.parseInt(p);
                }, Collections.singletonList("33"))

                ;

        queue.start();
    }

}
