package ug.co.shop4me.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.view.activities.JoinExpressActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpressFragment extends Fragment {
    View rootview;
    TextView expressAmount;
    TextView expressExpiry;
    TextView txt;
    Button b;
    String amount;

    public ExpressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootview= inflater.inflate(R.layout.fragment_express, container, false);
        expressAmount = (TextView) rootview.findViewById(R.id.expressAmount);
        expressExpiry = (TextView) rootview.findViewById(R.id.expressExpiry);
        txt = (TextView) rootview.findViewById(R.id.txt);
        b=(Button) rootview.findViewById(R.id.joinExpress);
        b.setVisibility(View.GONE);
        expressAmount.setVisibility(View.GONE);
        expressExpiry.setVisibility(View.GONE);



        DatabaseReference expressRef = Utils.getDatabase().getReference().child("App Values").child("Express Amount");
        expressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount = dataSnapshot.getValue().toString();
                expressAmount.setText(MainActivity.formatter.format(Double.valueOf(amount)) +"/=");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        DatabaseReference expressExpiryRef = Utils.getDatabase().getReference().child("Users").child(MainActivity.userKey).child("expressExpiry");
        expressExpiryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(null==dataSnapshot.getValue() || dataSnapshot.getValue().toString().isEmpty()){
                    b.setVisibility(View.VISIBLE);
                    expressAmount.setVisibility(View.VISIBLE);
                    expressExpiry.setVisibility(View.GONE);
                    txt.setText("Get unlimited free deliveries for a full year for just");
                }else {
                    txt.setText("Your express membership expires on");
                    expressExpiry.setVisibility(View.VISIBLE);
                    expressExpiry.setText(dataSnapshot.getValue().toString());
                    b.setVisibility(View.GONE);
                    expressAmount.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getActivity(), JoinExpressActivity.class);
                in.putExtra("amount", amount);
                startActivity(in);
            }
        });

        return rootview;
    }



}
