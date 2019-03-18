package ug.co.shop4me.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ug.co.shop4me.R;

public class JoinExpressActivity extends AppCompatActivity {
    WebView myWebView;
    ProgressDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_express);
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
        setTitle("Join Express");

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String amount= extras.getString("amount");
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading Payment Info...");
        mProgressDialog.show();

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

       // String url = "http://shop4me.co.ug/api/payX.html?name="+ MainActivity.fullname+"&email="+MainActivity.email+
               // "&phone="+MainActivity.phone1+"&address="+MainActivity.address;
        //url= url.replace(" ", "%20");

        String url = "http://shop4me.co.ug/api/pesapal-iframe.php?name="+ MainActivity.fullname+"&email="+MainActivity.email+
                "&phone="+MainActivity.phone1+"&address="+MainActivity.address+"&price="+amount+"&ref="+"Express-"+MainActivity.userId;
        url= url.replace(" ", "%20");

        myWebView.loadUrl(url);

        myWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                mProgressDialog.dismiss();
            }
        });
    }

}
