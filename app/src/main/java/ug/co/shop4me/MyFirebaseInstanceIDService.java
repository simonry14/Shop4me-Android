package ug.co.shop4me;

/**
 * Created by kaelyn on 26/11/2016.
 */


import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.database.ChildEventListener;

import ug.co.shop4me.model.User;
import ug.co.shop4me.view.activities.MainActivity;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String PREFS = "Shop4me";
    public static SharedPreferences mySharedPreferences;
    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("General");

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
try{
    mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
    SharedPreferences.Editor editor = mySharedPreferences.edit();
    editor.putString("registration token", refreshedToken);
    editor.apply();

    sendRegistrationToServer(refreshedToken);

}catch (Exception e){

}

    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {
        // TODO: Implement this method to send token to your app server.
      /*  RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://shop4me.co.ug/api/saveRegistrationToken/" + token + "/" + MainActivity.userKey+ "/" + MainActivity.email.replace(".",",");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);


**/
        //SEND TO FB DB
        //Send to FB DB

        final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
        Query qq = usersRef.orderByChild("userId").equalTo(MainActivity.userId);
        qq.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user = dataSnapshot.getValue(User.class);

                    user.setToken(token);
                    usersRef.child(dataSnapshot.getKey()).setValue(user);
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


}
