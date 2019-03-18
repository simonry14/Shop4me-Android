package ug.co.shop4me.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;


import java.util.ArrayList;

import ug.co.shop4me.view.activities.MainActivity;
import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.User;
import ug.co.shop4me.model.entities.ProfileItem;

/**
 * Created by kaelyn on 31/12/2016.
 */

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ProfileItem> profileItemsArrayList;

    public ProfileAdapter(Context mContext, ArrayList<ProfileItem> profileItemsArrayList) {
        this.mContext = mContext;
        this.profileItemsArrayList = profileItemsArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ProfileItem pi = profileItemsArrayList.get(position);
        ((ViewHolder) holder).title.setText(pi.getTitle());
        ((ViewHolder) holder).content.setText(pi.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == 0) {
                    Toast.makeText(mContext, "Name cannot be changed", Toast.LENGTH_LONG).show();
                }
                if (position == 1) {
                    Toast.makeText(mContext, "Email cannot be changed", Toast.LENGTH_LONG).show();
                }

                if (position == 2 || position == 3) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
                    final EditText edittext = new EditText(mContext);
                    edittext.setText(pi.getContent());
                    try {
                        edittext.setSelection(pi.getContent().length());
                    } catch (Exception e) {

                    }

                    // alert.setMessage(pi.getContent());
                    alert.setTitle(pi.getTitle());

                    alert.setView(edittext);

                    alert.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //What ever you want to do with the value
                            Editable YouEditTextValue = edittext.getText();
                            //OR
                            // String YouEditTextValue = edittext.getText().toString();
                            final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
                            Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
                            qq.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    User user = dataSnapshot.getValue(User.class);
                                    if (position == 2) {
                                        user.setPhone1(edittext.getText().toString());
                                        usersRef.child(dataSnapshot.getKey()).setValue(user);
                                        ((ViewHolder) holder).content.setText(edittext.getText().toString());
                                        // notifyItemChanged(2);
                                    }
                                    if (position == 3) {
                                        user.setPhone2(edittext.getText().toString());
                                        usersRef.child(dataSnapshot.getKey()).setValue(user);
                                        ((ViewHolder) holder).content.setText(edittext.getText().toString());
                                        //  notifyItemChanged(3);
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
        });
    }

    @Override
    public int getItemCount() {
        return profileItemsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;


        public ViewHolder(View view) {
            super(view);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);


        }
    }
}
