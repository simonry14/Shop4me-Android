package ug.co.shop4me.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import ug.co.shop4me.R;
import ug.co.shop4me.model.entities.Contact;
import ug.co.shop4me.model.entities.Store;

/**
 * Created by kaelyn on 11/05/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Contact> contactList;

    public ContactAdapter(Context mContext, List<Contact> contactList){
        this.contactList=contactList;
        this.mContext=mContext;

    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_items, parent, false);
        return new ContactAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Contact pd =contactList.get(position);
        ((ContactAdapter.ViewHolder) holder).titleTextView.setText(pd.getContactName());
        ((ContactAdapter.ViewHolder) holder).titleTextView2.setText(pd.getContactDescription());
             (((ContactAdapter.ViewHolder) holder).coverImageView).setImageResource(pd.getContactImage());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:0777718828"));
                    mContext.startActivity(intent);
                }
                if(position == 1){
                    String smsNumber = "256772122373"; //without '+'
                    try {
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        //sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        //sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                        sendIntent.putExtra("jid", smsNumber + "@s.whatsapp.net"); //phone number without "+" prefix
                        sendIntent.setPackage("com.whatsapp");
                        mContext.startActivity(sendIntent);
                    } catch(Exception e) {
                        Toast.makeText(mContext, "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                if(position == 2){
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("plain/text");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "info@shop4me.co.ug" });
                    mContext.startActivity(Intent.createChooser(intent, ""));
                }
                if(position == 3){
                    Intent intent = null;
                    try {
                        // get the Twitter app if possible
                        mContext.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=Snowden"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/USERID_OR_PROFILENAME"));
                    }
                    mContext.startActivity(intent);

                }

                if(position == 4){
                    Intent intent = null;
                    try {
                        // get the Twitter app if possible
                        mContext.getPackageManager().getPackageInfo("com.twitter.android", 0);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=Snowden"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    } catch (Exception e) {
                        // no Twitter app, revert to browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/USERID_OR_PROFILENAME"));
                    }
                    mContext.startActivity(intent);


                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                    String facebookUrl = getFacebookPageURL(mContext);
                    facebookIntent.setData(Uri.parse(facebookUrl));
                    mContext.startActivity(facebookIntent);


                }
            }
        });
    }

    public static String FACEBOOK_URL = "https://www.facebook.com/Shop4me-Uganda-1602414606448756";
    public static String FACEBOOK_PAGE_ID = "1602414606448756";

    //method to get the right URL to use in the intent
    public String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    @Override
    public int getItemCount() {
       return contactList.size();
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
