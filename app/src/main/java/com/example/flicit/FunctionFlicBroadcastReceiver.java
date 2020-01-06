package com.example.flicit;

import android.content.Context;
import android.widget.Toast;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

public class FunctionFlicBroadcastReceiver extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        FlicConfig.setFlicCredentials();
    }

    @Override
    public void onButtonSingleOrDoubleClickOrHold(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold){
        if(isSingleClick) {
            Toast.makeText(context, "Single Click", Toast.LENGTH_SHORT).show();
        } else if (isDoubleClick) {
            Toast.makeText(context, "Double Click", Toast.LENGTH_SHORT).show();
        } else if (isHold) {
            Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
        }
    }
}
