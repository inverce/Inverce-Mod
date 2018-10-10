package com.inverce.mod.vision.interfaces;

import android.support.annotation.IntDef;

import com.google.android.gms.vision.barcode.Barcode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef(flag = true, value = {
        Barcode.ALL_FORMATS, Barcode.EAN_13, Barcode.EAN_8, Barcode.UPC_A, Barcode.UPC_E,
        Barcode.CODE_39, Barcode.CODE_93, Barcode.CODE_128, Barcode.ITF, Barcode.CODABAR,
        Barcode.QR_CODE, Barcode.DATA_MATRIX, Barcode.PDF417, Barcode.AZTEC})
public @interface BarcodeFormats { }
