package com.inverce.mod.vision;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.inverce.mod.core.IM;
import com.inverce.mod.vision.adapters.BarcodeTrackerFactory;
import com.inverce.mod.vision.camera.CameraSource;
import com.inverce.mod.vision.camera.CameraSourcePreview;
import com.inverce.mod.vision.camera.Permissions;
import com.inverce.mod.vision.interfaces.BarcodeFormats;
import com.inverce.mod.vision.interfaces.NewDetectionListener;

import java.io.IOException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static android.hardware.Camera.Parameters.FLASH_MODE_OFF;
import static android.hardware.Camera.Parameters.FLASH_MODE_TORCH;
import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;
import static android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;

@SuppressWarnings({"unused", "deprecation"})
public class VisionScannerFragment extends Fragment {
    public static final int RC_HANDLE_GMS = 9207;
    public static final int RC_HANDLE_CAMERA_PERM = 9208;

    private static final String TAG = "IM.Barcode";

    @Nullable
    protected BarcodeDetector barcodeDetector;
    @Nullable
    protected CameraSource mCameraSource;
    protected CameraSourcePreview mCameraSourcePreview;
    protected int barcodeFormats = Barcode.ALL_FORMATS;
    protected NewDetectionListener detectionListener;
    protected ScheduledFuture<?> selfFocus;

    /**
     * true if no further barcode should be detected or given as a result
     */
    protected boolean mDetectionConsumed = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inverce_barcode_scanner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCameraSourcePreview = (CameraSourcePreview) view.findViewById(R.id.inverce_camera_preview);
    }

    public BarcodeDetector onCreateBarcodeDetector(Activity activity) {
        return new BarcodeDetector.Builder(activity)
                .setBarcodeFormats(barcodeFormats)
                .build();
    }

    public CameraSource onCreateCameraSource(Activity activity, BarcodeDetector barcodeDetector) {
        return new CameraSource.Builder(activity, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setFlashMode(null)
                .setFocusMode(FOCUS_MODE_CONTINUOUS_PICTURE)
                .build();
    }

    public VisionScannerFragment setBarcodeFormats(@BarcodeFormats int barcodeFormats) {
        this.barcodeFormats = barcodeFormats;
        return this;
    }

    public VisionScannerFragment setDetectionListener(NewDetectionListener detectionListener) {
        this.detectionListener = detectionListener;
        return this;
    }

    public void initialize() {
        barcodeDetector = onCreateBarcodeDetector(getActivity());
        mCameraSource = onCreateCameraSource(getActivity(), barcodeDetector);
    }

    @Override
    public void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Permissions.hasPermissions(Permissions.CAMERA_PERMISSIONS, getActivity())) {
            startCameraSource();
        }

        IM.onBg().schedule(this::checkUpFocus, 3, TimeUnit.SECONDS);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.stop();
        }

        if (selfFocus != null) {
            selfFocus.cancel(false);
            selfFocus = null;
        }
    }

    private void checkUpFocus() {
        try {
            if (mCameraSource != null && mCameraSource.getCamera() != null && mCameraSource.getCamera().getParameters() != null) {
                synchronized (mCameraSource.getCameraLock()) {
                    if (!mCameraSource.getCamera().getParameters().getSupportedFocusModes().contains(FOCUS_MODE_CONTINUOUS_PICTURE)) {
                        mCameraSource.setFocusMode(FOCUS_MODE_AUTO);
                        selfFocus = IM.onUi().scheduleAtFixedRate(this::tryFocus, 0, 3, TimeUnit.SECONDS);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "camera: " + e.toString());
        }

    }

    private void tryFocus() {
        try {
            if (mCameraSource != null) {
                mCameraSource.autoFocus(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Nullable
    public CameraSource getCameraSource() {
        return mCameraSource;
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if (code != ConnectionResult.SUCCESS && getActivity() != null) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), code, RC_HANDLE_GMS);
            dialog.show();
        }

        if (mCameraSource != null && barcodeDetector != null) {
            try {
                BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(p -> {
                    if (detectionListener != null) detectionListener.onNewDetection(p);
                });
                barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
                mCameraSourcePreview.start(mCameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    public void setTorch(boolean enabled) {
        try {
            if (checkPermissions(getContext(), false) && mCameraSource != null) {
                mCameraSource.setFlashMode(enabled ? FLASH_MODE_TORCH : FLASH_MODE_OFF);
                //noinspection MissingPermission
//                mCameraSource.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isTorchEnabled() {
        try {
            return mCameraSource != null && FLASH_MODE_OFF.equals(mCameraSource.getFlashMode());
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean checkPermissions(Context context, boolean requestIfNotPresent) {
        if (context == null) return false; // allow passing null

        int mCameraPermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        if (mCameraPermission != PackageManager.PERMISSION_GRANTED) {
            if (requestIfNotPresent && context instanceof Activity) {
                requestCameraPermission((Activity) context, true);
            }
            return false;
        } else {
            return true;
        }
    }


    public void requestCameraPermission(final Activity context, boolean showSnackBarRationale) {
        final String[] mPermissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(context, mPermissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        if (!showSnackBarRationale) {
            ActivityCompat.requestPermissions(context, mPermissions, RC_HANDLE_CAMERA_PERM);
        } else if (getView() != null) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(context, mPermissions, RC_HANDLE_CAMERA_PERM);
                }
            };

            Snackbar.make(getView(), R.string.im_permission_camera_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, listener)
                    .show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isRemoving()) {
            clean();
        }
    }

    private void clean() {
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.release();
            mCameraSourcePreview = null;
        }
    }
}

