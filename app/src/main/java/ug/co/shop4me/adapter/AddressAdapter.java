package ug.co.shop4me.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Address;
import ug.co.shop4me.model.entities.Store;
import ug.co.shop4me.view.activities.AddAddressActivity;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Address> addressList;

    public AddressAdapter(Context mContext, List<Address> storeList){
        this.addressList=storeList;
        this.mContext=mContext;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_items, parent, false);
        return new AddressAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Address pd =addressList.get(position);
        ((AddressAdapter.ViewHolder) holder).address.setText(pd.getAddressName());

      ((AddressAdapter.ViewHolder) holder).coverImageView.setImageResource(R.drawable.homeicon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(mContext, AddAddressActivity.class);
                intent.putExtra("address", pd.getAddressName());
                intent.setAction("EDIT");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
       return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView address;
        public ImageView coverImageView;


        public ViewHolder(View v) {
            super(v);
            address = (TextView) v.findViewById(R.id.storeNameTextView);
            coverImageView = (ImageView) v.findViewById(R.id.storeImageView);

        }
    }
}
