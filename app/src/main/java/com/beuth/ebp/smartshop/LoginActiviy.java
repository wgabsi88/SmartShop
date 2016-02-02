package com.beuth.ebp.smartshop;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Jihed on 02.02.2016.
 */
public class LoginActiviy extends AppCompatActivity {
    ListReposTask getlistTask;
    FetchToken getFetchToken;
    Button btngoeBay;
    Button btngoShop;
    TextView txtgoeBay;
    TextView txtgoShop;
    String sessionIDBody;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getlistTask = new ListReposTask();
        getlistTask.execute();
        btngoeBay = (Button) findViewById(R.id.buttongotoeBay);
        btngoShop = (Button) findViewById(R.id.buttongotosmartshop);
        txtgoeBay = (TextView) findViewById(R.id.textgotoebay);
        txtgoShop = (TextView) findViewById(R.id.textgotosmartshop);

        btngoeBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String sessionID = "qqsDAA**9fa7080e1520a471d237f6c7fffff5da";
                String sessionID = sessionIDBody;
                String ruName = "beuth-beuth5863-6795--zmfzuz";
                String uriString = "https://signin.sandbox.ebay.com/ws/eBayISAPI.dll?SignIn&RUName="
                        + ruName + "&SessID=" + sessionID + "";
                Uri uri = Uri.parse(uriString); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

                btngoeBay.setVisibility(View.GONE);
                txtgoeBay.setVisibility(View.GONE);
                btngoShop.setVisibility(View.VISIBLE);
                txtgoShop.setVisibility(View.VISIBLE);
            }
        });

        btngoShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sessionID = sessionIDBody;
                getFetchToken = new FetchToken();
                getFetchToken.execute();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("userToken",token);
                startActivity(intent);
            }
        });
    }


    class ListReposTask extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... params) {
            GithubService githubService = new RestAdapter.Builder()
                    .setEndpoint(GithubService.ENDPOINT)
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            retrofit.client.Response r = cause.getResponse();
                            if (r != null && r.getStatus() == 405) {
                                //      Toast.makeText(this, "Impossible d'effectuer cette action", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);

            Response repoList = githubService.sessionISResponse();
            return repoList;
        }

        @Override
        protected void onPostExecute(Response repos) {
            super.onPostExecute(repos);
            sessionIDBody = repos.getBody();
            Log.e("euloooooooooooo", "" + repos.getBody());
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
                            if (r != null && r.getStatus() == 405) {
                                //      Toast.makeText(this, "Impossible d'effectuer cette action", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);

            Response repoList = githubService.getTokenResponse(sessionIDBody);
            return repoList;
        }

        @Override
        protected void onPostExecute(Response repos) {
            super.onPostExecute(repos);
            token = repos.getBody();
            Log.e("euloooooooooooo", "" + repos.getBody());
        }
    }

}
