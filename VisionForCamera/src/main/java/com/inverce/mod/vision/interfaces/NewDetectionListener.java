package com.inverce.mod.vision.interfaces;

import com.google.android.gms.vision.barcode.Barcode;

public interface NewDetectionListener {
    void onNewDetection(Barcode barcode);
}
