package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class LoginActiviy extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    SessionIDTask getlistTask;
    FetchToken getFetchToken;
    Button btngoeBay;
    Button btngoShop;
    TextView txtgoeBay;
    TextView txtgoShop;
    String sessionIDBody;
    String token;
    String tokensucces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getString("logged", "").equals("logged")) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        getlistTask = new SessionIDTask();
        getlistTask.execute();
        btngoeBay = (Button) findViewById(R.id.buttongotoeBay);
        btngoShop = (Button) findViewById(R.id.buttongotosmartshop);
        txtgoeBay = (TextView) findViewById(R.id.textgotoebay);
        txtgoShop = (TextView) findViewById(R.id.textgotosmartshop);

        btngoeBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionIDBody != null && sessionIDBody != "null") {
                    //String sessionID = "qqsDAA**9fa7080e1520a471d237f6c7fffff5da";
                    String ruName = "beuth-beuth5863-6795--zmfzuz";
                    String uriString = "https://signin.sandbox.ebay.com/ws/eBayISAPI.dll?SignIn&RUName="
                            + ruName + "&SessID=" + sessionIDBody + "";
                    Uri uri = Uri.parse(uriString); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                    btngoeBay.setVisibility(View.GONE);
                    txtgoeBay.setVisibility(View.GONE);
                    btngoShop.setVisibility(View.VISIBLE);
                    txtgoShop.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "please click again", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btngoShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFetchToken = new FetchToken();
                getFetchToken.execute();
            }
        });
    }

    class SessionIDTask extends AsyncTask<String, Void, Response> {

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
                return githubService.sessionIDResponse();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response repos) {
            try {
                super.onPostExecute(repos);
                sessionIDBody = repos.getBody();
                Log.e("SessionIDBody: ", "" + repos.getBody());
            } catch (Exception e) {
                sessionIDBody = null;
            }
            if (sessionIDBody == null) {
                Toast.makeText(getApplicationContext(), "can not get session ID, fix internet and try again", Toast.LENGTH_LONG).show();
                startActivity(getIntent());
            }
        }
    }

    class FetchToken extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... params) {
            GithubService githubService = new RestAdapter.Builder()
                    .setEndpoint(GithubService.ENDPOINT)
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            retrofit.client.Response r = cause.getResponse();
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);
            try {
                return githubService.getTokenResponse(sessionIDBody);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response repos) {
            super.onPostExecute(repos);
            try {
                token = repos.getBody();
                Log.e("euloooooooooooo", "" + repos.getBody());
                tokensucces = repos.getStatus();
                Log.e("token body", "" + repos.getBody());
                Log.e("token status", "" + repos.getStatus());
            } catch (Exception e) {
                token = null;
                tokensucces = null;
            }
            if (token == null || token.equals("null")) {
                Toast.makeText(getApplicationContext(), "can not get token please login and agree", Toast.LENGTH_LONG).show();
                btngoeBay.setVisibility(View.VISIBLE);
                txtgoeBay.setVisibility(View.VISIBLE);
                btngoShop.setVisibility(View.GONE);
                txtgoShop.setVisibility(View.GONE);
            } else {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("logged", "logged");
                editor.putString("userToken", token);
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("userToken", token);
                startActivity(intent);
            }
        }
    }
}