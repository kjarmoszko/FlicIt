package com.example.flicit;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

public class FlicItBroadcastReceiver extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        FlicConfig.setFlicCredentials();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onButtonSingleOrDoubleClickOrHold(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold){
        if(isSingleClick) {
            Toast.makeText(context, "Single Click", Toast.LENGTH_SHORT).show();
            Functionalities.getInstance(context).flashLightOn();
        } else if (isDoubleClick) {
            Toast.makeText(context, "Double Click", Toast.LENGTH_SHORT).show();
            Functionalities.getInstance(context).flashLightOff();
        } else if (isHold) {
            Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
        }
    }
}
