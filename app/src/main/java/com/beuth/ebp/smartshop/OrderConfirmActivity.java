package com.beuth.ebp.smartshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OrderConfirmActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    String name;
    String street;
    int housenr;
    int zip;
    String city;
    String email;
    String phone;
    Button confirmButton;
    SharedPreferences settings;
    EditText txtrstreet;
    EditText txtrcity;
    EditText txtrzip;
    EditText txtname;
    EditText txtemail;
    EditText txtmobile;
    EditText txtsstr;
    EditText txtscity;
    EditText txtszip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_order);
        // UI Declartion
         txtrstreet = (EditText) findViewById(R.id.str);
         txtrcity = (EditText) findViewById(R.id.city);
         txtrzip = (EditText) findViewById(R.id.zip);
         txtname = (EditText) findViewById(R.id.name);
         txtemail = (EditText) findViewById(R.id.email);
         txtmobile = (EditText) findViewById(R.id.mobile);
         txtsstr = (EditText) findViewById(R.id.senderstr);
         txtscity = (EditText) findViewById(R.id.sendercity);
         txtszip = (EditText) findViewById(R.id.senderzip);
        //get strings from shared preferences
         settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("senderstr", "") != null) {
            txtsstr.setText(settings.getString("senderstr", ""));
            txtscity.setText(settings.getString("sendercity", ""));
            txtszip.setText(settings.getString("senderzip", ""));

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            //  Log.e("name",""+name);
            street = extras.getString("street");
            //  Log.e("street",""+street);
            housenr = extras.getInt("housenr");
            zip = extras.getInt("zip");
            // Log.e("zip",""+zip);
            city = extras.getString("city");
            //  Log.e("city",""+city);
            email = extras.getString("email");
            //  Log.e("email",""+email);
            phone = extras.getString("phone");
            //  Log.e("phone",""+phone); */

        }



        txtrstreet.setText(street);
        txtrcity.setText(city);
        txtrzip.setText("" + zip);
        txtname.setText(name);
        txtmobile.setText(phone);
        txtemail.setText(email);



        confirmButton = (Button) findViewById(R.id.confirmbtn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(OrderConfirmActivity.this, OrderConfirmActivity.class);

             //   startActivity(intent);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("senderstr", txtsstr.getText().toString());
                editor.putString("sendercity", txtscity.getText().toString());
                editor.putString("senderzip", txtszip.getText().toString());
                editor.commit();

            }
        });

    }
}





