package com.example.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.processing.ProcessingQueue;
import com.inverce.mod.processing.Processor;
import com.inverce.mod.processing.QueueListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements Intre {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        List<String> list = Arrays.asList(
                "1", "2", "3", "4", "1", "2", "3", "4", "1", "2",
                "3", "4", "1", "2", "3", "4", "1", "2", "3", "4");

        Processor<String, Integer> processor = p -> {
            Thread.sleep(2000);
            Log.w("22");
            return Integer.parseInt(p);
        };

        ProcessingQueue queue = ProcessingQueue.create()
                .setFailureAction(ProcessingQueue.FailureAction.IGNORE)
                .setContinuous(false)
                .process(processor, list)
                .setListener(new QueueListenerAdapter() {
                        @Override
                        public void onQueueFinished(@NonNull ProcessingQueue queue) {
                            super.onQueueFinished(queue);
                            Log.w("Processing: " + queue.getProcessing().size());
                            Log.w("Awaiting: " + queue.getAwaiting().size());
                            IM.onBg().schedule(() -> queue.process(processor, list), 5, TimeUnit.SECONDS);
                        }

                       
                });

        queue.start();

//        SharedCipherValue d1 = new SharedCipherValue("key", "cipher1");
//        d1.set("value");
//
//        SharedCipherValue d2 = new SharedCipherValue("key", "cipher1");
//
//        Log.w("LL: " + d1.get() + " " + d2.get());
    }

    @Override
    public void pp() {
        Log.w("PP");
    }
}
