package com.app.leon.abfa.Utils;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public final class FlashLightManager implements IFlashLightManager {
    private Context context;
    private boolean isFlashOn = false;

    public FlashLightManager(Context context) {
        this.context = context;
    }

    public void turnOn() {
        CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            boolean flashShouldBecome = true;
            String[] cameraId = camManager.getCameraIdList();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager.setTorchMode(cameraId[0], flashShouldBecome);
            }
            isFlashOn = flashShouldBecome;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void turnOff() {
        CameraManager camManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            boolean flashShouldBecome = false;
            String[] cameraId = camManager.getCameraIdList();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                camManager.setTorchMode(cameraId[0], flashShouldBecome);
            }
            isFlashOn = flashShouldBecome;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean toggleFlash() {
        if (isFlashOn) {
            turnOff();
            return false;
        } else {
            turnOn();
            return true;
        }
    }
}
