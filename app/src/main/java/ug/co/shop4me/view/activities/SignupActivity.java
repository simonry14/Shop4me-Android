package ug.co.shop4me.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.User;

import java.util.Date;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {
    public static final String PREFS = "PnD";
    public static SharedPreferences mySharedPreferences;
    DatabaseReference mRootRef;
    ProgressDialog mProgressDialog;
    String userKey;
    private EditText inputEmail, inputPassword, inputFullName, address, phone;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String openCartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputFullName = (EditText) findViewById(R.id.fullname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mProgressDialog = new ProgressDialog(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                    mProgressDialog.hide();
                    final String inviteCode = generateInviteCode();

                    //Store  user in database
                    mRootRef = Utils.getDatabase().getReference();
                    DatabaseReference userRef = mRootRef.child("Users").push();
                  //  User ud = new User(inputEmail.getText().toString().trim(), inputFullName.getText().toString(), auth.getCurrentUser().getUid(), new Date().toString(), auth.getCurrentUser().getProviderId(), inviteCode, "", "");
                    User u = new User(inputEmail.getText().toString().trim(),inputFullName.getText().toString(), auth.getCurrentUser().getUid(), new Date().toString(),"ANDROID",inviteCode, address.getText().toString(),
                            phone.getText().toString(),"","","","","0","0");
                    userRef.setValue(u);

//create Opencart User
                    createOpenCartUser(user, userRef.getKey());


                    //Open Mainactivity//Redirect to Mainactivity
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    intent.setAction("FROM NEW USER CREATED OR LOGIN");
                    intent.putExtra("email", inputEmail.getText().toString().trim());
                    intent.putExtra("fullname", inputFullName.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    intent.putExtra("phone1", phone.getText().toString());
                    intent.putExtra("userid", user.getUid());
                    intent.putExtra("inviteCode", inviteCode);
                    intent.putExtra("userKey", userRef.getKey());
                    startActivity(intent);

                } else {
                    // User is signed out
                    Log.d("", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String addressT = address.getText().toString();
                String phoneN = phone.getText().toString();
                final String fullname = inputFullName.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Enter a valid email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(addressT)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(phoneN)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mProgressDialog.setMessage("Registering...");
                mProgressDialog.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullname)
                                            // .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();
                                    auth.getCurrentUser().updateProfile(profileUpdates);


                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    private String generateInviteCode() {
        char[] chars1 = "ABCDEFG012GHIJKL345MNOPQR678STUVWXYZ9".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < 6; i++) {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        return sb1.toString();
    }


    private void sendWelcomeEmail(String email, String names) {
        RequestQueue queue = Volley.newRequestQueue(this);
       // String url = "http://qnsnap.com/rest/sendWelcomeEmail/" + email + "/" + names;
        String url = MainActivity.BASE_URL+"sendWelcomeEmail?email="+email+
                "&names="+names;
        url= url.replace(" ", "%20");
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public final static  boolean isValidEmail(CharSequence email){
        if(TextUtils.isEmpty(email)){
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private void createOpenCartUser(final FirebaseUser u , String uKey){
        RequestQueue queue = Volley.newRequestQueue(this);
        String firstName = getFirstName(inputFullName.getText().toString().trim());
        String lastName = getLastName(inputFullName.getText().toString().trim());

        String url = MainActivity.BASE_URL+"create_user?firstname="+firstName+
                "&lastname="+lastName+"&email="+inputEmail.getText().toString().trim()+"&key="+uKey;
        url= url.replace(" ", "%20");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res = response;
                        String res1 = res.substring(1);
                        openCartId  = res1;
                        //Save This ID Already
                        mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("openCartId", openCartId);
                        editor.apply();
                        saveOpenCartIdInFirebaseDB(openCartId, u.getUid());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


   private void saveOpenCartIdInFirebaseDB(final String openCartId, final String uid){

       final DatabaseReference usersRef = Utils.getDatabase().getReference().child("Users");
       Query qq = usersRef.orderByChild("userId").equalTo(uid);
       qq.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
               User user = dataSnapshot.getValue(User.class);
                   user.setOpenCartId(openCartId);
                   usersRef.child(dataSnapshot.getKey()).setValue(user);

               sendWelcomeEmail(inputEmail.getText().toString().trim(), inputFullName.getText().toString());


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

    private String getFirstName(String fullname){
        String firstName="";

        String[] nameArray = fullname.split(" ");
        int nameArrayLength = nameArray.length;
        if(nameArrayLength==1){
            firstName = nameArray[0];
        }else if(nameArrayLength==0){

        }
        else if(nameArrayLength==2){
            firstName = nameArray[0];
        }else if(nameArrayLength==3){
            firstName = nameArray[0]+ " "+nameArray[1];
        }else if(nameArrayLength==4){
            firstName = nameArray[0]+ " "+nameArray[1]+ " "+nameArray[2];
        }else{
            firstName = nameArray[0];
        }

        return firstName;
    }

    private String getLastName(String fullname){
        String lastName="";

        String[] nameArray = fullname.split(" ");
        int nameArrayLength = nameArray.length;
        if(nameArrayLength==1){

            lastName = nameArray[0];
        }else if(nameArrayLength==0){

        }
        else if(nameArrayLength==2){
            lastName = nameArray[1];
        }else if(nameArrayLength==3){
            lastName = nameArray[2];
        }else if(nameArrayLength==4){
            lastName = nameArray[3];
        }else{
            lastName = nameArray[1];
        }

        return lastName;
    }

}
