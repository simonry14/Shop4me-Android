/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ug.co.shop4me.view.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

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

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.adapter.ProductListAdapter;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProductsFragment extends Fragment {

	private static final String ARG_POSITION = "position";
    private static final String CAT_ID = "catId";
	private int position;
    private int catId;
    ProductListAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Product> productList;
    String URL;
    String BASE_URL = MainActivity.BASE_URL+"all_from_category";


	public static ProductsFragment newInstance(int position, int catId) {

		ProductsFragment f = new ProductsFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
        b.putInt(CAT_ID, catId);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		position = getArguments().getInt(ARG_POSITION);
        catId = getArguments().getInt(CAT_ID);

        if(catId ==89){
            if(position==0){
                URL = "";URL = BASE_URL+"/104";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/105";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/106";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/107";
            }
            if(position==4){
                URL = "";URL = BASE_URL+"/108";
            }
            if(position==5){
                URL = "";URL = BASE_URL+"/109";
            }
        }
        else if(catId ==90){
            if(position==0){
                URL = "";URL = BASE_URL+"/100";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/101";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/102";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/103";
            }
        }

        else if(catId ==92){
            if(position==0){
                URL = "";URL = BASE_URL+"/110";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/111";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/112";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/113";
            }
        }

        else if(catId ==93){
            if(position==0){
                URL = "";URL = BASE_URL+"/114";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/115";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/116";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/117";
            }
        }

        else if(catId ==97){
            if(position==0){
                URL = "";URL = BASE_URL+"/118";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/119";
            }
        }

        else if(catId ==98){
            if(position==0){
                URL = "";URL = BASE_URL+"/120";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/121";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/122";
            }
        }

        else if(catId ==99){

            if(position==0){
                URL = "";URL = BASE_URL+"/123";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/124";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/125";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/126";
            }
            if(position==4){
                URL = "";URL = BASE_URL+"/127";
            }

        }

        else if(catId ==84){

            if(position==0){
                URL = "";URL = BASE_URL+"/128";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/129";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/130";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/131";
            }
        }

        else if(catId ==95){

            if(position==0){
                URL = "";URL = BASE_URL+"/132";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/133";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/134";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/135";
            }
            if(position==4){
                URL = "";URL = BASE_URL+"/136";
            }

        }

        else if(catId ==94){
            if(position==0){
                URL = "";URL = BASE_URL+"/137";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/138";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/139";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/140";
            }
        }

        else if(catId ==96){

            if(position==0){
                URL = "";URL = BASE_URL+"/141";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/142";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/143";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/144";
            }
            if(position==4){
                URL = "";URL = BASE_URL+"/145";
            }
            if(position==5){
                URL = "";URL = BASE_URL+"/146";
            }
            if(position==6){
                URL = "";URL = BASE_URL+"/147";
            }


        }

        else if(catId ==148){
            if(position==0){
                URL = "";URL = BASE_URL+"/149";
            }
            if(position==1){
                URL = "";URL = BASE_URL+"/150";
            }
            if(position==2){
                URL = "";URL = BASE_URL+"/151";
            }
            if(position==3){
                URL = "";URL = BASE_URL+"/152";
            }
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);
		final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        productList = new ArrayList<>();
        recyclerView = new RecyclerView(getActivity());
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
		recyclerView.setLayoutManager(linearLayoutManager);
		recyclerView.setHasFixedSize(true);
        new getProducts().execute();
		//TextView v = new TextView(getActivity());
		params.setMargins(margin, margin, margin, margin);
        recyclerView.setLayoutParams(params);
        recyclerView.setLayoutParams(params);
		fl.addView(recyclerView);
		return fl;
	}

    private void getData() {
        Log.i("getData", "called");

        RequestQueue que = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(
                Request.Method.GET,
                URL,
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
         String url =MainActivity.IMAGE_URL;

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
            adapter = new ProductListAdapter(getActivity(), productList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}