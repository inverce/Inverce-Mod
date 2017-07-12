package com.example.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.Log;
import com.inverce.mod.integrations.processing.ProcessingQueue;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        new ProcessingQueue()
                .setAsynchronous(true)
                .setFailureAction(ProcessingQueue.FailureAction.ABORT)
                .process(p -> {
                    Log.w("Job started: " + p);
                    Thread.sleep(3000);
                    Log.w("Job finishing: " + p);
                    return Integer.parseInt(p);
                }, "33")
                .process((String s) -> {
                        Log.w("Job started: " + s);
                        Thread.sleep(3000);
                        Log.w("Job finishing: " + s);
                        return Integer.parseInt(s);
                }, Arrays.asList("1", "33", "26", "s", "55", "1", "33", "26", "s", "55", "1", "33", "26", "s", "55", "1", "33", "26", "s", "55"))
                .start();
    }

}
