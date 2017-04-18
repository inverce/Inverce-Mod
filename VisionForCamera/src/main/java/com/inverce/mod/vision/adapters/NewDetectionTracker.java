package com.inverce.mod.vision.adapters;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;
import com.inverce.mod.vision.interfaces.NewDetectionListener;

public class NewDetectionTracker extends Tracker<Barcode> {
    private NewDetectionListener mListener;

    public void setListener(NewDetectionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onNewItem(int id, Barcode item) {
        if (mListener != null){
            mListener.onNewDetection(item);
        }
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detectionResults, Barcode item) {

    }

    @Override
    public void onMissing(Detector.Detections<Barcode> detectionResults) {

    }

    @Override
    public void onDone() {

    }

}
