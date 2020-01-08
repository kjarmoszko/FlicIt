package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ClickChangeActivity extends AppCompatActivity {
    private Button singleClickButton;
    private Button doubleClickButton;
    private Button holdButton;
    private String mac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_change);

        singleClickButton = (Button) findViewById(R.id.singleButton);
        doubleClickButton = (Button) findViewById(R.id.doubleButton);
        holdButton = (Button) findViewById(R.id.holdButton);

        Intent intent = getIntent();
        mac = intent.getExtras().getString("mac");
    }

    public void singleClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        startActivityForResult(intent, 1);
    }

    public void doubleClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        startActivityForResult(intent, 2);
    }

    public void holdClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if(resultCode == RESULT_OK) {
                    DatabaseHelper.getInstance(this).changeFunctionality(mac, 0, Integer.parseInt(data.getExtras().getString("functionality")));
                }
                break;
            case 1:
                if(resultCode == RESULT_OK) {
                    DatabaseHelper.getInstance(this).changeFunctionality(mac, 1, Integer.parseInt(data.getExtras().getString("functionality")));
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    DatabaseHelper.getInstance(this).changeFunctionality(mac, 2, Integer.parseInt(data.getExtras().getString("functionality")));
                }
                break;
        }
    }
}
