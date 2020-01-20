package com.example.flicit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 3;
    private boolean flashlightOn = false;
    private ImageView phoneButton;
    private ImageView lockButton;
    private ImageView flicButton;
    private ImageView flashlightButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlicConfig.setFlicCredentials();

        flicButton = (ImageView) findViewById(R.id.flicButton);
        phoneButton = (ImageView) findViewById(R.id.phoneButton);
        flashlightButton = (ImageView) findViewById(R.id.flashlightButton);


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

        lockButton = (ImageView) findViewById(R.id.lockButton); //db test
        viewAll(); //db test
    }

    public void viewAll() { //db test
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DatabaseHelper.getInstance(MainActivity.this).getAllData();
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
        poprawienie listy kontatków na listę a nie przyciski
            ##ekran podczas dzwonienia##
    Wiadomości:
        lista wiadomości
        pisanie i wysyłka wiadomości
        ramka po nieodczytanej wiadomości
    Manage Flic:
        dodawanie nazwy Flica
        usuwanie Flica
        poprawa bazy danych na 2 tabele
        lista fliców a nie przyciski
        jakieś ładniejsze menu z ikonami i może nadawaniem kolorów przyciskom
        edycja funkcji Flica - przerobienie na listę pod rodzaje kliknięcia nazawa wybranej funkcji /prawdopodobnie potrzebny enum/
    Latarka:
        ##zmiana ikony na świecącą gdy latarka jest włączona i na zgaszoną wpp##
    Głośność:
        dodanie activity głośości, 2 paski systemowa i aplikacji
        przyciski z plusem i minusem i wycisz
    Blokada:
        blokowanie komórki z ekranem z przyciskiem odblokowania, dużym zegarem i ewentualnie że ktoś dzwonił, wysłał wiadomość
    Aplikacja:
        przerobienie aplikacji na ekran głowny, uruchamianie po starcie i obsługa flica nawet po zamknięciu aplikacji
        usunięcie górnego paska
        zrobienie na górze zegarka i siły sygnału telefnicznego
        ##checkfunctionalities wywalić do oddzielnej klasy?##
    Funkcjonalności:
        znajdz telefon
        asystent google
        odbierz telefon i włącz głośnomówiący
        zadzwoń na podany nr /zmiana bazy danych/
        obsługa budzika
        emergency sms /zmiana bazy danych/
        ##text to speech##
*/

