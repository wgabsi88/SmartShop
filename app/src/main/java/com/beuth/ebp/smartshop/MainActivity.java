package com.beuth.ebp.smartshop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "LoginPrefs";
    ListReposTask getListTask;
    OrderTask getOrderTask;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;
    List<Order> reposer;
    String token;
    private int  selectedTabPosition;
    int data;
    SharedPreferences settings ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingbtn);

        settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("userToken", "");
        Log.e("token on create ", token);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));

        getOrderTask = new OrderTask();
        getOrderTask.execute();

        getListTask = new ListReposTask();
        getListTask.execute();
     //   if(lastTab >= 0){
           //selectLastSelectedTab(1);

    //    }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Item")
                        .setMessage("Are you sure you want to add new Item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "add Item succes", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "add Item canceled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


    }

    public void closeApp() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            closeApp(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                                          @Override
                                          public void run() {
                                              exit = false;
                                          }
                                      },
                    3 * 1000);
        }
    }


    @Override
     public void onStart() {
        super.onStart();
      //  settings = getSharedPreferences(PREFS_NAME, 0);
        int lastTab = settings.getInt("last", -1);

        Log.e("onStart", "" + lastTab);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("last", selectedTabPosition);

        editor.commit();

        Log.e("onDestroy", "" + selectedTabPosition);
    }


    @Override
    public void onResume() {
        super.onResume();
        // Always call the superclass method first

        getListTask = new ListReposTask();
        getListTask.execute();

        Log.e("onResume", ""  );

    }
    @Override
    public void onPause() {
        super.onPause();
        selectedTabPosition = tabLayout.getSelectedTabPosition();
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("logged", "logged");
        editor.putString("userToken", token);
        editor.commit();
        Log.e("selected last tab",""+selectedTabPosition);
    }
    class ListReposTask extends AsyncTask<String, Void, List<Item>> {
        int lastTab;
        @Override
        protected List<Item> doInBackground(String... params) {
            settings = getSharedPreferences(PREFS_NAME, 0);
            lastTab = settings.getInt("last", -1);
            // Log.e("before header",token);
            GithubService githubService = new RestAdapter.Builder()
                    .setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade request) {
                            request.addHeader("token", token);
                        }
                    })
                    .setEndpoint(GithubService.ENDPOINT)
                    .setErrorHandler(new ErrorHandler() {
                        @Override
                        public Throwable handleError(RetrofitError cause) {
                            Response r = cause.getResponse();
                            if (r != null && r.getStatus() == 405) {
                                Toast.makeText(getApplicationContext(), "Impossible d'effectuer cette action", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);

            //  Items repoList = githubService.getItemsList(token1);
            //   Log.e("before list",token);
            Items repoList = githubService.getItemsList(token);
            //  Log.e("after list",""+repoList);
            return repoList.getItems();
        }

        @Override
        protected void onPostExecute(List<Item> repos) {
            super.onPostExecute(repos);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), repos, reposer);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


        }
    }

    class OrderTask extends AsyncTask<String, Void, List<Order>> {

        @Override
        protected List<Order> doInBackground(String... params) {
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
                Orders repoList = githubService.listOrder();
                return repoList.getOrders();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Order> repos) {
            try {
                super.onPostExecute(repos);
                reposer = repos;
               // TabLayout.Tab selectedTab = tabLayout.getTabAt(1);
             //   selectedTab.select();
                Log.e("onPostExecute", "" + reposer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void selectLastSelectedTab(int selectedTabPosition) {
        if (selectedTabPosition >= 0) {
            TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabPosition);
            selectedTab.select();
        }
    }
/*
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("last", selectedTabPosition);
        Log.e("onsave", "" + selectedTabPosition);

        // etc.
    }
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        int selectedTabPosition = savedInstanceState.getInt("last");

        Log.e("onrestore", "" + selectedTabPosition);

    } */
}