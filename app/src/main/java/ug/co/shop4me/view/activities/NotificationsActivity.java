package ug.co.shop4me.view.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import ug.co.shop4me.R;

import static ug.co.shop4me.view.activities.MainActivity.mySharedPreferences;

public class NotificationsActivity extends AppCompatActivity {
    Switch switch1;
    Switch switch2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
        setTitle("Notifications");
        switch1 = (Switch) findViewById(R.id.switchButtonOrder);
        switch2 = (Switch) findViewById(R.id.switchButtonSales);
        switch1.setChecked(MainActivity.orderNotification);
        switch2.setChecked(MainActivity.salesNotification);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("orderNotification", true);
                    editor.commit();
                    MainActivity.orderNotification = true;
                } else {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("orderNotification", false);
                    editor.commit();
                    MainActivity.orderNotification = false;
                }
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("salesNotification", true);
                    editor.commit();
                    MainActivity.salesNotification = true;
                } else {
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putBoolean("salesNotification", false);
                    editor.commit();
                    MainActivity.salesNotification = false;
                }
            }
        });
    }

}
