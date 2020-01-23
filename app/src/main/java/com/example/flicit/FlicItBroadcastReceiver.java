package com.example.flicit;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.flicit.database.DatabaseHelper;

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
            switch (DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getType()) {
                case "0":
                    break;
                case "1":
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case "2":
                    Functionalities.getInstance(context).findPhone();
                    break;
                case "3":
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case "4":
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case "5":
                    Functionalities.getInstance(context).vibrate();
                    break;
                case "6":
                    Functionalities.getInstance(context).runGoogleAssistant();
                    break;
                case "7":
                    Functionalities.getInstance(context).pickUpCall();
                    break;
                case "8":
                    Functionalities.getInstance(context).speakerService();
                    break;
                case "9":
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),1).getNumber());
                    break;
                case "10":
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 1).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        } else if (isDoubleClick) {
            Toast.makeText(context, "Double Click", Toast.LENGTH_SHORT).show();
            switch (DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getType()) {
                case "0":
                    break;
                case "1":
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case "2":
                    Functionalities.getInstance(context).findPhone();
                    break;
                case "3":
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case "4":
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case "5":
                    Functionalities.getInstance(context).vibrate();
                    break;
                case "6":
                    Functionalities.getInstance(context).runGoogleAssistant();
                    break;
                case "7":
                    Functionalities.getInstance(context).pickUpCall();
                    break;
                case "8":
                    Functionalities.getInstance(context).speakerService();
                    break;
                case "9":
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),0).getNumber());
                    break;
                case "10":
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 2).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        } else if (isHold) {
            Toast.makeText(context, "Hold", Toast.LENGTH_SHORT).show();
            switch (DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getType()) {
                case "0":
                    break;
                case "1":
                    Functionalities.getInstance(context).flashlightService();
                    break;
                case "2":
                    Functionalities.getInstance(context).findPhone();
                    break;
                case "3":
                    Functionalities.getInstance(context).blinkFlash();
                    break;
                case "4":
                    Functionalities.getInstance(context).soundAlarm();
                    break;
                case "5":
                    Functionalities.getInstance(context).vibrate();
                    break;
                case "6":
                    Functionalities.getInstance(context).runGoogleAssistant();
                    break;
                case "7":
                    Functionalities.getInstance(context).pickUpCall();
                    break;
                case "8":
                    Functionalities.getInstance(context).speakerService();
                    break;
                case "9":
                    Functionalities.getInstance(context).emergencyCall(DatabaseHelper.getInstance(context).getFunction(button.getButtonId(),2).getNumber());
                    break;
                case "10":
                    String number = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getNumber();
                    String message = DatabaseHelper.getInstance(context).getFunction(button.getButtonId(), 0).getMessage();
                    Functionalities.getInstance(context).emergencySms(number, message);
                    break;
                default:
                    break;
            }
        }
    }
}
