package com.beuth.ebp.smartshop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
    AddProductTask addProductTask;
    TabLayout tabLayout;
    FloatingActionButton floatingActionButton;
    List<Order> reposer;
    private SessionId tabId;
    String token;
    String addItemResponse;
    String itemTitle;
    String itemDescitpion;
    String itemStartPrice;
    int selectedTabPosition;
    SharedPreferences settings;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] osArray = new String[2];
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingbtn);
        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        builder = new AlertDialog.Builder(this);
        settings = getSharedPreferences(PREFS_NAME, 0);
        token = settings.getString("userToken", "");

        tabId = SessionId.getInstance();

        Log.e("token on create ", token);

        addDrawerItems();
        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Products"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));

        getOrderTask = new OrderTask();
        getOrderTask.execute();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProductDialog();
            }
        });
    }

    public void showAddProductDialog() {
        selectedTabPosition = tabLayout.getSelectedTabPosition();

        tabId.setSessionId(selectedTabPosition);
        Context context = MainActivity.this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText titleBox = new EditText(context);
        titleBox.setHint("Title");
        titleBox.setHeight(150);
        layout.addView(titleBox);

        final EditText descriptionBox = new EditText(context);
        descriptionBox.setHint("Description");
        descriptionBox.setHeight(300);
        layout.addView(descriptionBox);

        final EditText startPriceBox = new EditText(context);
        startPriceBox.setHint("price in EURO (ex. 200)");
        startPriceBox.setHeight(150);
        startPriceBox.setCursorVisible(true);
        layout.addView(startPriceBox);

        dialog.setTitle("Add new product form");
        dialog.setMessage("Put product info and confirm");
        dialog.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        if (!titleBox.getText().toString().equals("") && !descriptionBox.getText().toString().equals("") && !startPriceBox.getText().toString().equals("")) {
                            itemTitle = titleBox.getText().toString();
                            itemDescitpion = descriptionBox.getText().toString();
                            itemStartPrice = startPriceBox.getText().toString();
                            addProductTask = new AddProductTask();
                            addProductTask.execute();
                            Toast.makeText(MainActivity.this, "call add product Request of : " + itemTitle, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dialog.setNegativeButton(android.R.string.no, executeNegativeAddProductDialog());
        dialog.setIcon(R.drawable.ic_action_add);
        dialog.setView(layout);
        dialog.show();
    }

    private DialogInterface.OnClickListener executeNegativeAddProductDialog() {
        return new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "add product canceled", Toast.LENGTH_SHORT).show();
            }
        };
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
            }, 3 * 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
         // Always call the superclass method first
        getListTask = new ListReposTask();
        getListTask.execute();
        getOrderTask = new OrderTask();
        getOrderTask.execute();

    }
    @Override
    public void onPause() {
        super.onPause();
        selectedTabPosition = tabLayout.getSelectedTabPosition();

        tabId.setSessionId(selectedTabPosition);

        Log.e("selectedtab",""+selectedTabPosition);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("start", "" + selectedTabPosition);
selectLastSelectedTab();
    }

    class ListReposTask extends AsyncTask<String, Void, List<Item>> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Updating");
            progressDialog.show();
        }

        @Override
        protected List<Item> doInBackground(String... params) {
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
                                Toast.makeText(getApplicationContext(), "Server Error : 405", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);
            try {
                return githubService.getItemsList(token).getItems();
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Item> repos) {
            if (repos == null) {
                super.onPostExecute(repos);
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "you don't have any product, you can add a new product from this Dialog", Toast.LENGTH_LONG).show();
                showAddProductDialog();
            } else {
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
                progressDialog.dismiss();
              int  selected = tabId.getSessionId();
                TabLayout.Tab tab = tabLayout.getTabAt(selected);
                tab.select();
                Log.e("selectedtab", "" + selected);

            }

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
                                Toast.makeText(getApplicationContext(), "Server Error : 405", Toast.LENGTH_SHORT).show();
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
                Log.e("onPostExecute", "" + reposer);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    class AddProductTask extends AsyncTask<String, Void, com.beuth.ebp.smartshop.Response> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Add product to shop");
            progressDialog.show();
        }

        @Override
        protected com.beuth.ebp.smartshop.Response doInBackground(String... params) {
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
                                Toast.makeText(getApplicationContext(), "Server Error : 405", Toast.LENGTH_SHORT).show();
                            }
                            return cause;
                        }
                    })
                    .build().create(GithubService.class);
            try {
                com.beuth.ebp.smartshop.Response response = githubService.getAddItemResponse(token, itemTitle, itemDescitpion, itemStartPrice);
                return response;
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
                if (!repos.getBody().equals("null")) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "add product success with ID: " + addItemResponse, Toast.LENGTH_SHORT).show();
                    getListTask.execute();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "add product error from server", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                addItemResponse = null;
            }
            if (addItemResponse == null) {
                Toast.makeText(getApplicationContext(), "can not add Product, see Connection and try again", Toast.LENGTH_SHORT).show();
                startActivity(getIntent());
            }
        }
    }


    private void addDrawerItems() {
        osArray = new String[]{"Abmelden", "Impressum"};
        Integer[] imageId = {
                R.drawable.logouticon,
                R.drawable.info
        };
        CustomNavRow adapter = new CustomNavRow(MainActivity.this, osArray, imageId);

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        builder.setTitle("Abmelden")
                                .setMessage("Sind Sie sicher, dass Sie sich abmelden wollen?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.clear();
                                        editor.commit();
                                        Intent homeIntent = new Intent(getApplicationContext(), LoginActiviy.class);
                                        startActivity(homeIntent);
                                    }
                                })
                                .setNegativeButton("Nein", null)
                                .show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "SmartShop", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MainActivity.this, "you didnt clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("SmartShop");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("SmartShop");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks CustomNavRowhere. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectLastSelectedTab() {
        if (selectedTabPosition >= 0) {
            TabLayout.Tab selectedTab = tabLayout.getTabAt(selectedTabPosition);
            selectedTab.select();
        }
    }
}