package ug.co.shop4me.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;

import ug.co.shop4me.R;
import ug.co.shop4me.Utils;
import ug.co.shop4me.model.User;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import io.fabric.sdk.android.Fabric;

public class SignUpOrLogInActivity extends FragmentActivity {
    public static final String PREFS = "PnD";
    public static SharedPreferences mySharedPreferences;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "QBvVsVJsxcBQiZ7FF3uM9x5Ud";
    private static final String TWITTER_SECRET = "lP1GWZs4m9QUnirOmmZT3tgsmlSGo5lQekUPavurgcy7EqJORH";
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "FacebookLogin";
    LoginButton loginButton;
    Button emailLogin;
    Button emailSignUp;
    CallbackManager callbackManager;
    GoogleApiClient mGoogleApiClient;
    String email;
    String name;
    ProgressDialog pd;
    DatabaseReference mRootRef;
    StorageReference imagesRef;
    String userKey;
    String openCartId;
    private FirebaseAuth auth;
   // private StorageReference mStorage;
    private TwitterLoginButton mLoginButton;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // mStorage = FirebaseStorage.getInstance().getReference();
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_sign_up_or_log_in);
        TextView yourtextview = (TextView) findViewById(R.id.jazz);


        String text = "By signing up you agree to our <br> <font color=#43B02A>terms of use</font> and <font color=#43B02A>privacy policy</font>";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                //Start Browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//Progress Dialog
        pd = new ProgressDialog(SignUpOrLogInActivity.this);


        yourtextview.setText(Html.fromHtml(ss.toString()));
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        //If User is already logged in go to Main Activity
        if (auth.getCurrentUser() != null) {
            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Intent intent = new Intent(SignUpOrLogInActivity.this, MainActivity.class);
            startActivity(intent);

            finish();
        }

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    final String inviteCode = generateInviteCode();
                    //Save user in Firebase Database
                    mRootRef = Utils.getDatabase().getReference();
                    DatabaseReference qnRef = mRootRef.child("Users").push();

                    User u = new User(user.getEmail(),user.getDisplayName(), user.getUid(), new Date().toString(),"ANDROID",inviteCode, "",
                            "","","","","","0","0");
                    qnRef.setValue(u);

                    //Create User in OpenCart DB
                    createOpenCartUser(user, qnRef.getKey());

                    //Send Welcome email
                    sendWelcomeEmail(user.getEmail(), user.getDisplayName());

//Open Mainactivity
                    Intent intent = new Intent(SignUpOrLogInActivity.this, MainActivity.class);
                    intent.setAction("FROM NEW USER CREATED OR LOGIN");

                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("fullname", user.getDisplayName());
                    intent.putExtra("firstname", user.getDisplayName());
                    intent.putExtra("lastname", user.getDisplayName());
                    intent.putExtra("userid", user.getUid());
                    intent.putExtra("inviteCode", inviteCode);
                    intent.putExtra("userKey", qnRef.getKey());
                    intent.putExtra("address", "");
                    intent.putExtra("phone1", "");
                    startActivity(intent);
                    pd.hide();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        //FACEBOOK LOGIN

        loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions("email");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));


        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        //GOOGLE LOGIN
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setColorScheme(SignInButton.COLOR_DARK);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                //  .enableAutoManage()
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//TWITTER BUTTON


        mLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w(TAG, "twitterLogin:failure", exception);
                //updateUI(null);
            }
        });


        //EMAIL LOGIN

        emailLogin = (Button) findViewById(R.id.emailLoginButton);
        emailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpOrLogInActivity.this, LoginActivity.class));

            }
        });

        emailSignUp = (Button) findViewById(R.id.emailSignUpButton);
        emailSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpOrLogInActivity.this, SignupActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                //updateUI(null);
                // [END_EXCLUDE]
            }
        }


        mLoginButton.onActivityResult(requestCode, resultCode, data);


    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        pd.setMessage("Successfully logged in. Redirecting...");
        pd.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUpOrLogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        pd.setMessage("Successfully logged in. Redirecting...");
        pd.show();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUpOrLogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]
                        // hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    private void handleTwitterSession(TwitterSession session) {
        Log.d(TAG, "handleTwitterSession:" + session);
        pd.setMessage("Successfully logged in. Redirecting...");
        pd.show();
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignUpOrLogInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
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
     //   String url = "http://qnsnap.com/rest/sendWelcomeEmail/" + email + "/" + names;
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

    private void createOpenCartUser(final FirebaseUser u, String uKey){
        RequestQueue queue = Volley.newRequestQueue(this);
        String firstName=getFirstName(u.getDisplayName());
        String lastName = getLastName(u.getDisplayName());

        String url = MainActivity.BASE_URL+"create_user?firstname="+firstName+
                "&lastname="+lastName+"&email="+u.getEmail()+"&key="+uKey;
        url= url.replace(" ", "%20");
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res = response;
                        String res1 = res.substring(1);
                        openCartId  = res1;

                        mySharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("openCartId", openCartId);
                        editor.apply();
                        //Update FB DB With OPENCART USER ID
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
/*
//Save Profile Picture

                    for (UserInfo profile : user.getProviderData()) {
                            // Id of the provider (ex: google.com
                            Uri photoUrl = profile.getPhotoUrl();
                            //Create QuestionSnap Folder
                            File rootPath = new File(Environment.getExternalStorageDirectory(), "QuestionSnap");
                            if (!rootPath.exists()) {
                            rootPath.mkdirs();
                            }
                            File localFile = new File(rootPath, "profile.jpg");
                            try {
                            InputStream in = getContentResolver().openInputStream(photoUrl);
                            OutputStream out = new FileOutputStream(localFile);
                            byte[] buf = new byte[1024];
                            in.read(buf);
                            do {
                            out.write(buf);
                            } while (in.read(buf) != -1);
                            } catch (java.io.IOException e) {
                            e.printStackTrace();
                            }
                            }
*/