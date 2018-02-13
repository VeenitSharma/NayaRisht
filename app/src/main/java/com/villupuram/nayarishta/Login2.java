package com.villupuram.nayarishta;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.callback.Callback;

import static com.villupuram.nayarishta.SplashEducationCarrier.SplashEducation;
import static com.villupuram.nayarishta.SplashLocationDetails.SplashLocation;
import static com.villupuram.nayarishta.SplashPersonalDetails.SplasgPI;
import static com.villupuram.nayarishta.constant.base_api_url.Check_FB_Rregistration;
import static com.villupuram.nayarishta.constant.base_api_url.FacebookloginOrnot;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.basicdetails;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;
import static com.villupuram.nayarishta.constant.base_api_url.varify_profile;



public class Login2 extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/loginUser.php";
    private static final String URL_FOR_FB = "http://nayarishta.com/nayarishta-app/api/socialLogin.php";
    public static final String user_id = "0";
    private static String FB_URL="";
    private static String g_url="";
    ProgressDialog progressDialog;
    public static final String MyPREFERENCES = "MyPrefs";
    //for google plus login
    private static final int RC_SIGN_IN = 007;
    private ImageButton btnSignIn;
    private ProgressDialog mProgressDialog;
    //for facebook login
    CallbackManager callbackManager;
    AccessToken accessToken;
    String acctoken;
    //google email id
    public static String emailtext = "";
    //general string
    public static String channel = "";
    //Username and password for Login
    private EditText editTextUsername;
    private EditText editTextPassword;
    //Login Button
    private Button buttonLogin;
    //Sharedpreferenced
    static String user = "", pass = "", FB_ID="", FB_NAME="", GOOGLE="google",GoogleToken="";
    SharedPreferences sharedpreferences;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    private static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    private static final String TAGg = "RetrieveAccessToken";
    public static Integer AGE;
    Boolean signUpModeActive;
    static ArrayList<String> userList = new ArrayList<>();

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String ace = "";
    public static final String PROFILE="";
    AccessTokenTracker accessTokenTracker;


    AccountManager mAccountManager;
    String token;
    int serverCode;


    private AQuery aQuery;
    private int SIGN_IN = 55664;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    Button steps;
    // ...

    private String mAccountName;
    GoogleSignInAccount acct;
    String idToken;
    String CLIENT_SECRET_FILE = "/path/to/client_secret.json";
    String[] tokenvalue;
    SharedPreferences sharedpConstant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login2);
        //for facebook
        AppEventsLogger.activateApp(this);

        mAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(this);
        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.villupuram.nayarishta",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }


        if (basicdetails.toString().equals("basic"))
        {


        }

        btnSignIn = (ImageButton) findViewById(R.id.btn_sign_in);
        steps = (Button)findViewById(R.id.step);


        editTextUsername = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        Button forgot = (Button) findViewById(R.id.forgot);

//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId().requestIdToken(this.getResources().getString(R.string.server_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
//                        new NetHttpTransport(),
//                        JacksonFactory.getDefaultInstance(),
//                        "https://www.googleapis.com/oauth2/v4/token",
//                        clientSecrets.getDetails().getClientId(),
//                        clientSecrets.getDetails().getClientSecret(),
//                        authCode,
//                        REDIRECT_URI)  // Specify the same redirect URI that you use with your web
//                        // app. If you don't have a web version of your app, you can
//                        // specify an empty string.
//                        .execute();

//        aQuery = new AQuery(this);

//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();

            //connect it
        callbackManager = CallbackManager.Factory.create();



        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Success", "Login");

                AccessToken token = AccessToken.getCurrentAccessToken();
                Log.d("access only Token is", String.valueOf(token.getToken()));
                String facebook_id_token = String.valueOf(token.getToken());
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                String login_type = "fb";

                String user_facebook= loginResult.getAccessToken().getUserId();

                System.out.print(user_facebook);

                accessToken = AccessToken.getCurrentAccessToken();

//                handleFacebookAccessToken(loginResult.getAccessToken());


                fb_login(login_type, facebook_id_token);

            }

            @Override
            public void onCancel() {
                Toast.makeText(Login2.this, "Login Cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        LoginManager.getInstance().logOut();
                    }
                }
            }
        });

        ImageButton btn_fb_login = (ImageButton) findViewById(R.id.login_button);

        btn_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LoginManager.getInstance().logInWithReadPermissions(Login2.this, Arrays.asList("public_profile", "email"));
                FacebookloginOrnot=1;

//                Intent intent = new Intent();
//                intent.putExtra("forlogin","notHello");
            }
        });


        // If the access token is available already assign it.


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

//        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editTextUsername = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);



        // Customizing G+ button


        buttonLogin = (Button) findViewById(R.id.submit);

//        share.setVisibility(ViewSome.INVISIBLE);


        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);



        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(editTextUsername.getText().toString(),
                        editTextPassword.getText().toString());
            }
        });



        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login2.this, ForgotPassword.class);
                startActivity(intent);
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }








    //google
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent = new Intent(getApplicationContext(),Login2.class);
                        startActivity(intent);
                    }
                });
    }

    private void fb_login(final String login_type, final String access_token) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();

        FB_URL = URL_FOR_FB + "?login_type=" + login_type + "&access_token=" + access_token;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                FB_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {

                    JSONObject jsonObj = new JSONObject(response);
                    String  channeld="", id="";

                    JSONArray jObj = jsonObj.getJSONArray("user");

                    JSONObject c = jObj.getJSONObject(0);

                    String userid = c.getString("UserProfileId");
                    String userName = c.getString("UserProfileUserName");


                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("user_id", userid);
                    editor.putString("logintype", "fb");
                    editor.commit();

                    Intent logincheck = getIntent();
                    SharedPreferences shared = getSharedPreferences(SplashEducation, MODE_PRIVATE);
                    String channel = (shared.getString("logintype", ""));

                    SharedPreferences sharesd = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                    channeld = (sharesd.getString("basicdetail", ""));
                    id = (sharesd.getString("id", ""));
                    if (channel.equals("")||channel.equals(null))
                    {
                        if (!channeld.equals("basicdetail")){
                            chat(userName,access_token);

                            Intent intent = new Intent(getApplicationContext(), SplashPersonalDetails.class);
                            intent.putExtra("logintype", "fb");
                            startActivity(intent);
                        }else {
                            chat(userName,access_token);
                            SharedPreferences.Editor editogr = sharedpConstant.edit();
                            editogr.putString("basicdetail", "basicdetail");
                            editogr.putString("id", id);
                            editor.putString("session_status", "logout");
                            editogr.commit();

                            Intent intent_basiclogin = getIntent();
                            String basic_gender = intent_basiclogin.getStringExtra("Gender");
                            String   basic_agefrom = intent_basiclogin.getStringExtra("agefrom");
                            String   basic_ageto = intent_basiclogin.getStringExtra("ageto");
                            String   basic_heightfrom = intent_basiclogin.getStringExtra("heightfrom");
                            String   basic_heightto = intent_basiclogin.getStringExtra("heightto");
                            String   basic_riligion = intent_basiclogin.getStringExtra("riligion");
                            String   basic_occupation = intent_basiclogin.getStringExtra("occupation");
                            String   json = intent_basiclogin.getStringExtra("json");
                            SharedPreferences.Editor editsor = sharedpreferences.edit();
                            editsor.putString("logintype", "fb");
                            editsor.commit();

                            Intent intent = new Intent(Login2.this,BasicDetail.class);
                            intent.putExtra("id",id);
                            intent.putExtra("Gender",basic_gender);
                            intent.putExtra("agefrom",basic_agefrom);
                            intent.putExtra("ageto",basic_ageto);
                            intent.putExtra("heightfrom",basic_heightfrom);
                            intent.putExtra("heightto",basic_heightto);
                            intent.putExtra("riligion",basic_riligion);
                            intent.putExtra("occupation",basic_occupation);
                            intent.putExtra("json",json);
//                                         intent.putExtra("gender",gender);
                            intent.putExtra("type","viewprofile");
                            startActivity(intent);

//                                         SharedPreferences.Editor editodr = sharedpConstant.edit();
//                                         editodr.putString("session_status", "logout");
//                                         editor.putString("basicdetail", "");
//                                         editodr.commit();

                            Toast.makeText(Login2.this, "Logged in!", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        chat(userName,access_token);
                        Intent intent = new Intent(getApplicationContext(), TryFragmentActivity.class);
                        intent.putExtra("logintype", "fb");
                        startActivity(intent);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
//        {
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("login_type", login_type);
//                params.put("access_token", access_token);
//                return params;
//            }
//        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void g_login(final String login_type, final String access_token) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        g_url = URL_FOR_FB+"?login_type="+login_type+"&access_token="+access_token;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                g_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    String  channeld="", id="";

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray jObj = jsonObj.getJSONArray("user");
                    JSONObject c = jObj.getJSONObject(0);
                    String userid = c.getString("UserProfileId");
                    String userName = c.getString("UserProfileUserName");
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("user_id", userid);
                    editor.putString("logintype", "google");
                    editor.commit();

                     SharedPreferences shared = getSharedPreferences(SplashEducation, MODE_PRIVATE);
                    String channel = (shared.getString("logintype", ""));


                    SharedPreferences sharesd = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                    channeld = (sharesd.getString("basicdetail", ""));
                    id = (sharesd.getString("id", ""));

                    if (channel.equals("")||channel.equals(null))
                    {
                        if (!channeld.equals("basicdetail")){
                            chat(userName,access_token);
                            Intent intent = new Intent(getApplicationContext(), SplashPersonalDetails.class);
                            intent.putExtra("logintype", "google");
                            startActivity(intent);
                        }
                        else {
                            chat(userName,access_token);
                            SharedPreferences.Editor editogr = sharedpConstant.edit();
                            editogr.putString("basicdetail", "basicdetail");
                            editogr.putString("id", id);
                            editor.putString("session_status", "logout");
                            editogr.commit();

                            Intent intent_basiclogin = getIntent();
                            String basic_gender = intent_basiclogin.getStringExtra("Gender");
                            String   basic_agefrom = intent_basiclogin.getStringExtra("agefrom");
                            String   basic_ageto = intent_basiclogin.getStringExtra("ageto");
                            String   basic_heightfrom = intent_basiclogin.getStringExtra("heightfrom");
                            String   basic_heightto = intent_basiclogin.getStringExtra("heightto");
                            String   basic_riligion = intent_basiclogin.getStringExtra("riligion");
                            String   basic_occupation = intent_basiclogin.getStringExtra("occupation");
                            String   json = intent_basiclogin.getStringExtra("json");
                            SharedPreferences.Editor editsor = sharedpreferences.edit();
                            editsor.putString("logintype", "google");
                            editsor.commit();

                            Intent intent = new Intent(Login2.this,BasicDetail.class);
                            intent.putExtra("id",id);
                            intent.putExtra("Gender",basic_gender);
                            intent.putExtra("agefrom",basic_agefrom);
                            intent.putExtra("ageto",basic_ageto);
                            intent.putExtra("heightfrom",basic_heightfrom);
                            intent.putExtra("heightto",basic_heightto);
                            intent.putExtra("riligion",basic_riligion);
                            intent.putExtra("occupation",basic_occupation);
                            intent.putExtra("json",json);
//                                         intent.putExtra("gender",gender);
                            intent.putExtra("type","viewprofile");
                            startActivity(intent);

//                                         SharedPreferences.Editor editodr = sharedpConstant.edit();
//                                         editodr.putString("session_status", "logout");
//                                         editor.putString("basicdetail", "");
//                                         editodr.commit();

                            Toast.makeText(Login2.this, "Logged in!", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        chat(userName,access_token);
                        Intent intent = new Intent(getApplicationContext(), TryFragmentActivity.class);
                        intent.putExtra("logintype", "google");
                        startActivity(intent);
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_type", login_type);
                params.put("access_token", access_token);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
//            String scopes = "oauth2:profile email";
            String scopes = "oauth2:"+ Scopes.PLUS_LOGIN
                    + " "
                    + Scopes.PROFILE;
            String token = null;
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } catch (UserRecoverableAuthException e) {
                startActivityForResult(e.getIntent(), SIGN_IN);
                //REQ_SIGN_IN_REQUIRED = 55664;
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("AccessToken",s);
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//
//            handleSignInResult(result);
//

//
        final String[] token = {""};
//        }



        //If signin
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                final GoogleSignInAccount account = result.getSignInAccount();

//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            String scope = "oauth2:"+Scopes.EMAIL+" "+ Scopes.PROFILE;
//                         String  accessToken = GoogleAuthUtil.getToken(getApplicationContext(), account.getAccount(), scope, new Bundle());
//                            Log.d(TAG, "accessToken:"+ accessToken); //accessToken:ya29.Gl...
//
//
////                            tokenvalue[0]=accessToken;
////                            System.out.print(token[0]);
//                            testing();
//
////                            g_login("google",accessToken);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (GoogleAuthException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                });
//                thread.start();
//
//            }
//        } else {
//                Toast.makeText(getApplicationContext(),"Token: null",Toast.LENGTH_LONG).show();
//            }
//

//
//
//        }
//        }
                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String token = null;
                        String scope = "oauth2:"+Scopes.EMAIL+" "+ Scopes.PROFILE;
                        try {
                            token = GoogleAuthUtil.getToken(
                                    Login2.this,
                                    account.getAccount(), scope);
                        } catch (IOException transientEx) {
                            // Network or server error, try later
                            Log.e(TAG, transientEx.toString());
                        } catch (UserRecoverableAuthException e) {
                            // Recover (with e.getIntent())
                            Log.e(TAG, e.toString());
                            Intent recover = e.getIntent();
                            startActivityForResult(recover, RC_SIGN_IN);
                        } catch (GoogleAuthException authEx) {
                            Log.e(TAG, authEx.toString());
                        }

                        return token;
                    }

                    @Override
                    protected void onPostExecute(String token) {
                        Log.i(TAG, "Access token retrieved:" + token);

                        g_login("google",token);



                    }

                };
                task.execute();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        int statusCode = result.getStatus().getStatusCode();
        if (result.isSuccess() && result.getSignInAccount() != null) {
            // Signed in successfully
            // Signed in successfully, show authenticated UI.



            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            Log.d(TAG, "handleSignInResult2: "+acct.getIdToken());



            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();
            String authCode = acct.getServerAuthCode();
            Log.d(TAG, "handleSignInResult2: "+acct.getIdToken());

           //accessToken:ya29.Gl...



            String token = acct.getIdToken();
//            String authCode = acct.getServerAuthCode();


                g_login("google",idToken);
//            Account mAccount = AccountManager.get(this).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)[0];
//
//            new RetrieveTokenTask().execute(mAccount.name);




//            Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
//            startActivity(intent);





        }
//
//
//
    }



//    private String handleSignInResult(GoogleSignInResult result) {
//        //If the login succeed
//        if (result.isSuccess()) {
//            //Getting google account
//            final GoogleSignInAccount acct = result.getSignInAccount();
//
//            //Displaying name and email
//            String name = acct.getDisplayName();
//            final String mail = acct.getEmail();
//            // String photourl = acct.getPhotoUrl().toString();
//
//            final String givenname = "", familyname = "", displayname = "", birthday = "";
//
//
//            String accountName = Plus.AccountApi.getAccountName(mGoogleApiClient);
//            String Token = Plus.AccountApi.getAccountName(mGoogleApiClient);
//            System.out.print(accountName);
//
//             Account mAccount = AccountManager.get(this).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE)[0];
//             final String SCOPE = "audience:server:client_id:" +R.string.server_client_id;
//            try {
//                String token = GoogleAuthUtil.getToken(Login2.this, mAccount.name, SCOPE);
//                Toast.makeText(getApplicationContext(),"hii",Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),token.toString(),Toast.LENGTH_LONG).show();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (GoogleAuthException e) {
//                e.printStackTrace();
//            }
//
//
//
//
////            String accessToken = "";
////            try {
////                URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo");
////                // get Access Token with Scopes.PLUS_PROFILE
////                String sAccessToken;
////                sAccessToken = GoogleAuthUtil.getToken(
////                        Login2.this,
////                        accountName + "",
////                        "oauth2:"
////                                + Scopes.PLUS_LOGIN + " "
////                                + "https://www.googleapis.com/auth/plus.login" + " "
////                                + "https://www.googleapis.com/auth/plus.profile.emails.read");
////                Toast.makeText(getApplicationContext(),sAccessToken,Toast.LENGTH_LONG).show();
////            } catch (UserRecoverableAuthException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////                Intent recover = e.getIntent();
////                startActivityForResult(recover, 125);
////                return "";
////            } catch (IOException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            } catch (GoogleAuthException e) {
////                // TODO Auto-generated catch block
////                e.printStackTrace();
////            }
//
//
//        }
//
//        return null;
//    }



    private void loginUser(final String username, final String password) {


        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Please wait a moment");
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        globaldata globaldata = ((globaldata) this.getApplication());
       String verify_logout =  globaldata.splashPersonalDetails.verify_logout.toString();
        if (!verify_logout.equals("")) {
            if (verify_logout.equals("personal")) {

                Toast.makeText(getApplicationContext(), "Please Complete Registration Steps", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashPersonalDetails.class);
                intent.putExtra("logintype","loginuser");
                startActivity(intent);

            }
          else  if (varify_profile.equals("location")) {
                Toast.makeText(getApplicationContext(), "Please Complete Registration Steps", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashLocationDetails.class);
                intent.putExtra("logintype", "loginuser");
                startActivity(intent);
            }

            else  if (varify_profile.equals("education")) {
                Toast.makeText(getApplicationContext(), "Please Complete Registration Steps", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashEducationCarrier.class);
                intent.putExtra("logintype", "loginuser");
                startActivity(intent);
            }

            else  if (varify_profile.equals("complete")) {

                Toast.makeText(getApplicationContext(), "Please Complete Registration Steps", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), SplashCompleteProfile.class);
                intent.putExtra("logintype", "loginuser");
                startActivity(intent);
            }

        } else {

            if (editTextUsername.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please Enter Your Name !", Toast.LENGTH_LONG).show();

            } else if (editTextUsername.getText().toString().matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "enter your name !", Toast.LENGTH_LONG).show();
            } else if (editTextPassword.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "Password can't be blank !", Toast.LENGTH_LONG).show();
            } else if (editTextPassword.getText().length() < 6) {
                Toast.makeText(getApplicationContext(), "Minimum 6 digits of Password!", Toast.LENGTH_LONG).show();

            } else if (editTextPassword.getText().length() > 15) {
                Toast.makeText(getApplicationContext(), "Not more than 15 digits!", Toast.LENGTH_LONG).show();
            } else {

                user = editTextUsername.getText().toString();
                pass = editTextPassword.getText().toString();

                showDialog();
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        URL_FOR_LOGIN, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Register Response: " + response.toString());



                        try {
                            JSONObject jarray = new JSONObject(response);

                            JSONArray jUser = jarray.getJSONArray("user");
                            System.out.print(jUser);
                            JSONObject c = jUser.getJSONObject(0);
                            String userid = c.getString("UserProfileId");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("user_id", userid);
                            editor.putString("password",password);

                            editor.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            String emsg;
                            boolean error = jObj.getBoolean("error");
                            emsg = jObj.getString("message");
                            String status = "";
                            if (!emsg.equals("Invalid username or password")) {
                                status = jObj.getString("status");
                            }


                            if (!error) {

                                String message = jObj.getString("message");

                                if (!message.equals("Login Successfull!")) {
                                    String user = jObj.getString("user");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("user_id", user);
                                    editor.commit();
                                }


                                SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                                channel = (shared.getString("user_id", ""));


                                if (status.equals("2")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Verify your OTP here!", Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(Login2.this, SplashCompleteProfile.class));

                                } else {
                                    String  channeld="", id="";
                                    Intent logincheck = getIntent();
                                    String loginfrom = logincheck.getStringExtra("loginfrom");

                                    SharedPreferences sharesd = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                                    channeld = (sharesd.getString("basicdetail", ""));
                                    id = (sharesd.getString("id", ""));
                                     if (getIntent().getStringExtra("loginfrom") == null){
                                         chat(user,pass);

                                         SharedPreferences.Editor editor = sharedpreferences.edit();
                                         editor.putString("logintype", "simplelogin");
                                         editor.commit();
                                         startActivity(new Intent(Login2.this, TryFragmentActivity.class));
                                         Toast.makeText(Login2.this, "Logged in!", Toast.LENGTH_LONG).show();
                                     }
                                     else {
                                         chat(user,pass);

                                         SharedPreferences.Editor editogr = sharedpConstant.edit();
                                         editogr.putString("basicdetail", "basicdetail");
                                         editogr.putString("id", id);
                                         editogr.commit();
                                         Intent intent_basiclogin = getIntent();
                                        String basic_gender = intent_basiclogin.getStringExtra("Gender");
                                      String   basic_agefrom = intent_basiclogin.getStringExtra("agefrom");
                                      String   basic_ageto = intent_basiclogin.getStringExtra("ageto");
                                      String   basic_heightfrom = intent_basiclogin.getStringExtra("heightfrom");
                                      String   basic_heightto = intent_basiclogin.getStringExtra("heightto");
                                      String   basic_riligion = intent_basiclogin.getStringExtra("riligion");
                                      String   basic_occupation = intent_basiclogin.getStringExtra("occupation");
                                      String   json = intent_basiclogin.getStringExtra("json");
                                         SharedPreferences.Editor editor = sharedpreferences.edit();
                                         editor.putString("logintype", "simplelogin");
                                         editor.commit();

                                         Intent intent = new Intent(Login2.this,BasicDetail.class);
                                         intent.putExtra("id",id);
                                         intent.putExtra("Gender",basic_gender);
                                         intent.putExtra("agefrom",basic_agefrom);
                                         intent.putExtra("ageto",basic_ageto);
                                         intent.putExtra("heightfrom",basic_heightfrom);
                                         intent.putExtra("heightto",basic_heightto);
                                         intent.putExtra("riligion",basic_riligion);
                                         intent.putExtra("occupation",basic_occupation);
                                         intent.putExtra("json",json);
//                                         intent.putExtra("gender",gender);
                                         intent.putExtra("type","viewprofile");
                                         startActivity(intent);

//                                         SharedPreferences.Editor editodr = sharedpConstant.edit();
//                                         editodr.putString("session_status", "logout");
//                                         editor.putString("basicdetail", "");
//                                         editodr.commit();

                                         Toast.makeText(Login2.this, "Logged in!", Toast.LENGTH_LONG).show();
                                     }


//                                    chat(user, pass);
                                }


                            } else {
                                if (emsg.equals("Invalid username or password")) {

                                    Toast.makeText(getApplicationContext(),
                                            "Invalid username or password", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Username and password dosen't match  ", Toast.LENGTH_LONG).show();

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.print(e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", username);
                        params.put("password", password);
                        return params;
                    }
                };

                strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
            }

        }
    }


    private void chat(final String usernamereg,final String passwordreg){

//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//        editor.putString("age", String.valueOf(age));
//        editor.commit();
        final ProgressDialog pd = new ProgressDialog(Login2.this);
        pd.setMessage("Please wait a moment");
        pd.show();


        String url = "https://nayarishta-app-8d5f4.firebaseio.com/users.json";


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Firebase reference = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/users");

                JSONObject obj = null;
                try {
                    obj = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!obj.has(usernamereg)) {

                            reference.child(usernamereg).child("password").setValue(passwordreg);


                        } else {
//                            Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();

                }

                pd.dismiss();
            }

        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError );
                pd.dismiss();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(Login2.this);
        rQueue.add(request);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    public void register(View a) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    public void click(View a) {

//        if (basicdetails.toString().equals("basic"))
//        {
//            finish();
//        }
//        else {
            Intent intent = new Intent(this, Splash.class);
            startActivity(intent);
//        }

    }


    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void forgotpassword(View a) {
        Intent intent = new Intent(this, Chat.class);
        startActivity(intent);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}