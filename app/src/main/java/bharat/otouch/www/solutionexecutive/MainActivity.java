package bharat.otouch.www.solutionexecutive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    android.support.v7.app.ActionBar actionBar;

    private DrawerLayout mdraer;
    private ActionBarDrawerToggle mtoggle;

    // CONNECTION_TIMEOUT and READ_TIMEOUT are in milliseconds
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;

    private RecyclerView recyclerView;
    private Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mdraer = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBar = getSupportActionBar();
        actionBar.show();

        mtoggle = new ActionBarDrawerToggle(this, mdraer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mdraer.addDrawerListener(mtoggle);
        mtoggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Make call to AsyncTask
        new MainActivity.AsyncFetch().execute();

    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.account) {
            Intent h=new Intent(this, OpenAccount.class);
            startActivity(h);

        }
        /*else if (id == R.id.out) {
            Intent a=new Intent(this,AboutUs.class);
            startActivity(a);
        }
        */
        return false;
    }

    class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL("http://172.28.172.2/problemsolver/jsondata.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }
            try {
                int response_code = conn.getResponseCode();
                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            //this method will be running on UI thread
            //   Toast.makeText(MainActivity.this, "result"+result, Toast.LENGTH_SHORT).show();
            pdLoading.dismiss();
            List<UserData> data = new ArrayList<>();

            pdLoading.dismiss();
            try {
                Log.d("TAG", result);
                JSONArray jArray = new JSONArray(result);
              //  Toast.makeText(MainActivity.this, "json result" + result, Toast.LENGTH_SHORT).show();
                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject json_data = jArray.getJSONObject(i);
                    UserData fishData = new UserData();
                    Log.d("TAG", json_data.getString("Name"));
                    Log.d("TAG", json_data.getString("Email"));
                    Log.d("TAG", json_data.getString("Mobile"));
                    Log.d("TAG", json_data.getString("Query"));
                    Log.d("TAG",json_data.getString("Fcm_token"));
                    //Log.d("TAG",json_data.getString("Image"));

                 /*   Toast.makeText(MainActivity.this, "Name"+json_data.getString("Name"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Email"+json_data.getString("Email"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Mobile"+json_data.getString("Mobile"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "Query"+json_data.getString("Query"), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Image"+json_data.getString("Image"), Toast.LENGTH_SHORT).show();
                   */
                    fishData.UserName = json_data.getString("Name");
                    fishData.UserEmail = json_data.getString("Email");
                    fishData.UserMobile = json_data.getString("Mobile");
                    fishData.UserQuery = json_data.getString("Query");
                    fishData.Fcmtoken = json_data.getString("Fcm_token");
                    //  fishData.sizeName= json_data.getString("name");
                    // fishData.price= json_data.getInt("Id");
                    //Log.d("TAG","Server result"+""+json_data.getString("img"));
                    data.add(fishData);
                }

                // Setup and Handover data to recyclerview
                recyclerView = (RecyclerView) findViewById(R.id.fishPriceList);
                Log.d("TAG", "recycle!!");
                //  Toast.makeText(MainActivity.this, "data pass to adapter" + data, Toast.LENGTH_SHORT).show();
                mAdapter = new Adapter(MainActivity.this, data);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mtoggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
