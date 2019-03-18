package ug.co.shop4me.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class OrderProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Product> storeList;

    public OrderProductsAdapter(Context mContext, List<Product> storeList){
        this.storeList=storeList;
        this.mContext=mContext;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_product_list, parent, false);
        return new OrderProductsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Product pd =storeList.get(position);
        ((OrderProductsAdapter.ViewHolder) holder).titleTextView.setText(pd.getQuantity());
        ((OrderProductsAdapter.ViewHolder) holder).titleTextView2.setText(pd.getItemName());
        ((OrderProductsAdapter.ViewHolder) holder).titleTextView3.setText(MainActivity.formatter.format(Long.valueOf(pd.getSellMRP().split("[.]")[0] ))+ "/=");
                String  ImageUrl = pd.getImageURL();
        Glide.with(mContext).load(ImageUrl).centerCrop().into(((OrderProductsAdapter.ViewHolder) holder).coverImageView);
    }

    @Override
    public int getItemCount() {
       return storeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView titleTextView2;
        public TextView titleTextView3;
        public ImageView coverImageView;


        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.orderQuantity);
            titleTextView2 = (TextView) v.findViewById(R.id.item_name);
            titleTextView3 = (TextView) v.findViewById(R.id.item_price);
            coverImageView = (ImageView) v.findViewById(R.id.product_thumb2);

        }
    }
}
