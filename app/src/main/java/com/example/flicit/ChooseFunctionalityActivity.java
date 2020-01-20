package com.example.flicit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseFunctionalityActivity extends AppCompatActivity {
    Button flashlightButton;
//    Button noneButton, flashButton, cancelButton;
    Intent functionality;
    private static final int CAMERA_REQUEST = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_functionality);

//        noneButton = (Button) findViewById(R.id.noneButton);
        flashlightButton = (Button) findViewById(R.id.flashlightButton);
//        flashButton = (Button) findViewById(R.id.flashButton);
//        cancelButton = (Button) findViewById(R.id.cancelButton);

        functionality = new Intent();
        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashlightButtonClicked();
            }
        });
    }

    public void cancelButtonClicked(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
    public void noneButtonClicked(View v) {
        functionality.putExtra("functionality", "0");
        setResult(RESULT_OK, functionality);
        finish();
    }
    public void flashlightButtonClicked() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            functionality.putExtra("functionality", "1");
            setResult(RESULT_OK, functionality);
            finish();
        }
    }

    public void flashButtonClicked(View v) {
        functionality.putExtra("functionality", "3");
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void soundAlarmButtonClicked(View v) {
        functionality.putExtra("functionality", "4");
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void vibrateButtonClicked(View v) {
        functionality.putExtra("functionality", "5");
        setResult(RESULT_OK, functionality);
        finish();
    }

    public void findPhoneClicked(View v) {
        functionality.putExtra("functionality", "2");
        setResult(RESULT_OK, functionality);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flashlightButtonClicked();
                } else {
                    Toast.makeText(ChooseFunctionalityActivity.this, "Flashlight Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
