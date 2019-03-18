package ug.co.shop4me.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ug.co.shop4me.R;

public class InstapayActivity extends AppCompatActivity {
WebView myWebView;
    String address, time, paymentMethod, total, id;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instapay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setTitle("Payment");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Payment Info...");
        mProgressDialog.show();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        address= extras.getString("address");
        time = extras.getString("time");
        paymentMethod = extras.getString("payment");
        total = extras.getString("total");
        id = extras.getString("id");

         myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

       // String url = "http://shop4me.co.ug/api/pay.html?name="+ MainActivity.fullname+"&email="+MainActivity.email+
              //  "&phone="+MainActivity.phone1+"&address="+MainActivity.address;
        //url= url.replace(" ", "%20");

        String url = "http://shop4me.co.ug/api/pesapal-iframe.php?name="+ MainActivity.fullname+"&email="+MainActivity.email+
                "&phone="+MainActivity.phone1+"&address="+MainActivity.address+"&price="+total+"&ref="+id;
        url= url.replace(" ", "%20");

        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mProgressDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payment_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.orders_menu_item) {
            startActivity(new Intent(this, OrderSuccessActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
