package ug.co.shop4me.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import ug.co.shop4me.*;
import ug.co.shop4me.model.User;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.adapter.ShoppingListAdapter;
import ug.co.shop4me.view.customview.OnStartDragListener;
import ug.co.shop4me.view.customview.SimpleItemTouchHelperCallback;

import static ug.co.shop4me.view.activities.MainActivity.PREFS;

public class CartActivity extends AppCompatActivity implements OnStartDragListener {
    private ItemTouchHelper mItemTouchHelper;
    private static FrameLayout noItemDefault;
    private static RecyclerView recyclerView;
    static Button b;
    static Button startShopping;
    static  TextView empty;
    public static Integer total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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

setTitle("Shopping Cart");
        b = (Button) findViewById(R.id.checkout);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if ddress nd phone re sved
                if(MainActivity.address.isEmpty() || MainActivity.phone1.isEmpty()){ //OR OPERATOR
                    showAlert();
                }else {

                    Intent in = new Intent(CartActivity.this, CheckoutActivity.class);
                    startActivity(in);
                }
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.product_list_recycler_view);
        startShopping = (Button) findViewById(R.id.startShopping);
        empty = (TextView) findViewById(R.id.empty);

        if (MainActivity.shoppingCartList.size() != 0) {
            startShopping.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            ShoppingListAdapter shoppinListAdapter = new ShoppingListAdapter(getApplicationContext());
            recyclerView.setAdapter(shoppinListAdapter);
            JazzyRecyclerViewScrollListener jazzyScrollListener = new JazzyRecyclerViewScrollListener();
            recyclerView.setOnScrollListener(jazzyScrollListener);
            jazzyScrollListener.setTransitionEffect(11);

            shoppinListAdapter.SetOnItemClickListener(new ShoppingListAdapter.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {


                        }
                    });

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(shoppinListAdapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }

        else {
            recyclerView.setVisibility(View.GONE);
            b.setVisibility(View.GONE);
startShopping.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent in = new Intent(CartActivity.this, MainActivity.class);
        in.setAction("FROM EMPTY CART");
        startActivity(in);
    }
});
        }

try{
    startShopping.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent in = new Intent(CartActivity.this, MainActivity.class);
            in.setAction("FROM EMPTY CART");
            startActivity(in);
        }
    });
} catch (Exception e){

}
        updateCartAmount();
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

public static void updateCartAmount(){

    total = 0;
    for (Product p:MainActivity.shoppingCartList){
        total = total+  Integer.valueOf(p.getSellMRP().split("[.]")[0]) * Integer.valueOf(p.getQuantity());
    }
    b.setText("CHECKOUT    " + "(" +MainActivity.formatter.format(total)+"/=)");
}

public static void cartNowEmpty(){
    recyclerView.setVisibility(View.GONE);
    b.setVisibility(View.GONE);
    startShopping.setVisibility(View.VISIBLE);
    empty.setVisibility(View.VISIBLE);
}

void showAlert(){
    AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyAlertDialogStyle);

    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);

    final EditText edittext = new EditText(this);
    edittext.setHint("Address");
    final EditText edittext2 = new EditText(this);
    edittext2.setHint("Phone Number");
    edittext.setText(MainActivity.address);
    edittext2.setText(MainActivity.phone1);
    alert.setTitle("Enter your Address and Phone Number");

    layout.addView(edittext);
    layout.addView(edittext2);

    alert.setView(layout);


    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            //What ever you want to do with the value
            Editable YouEditTextValue = edittext.getText();
            //OR
            // String YouEditTextValue = edittext.getText().toString();
            final DatabaseReference usersRef = ug.co.shop4me.Utils.getDatabase().getReference().child("Users");
            Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
            qq.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User user = dataSnapshot.getValue(User.class);

                        user.setAddress(edittext.getText().toString());
                        user.setPhone1(edittext2.getText().toString());
                        usersRef.child(dataSnapshot.getKey()).setValue(user);
                 SharedPreferences   mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = mySharedPreferences.edit();
                    editor.putString("address", edittext.getText().toString());
                    editor.putString("phone1", edittext2.getText().toString());
                    editor.apply();
                    MainActivity.address = edittext.getText().toString();
                    MainActivity.phone1 = edittext2.getText().toString();

                    //Go to checkout if all is good
                    if(!MainActivity.address.isEmpty() && !MainActivity.phone1.isEmpty()) {
                          Intent in = new Intent(CartActivity.this, CheckoutActivity.class);
                         startActivity(in);
                    }

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
    });

    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
            // what ever you want to do with No option.
        }
    });

    alert.show();

}

}
