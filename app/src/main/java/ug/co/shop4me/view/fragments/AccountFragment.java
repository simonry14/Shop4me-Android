package ug.co.shop4me.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import ug.co.shop4me.AccountItem;
import ug.co.shop4me.R;
import ug.co.shop4me.view.activities.SignUpOrLogInActivity;
import ug.co.shop4me.adapter.AccountAdapter;


public class AccountFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<AccountItem> accountItems;
    AccountAdapter mAdapter;
    Button logoutButton;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.pr_recycler_view);
        logoutButton = (Button) view.findViewById(R.id.logoutButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        accountItems= new ArrayList<>();
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        accountItems.add(new AccountItem("My Account", R.mipmap.ic_account_box_black_48dp));
        accountItems.add(new AccountItem("Addresses", R.mipmap.ic_place_black_48dp));
        accountItems.add(new AccountItem("Payment Methods", R.mipmap.ic_payment_black_48dp));
        accountItems.add(new AccountItem("Loyalty Points & Referral Credit", R.mipmap.ic_card_membership_black_48dp));
        accountItems.add(new AccountItem("Notifications", R.mipmap.ic_notifications_black_48dp));

        mAdapter = new AccountAdapter(getContext(), accountItems);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), SignUpOrLogInActivity.class);
                startActivity(intent);

            }
        });


        return view;
    }

    public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
        private Drawable mDivider;

        public SimpleDividerItemDecoration(Context context) {
            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }



}
