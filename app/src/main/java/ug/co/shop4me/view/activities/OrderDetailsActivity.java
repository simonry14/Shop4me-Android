package ug.co.shop4me.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import ug.co.shop4me.adapter.OrderProductsAdapter;
import ug.co.shop4me.model.entities.Product;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView idTxt;
    TextView dateTxt;
    TextView totalTxt;
    TextView payment;
    TextView shipping;
    TextView time;
   RecyclerView ordersProductsRecyclerView;
    OrderProductsAdapter adapter;
    ArrayList<Product> orderProductsList;
    String URL = MainActivity.BASE_URL+"all_products_in_order/";
    String orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Order Details");

        idTxt = (TextView) findViewById(R.id.idTxt);
        dateTxt = (TextView) findViewById(R.id.dateTxt);
        totalTxt = (TextView) findViewById(R.id.totalTxt);
        payment = (TextView) findViewById(R.id.paymentTxt);
        shipping = (TextView) findViewById(R.id.addressTxt);

        time = (TextView) findViewById(R.id.timeTxt);

                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
        orderId = extras.getString("id");
        idTxt.setText(orderId);
        dateTxt.setText(MainActivity.formatter.format(Long.valueOf(extras.getString("total")))+ "/=");
        totalTxt.setText(extras.getString("date"));
        payment.setText(extras.getString("payment"));
        shipping.setText(extras.getString("address"));
        time.setText(extras.getString("time"));

        ordersProductsRecyclerView = (RecyclerView) findViewById(R.id.order_product_list_recycler_view);
        orderProductsList = new ArrayList<>();
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(this);
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        ordersProductsRecyclerView.setLayoutManager(MyLayoutManager2);
         adapter = new OrderProductsAdapter(this, orderProductsList);
        ordersProductsRecyclerView.setAdapter(adapter);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
new getOrderProducts().execute();
    }

    public class getOrderProducts extends AsyncTask<String, Void, String> {
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
            adapter.notifyDataSetChanged();
        }
    }

    //Uses volley to asynchronously download data from the rest source and fills the list of items.
    private void getData() {
        Log.i("getData", "called");
        RequestQueue que = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL+orderId,
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
            Product p = new Product(obj.optString("price"), obj.optString("quantity"), imgUrl, obj.optString("name"));
            orderProductsList.add(p);
        }
        adapter.notifyDataSetChanged();
        // productList = items;
    }

}
