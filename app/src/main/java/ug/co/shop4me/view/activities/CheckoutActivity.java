package ug.co.shop4me.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;

public class CheckoutActivity extends AppCompatActivity {
    public String address;
    public String time;
    public String paymentMethod;
RadioButton address1;
    RadioButton pickFromStore;
    RadioButton time1;
    RadioButton time2;
    RadioButton specifyAddress;
    EditText specifyAddressTxt;
    RadioButton specifyTime;
    EditText specifyTimeTxt, txtInvite;
    Button b;
    RadioButton payinstoreRB;
    RadioButton codRB;
    RadioButton mmRB;
    RadioButton cardRB;
    Spinner spinner;
    TextView delTitle, inviteCodeHeader;
    String BASE_URL = MainActivity.BASE_URL+"all_orders/";
    Boolean v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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
        setTitle("Check Out");

        delTitle = (TextView) findViewById(R.id.del);
        specifyAddress = (RadioButton) findViewById(R.id.SpecifyAddress);
        specifyTime = (RadioButton) findViewById(R.id.specifyTime);
        address1 = (RadioButton) findViewById(R.id.address1);
        pickFromStore = (RadioButton) findViewById(R.id.address2);
        time1 = (RadioButton) findViewById(R.id.time1);
        time2 = (RadioButton) findViewById(R.id.time2);
        specifyAddressTxt = (EditText) findViewById(R.id.txtSpecifyAddress) ;
        specifyAddressTxt.setVisibility(View.GONE);
        payinstoreRB = (RadioButton) findViewById(R.id.payinstore);
        codRB = (RadioButton) findViewById(R.id.cod);
        mmRB = (RadioButton) findViewById(R.id.mm);
        cardRB = (RadioButton) findViewById(R.id.card);

        spinner = (Spinner) findViewById(R.id.store_spinner);
        spinner.setVisibility(View.GONE);
        payinstoreRB.setVisibility(View.GONE);
        //Set right address
        address1.setText(MainActivity.address);

        //Set default time and address
        address=address1.getText().toString();
        time=time1.getText().toString();
        paymentMethod = codRB.getText().toString();

        //invite code stuff
        inviteCodeHeader = (TextView) findViewById(R.id.inviteCodeHeader);
        txtInvite = (EditText) findViewById(R.id.txtInvite) ;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        inviteCodeHeader.setVisibility(View.GONE);
        txtInvite.setVisibility(View.GONE);
        isFirstOrder();


        specifyAddress.setOnClickListener(new View.OnClickListener() {//Speccify Address
            @Override
            public void onClick(View v) {
                delTitle.setText("Delivery Date and Time");
                spinner.setVisibility(View.GONE);
                payinstoreRB.setVisibility(View.GONE);
                codRB.setEnabled(true);
                codRB.setChecked(true);
                mmRB.setEnabled(true);
                cardRB.setEnabled(true);
                if(specifyAddress.isChecked()){
                    specifyAddressTxt.setVisibility(View.VISIBLE);
                }else{
                    specifyAddressTxt.setVisibility(View.GONE);
                }
            }
        });

        address1.setOnClickListener(new View.OnClickListener() {//saved address
            @Override
            public void onClick(View v) {
                delTitle.setText("Delivery Date and Time");
                    specifyAddressTxt.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                payinstoreRB.setVisibility(View.GONE);
                address=address1.getText().toString();
                codRB.setEnabled(true);
                codRB.setChecked(true);
                mmRB.setEnabled(true);
                cardRB.setEnabled(true);
            }
        });

        pickFromStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delTitle.setText("Pickup Date and Time");
                    specifyAddressTxt.setVisibility(View.GONE);
                codRB.setEnabled(false);
                mmRB.setEnabled(false);
                cardRB.setEnabled(false);
                paymentMethod = "Pay in Supermarket: ";
                address = "Pick from Supermarket: ";
                spinner.setVisibility(View.VISIBLE);

                payinstoreRB.setVisibility(View.VISIBLE);
                payinstoreRB.setChecked(true);
               final ArrayAdapter<CharSequence> adapter = new ArrayAdapter(CheckoutActivity.this, android.R.layout.simple_spinner_item, new ArrayList<CharSequence>());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final DatabaseReference mRootRef = Utils.getDatabase().getReference().child("Stores");

                mRootRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot qq : dataSnapshot.getChildren()) {
                            String name = qq.child("name").getValue().toString();
                            String branches = qq.child("branches").getValue().toString();
                            String[] bArray =  branches.split(",");
                            for (int i =0; i<bArray.length; i++){
                                String bName = name+", "+bArray[i]+" Branch";
                                adapter.add(bName);
                            }
                        }
                        spinner.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        address = "Pick from Supermarket: "+ spinner.getSelectedItem().toString();
                        paymentMethod = "Pay in Supermarket: "+ spinner.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }
        });

        specifyTimeTxt = (EditText) findViewById(R.id.txtSpecifyTime) ;
        specifyTimeTxt.setVisibility(View.GONE);
        specifyTime.setOnClickListener(new View.OnClickListener() {//Specify Time
            @Override
            public void onClick(View v) {
                if(specifyTime.isChecked()){
                    specifyTimeTxt.setVisibility(View.VISIBLE);
                }else{
                    specifyTimeTxt.setVisibility(View.GONE);
                }
            }
        });

        time1.setOnClickListener(new View.OnClickListener() {//ASAP
            @Override
            public void onClick(View v) {
                specifyTimeTxt.setVisibility(View.GONE);
                time = time1.getText().toString();
            }
        });
        time2.setOnClickListener(new View.OnClickListener() { //next day
            @Override
            public void onClick(View v) {
                specifyTimeTxt.setVisibility(View.GONE);
                time = time2.getText().toString();
            }
        });

        //PAYMENT METHODS RADIO BUTTONS CLICKED
        codRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "Cash on Delivery";
            }
        });

        mmRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "Mobile Money";
            }
        });

        cardRB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = "Debit Card";
            }
        });

        b = (Button) findViewById(R.id.proceed);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToOrder();

            }
        });

    }

    void goToPayment(){

        if (specifyAddress.isChecked()) {
            if (specifyAddressTxt.getText().toString() == "") {
                Toast.makeText(CheckoutActivity.this, "Please specify address", Toast.LENGTH_SHORT).show();
            } else {
                if (specifyTime.isChecked()) {
                    if (specifyTimeTxt.getText().toString() == "") {
                        Toast.makeText(CheckoutActivity.this, "Please specify time", Toast.LENGTH_SHORT).show();
                    } else {
                        address = specifyAddressTxt.getText().toString();
                        time = specifyTimeTxt.getText().toString();
                        Intent in = new Intent(CheckoutActivity.this, InstapayActivity.class);
                        in.putExtra("address", address);
                        in.putExtra("time", time);
                        in.putExtra("payment", paymentMethod);
                        startActivity(in);
                    }
                } else {
                    address = specifyAddressTxt.getText().toString();
                    Intent in = new Intent(CheckoutActivity.this, InstapayActivity.class);
                    in.putExtra("address", address);
                    in.putExtra("time", time);
                    in.putExtra("payment", paymentMethod);
                    startActivity(in);
                }
            }

        } else {

            if (specifyTime.isChecked()) {
                if (specifyTimeTxt.getText().toString() == "") {
                    Toast.makeText(CheckoutActivity.this, "Please specify time", Toast.LENGTH_SHORT).show();
                } else {
                    time = specifyTimeTxt.getText().toString();
                    Intent in = new Intent(CheckoutActivity.this, InstapayActivity.class);
                    in.putExtra("address", address);
                    in.putExtra("time", time);
                    in.putExtra("payment", paymentMethod);
                    startActivity(in);
                }
            } else {
                Intent in = new Intent(CheckoutActivity.this, InstapayActivity.class);
                in.putExtra("address", address);
                in.putExtra("time", time);
                in.putExtra("payment", paymentMethod);
                startActivity(in);
            }
        }

    }


    private void isFirstOrder() {

        RequestQueue que = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                BASE_URL+ MainActivity.customerId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("onResponse", response);
                        JSONArray mainArray = null;
                        try {
                            mainArray = new JSONArray(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int length = mainArray.length();
                        if (length == 0){
                            v = true;
                            inviteCodeHeader.setVisibility(View.VISIBLE);
                            txtInvite.setVisibility(View.VISIBLE);
                        }else {
                            v=false;
                            inviteCodeHeader.setVisibility(View.GONE);
                            txtInvite.setVisibility(View.GONE);
                        }
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


    void goToOrder(){

        if (specifyAddress.isChecked()) {
            if (specifyAddressTxt.getText().toString() == "") {
                Toast.makeText(CheckoutActivity.this, "Please specify address", Toast.LENGTH_SHORT).show();
            } else {
                if (specifyTime.isChecked()) {
                    if (specifyTimeTxt.getText().toString() == "") {
                        Toast.makeText(CheckoutActivity.this, "Please specify time", Toast.LENGTH_SHORT).show();
                    } else {
                        address = specifyAddressTxt.getText().toString();
                        time = specifyTimeTxt.getText().toString();
                        Intent in = new Intent(CheckoutActivity.this, PlaceOrderActivity.class);
                        in.putExtra("address", address);
                        in.putExtra("time", time);
                        in.putExtra("payment", paymentMethod);
                        startActivity(in);
                    }
                } else {
                    address = specifyAddressTxt.getText().toString();
                    Intent in = new Intent(CheckoutActivity.this, PlaceOrderActivity.class);
                    in.putExtra("address", address);
                    in.putExtra("time", time);
                    in.putExtra("payment", paymentMethod);
                    startActivity(in);
                }
            }

        } else {

            if (specifyTime.isChecked()) {
                if (specifyTimeTxt.getText().toString() == "") {
                    Toast.makeText(CheckoutActivity.this, "Please specify time", Toast.LENGTH_SHORT).show();
                } else {
                    time = specifyTimeTxt.getText().toString();
                    Intent in = new Intent(CheckoutActivity.this, PlaceOrderActivity.class);
                    in.putExtra("address", address);
                    in.putExtra("time", time);
                    in.putExtra("payment", paymentMethod);
                    startActivity(in);
                }
            } else {
                Intent in = new Intent(CheckoutActivity.this, PlaceOrderActivity.class);
                in.putExtra("address", address);
                in.putExtra("time", time);
                in.putExtra("payment", paymentMethod);
                startActivity(in);
            }
        }

    }


}
