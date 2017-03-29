package com.example.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.events.Event;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IMInitializer.initialize(this);
        setContentView(R.layout.activity_main);

        final Handler handler = new Handler(Looper.getMainLooper());

        Event<Intre> test = new Event<>(Intre.class);

        test.addListener(new IntreImpl());
        test.addListener(() -> Log.w("pp2"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("d");
                for (int i=0;i<1; i++) {
                    IM.onBg().schedule(() -> {
                        test.post().pp();
                        System.gc();
                    }, 1500, TimeUnit.MILLISECONDS);
                }
                handler.postDelayed(this, 2000);
            }
        }, 2000);

    }

    static class IntreImpl implements Intre {
        @Override
        public void pp() {
            Log.w("PP1");
        }
    }
}
