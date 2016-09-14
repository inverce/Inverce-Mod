package com.inverce.samples.InverceMod;

import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.inverce.logging.Log;
import com.inverce.utils.IM;
import com.inverce.utils.events.AsyncFeature;
import com.inverce.utils.events.Event;
import com.inverce.utils.stateless.StateMachine;
import com.stablekernel.annotationprocessor.generated.GeneratedClass;

public class MainActivity extends AppCompatActivity implements MainActivityInteractions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.init(this);
        IM.install(this.getApplication());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        StateMachine.get(SM.class).post().testMe();

        Event.Bus.register(MainActivityInteractions.class, this);
    }

    @Override
    @SuppressWarnings("all")
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override

    @SuppressWarnings("all")
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void changeFragmentPage(Fragment page) {
    }

    @Override
    public AsyncFeature<Canvas> createDrawing() {
        return null;
    }
}
