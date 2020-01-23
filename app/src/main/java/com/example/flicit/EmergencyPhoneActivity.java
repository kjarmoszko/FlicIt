package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flicit.database.DatabaseHelper;
import com.example.flicit.database.Function;

public class EmergencyPhoneActivity extends AppCompatActivity {
    private EditText numberField;
    private ImageView acceptButton;
    private String functionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone);

        numberField = (EditText) findViewById(R.id.emergencyNumber);
        acceptButton = (ImageView) findViewById(R.id.acceptButton);

        Intent intent = getIntent();
        functionId = intent.getExtras().getString("functionId");
        Function function = DatabaseHelper.getInstance(this).getFunction(Long.parseLong(functionId));
        numberField.setText(function.getNumber());

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEmergencyPhone();
            }
        });
    }

    public void addEmergencyPhone() {
        String number = numberField.getText().toString();
        Function function = DatabaseHelper.getInstance(this).getFunction(Long.parseLong(functionId));
        function.setNumber(number);
        long result = DatabaseHelper.getInstance(this).updateFunction(function);
        if(result >=0 )
            setResult(RESULT_OK);
        finish();
    }
}
