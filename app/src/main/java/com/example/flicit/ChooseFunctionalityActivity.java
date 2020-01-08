package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseFunctionalityActivity extends AppCompatActivity {
    Button noneButton, lightOnButton, lightOffButton, flashButton, cancelButton;
    Intent functionality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_functionality);

        noneButton = (Button) findViewById(R.id.noneButton);
        lightOnButton = (Button) findViewById(R.id.lightOnButton);
        lightOffButton = (Button) findViewById(R.id.lightOffButton);
        flashButton = (Button) findViewById(R.id.flashButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);

        functionality = new Intent();
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
    public void lightOnButtonClicked(View v) {
        functionality.putExtra("functionality", "1");
        setResult(RESULT_OK, functionality);
        finish();
    }
    public void lightOffButtonClicked(View v) {
        functionality.putExtra("functionality", "2");
        setResult(RESULT_OK, functionality);
        finish();
    }
    public void flashButtonClicked(View v) {
        functionality.putExtra("functionality", "3");
        setResult(RESULT_OK, functionality);
        finish();
    }

}
