package ug.co.shop4me.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.User;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.fragments.AccountFragment;
import ug.co.shop4me.view.fragments.CategoryFragment;
import ug.co.shop4me.view.fragments.ContactFragment;
import ug.co.shop4me.view.fragments.ExpressFragment;
import ug.co.shop4me.view.fragments.OrdersFragment;
import ug.co.shop4me.view.fragments.ReferFragment;
import ug.co.shop4me.view.fragments.StoresFragment;
import ug.co.shop4me.view.fragments.SuggestFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CategoryFragment.OnFragmentInteractionListener {
    public static final String PREFS = "Shop4me";
    public static SharedPreferences mySharedPreferences;
    private FirebaseAuth auth;
    TextView fullnameTV ;
    TextView emailTV ;
    public static String userId;
    public static String fullname;
    public static String email;
    public static String inviteCode;
    public static String userKey;
    public static String customerId;
    public static String phone1;
    public static String address;
    public static String regToken;
    //public static String BASE_URL = "http://192.168.43.235/ranger2/api/index.php/";
   // public static String IMAGE_URL = "http://192.168.43.235/ranger2/image/catalog/";
   // public static String BASE_URL = "http://192.168.1.158/ranger2/api/index.php/";
   // public static String IMAGE_URL = "http://192.168.1.158/ranger2/image/catalog/";
    public static String BASE_URL = "http://shop4me.co.ug/api/index.php/";
    public static String IMAGE_URL = "http://shop4me.co.ug/image/catalog/";
    private static BigDecimal checkoutAmount = new BigDecimal(BigInteger.ZERO);
    public static List<Product> shoppingCartList;

    private static FrameLayout redCircle;
    private static TextView  countTextView;
    public static DecimalFormat formatter;


    public static boolean salesNotification;
    public static boolean orderNotification;

    public static boolean isExpress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        fullnameTV = (TextView) headerView.findViewById(R.id.fullname);
        emailTV = (TextView) headerView.findViewById(R.id.email);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        formatter = new DecimalFormat("#,###,###");

        //Get cart from phone storage.
        mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        //Set cart
        shoppingCartList = new ArrayList<>();
        getCart();


        try {
            if (getIntent().getAction().equals("FROM NEW USER CREATED OR LOGIN")) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                email = extras.getString("email");
                fullname = extras.getString("fullname");
                userId = extras.getString("userid");
                userKey = extras.getString("userKey");
                inviteCode = extras.getString("inviteCode");
                try{customerId = extras.getString("openCartId");}catch (Exception e){}
                address = extras.getString("address");
                phone1 = extras.getString("phone1");
                emailTV.setText(email);
                fullnameTV.setText(fullname);
                //Save full name and email in SharedPreference
                mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("userid", userId);
                editor.putString("fullname", fullname);
                editor.putString("email", email);
                editor.putString("inviteCode", inviteCode);
                editor.putString("userKey", userKey);
                try{ editor.putString("customerId", customerId);}catch (Exception e){}
                editor.putString("address", address);
                editor.putString("phone1", phone1);
                editor.apply();
                //
                regToken = mySharedPreferences.getString("registration token","");
                if (regToken.length() > 0) {

                    saveTokenInFbDb(regToken);

                } else{
                    //refresh token and send to server
                }

            }
        } catch (Exception e) {

        }

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {

        }

        //Get User Infor from Shared Preference
        mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        userId = mySharedPreferences.getString("userid", "");
        fullname = mySharedPreferences.getString("fullname", "");
        email = mySharedPreferences.getString("email", "");
        inviteCode = mySharedPreferences.getString("inviteCode", "");
        userKey = mySharedPreferences.getString("userKey", "");
        customerId = mySharedPreferences.getString("customerId", "0"); //IF USER HAS NO ID, LET THEM USE 1
        phone1 = mySharedPreferences.getString("phone1", ""); //TO DO
        address = mySharedPreferences.getString("address", ""); //TO DO

        salesNotification = mySharedPreferences.getBoolean("salesNotification", true);
        orderNotification = mySharedPreferences.getBoolean("orderNotification", true);

        //Set username and password
        emailTV.setText(email);
        fullnameTV.setText(fullname);

        //Set initialfragment to categories
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, new CategoryFragment()).commit();


        try {
            if (getIntent().getAction().equals("FROM PLACING ORDER")) {
                fragmentManager.beginTransaction().replace(R.id.frame_container, new OrdersFragment()).commit();
                setTitle("My Orders");
            }
            if (getIntent().getAction().equals("FROM EMPTY CART")) {
                fragmentManager.beginTransaction().replace(R.id.frame_container, new CategoryFragment()).commit();
                setTitle("Categories");
            }
        } catch (Exception e) {

        }

        DatabaseReference expressExpiryRef = Utils.getDatabase().getReference().child("Users").child(MainActivity.userKey).child("expressExpiry");
        expressExpiryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (null == dataSnapshot.getValue() || dataSnapshot.getValue().toString().isEmpty()) {
                    isExpress = false;
                } else {
                    isExpress = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (customerId.equals("0") ){
        DatabaseReference custIdExpiryRef = Utils.getDatabase().getReference().child("Users").child(MainActivity.userKey).child("openCartId");
            custIdExpiryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (null == dataSnapshot.getValue() || dataSnapshot.getValue().toString().isEmpty()) {

                } else {
                    customerId = dataSnapshot.getValue().toString();
                    mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("customerId", customerId);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
// update the main content by replacing fragments
        Fragment fragment = null;
        if (id == R.id.nav_categories) {
fragment = new CategoryFragment();
            setTitle("Browse Products");
        } else if (id == R.id.nav_stores) {
fragment = new StoresFragment();
            setTitle("Supermarket");
        } else if (id == R.id.nav_orders) {
fragment = new OrdersFragment();
            setTitle("My Orders");
        } else if (id == R.id.nav_express) {
fragment = new ExpressFragment();
            setTitle("Shop4me Express");
        } else if (id == R.id.nav_refer) {
fragment = new ReferFragment();
            setTitle("Refer Friends");
        } else if (id == R.id.nav_suggest) {
fragment = new SuggestFragment();
            setTitle("Suggest a Product");
        } else if (id == R.id.nav_settings) {
            fragment = new AccountFragment();
            setTitle("My Account");
        } else if (id == R.id.nav_contact) {
            fragment = new ContactFragment();
            setTitle("Contact Us");
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager() ;
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
         //   mDrawerList.setItemChecked(position, true);
           // mDrawerList.setSelection(position);
            //setTitle(navMenuTitles[position]);
            //mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public static int  getItemCount() {
        return MainActivity.shoppingCartList.size();
    }

    public static void saveCart(){

        //Save full name and email in SharedPreference
        Gson g = new Gson();
        String jsonProducts = g.toJson(shoppingCartList);

        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("jsonProducts", jsonProducts);
        editor.apply();
        getCart();
    }

    public static void getCart(){
        Gson gson = new Gson();
        String jsonProducts = mySharedPreferences.getString("jsonProducts","");
        try {
            shoppingCartList = gson.fromJson(jsonProducts, new TypeToken<List<Product>>() {}.getType());
            if (null == shoppingCartList){
                shoppingCartList = new ArrayList<Product>();
            }
        }catch (Exception e){

        }

    }

    @Override
    public void setTitle(CharSequence title) {

        getSupportActionBar().setTitle(title);
    }

    void saveTokenInFbDb(final String token){

        final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
        Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
        qq.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                user.setToken(token);
                usersRef.child(dataSnapshot.getKey()).setValue(user);
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

    }
}
