package com.example.flicit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flicit.database.DatabaseHelper;
import com.example.flicit.database.Flic;

import java.util.ArrayList;

import io.flic.lib.FlicAppNotInstalledException;
import io.flic.lib.FlicBroadcastReceiverFlags;
import io.flic.lib.FlicButton;
import io.flic.lib.FlicManager;
import io.flic.lib.FlicManagerInitializedCallback;

public class FlicManageActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flic_manage);


        listView = (ListView) findViewById(R.id.flicList);
        getConnectedFlics();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    DatabaseHelper.getInstance(FlicManageActivity.this).createFlic(button.getButtonId());
                    Toast.makeText(FlicManageActivity.this, "Grabbed a button", Toast.LENGTH_SHORT).show();
                    getConnectedFlics();
                } else {
                    Toast.makeText(FlicManageActivity.this, "Did not grab any button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getConnectedFlics() {


        ArrayList<Flic> flicList = new ArrayList<Flic>();

        Cursor cursor = DatabaseHelper.getInstance(this).getAllData();
        while (cursor.moveToNext()) {
            Flic flic = new Flic();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_NAME));
            if(name == null) {
                flic.setName("No name");
            }
            else {
                flic.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_NAME)));
            }
            flic.setMac(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TableFlic.COLUMN_MAC)));
            flicList.add(flic);

        }

        FlicListAdapter flicListAdapter = new FlicListAdapter(this, R.layout.flic_img, flicList);
        listView.setAdapter(flicListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlicManageActivity.this, ClickChangeActivity.class);
                    intent.putExtra("mac", parent.getItemAtPosition(position).toString());
                    startActivity(intent);
            }
        });
    }
}
