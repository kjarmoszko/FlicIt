package com.example.flicit.phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.flicit.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.TreeMap;

public class ContactListActivity extends AppCompatActivity {
    private LinearLayout linearLayout;
    private static final int REQUEST_CALL = 1;
    private static final int PICK_CONTACT = 2;
    private static final int READ_LOG = 3;
    private static String dial;
    private ListView listView;
    private ArrayList<String> contactList;
    private TreeMap<String, String> contactMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        PhonePageAdapter phonePageAdapter = new PhonePageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.POSITION_NONE);
        phonePageAdapter.addFragment(new CallLogFragment(), "Last Calls");
        phonePageAdapter.addFragment(new ContactListFragment(), "Contacts");
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(phonePageAdapter);
        tabLayout.setupWithViewPager(viewPager);


//        listView = (ListView) findViewById(R.id.contactList);
//
//        if (ContextCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, PICK_CONTACT);
////        if(ContextCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
////            ActivityCompat.requestPermissions(ContactListActivity.this, new String[] {Manifest.permission.READ_CALL_LOG}, READ_LOG);
//        } else {
//            getAllContacts();
//            getCallLogs();
//        }
    }

    private void getCallLogs() {
        LinkedList<Call> callList = new LinkedList<>();
        if(ContextCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, READ_LOG);
        }
        else {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                Call call = new Call();
                call.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));
                call.setCallDate(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE)));
                call.setCallDuration(cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION)));
                call.setCallType(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)));
                call.setName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                callList.addFirst(call);
            }

            CallLogAdapter callLogAdapter = new CallLogAdapter(this, R.layout.call_log_item, callList, contactMap);
            listView.setAdapter(callLogAdapter);
        }
    }

    private void getAllContacts() {
        contactList = new ArrayList<String>();
        contactMap = new TreeMap<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{id},
                    null);
            if (phoneCursor.moveToNext()) {
                final String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String str  = name + "\n" + number;
                contactList.add(str);
                contactMap.put(number, name);
            }

        }
        Collections.sort(contactList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.contact_item, R.id.contactItem, contactList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str[] = parent.getItemAtPosition(position).toString().split("\n");
                dial = "tel:" + str[1];
                        makePhoneCall();
            }
        });
    }

    public void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(ContactListActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ContactListActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    Toast.makeText(this, "Call Phone Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case PICK_CONTACT:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAllContacts();
                } else {
                    Toast.makeText(this, "Pick Contact Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case READ_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCallLogs();
                } else {
                    Toast.makeText(this, "Call log Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

