package com.inverce.samples.InverceMod;

import android.app.Fragment;
import android.graphics.Canvas;

import com.inverce.utils.events.AsyncFeature;
import com.inverce.utils.events.annotation.EventInfo;
import com.inverce.utils.events.annotation.Listener;
import com.inverce.utils.events.annotation.ThreadPolicy;

public interface MainActivityInteractions extends Listener{
    void changeFragmentPage(Fragment page);

    @EventInfo(thread = ThreadPolicy.UiThread, yieldAll = true)
    AsyncFeature<Canvas> createDrawing();
}
