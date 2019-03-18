package ug.co.shop4me.view.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
//import android.widget.SearchView;
import android.support.v7.widget.SearchView;

import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.adapter.ProductListAdapter;
import ug.co.shop4me.view.fragments.ProductsFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;



public class ProductListActivity extends AppCompatActivity{//} implements SearchView.OnQueryTextListener {
    private ArrayList<Product> productList = new ArrayList<Product>();
    String BASE_URL = MainActivity.BASE_URL+"all_from_category";
    String URL ="";
       public static     ProductListAdapter adapter;
    RecyclerView recyclerView;

    public static int itemCount = 0;
    private static BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    private static TextView  checkOutAmount; private static TextView itemCountTextView;

    private static FrameLayout redCircle;
    private static TextView  countTextView;
    private static int  alertCount = 0;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    PagerSlidingTabStrip tabs;
    ViewPager pager;
    DairyAdapter adapter3;
    FruitsAdapter adapter4;
   public static Integer categoryId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
                pager = (ViewPager) findViewById(R.id.pager);

         categoryId = extras.getInt("categoryId");
        if(categoryId ==89){
            setTitle("Dairy & Eggs");
            adapter3 = new DairyAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter3);
        }
       else if(categoryId ==90){
            setTitle("Fruits & Vegetables");
            adapter4 = new FruitsAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter4);
       }

        else if(categoryId ==92){
            setTitle("Bakery");
            URL = "";
            URL = BASE_URL+"/92";
            BakeryAdapter  adapter = new BakeryAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==93){
            setTitle("Soft Drinks");
            URL = "";
            URL = BASE_URL+"/93";
            SoftDrinksAdapter  adapter = new SoftDrinksAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==97){
            setTitle("Rice & Pasta");
            URL = "";
            URL = BASE_URL+"/97";
            RiceAdapter  adapter = new RiceAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==98){
            setTitle("Breakfast Goodies");
            URL = "";
            URL = BASE_URL+"/98";
            BreakfastAdapter  adapter = new BreakfastAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==99){
            setTitle("Snacks & Candies");
            URL = "";
            URL = BASE_URL+"/99";
            SnacksAdapter  adapter = new SnacksAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==84){
            setTitle("Meat & Fish");
            URL = "";
            URL = BASE_URL+"/84";
            MeatAdapter  adapter = new MeatAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==95){
            setTitle("Baby Items");
            URL = "";
            URL = BASE_URL+"/95";
            BabyAdapter  adapter = new BabyAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==94){
            setTitle("Health & Beauty");
            URL = "";
            URL = BASE_URL+"/94";
            HealthAdapter  adapter = new HealthAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==96){
            setTitle("Alcohol");
            URL = "";
            URL = BASE_URL+"/96";
            AlcoholAdapter  adapter = new AlcoholAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        else if(categoryId ==148){
            setTitle("Pantry");
            URL = "";
            URL = BASE_URL+"/148";
            PantryAdapter  adapter = new PantryAdapter(getSupportFragmentManager(),categoryId);
            pager.setAdapter(adapter);
        }

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);
        tabs.setViewPager(pager);
    }


/*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        adapter.getFilter().filter(query);
        return true;
    }
*/
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
            adapter = new ProductListAdapter(ProductListActivity.this, productList);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_list_menu, menu);
   /*     SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search_menu_item);
        searchView = (SearchView) searchMenuItem.getActionView();

       // searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       // searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search "+getTitle()+"...");
        //searchView.clearFocus();
       // searchView.setIconified(false);
*/
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
        switch (item.getItemId()) {
            case R.id.cart_menu_item:
                startActivity(new Intent(this, CartActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void updateItemCount(boolean ifIncrement) {
        if (ifIncrement) {
            itemCount++;
            itemCountTextView.setText(String.valueOf(itemCount));

        } else {
            itemCountTextView.setText(String.valueOf(itemCount <= 0 ? 0 : --itemCount));
        }

        //  toggleBannerVisibility();
    }

    public static void updateAlertIcon(boolean increament) {
        // if alert count extends into two digits, just show the red circle
        alertCount = MainActivity.shoppingCartList.size();
      //  if(increament) {
        //    alertCount = alertCount + 1;
            //alertCount = MainActivity.shoppingCartSize;
      //  }else{
       //     alertCount = alertCount - 1;
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
    public void onResume() {
        super.onResume();  // Always call the superclass method first



    }

    public class DairyAdapter extends FragmentPagerAdapter {
            String[]  TITLES = { "Milk", "Eggs", "Yogurt", "Butter", "Ice Cream", "Cheese", "Powdered Milk"};
             Integer catId;
        public DairyAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class FruitsAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Fresh Fruits", "Fresh Vegetables", "Packaged Fruits & Vegetables", "Fresh Herbs"};
        Integer catId;
        public FruitsAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class BakeryAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Bread", "Doughnut", "Cookies", "Cakes"};
        Integer catId;
        public BakeryAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class SoftDrinksAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Soda", "Water", "Juices", "Energy Drinks"};
        Integer catId;
        public SoftDrinksAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class RiceAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Rice", "Pasta"};
        Integer catId;
        public RiceAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class BreakfastAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Cereal", "Coffee", "Tea"};
        Integer catId;
        public BreakfastAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class SnacksAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Crisps", "Biscuits", "Chocolates", "Candy", "Nuts & Seeds"};
        Integer catId;
        public SnacksAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class MeatAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Chicken", "Beef", "Seafood", "Pork"};
        Integer catId;
        public MeatAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class HealthAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Skin Care", "Deodrants", "Soap", "Feminine Care", "Hair Care"};
        Integer catId;
        public HealthAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class AlcoholAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Beer","Wine","Liquer","Whiskey","Rum","Gin","Vodka" };
        Integer catId;
        public AlcoholAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

    public class BabyAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Daipers & Wipes","Baby Food & Formula","Baby Accessories","Bath & Body Care"};
        Integer catId;
        public BabyAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }


    public class PantryAdapter extends FragmentPagerAdapter {
        String[]  TITLES = { "Cooking Oil","Spices & Seasonings","Spreads","Condiments"};
        Integer catId;
        public PantryAdapter(FragmentManager fm, Integer catId) {
            super(fm);
            this.catId = catId;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
        @Override
        public int getCount() {
            return TITLES.length;
        }
        @Override
        public Fragment getItem(int position) {
            return ProductsFragment.newInstance(position,catId);
        }
    }

}
