package com.example.flicit;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Functionalities {
    private Context context;

    private static final Functionalities instance = new Functionalities();

    private Functionalities() {}

    public static Functionalities getInstance() {
        return instance;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void flashLightOn() {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        } catch (CameraAccessException e) {

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void flashLightOff() {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
        } catch (CameraAccessException e) {

        }
    }
}
