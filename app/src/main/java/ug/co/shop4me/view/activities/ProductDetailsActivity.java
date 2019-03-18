package ug.co.shop4me.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.adapter.RecommendListAdapter;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.util.ColorGenerator;
import ug.co.shop4me.view.customview.TextDrawable;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class ProductDetailsActivity extends AppCompatActivity {

    private int productListNumber;
    private ImageView itemImage;
    private TextView itemSellPrice;
    private TextView itemName;
    private TextView quantity;
    private TextView itemdescription;
    private TextDrawable.IBuilder mDrawableBuilder;
    private TextDrawable drawable;
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private String subcategoryKey;
    private boolean isFromCart;

    private RecyclerView recommendedRecyclerView;
    private RecyclerView relatedRecyclerView;
    private Toolbar mToolbar;
    Product product;
    String pName, pPrice, pDesc, pImgURL, pId;
    Integer position;

    private static FrameLayout redCircle;
    private static TextView  countTextView;
    private static int  alertCount = 0;
    RecommendListAdapter adapter2;
    List<Product> similarProductList;

   String BASE_URL = MainActivity.BASE_URL+"all_related_products/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
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

        similarProductList = new ArrayList<>();

        recommendedRecyclerView = (RecyclerView) findViewById(R.id.recommendedRecyclerView);
        relatedRecyclerView = (RecyclerView) findViewById(R.id.relatedRecyclerView);

        LinearLayoutManager MyLayoutManager = new LinearLayoutManager(this);
        MyLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(this);
        MyLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        recommendedRecyclerView.setLayoutManager(MyLayoutManager);
        relatedRecyclerView.setLayoutManager(MyLayoutManager2);

       // topSellingPager = (ClickableViewPager) findViewById(R.id.top_selleing_pager);

        itemSellPrice = ((TextView) findViewById(R.id.category_discount));

        quantity = ((TextView) findViewById(R.id.iteam_amount));

        itemName = ((TextView) findViewById(R.id.product_name));

        itemdescription = ((TextView) findViewById(R.id.product_description));

        itemImage = (ImageView) findViewById(R.id.product_image);

        try {
            if (getIntent().getAction().equals("FROM PD LIST ADAPTER")) {


                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                 product = intent.getParcelableExtra("product");
                pName = extras.getString("pName");
                pId = extras.getString("pId");
                pPrice = extras.getString("pPrice");
                pDesc = extras.getString("pDesc");
                pImgURL = extras.getString("pImgURL");
                position = extras.getInt("position");
                itemName.setText(pName);
                itemdescription.setText(pDesc);
                itemSellPrice.setText(MainActivity.formatter.format(Long.valueOf(pPrice.split("[.]")[0] ))+ "/=");
                quantity.setText(extras.getString("pQuantity"));

                Glide.with(this).load(pImgURL).placeholder(drawable).error(drawable).centerCrop().into(itemImage);
setTitle("");
            }
        }catch (Exception e){

        }

       // fillProductData();

       findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                            // current object
                          //  Product tempObj = GlobaDataHolder.getGlobaDataHolder().getProductMap().get(subcategoryKey).get(productListNumber);
                            Product tempObj = product;
                            // if current object is lready in shopping list
                            if (MainActivity.shoppingCartList.contains(tempObj)) {
                                // get position of current item in shopping list
                                int indexOfTempInShopingList = MainActivity.shoppingCartList.indexOf(tempObj);
                                tempObj = MainActivity.shoppingCartList.get(indexOfTempInShopingList); // use this as the object since it has a quantity. original one has empty quantity

                                // increase quantity of current item in shopping
                                // list
                                if (Integer.parseInt(tempObj.getQuantity()) == 0) {
                                    ProductListActivity.updateAlertIcon(true);
                                }

                                // update quanity in shopping list
                                MainActivity.shoppingCartList.get(indexOfTempInShopingList).setQuantity(String.valueOf(Integer.valueOf(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity()) + 1));

                                // update current item quanitity
                                quantity.setText(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity());

                            } else {

                                tempObj.setQuantity(String.valueOf(1));
                                quantity.setText(tempObj.getQuantity());
                                MainActivity.shoppingCartList.add(tempObj);
                                MainActivity.saveCart();
                               updateAlertIcon(true);
                            }
//                            Utils.vibrate(getApplicationContext());
                      //  ProductListActivity.adapter.notifyItemChanged(position);
                    }

                });

      findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                            Product tempObj = product;

                            if (MainActivity.shoppingCartList.contains(tempObj)) {

                                int indexOfTempInShopingList = MainActivity.shoppingCartList.indexOf(tempObj);

                                tempObj = MainActivity.shoppingCartList.get(indexOfTempInShopingList); // use this as the object since it has a quantity. original one has empty quantity


                                if (Integer.valueOf(tempObj.getQuantity()) != 0) {

                                    MainActivity.shoppingCartList.get(indexOfTempInShopingList).setQuantity(String.valueOf(Integer.valueOf(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity()) - 1));

                                    quantity.setText(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity());

                                   // Utils.vibrate(getApplicationContext());

                                    if (Integer.valueOf(MainActivity.shoppingCartList.get(indexOfTempInShopingList).getQuantity()) == 0) {

                                        MainActivity.shoppingCartList.remove(indexOfTempInShopingList);
                                        alertCount = 1;

                                    }
                                    MainActivity.saveCart();
                                    updateAlertIcon(false);

                                }

                            } else {

                            }

//ProductListActivity.adapter.notifyItemChanged(position);

                    }

                });

     new   getSimilarProducts().execute();

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

    public static void updateAlertIcon(boolean increament) {
        // if alert count extends into two digits, just show the red circle
        alertCount = MainActivity.shoppingCartList.size();
       // if(increament) {
        //    alertCount = alertCount + 1;
            //alertCount = MainActivity.shoppingCartSize;
       // }else{
      //      alertCount = alertCount - 1;
           // alertCount = MainActivity.shoppingCartSize;
      //  }

        countTextView.setText(String.valueOf(alertCount));

        if(alertCount!=0) {
            redCircle.setVisibility(VISIBLE);
        }else{
            redCircle.setVisibility(GONE);
        }
    }

    private void getData() {
        Log.i("getData", "called");

        RequestQueue que = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                BASE_URL+pId,
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

            String url =MainActivity.IMAGE_URL;;


            for (int i = 0; i < length; i++) {
                //pull out every object in the array and add it to the list.
                JSONObject obj = mainArray.optJSONObject(i);
                String imgPath = obj.optString("image");
                String imgNameFull = imgPath.split("/")[1];
                String imgName = imgNameFull.split("[.]")[0];
                String imgExt = imgNameFull.split("[.]")[1];
                String img = imgName + "-40x40." + imgExt;
                // String imgUrl = url+img;
                String imgUrl = url + imgNameFull;
                Product p = new Product(obj.optString("name"), obj.optString("description"), "", "123", "", obj.optString("price"),
                        "", imgUrl, obj.optString("product_id"));


                similarProductList.add(p);
            }

            adapter2.notifyDataSetChanged();
            // productList = items;
        }catch (Exception e){

        }
    }

    public class getSimilarProducts extends AsyncTask<String, Void, String> {

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

adapter2 = new RecommendListAdapter(ProductDetailsActivity.this, similarProductList);



            recommendedRecyclerView.setAdapter(adapter2);
            relatedRecyclerView.setAdapter(adapter2);

            adapter2.notifyDataSetChanged();

        }
    }

}
