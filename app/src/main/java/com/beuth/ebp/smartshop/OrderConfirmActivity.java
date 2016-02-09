package com.beuth.ebp.smartshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class OrderConfirmActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";

    String confirmOrderBody;
    String confirmOrderSuccess;

    String name;
    String street;
    int housenr;
    int zip;
    int position;
    String city;
    String email;
    String phone;
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
    ConfirmOrderTask getOrderTask;
    LinearLayout section1;
    LinearLayout section2;
    LinearLayout section3;
    LinearLayout recieverlayoutadress;
    LinearLayout recieverlayoutinfos;
    LinearLayout senderlayoutadress;
    Button confirmButton;
    Button continue1;
    Button continue2;

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
        section1 = (LinearLayout) findViewById(R.id.sectionhide1);
        section2 = (LinearLayout) findViewById(R.id.sectionhide2);
        section3 = (LinearLayout) findViewById(R.id.sectionhide3);
        recieverlayoutadress = (LinearLayout) findViewById(R.id.sectionrecieveradress);
        recieverlayoutinfos = (LinearLayout) findViewById(R.id.sectionrecieverinfos);
        senderlayoutadress = (LinearLayout) findViewById(R.id.sectionsenderadress);
        continue1 = (Button) findViewById(R.id.blockbtn1);
        continue2 = (Button) findViewById(R.id.blockbtn2);

        //get strings from shared preferences
        settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("senderstr", "") != null) {
            txtsstr.setText(settings.getString("senderstr", ""));
            txtscity.setText(settings.getString("sendercity", ""));
            txtszip.setText(settings.getString("senderzip", ""));
        } else {
            section3.setVisibility(View.GONE);
            senderlayoutadress.setVisibility(View.VISIBLE);
        }



        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section1.setVisibility(View.VISIBLE);
                recieverlayoutadress.setVisibility(View.GONE);
                section2.setVisibility(View.GONE);
                recieverlayoutinfos.setVisibility(View.VISIBLE);
            }
        });
        continue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section2.setVisibility(View.VISIBLE);
                recieverlayoutinfos.setVisibility(View.GONE);
                section3.setVisibility(View.GONE);
                senderlayoutadress.setVisibility(View.VISIBLE);
            }
        });

        section1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section1.setVisibility(View.GONE);
                section2.setVisibility(View.VISIBLE);
                recieverlayoutinfos.setVisibility(View.GONE);
                section3.setVisibility(View.VISIBLE);
                senderlayoutadress.setVisibility(View.GONE);
                recieverlayoutadress.setVisibility(View.VISIBLE);
            }
        });
        section2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section1.setVisibility(View.VISIBLE);
                recieverlayoutadress.setVisibility(View.GONE);
                recieverlayoutinfos.setVisibility(View.VISIBLE);
                section2.setVisibility(View.GONE);
                section3.setVisibility(View.VISIBLE);
                senderlayoutadress.setVisibility(View.GONE);
            }
        });
        section3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                section1.setVisibility(View.VISIBLE);
                section2.setVisibility(View.VISIBLE);
                section3.setVisibility(View.GONE);
                recieverlayoutinfos.setVisibility(View.GONE);
                recieverlayoutadress.setVisibility(View.GONE);
                senderlayoutadress.setVisibility(View.VISIBLE);
            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            street = extras.getString("street");
            housenr = extras.getInt("housenr");
            zip = extras.getInt("zip");
            city = extras.getString("city");
            email = extras.getString("email");
            phone = extras.getString("phone");
            position = extras.getInt("position");
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
                getOrderTask = new ConfirmOrderTask();
                getOrderTask.execute();
            }
        });

    }

    class ConfirmOrderTask extends AsyncTask<String, Void, Response> {

        ProgressDialog progressDialog = new ProgressDialog(OrderConfirmActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Confirming Order");
            progressDialog.show();
        }

        @Override
        protected Response doInBackground(String... params) {
            GithubService githubService = new RestAdapter.Builder()
                    .setEndpoint(GithubService.ENDPOINT)
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            retrofit.client.Response r = cause.getResponse();
                            if (r != null && r.getStatus() == 405) {
                                Toast.makeText(getApplicationContext(), "Impossible d'effectuer cette action", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);
            try {

                return githubService.ConfirmOrder(position);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response params) {
            try {
                super.onPostExecute(params);
                confirmOrderBody = params.getBody();
                confirmOrderSuccess = params.getStatus();
                Log.e("onPostExecute", "");
            } catch (Exception e) {
                confirmOrderBody = null;
            }

            if (confirmOrderBody != null && !confirmOrderBody.equals("null")) {
                Toast.makeText(getApplicationContext(), "confirm order for : " + name + " with success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderConfirmActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "error when confirming order", Toast.LENGTH_SHORT).show();
            }
        }
    }
}





