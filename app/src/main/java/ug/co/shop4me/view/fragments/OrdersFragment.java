package ug.co.shop4me.view.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.adapter.OrderItemAdapter;
import ug.co.shop4me.model.entities.OrderItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    View rootview;
    RecyclerView ordersRecyclerView;
    OrderItemAdapter adapter;
   String BASE_URL = MainActivity.BASE_URL+"all_orders/";

    List<OrderItem> orderItemList;
    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_orders, container, false);
        ordersRecyclerView = (RecyclerView) rootview.findViewById(R.id.ordersRecyclerView);
        orderItemList = new ArrayList<>();
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getContext());
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        ordersRecyclerView.setLayoutManager(MyLayoutManager2);
        adapter = new OrderItemAdapter(getContext(), orderItemList);
        ordersRecyclerView.setAdapter(adapter);
        new getOrderItems().execute();
        return rootview;
    }

    private void getData() {
        Log.i("getData", "called");

        RequestQueue que = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                BASE_URL+ MainActivity.customerId,
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
        try {
            int length = mainArray.length();
            for (int i = 0; i < length; i++) {
                //pull out every object in the array and add it to the list.
                JSONObject obj = mainArray.optJSONObject(i);
                OrderItem s = new OrderItem(obj.optString("order_id"),obj.optString("date_added"),obj.optString("total").split("[.]")[0],

                        obj.optString("name") ,obj.optString("payment_method"),obj.optString("shipping_address_1"),obj.optString("comment"));

                orderItemList.add(s);
            }
            adapter.notifyDataSetChanged();
        }catch (Exception e){
        }
    }

    public class getOrderItems extends AsyncTask<String, Void, String> {

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

}
