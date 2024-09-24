package com.example.setup;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private final String TAG = "AndroidApp";
    private EditText editTextName, editTextNumber;
    private Spinner spinner;
    private Button btnAddContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.editTextName = (EditText) findViewById(R.id.name);
        this.editTextNumber = (EditText) findViewById(R.id.number);
        this.spinner = (Spinner) findViewById(R.id.spinner);
        this.btnAddContact = (Button) findViewById(R.id.addContact);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},1);
        else{
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_CONTACTS},1);
        }

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertContact();
            }
        });
    }

    private void insertContact(){
        Log.d(TAG,"insertContact started");
        String name = editTextName.getText().toString();
        String number = editTextNumber.getText().toString();
        String type = spinner.getSelectedItem().toString();

        // add in two tables Raw Contacts and Data table both. Add in Data table twice for both name and phone number
        Uri uri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI,new ContentValues()); // raw contact created
        long id = ContentUris.parseId(uri);

        ContentValues nameValues = new ContentValues();
        nameValues.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        nameValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,name);
        nameValues.put(ContactsContract.Data.RAW_CONTACT_ID,id);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI,nameValues);

        ContentValues numberValues = new ContentValues();
        numberValues.put(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        numberValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER,number);
        numberValues.put(ContactsContract.CommonDataKinds.Phone.TYPE,ContactsContract.CommonDataKinds.Phone.TYPE_HOME); // type mobile, work, fax etc.
        numberValues.put(ContactsContract.Data.RAW_CONTACT_ID,id);
        Uri newlyAddedContact = getContentResolver().insert(ContactsContract.Data.CONTENT_URI,numberValues);
        Log.d(TAG,"insertContact uri: "+newlyAddedContact);
    }


//    public void btnGetContactPressed(View v){
//        getSms();
//    }

//    private void getPhoneContacts(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},0);
//        }
//        this.textView = (TextView) findViewById(R.id.textView);
//
//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        Cursor cursor = contentResolver.query(uri,null,null,null,null);
//        Log.i("CONTACT_PROVIDER_DEMO","Total # of contats ::: "+cursor.getCount());
//        if(null != cursor){
//            if(cursor.moveToFirst()){
//                String text = "";
//                for(int i = 0;i<cursor.getColumnCount();i++)
//                    text += cursor.getColumnName(i) + ": " + cursor.getString(i) + "\n";
//
//                textView.setText(text);
//                Log.i("CONTACT_PROVIDER_DEMO",text);
//            }
//            cursor.close();
//        }
//    }
//
//    private void getSms(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_SMS},0);
//        }
//        TextView textView = findViewById(R.id.textView);
//
//        ContentResolver contentResolver = getContentResolver();
//        Uri uri = Uri.parse("content://sms/");
//        Cursor cursor = contentResolver.query(uri,null,null,null,null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//            String text = "\n\n\n\n\n\n\n";
//            // Retrieve all fields for the current SMS
//            for (int i = 0;cursor != null && i < cursor.getColumnCount(); i++) {
//                String columnName = cursor.getColumnName(i);
//                String value = cursor.getString(i);
//                text += columnName + ": " + value+"\n";
//            }
//            Log.d("SmsReader",text );
//            Log.d("SmsReader", "--------------------------");
//            textView.setText(text);
//            cursor.close();
//        }
//    }

}