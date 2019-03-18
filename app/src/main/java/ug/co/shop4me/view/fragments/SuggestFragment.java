package ug.co.shop4me.view.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.Suggestion;


/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestFragment extends Fragment {
    Button btnSend;
    TextView txtS;
    View rootView;
    EditText edt;
    DatabaseReference mRootRef;
    ProgressDialog mProgressDialog;

    public SuggestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_suggest, container, false);
        mRootRef = Utils.getDatabase().getReference();
        mProgressDialog = new ProgressDialog(getActivity());

        edt = (EditText) rootView.findViewById(R.id.editTextSuggest);

        btnSend = (Button) rootView.findViewById(R.id.sendSuggest);
        txtS = (TextView) rootView.findViewById(R.id.txtLabel3);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a product name", Toast.LENGTH_LONG).show();
                }  else {
                   mProgressDialog.setMessage("Submitting...");
                    mProgressDialog.show();

                    //STORE DATA IN FIREBASE DATABASE
                    DatabaseReference qnRef = mRootRef.child("Suggested Products"); //Create node for user
                    Suggestion s = new Suggestion(edt.getText().toString(), MainActivity.fullname);
                    qnRef.push().setValue(s);
                    Toast.makeText(getContext(), "Submitted", Toast.LENGTH_LONG).show();
                    mProgressDialog.hide();

                    txtS.setText("Thank you. Your suggestion of "+ "'"+edt.getText().toString() +"'"+ " has been recieved" );
                    edt.setText("");
                }
            }
        });


        return rootView;

    }
}