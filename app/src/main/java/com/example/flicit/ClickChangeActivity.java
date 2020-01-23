package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.flicit.database.DatabaseHelper;
import com.example.flicit.database.Function;

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
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 1);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 1);
    }

    public void doubleClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 2);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 2);
    }

    public void holdClick(View v) {
        Intent intent = new Intent(ClickChangeActivity.this, ChooseFunctionalityActivity.class);
        Function function = DatabaseHelper.getInstance(this).getFunction(mac, 0);
        intent.putExtra("functionId", Long.toString(function.getId()));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 0);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 0, function);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 1);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 1, function);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Function function = DatabaseHelper.getInstance(this).getFunction(mac, 2);
                    function.setType(data.getExtras().getString("functionality"));
                    DatabaseHelper.getInstance(this).updateFunction(mac, 2, function);
                }
                break;
        }
    }
}
