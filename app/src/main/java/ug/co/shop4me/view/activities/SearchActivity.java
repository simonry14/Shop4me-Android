package ug.co.shop4me.view.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.util.MySuggestionProvider;
import ug.co.shop4me.view.adapter.ProductListAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SearchActivity extends AppCompatActivity {
    private static FrameLayout redCircle;
    private static TextView  countTextView;
    private ArrayList<Product> productList = new ArrayList<Product>();
    String BASE_URL = MainActivity.BASE_URL+"search_all_products/";
    public static     ProductListAdapter adapter;
    RecyclerView recyclerView;
    String query;
    private static int  alertCount = 0;
    TextView noResults;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.filteredRecyclerView);
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(this);
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
noResults = (TextView) findViewById(R.id.noResults);
        noResults.setVisibility(View.GONE);
        recyclerView.setLayoutManager(MyLayoutManager2);
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.show();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
             query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(query);
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    MySuggestionProvider.AUTHORITY, MySuggestionProvider.MODE);
            suggestions.saveRecentQuery(query, null);
            //doMySearch(query);
            new getProducts().execute();
        }

        progressDialog.dismiss();

    }

    public class getProducts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            new Thread() {
                @Override
                public void run() {
                    getData();
                }
            }.start();
            return "Done";
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Result", s);
            adapter = new ProductListAdapter(SearchActivity.this, productList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
    }

    //Uses volley to asynchronously download data from the rest source and fills the list of items.
    private void getData() {
        Log.i("getData", "called");

        RequestQueue que = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                BASE_URL+query,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse", response);


                        createListFromData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley error", ""+ error.getMessage());
                    }
                }
        );

        que.add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void createListFromData(String data) {


        JSONArray mainArray = null;
        try {
            mainArray = new JSONArray(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int length = mainArray.length();
        if(length==0){
            noResults.setVisibility(View.VISIBLE);
            noResults.setText("No search results for "+ query);
        }else{
            noResults.setVisibility(View.GONE);
        }
        String url =MainActivity.IMAGE_URL;;
        for (int i = 0; i < length; i++) {
            //pull out every object in the array and add it to the list.
            JSONObject obj = mainArray.optJSONObject(i);
            String imgPath = obj.optString("image");
            String imgNameFull = imgPath.split("/")[1];
            String imgName = imgNameFull.split("[.]")[0];
            String imgExt = imgNameFull.split("[.]")[1];
            String img = imgName +"-40x40."+imgExt;
            // String imgUrl = url+img;
            String imgUrl = url+imgNameFull;
            Product p = new Product(obj.optString("name"), obj.optString("description"), "", "123", "", obj.optString("price"),
                    "", imgUrl, obj.optString("product_id"));
            productList.add(p);
        }
        adapter.notifyDataSetChanged();
        // productList = items;

    }

    public static void updateAlertIcon(boolean increament) {
        // if alert count extends into two digits, just show the red circle
        alertCount = MainActivity.shoppingCartList.size();
      //  if(increament) {
         //   alertCount = alertCount + 1;
            //alertCount = MainActivity.shoppingCartSize;
       // }else{
        //    alertCount = alertCount - 1;
            //alertCount = MainActivity.shoppingCartSize;
       // }

        countTextView.setText(String.valueOf(alertCount));

        if(alertCount!=0) {
            redCircle.setVisibility(VISIBLE);
        }else{
            redCircle.setVisibility(GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_menu_item).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        searchView.setSubmitButtonEnabled(true);



        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem alertMenuItem = menu.findItem(R.id.cart_menu_item);
        FrameLayout rootView = (FrameLayout) alertMenuItem.getActionView();

       redCircle = (FrameLayout) rootView.findViewById(R.id.view_alert_red_circle);
        countTextView = (TextView) rootView.findViewById(R.id.view_alert_count_textview);
//redCircle.setVisibility(View.VISIBLE);
        countTextView.setText(String.valueOf(MainActivity.shoppingCartList.size()));
        if(MainActivity.shoppingCartList.size()>0){
            redCircle.setVisibility(View.VISIBLE);
        }
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(alertMenuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.cart_menu_item) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }

        if (id == R.id.search_menu_item) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
