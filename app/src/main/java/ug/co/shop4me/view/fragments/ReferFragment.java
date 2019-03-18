package ug.co.shop4me.view.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReferFragment extends Fragment {
    View rootView;
    Button shareButton;
TextView code;
    public ReferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_refer, container, false);
        shareButton = (Button) rootView.findViewById(R.id.shareCode);
        code = (TextView) rootView.findViewById(R.id.txtInviteCode);
        code.setText(MainActivity.inviteCode);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout shop4me, get groceries delivered to you in as little as 1 hour. Use Code "
                        +MainActivity.inviteCode+" to get 5,000/= credit");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        return rootView;
    }


}
