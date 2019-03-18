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

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Product;
import ug.co.shop4me.view.activities.ProductDetailsActivity;

/**
 * Created by kaelyn on 6/05/2017.
 */

public class RecommendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Product> productArrayListArrayList;

    public RecommendListAdapter(Context mContext, List<Product> productArrayListArrayList) {
        this.mContext = mContext;
        this.productArrayListArrayList = productArrayListArrayList;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_items, parent, false);
        return new RecommendListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Product pd = productArrayListArrayList.get(position);
        ((ViewHolder) holder).titleTextView.setText(pd.getItemName());
        ((ViewHolder) holder).titleTextView2.setText(MainActivity.formatter.format(Long.valueOf(pd.getSellMRP().split("[.]")[0])) +" /=");
      String  ImageUrl = pd.getImageURL();
        Glide.with(mContext).load(ImageUrl).centerCrop().into(((ViewHolder) holder).coverImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent pIntent = new Intent(mContext, ProductDetailsActivity.class);
                pIntent.setAction("FROM PD LIST ADAPTER");
                pIntent.putExtra("pName", pd.getItemName());
                pIntent.putExtra("pId", pd.getProductId());
                pIntent.putExtra("pDesc", pd.getItemShortDesc());
                pIntent.putExtra("pPrice", pd.getSellMRP());
                pIntent.putExtra("pImgURL", pd.getImageURL());
                pIntent.putExtra("pQuantity", "0");
                pIntent.putExtra("product", pd);
                pIntent.putExtra("position", position);

                mContext.startActivity(pIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productArrayListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView titleTextView2;
        public ImageView coverImageView;


        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.nameTextView);
            titleTextView2 = (TextView) v.findViewById(R.id.priceTextView);
            coverImageView = (ImageView) v.findViewById(R.id.coverImageView);

        }
    }
}
