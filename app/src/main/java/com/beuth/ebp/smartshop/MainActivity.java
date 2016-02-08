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
    ListReposTask getListTask;
    OrderTask getOrderTask;
    AddProductTask addProductTask;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;
    List<Order> reposer;
    String token;
    String addItemResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingbtn);

        SharedPreferences settings = getSharedPreferences(LoginActiviy.PREFS_NAME, 0);
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

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Item")
                        .setMessage("Are you sure you want to add new Item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                addProductTask = new AddProductTask();
                                addProductTask.execute();
                                //Toast.makeText(MainActivity.this, "add Item succes", Toast.LENGTH_SHORT).show();
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
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        getListTask = new ListReposTask();
        getListTask.execute();
    }

    class ListReposTask extends AsyncTask<String, Void, List<Item>> {

        @Override
        protected List<Item> doInBackground(String... params) {
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

            Items repoList = githubService.getItemsList(token);
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
                            return cause;                        }
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
                Log.e("onPostExecute", "" + reposer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class AddProductTask extends AsyncTask<String, Void, com.beuth.ebp.smartshop.Response> {

        @Override
        protected com.beuth.ebp.smartshop.Response doInBackground(String... params) {
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
                return githubService.getAddItemResponse();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(com.beuth.ebp.smartshop.Response repos) {
            try {
                super.onPostExecute(repos);
                addItemResponse = repos.getBody();
                Log.e("Product ID: ", "" + repos.getBody());
                Toast.makeText(getApplicationContext(),"add product succes with ID: " + addItemResponse, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                addItemResponse = null;
            }
            if (addItemResponse == null) {
                Toast.makeText(getApplicationContext(), "can not add Product, see Connection and try again", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
            }
        }
    }
}