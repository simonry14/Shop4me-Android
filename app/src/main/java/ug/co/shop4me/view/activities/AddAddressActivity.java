package ug.co.shop4me.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import ug.co.shop4me.R;

public class AddAddressActivity extends AppCompatActivity {
    TextView top;
    EditText address;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
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

        top = (TextView) findViewById(R.id.top);
        address = (EditText) findViewById(R.id.address);
        saveButton = (Button) findViewById(R.id.saveAddress);

        try {
            if (getIntent().getAction().equals("EDIT")) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                top.setText("Edit address below and tap the save button");
                address.setText(extras.getString("address"));
                try {
                    address.setSelection(extras.getString("address").length());
                } catch (Exception e) {

                }
                setTitle("Edit Address");
            }

            if (getIntent().getAction().equals("ADD")) {
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                top.setText("Enter the full address  below and tap the save button");
                setTitle("Add Address");
            }

        }catch (Exception e){

        }
saveButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       if(address.getText().toString().isEmpty()) {
           Toast.makeText(AddAddressActivity.this,"Please enter an address", Toast.LENGTH_LONG);
       }else{
           addCustomerAddress(getFirstName(MainActivity.fullname),getLastName(MainActivity.fullname),address.getText().toString());
       }
    }
});

    }

    private void addCustomerAddress(String firstName,String lastName, String address){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = MainActivity.BASE_URL+"add_address?customerid="+MainActivity.customerId+ "&firstname="+firstName+ "&lastname="+lastName
                +"&address="+address;
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
