package com.example.flicit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.flicit.database.DatabaseHelper;
import com.example.flicit.database.Function;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 3;
    private static final int MODIFY_AUDIO = 5;
    private ImageView phoneButton;
    private ImageView lockButton;
    private ImageView flicButton;
    public static ImageView flashlightButton;
    public static ImageView muteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlicConfig.setFlicCredentials();

        flicButton = (ImageView) findViewById(R.id.flicButton);
        phoneButton = (ImageView) findViewById(R.id.phoneButton);
        flashlightButton = (ImageView) findViewById(R.id.flashlightButton);
        muteButton = (ImageView) findViewById(R.id.muteButton);

        flicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FlicManageActivity.class);
                startActivity(intent);
            }
        });
        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNumberFromContacts();
            }
        });
        flashlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                } else {
                    Functionalities.getInstance(MainActivity.this).flashlightService();
                }
            }
        });
        muteButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, MODIFY_AUDIO);
                } else {
                    Functionalities.getInstance(MainActivity.this).speakerService();
                }
            }
        });

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (audioManager.getStreamVolume(AudioManager.STREAM_RING) == audioManager.getStreamMinVolume(AudioManager.STREAM_RING))
                muteButton.setImageResource(R.drawable.volume_off_icon);
        }

        lockButton = (ImageView) findViewById(R.id.lockButton); //db test

        viewAll(); //db test

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);

//        DatabaseHelper.getInstance(this).clearDb();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void viewAll() { //db test
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DatabaseHelper.getInstance(MainActivity.this).getAllData();
//                Cursor res = DatabaseHelper.getInstance(MainActivity.this).getAllFunction();
                if (res.getCount() == 0) {
                    //show massage
                    showMassage("Error", "No data");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (res.moveToNext()) {
                    buffer.append(res.getString(0) + "\n");
                    buffer.append(res.getString(1) + "\n");
                    buffer.append(res.getString(2) + "\n");
                    buffer.append(res.getString(3) + "\n\n");
//                    buffer.append(res.getString(4) + "\n\n");
                }
                //show all
                showMassage("Data", buffer.toString());
            }
        });
    }

    public void showMassage(String title, String message) { //db test
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void getNumberFromContacts() {
        Intent intent = new Intent(MainActivity.this, ContactListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Functionalities.getInstance(MainActivity.this).flashlightService();
                } else {
                    Toast.makeText(this, "Flashlight Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            case MODIFY_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Functionalities.getInstance(MainActivity.this).speakerService();
                } else {
                    Toast.makeText(this, "ModifyAudio Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


//    public void makePhoneCall() {
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
//        } else {
//            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_CALL:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    makePhoneCall();
//                } else {
//                    Toast.makeText(this, "Call Phone Permission DENIED", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case PICK_CONTACT:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getNumberFromContacts();
//                } else {
//                    Toast.makeText(this, "Pick Contact Permission DENIED", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case PICK_CONTACT:
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri contactData = data.getData();
//                    Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
//                    if (cursor.moveToFirst()) {
//                        String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//                        Cursor phoneCursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                                null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                                new String[]{id},
//                                null);
//                        if (phoneCursor.moveToFirst()) {
//                            String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            dial = "tel:" + number;
//                            makePhoneCall();
//                        }
//                    }
//                }
//                break;
//        }
//    }
}
/* TODO
    Dzwonienie:
        rejest połączeń
        ramka przy słuchawce po nieodebranym połączeniu
        dodawanie/edycja kontaktów
        duża klawiatura numeryczna i dzwonienie z niej
            ##ekran podczas dzwonienia##
    Wiadomości:
        lista wiadomości
        pisanie i wysyłka wiadomości
        ramka po nieodczytanej wiadomości
    Manage Flic:
    Blokada:
        blokowanie komórki z ekranem z przyciskiem odblokowania, dużym zegarem i ewentualnie że ktoś dzwonił, wysłał wiadomość
    Aplikacja:
        zrobienie na górze zegarka i siły sygnału telefnicznego
        ##checkfunctionalities wywalić do oddzielnej klasy?##
    Funkcjonalności:
        -usprawnić:findMyPhone - po kilkukrotnym kliknięciu uruchamia kilka razy
        -usprawnić:włącz głośnomówiący po odebraniu telefonu - wymaga napisania swojej aplikacji dzwoniącej https://developer.android.com/guide/topics/connectivity/telecom/selfManaged
        -usprawnić:emergency call - ##jak się uda zrobic własną aplikację dzwoniącą wybrać czy dzwonić z głośnomówiącym czy nie, co prawdopodobnie zmieni bazę danych##
        -emergency sms - jakies powiadomienie że został wysłany sms ratunkowy, po obeznaniu się z smsami
            ##obsługa budzika cos promotor wspominał##
            ##text to speech##
*/

