package ug.co.shop4me.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.adapter.ContactAdapter;
import ug.co.shop4me.adapter.StoresAdapter;
import ug.co.shop4me.model.entities.Contact;
import ug.co.shop4me.model.entities.Store;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {
    View rootview;
    RecyclerView contactRecyclerView;
    ContactAdapter adapter;

    List<Contact> storeList;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_contact, container, false);
        contactRecyclerView = (RecyclerView) rootview.findViewById(R.id.contactRecyclerView);
        storeList = new ArrayList<>();
        LinearLayoutManager MyLayoutManager2 = new LinearLayoutManager(getContext());
        MyLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);

        contactRecyclerView.setLayoutManager(MyLayoutManager2);

       storeList.add(new Contact("0777718828", "Call Us", R.drawable.call));
        storeList.add(new Contact("0777718828", "Chat with us on WhatsApp", R.drawable.whatsapp));
        storeList.add(new Contact("support@shop4me.co.ug", "Email us", R.drawable.email));
        storeList.add(new Contact("@shop4meUG", "Tweet us", R.drawable.twiiter));
        storeList.add(new Contact("www.facebook.com/Shop4me-Uganda", "Send us a Facebook Message", R.drawable.fb));

        adapter = new ContactAdapter(getContext(), storeList);
        contactRecyclerView.setAdapter(adapter);
        return rootview;
    }

}
