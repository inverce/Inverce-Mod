package com.example.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.events.Event;
import com.inverce.mod.navigation.Navigator;

import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        IM.onBg().scheduleAtFixedRate(() -> {
                Log.w("d");
                for (int i = 0; i < 1; i++) {
                    IM.onBg().execute(() -> Event.Bus.post(Intre.class).pp());
                }}, 2, 2, TimeUnit.SECONDS);

        Navigator.on()
                .back()
                .twice()
                .and()
                .forwardTo(Fragment.class)
                .commit();

    }

    @Override
    public void onBackPressed() {
//        if (Navigator.Callbacks.onBackPressed()) {
            super.onBackPressed();
//        }
    }
}
