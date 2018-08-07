package com.inverce.mod.vision.adapters

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import com.inverce.mod.vision.interfaces.NewDetectionListener

class BarcodeTrackerFactory(private val mDetectionListener: NewDetectionListener) : MultiProcessor.Factory<Barcode> {
    override fun create(barcode: Barcode): Tracker<Barcode> {
        val tracker = NewDetectionTracker()
        tracker.setListener(mDetectionListener)
        return tracker
    }
}

class NewDetectionTracker : Tracker<Barcode>() {
    private var mListener: NewDetectionListener? = null

    fun setListener(mListener: NewDetectionListener) {
        this.mListener = mListener
    }

    override fun onNewItem(id: Int, item: Barcode?) {
        if (mListener != null) {
            mListener!!.onNewDetection(item)
        }
    }
}