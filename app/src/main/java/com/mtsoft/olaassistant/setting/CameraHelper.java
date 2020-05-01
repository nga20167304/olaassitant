package com.mtsoft.olaassistant.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * Created by manhhung on 4/2/19.
 */

public class CameraHelper {
    private Camera camera;
    private Context context;

    public CameraHelper(Camera camera, Context context) {
        this.camera = camera;
        this.context = context;
    }

    private boolean isFlashAvailable() {
        Boolean available = context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!available) {
            AlertDialog alert = new AlertDialog.Builder(context).create();
            alert.setTitle("THÔNG BÁO");
            alert.setMessage("Thiết bị không hỗ trợ đèn pin!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alert.show();
        }
        return available;
    }


    public void turnOnFlashLight(CameraManager mCameraManager, String mCameraId) {
        if (isFlashAvailable()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mCameraManager.setTorchMode(mCameraId, true);
                } else {
//                    this.camera = Camera.open();
                    Camera.Parameters p = this.camera.getParameters();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    this.camera.setParameters(p);
                    this.camera.startPreview();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void turnOffFlashLight(CameraManager mCameraManager, String mCameraId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            } else {
                if (this.camera != null) {
                    this.camera.stopPreview();
                    this.camera.release();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Camera getCamera() {
        return camera;
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
