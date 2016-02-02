package com.beuth.ebp.smartshop;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = (Button) findViewById(R.id.buttontoken);
        getlistTask = new ListReposTask();
        getlistTask.execute();
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

            //  String user = params[0];
            Response repoList = githubService.sessionISResponse();
            return repoList;
        }

        @Override
        protected void onPostExecute(Response repos) {
            super.onPostExecute(repos);
            Log.e("euloooooooooooo", "" + repos.getBody());
        }
    }

}
