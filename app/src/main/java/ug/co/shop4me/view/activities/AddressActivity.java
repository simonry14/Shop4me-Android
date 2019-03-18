package ug.co.shop4me.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.adapter.AddressAdapter;
import ug.co.shop4me.model.User;
import ug.co.shop4me.model.entities.Address;

public class AddressActivity extends AppCompatActivity {
    RecyclerView addressRecyclerView;
    AddressAdapter adapter;
    List<Address> addressList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Addresses");

        addressRecyclerView = (RecyclerView) findViewById(R.id.addressRecyclerView);
        addressList = new ArrayList<>();
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(this);
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        addressRecyclerView.setLayoutManager(MyLayoutManager2);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                intent.setAction("ADD");
                startActivity(intent);
            }
        });

        final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
        Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
        qq.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                String address = user.getAddress();

                addressList.add(new Address(address));

                adapter = new AddressAdapter(AddressActivity.this, addressList);
                addressRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        //new getAddresses().execute();
    }

    public class getAddresses extends AsyncTask<String, Void, String> {
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

            adapter = new AddressAdapter(AddressActivity.this, addressList);
            addressRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    //Uses volley to asynchronously download data from the rest source and fills the list of items.
    private void getData() {
        Log.i("getData", "called");

        String url = MainActivity.BASE_URL+"get_address/"+ MainActivity.customerId;
        RequestQueue que = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
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

        for (int i = 0; i < length; i++) {
            //pull out every object in the array and add it to the list.
            JSONObject obj = mainArray.optJSONObject(i);
            addressList.add(new Address(obj.optString("address_1")));
            if(!obj.optString("address_2").isEmpty()) {
                addressList.add(new Address(obj.optString("address_2")));
            }
        }
        adapter.notifyDataSetChanged();
    }

}
