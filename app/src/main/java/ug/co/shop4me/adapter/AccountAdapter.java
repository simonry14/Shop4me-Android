package ug.co.shop4me.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ug.co.shop4me.AccountItem;
import ug.co.shop4me.R;
import ug.co.shop4me.view.activities.AddressActivity;
import ug.co.shop4me.view.activities.LoyaltyPoints;
import ug.co.shop4me.view.activities.MyAccountActivity;
import ug.co.shop4me.view.activities.NotificationsActivity;

/**
 * Created by kaelyn on 1/05/2017.
 */

public class AccountAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<AccountItem> accountItemsArrayList;

    public AccountAdapter(Context mContext, ArrayList<AccountItem> accountItemsArrayList) {
        this.mContext = mContext;
        this.accountItemsArrayList = accountItemsArrayList;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
         AccountItem ai = accountItemsArrayList.get(position);
        ((ViewHolder) holder).title.setText(ai.getName());
        ((ViewHolder) holder).img.setImageResource(ai.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (position == 0) {
                 //   Toast.makeText(mContext, "My account", Toast.LENGTH_LONG).show();
                    mContext.startActivity(new Intent(mContext, MyAccountActivity.class));
                }
                if (position == 1) {
                   // Toast.makeText(mContext, "Addresses", Toast.LENGTH_LONG).show();
                    mContext.startActivity(new Intent(mContext, AddressActivity.class));
                }
                if (position == 2) {
                    Toast.makeText(mContext, "Payment Method", Toast.LENGTH_LONG).show();
                }
                if (position == 3) {
                    mContext.startActivity(new Intent(mContext, LoyaltyPoints.class));
                }
                if (position == 4) {
                    mContext.startActivity(new Intent(mContext, NotificationsActivity.class));
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return accountItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) itemView.findViewById(R.id.title);
            img = (ImageView) itemView.findViewById(R.id.img);


        }
    }
}
