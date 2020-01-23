package com.example.flicit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.flicit.database.DatabaseHelper;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class FlicManageActivity extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flic_manage);
        getConnectedFlics();
    }

    public void grabButton(View v) {
        try {
            FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
                @Override
                public void onInitialized(FlicManager manager) {
                    manager.initiateGrabButton(FlicManageActivity.this);
                }

            });
        } catch (FlicAppNotInstalledException err) {
            Toast.makeText(this, "Flic App is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FlicManager.getInstance(this, new FlicManagerInitializedCallback() {
            @Override
            public void onInitialized(FlicManager manager) {
                FlicButton button = manager.completeGrabButton(requestCode, resultCode, data);
                if (button != null) {
                    button.registerListenForBroadcast(FlicBroadcastReceiverFlags.CLICK_OR_DOUBLE_CLICK_OR_HOLD);
                    DatabaseHelper.getInstance(FlicManageActivity.this).createFlicButton(button.getButtonId());
                    Toast.makeText(FlicManageActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                    getConnectedFlics();
                } else {
                    Toast.makeText(FlicManageActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getConnectedFlics() {
        linearLayout = findViewById(R.id.linear);
        linearLayout.removeAllViews();
        Cursor cursor = DatabaseHelper.getInstance(this).getAllData();
        while (cursor.moveToNext()) {
            final Button button = new Button(this);
            button.setText(cursor.getString(0));
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FlicManageActivity.this, ClickChangeActivity.class);
                    intent.putExtra("mac", button.getText().toString());
                    startActivity(intent);
                }
            });
            linearLayout.addView(button);
        }
        Button button = new Button(this);
        button.setText("Grab Flic");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                grabButton(v);
            }
        });
        linearLayout.addView(button);
    }

}
