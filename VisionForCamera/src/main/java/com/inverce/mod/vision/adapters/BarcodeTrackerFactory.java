package com.inverce.mod.vision.adapters;
import android.support.annotation.NonNull;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.inverce.mod.vision.interfaces.NewDetectionListener;

public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {
    private NewDetectionListener mDetectionListener;

    public BarcodeTrackerFactory(@NonNull NewDetectionListener listener) {
        mDetectionListener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        NewDetectionTracker tracker = new NewDetectionTracker();
        tracker.setListener(mDetectionListener);
        return tracker;
    }

}

