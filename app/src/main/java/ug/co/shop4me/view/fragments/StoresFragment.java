package ug.co.shop4me.view.fragments;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.adapter.StoresAdapter;
import ug.co.shop4me.model.entities.Store;


/**
 * A simple {@link Fragment} subclass.
 */
public class StoresFragment extends Fragment {
View rootview;
    RecyclerView storesRecyclerView;
    StoresAdapter adapter;
    String BASE_URL = MainActivity.BASE_URL+"all_stores";
    List<Store> storeList;
    ProgressDialog progressDialog;
    StorageReference imagesRef;
    private StorageReference mStorageRef;
    public StoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mStorageRef = FirebaseStorage.getInstance().getReference();
        imagesRef = mStorageRef.child("Stores Images");
       rootview = inflater.inflate(R.layout.fragment_stores, container, false);
        storesRecyclerView = (RecyclerView) rootview.findViewById(R.id.storesRecyclerView);
storeList = new ArrayList<>();
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getContext());
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        storesRecyclerView.setLayoutManager(MyLayoutManager2);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Searching...");
        progressDialog.show();
      // new getStores().execute();
        final DatabaseReference mRootRef = Utils.getDatabase().getReference().child("Stores");

        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot qq : dataSnapshot.getChildren()) {
                        String name = qq.child("name").getValue().toString();
                        String image = qq.child("image").getValue().toString();
                        String branches = qq.child("branches").getValue().toString();
                    Store s = new Store(name, branches,image);
                    storeList.add(s);
                }

                adapter = new StoresAdapter(getContext(), storeList);
                storesRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        progressDialog.dismiss();
        return rootview;
    }

    private void getData() {
        Log.i("getData", "called");
        RequestQueue que = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                BASE_URL,
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

            String url =MainActivity.IMAGE_URL;;

            for (int i = 0; i < length; i++) {
                //pull out every object in the array and add it to the list.
                JSONObject obj = mainArray.optJSONObject(i);
                String imgPath = obj.optString("store_image");
                String imgNameFull = imgPath.split("/")[1];
                String imgName = imgNameFull.split("[.]")[0];
                String imgExt = imgNameFull.split("[.]")[1];
                String img = imgName + "-40x40." + imgExt;
                // String imgUrl = url+img;
                String imgUrl = url + imgNameFull;
                Store s = new Store(obj.optString("store_name"), obj.optString("store_description"),imgUrl);

                storeList.add(s);
            }

            adapter.notifyDataSetChanged();
            // productList = items;
        }catch (Exception e){

        }
    }

    public class getStores extends AsyncTask<String, Void, String> {

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
            adapter = new StoresAdapter(getContext(), storeList);
            storesRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
