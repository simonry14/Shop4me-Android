package ug.co.shop4me.view.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.adapter.ProfileAdapter;
import ug.co.shop4me.model.User;
import ug.co.shop4me.model.entities.ProfileItem;

public class MyAccountActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ProfileAdapter mAdapter;
    ArrayList<ProfileItem> profileItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
setTitle("My Account");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.profile_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        profileItems = new ArrayList<>();
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));

        final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
        Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
        qq.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);
                String number1 = user.getPhone1();
                String number2 = user.getPhone2();

                try {
                    if (number1.equals(null)) {
                        number1 = "";
                    }
                    if (number2.equals(null)) {
                        number2 = "";
                    }

                } catch (Exception e) {

                }
                ProfileItem p1 = new ProfileItem("Name", MainActivity.fullname);
                ProfileItem p2 = new ProfileItem("Email", MainActivity.email);
                ProfileItem p3 = new ProfileItem("Phone Number 1", number1);
                ProfileItem p4 = new ProfileItem("Phone Number 2", number2);


                profileItems.clear();
                profileItems.add(p1);
                profileItems.add(p2);
                profileItems.add(p3);
                profileItems.add(p4);


                mAdapter = new ProfileAdapter(MyAccountActivity.this, profileItems);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
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
