package ug.co.shop4me.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Store;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class StoresAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Store> storeList;




    public StoresAdapter(Context mContext, List<Store> storeList){
        this.storeList=storeList;
        this.mContext=mContext;


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_items, parent, false);
        return new StoresAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Store pd =storeList.get(position);
        ((StoresAdapter.ViewHolder) holder).titleTextView.setText(pd.getStoreName());
        ((StoresAdapter.ViewHolder) holder).titleTextView2.setText(pd.getStoreDescription());
                String  imageName = pd.getStoreImage();

        StorageReference storageReference =
                FirebaseStorage.getInstance().getReference();

        StorageReference islandRef =
                storageReference.child("Stores Images").child(imageName);

        // Load the image using Glide
  /*      Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(islandRef)
                .into(((StoresAdapter.ViewHolder) holder).coverImageView);

*/
        islandRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                // Pass it to Picasso to download, show in ImageView and caching
                Picasso.with(mContext).load(uri.toString()).into(((StoresAdapter.ViewHolder) holder).coverImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    @Override
    public int getItemCount() {
       return storeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView titleTextView2;
        public ImageView coverImageView;


        public ViewHolder(View v) {
            super(v);
            titleTextView = (TextView) v.findViewById(R.id.storeNameTextView);
            titleTextView2 = (TextView) v.findViewById(R.id.storeDescTextView);
            coverImageView = (ImageView) v.findViewById(R.id.storeImageView);

        }
    }
}
