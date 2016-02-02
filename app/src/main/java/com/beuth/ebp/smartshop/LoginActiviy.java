package com.beuth.ebp.smartshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

/**
 * Created by Jihed on 02.02.2016.
 */
public class LoginActiviy extends Activity {
    ListReposTask getlistTask;
    Button btn;
    String sessionIDBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getlistTask = new ListReposTask();
        getlistTask.execute();
        btn = (Button) findViewById(R.id.buttontoken);

        btn.setOnClickListener(new View.OnClickListener() {
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

}
