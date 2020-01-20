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
    public void onButtonSingleOrDoubleClickOrHold(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isSingleClick, boolean isDoubleClick, boolean isHold) {
        if (isSingleClick) {
            Toast.makeText(context, "Single Click", Toast.LENGTH_SHORT).show();
            switch (DatabaseHelper.getInstance(context).getFunctionality(button.getButtonId(), 1)) {
                case 0:
                    break;
                case 1:
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case 2:

                    break;
                case 3:
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case 4:
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case 5:
                    Functionalities.getInstance(context).vibrate();
                    break;
                default:
                    break;
            }
        } else if (isDoubleClick) {
            Toast.makeText(context, "Double Click", Toast.LENGTH_SHORT).show();
            switch (DatabaseHelper.getInstance(context).getFunctionality(button.getButtonId(), 2)) {
                case 0:
                    break;
                case 1:
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case 2:

                    break;
                case 3:
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case 4:
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case 5:
                    Functionalities.getInstance(context).vibrate();
                    break;
                default:
                    break;
            }
        } else if (isHold) {
            Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
            switch (DatabaseHelper.getInstance(context).getFunctionality(button.getButtonId(), 0)) {
                case 0:
                    break;
                case 1:
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case 2:

                    break;
                case 3:
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case 4:
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case 5:
                    Functionalities.getInstance(context).vibrate();
                    break;
                default:
                    break;
            }
        }
    }
}
