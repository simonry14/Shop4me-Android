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
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.adapter.ShoppinListAdapter;

public class PlaceOrderActivity extends AppCompatActivity {
    Button b;
    String address;
    String payment;
    String time;
    TextView addressText;
    TextView paymentText;
    TextView timeText;
    TextView deliveryFee;
    TextView deliveryTitle;
    TextView totalFee;
    String fee ;
    Integer total;
    String orderId;
    private static RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
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

        setTitle("Review Order");

        addressText = (TextView) findViewById(R.id.address);
        timeText = (TextView) findViewById(R.id.time);
        paymentText = (TextView) findViewById(R.id.payment);

        deliveryTitle = (TextView) findViewById(R.id.feeTitle);
        deliveryFee = (TextView) findViewById(R.id.fee);
        totalFee = (TextView) findViewById(R.id.total);
        deliveryTitle.setText("Delivery Fee: ");
        b = (Button) findViewById(R.id.order);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        address = extras.getString("address");
        addressText.setText(address);
        time = extras.getString("time");
        timeText.setText(time);
        payment = extras.getString("payment");
        paymentText.setText(payment);
        recyclerView = (RecyclerView) findViewById(R.id.product_list_recycler_view);

        if (MainActivity.shoppingCartList.size() != 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            ShoppinListAdapter shoppinListAdapter = new ShoppinListAdapter(getApplicationContext());
            recyclerView.setAdapter(shoppinListAdapter);
        } else {


        }


        if (address.contains("Pick from Supermarket")) {
            deliveryTitle.setText("Service Fee: ");
                DatabaseReference usersRef = Utils.getDatabase().getReference().child("App Values").child("Service Fee");
                usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        fee = dataSnapshot.getValue().toString();
                        deliveryFee.setText(MainActivity.formatter.format(Integer.valueOf(fee)) + "/=");

                        total = 0;
                        for (Product p : MainActivity.shoppingCartList) {
                            total = total + Integer.valueOf(p.getSellMRP().split("[.]")[0]) * Integer.valueOf(p.getQuantity());
                        }
                        total = total + Integer.valueOf(fee);
                        totalFee.setText(MainActivity.formatter.format(total) + "/=");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }else{

        if (MainActivity.isExpress) {
            deliveryFee.setText("Free");
            total = 0;
            for (Product p : MainActivity.shoppingCartList) {
                total = total + Integer.valueOf(p.getSellMRP().split("[.]")[0]) * Integer.valueOf(p.getQuantity());
            }
            totalFee.setText(MainActivity.formatter.format(total) + "/=");
        } else {

            DatabaseReference usersRef = Utils.getDatabase().getReference().child("App Values").child("Delivery Fee");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fee = dataSnapshot.getValue().toString();
                    deliveryFee.setText(MainActivity.formatter.format(Integer.valueOf(fee)) + "/=");

                    total = 0;
                    for (Product p : MainActivity.shoppingCartList) {
                        total = total + Integer.valueOf(p.getSellMRP().split("[.]")[0]) * Integer.valueOf(p.getQuantity());
                    }
                    total = total + Integer.valueOf(fee);
                    totalFee.setText(MainActivity.formatter.format(total) + "/=");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (payment.equals("Mobile Money") || payment.equals("Debit Card")) {
                    new PlaceOrder().execute(); //place order then go to payment
                    Intent in = new Intent(PlaceOrderActivity.this, InstapayActivity.class);
                    in.putExtra("address", address);
                    in.putExtra("time", time);
                    in.putExtra("payment", payment);
                    in.putExtra("total", total.toString());
                    in.putExtra("id", orderId);
                    startActivity(in);
                }else  {
                    new PlaceOrder().execute();
                    Intent in = new Intent(PlaceOrderActivity.this, OrderSuccessActivity.class);
                    startActivity(in);
                }

            }
        });

    }

    public class PlaceOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            new Thread() {
                @Override
                public void run() {
                    placeOrder(getFirstName(MainActivity.fullname),getLastName(MainActivity.fullname),MainActivity.email,MainActivity.phone1,
                            payment,address,time,total.toString());
                }
            }.start();
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    private void placeOrder(String firstName,String lastName, String email,String telephone,String payment,String address, String comment,String total){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = MainActivity.BASE_URL+"add_order?customerid="+MainActivity.customerId+ "&firstname="+firstName+ "&lastname="+lastName+"&email="+email+"&telephone="+telephone+"&payment="+payment+"&address="+address+"&comment="+comment+"&total="+total;
        url= url.replace(" ", "%20");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.i("ORDER ID", response);
                        //Send Products
                        String res = response;
                        String res1 = res.substring(1);
                        orderId = res1;
                        addOrderProducts(orderId);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void addOrderProducts(String orderId){
        int count = 0;
        for(Product p :MainActivity.shoppingCartList){
            Log.d("INSIDE PRODUCTS","LOOP "+count);
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = MainActivity.BASE_URL+"add_order_products?orderid="+orderId+ "&productid="+p.getProductId()+ "&name="+p.getItemName()
                    +"&model="+" " +"&quantity="+p.getQuantity()+"&price="+p.getSellMRP()+
                    "&total="+Integer.valueOf(p.getSellMRP().split("[.]")[0])*Integer.valueOf(p.getQuantity());
            url= url.replace(" ", "%20");
// Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            queue.add(stringRequest);
count++;
        }
        Log.d("FINISHED PRODUCTS","FINISHED PRODUCTS");
        //empty cart list
        MainActivity.shoppingCartList.clear();
        MainActivity.saveCart();
    }

    private String getFirstName(String fullname){
        String firstName="";

        String[] nameArray = fullname.split(" ");
        int nameArrayLength = nameArray.length;
        if(nameArrayLength==1){
            firstName = nameArray[0];
        }else if(nameArrayLength==0){

        }
        else if(nameArrayLength==2){
            firstName = nameArray[0];
        }else if(nameArrayLength==3){
            firstName = nameArray[0]+ " "+nameArray[1];
        }else if(nameArrayLength==4){
            firstName = nameArray[0]+ " "+nameArray[1]+ " "+nameArray[2];
        }else{
            firstName = nameArray[0];
        }

        return firstName;
    }

    private String getLastName(String fullname){
        String lastName="";

        String[] nameArray = fullname.split(" ");
        int nameArrayLength = nameArray.length;
        if(nameArrayLength==1){

            lastName = nameArray[0];
        }else if(nameArrayLength==0){

        }
        else if(nameArrayLength==2){
            lastName = nameArray[1];
        }else if(nameArrayLength==3){
            lastName = nameArray[2];
        }else if(nameArrayLength==4){
            lastName = nameArray[3];
        }else{
            lastName = nameArray[1];
        }

        return lastName;
    }

}
