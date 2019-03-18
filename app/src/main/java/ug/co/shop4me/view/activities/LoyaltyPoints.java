package ug.co.shop4me.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.User;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

public class LoyaltyPoints extends AppCompatActivity {
    TextView pointTxt, creditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loyalty_points);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setTitle("Loyalty Points & Referral Credit");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pointTxt = (TextView) findViewById(R.id.top3);
        creditTxt = (TextView) findViewById(R.id.top6);

        final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
        Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
        qq.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                String points = user.getLoyaltyPoints();
                String credit = user.getReferralCredit();

                try {
                    if (points.equals(null)) {
                        points = "0";
                    }
                    if (credit.equals(null)) {
                        credit = "0/=";
                    }

                } catch (Exception e) {

                }

                pointTxt.setText(points);
                creditTxt.setText(credit);

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
