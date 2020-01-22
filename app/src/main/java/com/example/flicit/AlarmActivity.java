package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class AlarmActivity extends AppCompatActivity {
    private ImageView doneButton;
    Handler handler = new Handler();

    private Runnable panic = new Runnable() {
        @Override
        public void run() {
            Functionalities.getInstance(AlarmActivity.this).panic();
            handler.postDelayed(this, 800);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        doneButton = (ImageView) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(panic);
                finish();
            }
        });
        handler.post(panic);
    }

}
