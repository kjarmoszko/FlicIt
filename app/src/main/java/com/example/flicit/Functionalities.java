package com.example.flicit;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Functionalities {
    private Context context;

    private boolean flashlightOn = false;

    private static Functionalities instance;

    private Functionalities(Context context) {
        this.context = context;
    }

    public static synchronized Functionalities getInstance(Context context) {
        if (instance == null)
            instance = new Functionalities(context);
        return instance;
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    protected void flashLightOn() {
//        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//
//        try {
//            String cameraId = cameraManager.getCameraIdList()[0];
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                cameraManager.setTorchMode(cameraId, true);
//            }
//        } catch (CameraAccessException e) {
//
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    protected void flashLightOff() {
//        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//        try {
//            String cameraId = cameraManager.getCameraIdList()[0];
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                cameraManager.setTorchMode(cameraId, false);
//            }
//        } catch (CameraAccessException e) {
//
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void blinkFlash() {
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        String myString = "0101010101";
        long blinkDelay = 50; //Delay in ms
        for (int i = 0; i < myString.length(); i++) {
            if (myString.charAt(i) == '0') {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true);
                    }
                } catch (CameraAccessException e) {

                }
            } else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {

                }
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void soundAlarm() {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.car_alarm);
        mediaPlayer.start();
    }


    protected void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    protected void flashlightService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (flashlightOn) {
                        cameraManager.setTorchMode(cameraId, false);
                        flashlightOn = false;
                    } else {
                        cameraManager.setTorchMode(cameraId, true);
                        flashlightOn = true;
                    }
                }

            } catch (CameraAccessException e) {

            }
        }
    }
}
