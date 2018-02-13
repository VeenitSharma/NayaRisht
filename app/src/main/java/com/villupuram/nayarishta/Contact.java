package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

public class Contact extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "Contact";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/contact-us.php";
    ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    public static String disname="";
    EditText fullname, emailt,sub,message,captchaText;
    private static String URL_FOR_DETAIL = "";
    ImageView captchaImage;
    private FirebaseAuth mAuth;
    TextCaptcha textCaptcha = new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.LETTERS_ONLY);
    TextView referececaptcha;
    private DatabaseReference mDatabase;
    String username;
    SharedPreferences sharedpConstant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);



        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        SharedPreferences shared = getSharedPreferences(Tryfragment, MODE_PRIVATE);
        disname=(shared.getString("username",""));



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        fullname = (EditText)findViewById(R.id.fullname);
        emailt   =(EditText)findViewById(R.id.email);
        sub=(EditText)findViewById(R.id.sub);
        message= (EditText)findViewById(R.id.msgtext);
        captchaText= (EditText)findViewById(R.id.captchatext);
        captchaImage = (ImageView)findViewById(R.id.captchaimg);

        referececaptcha = (TextView)findViewById(R.id.referece);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        captchaImage.setImageBitmap(textCaptcha.getImage());
        String username= disname;
        if (bydefaultstatus==0){
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_dashboard_drawer);
//            navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
        }

        referececaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextCaptcha textCaptcha = new TextCaptcha(600, 150, 4, TextCaptcha.TextOptions.LETTERS_ONLY);
                captchaImage.setImageBitmap(textCaptcha.getImage());

            }
        });



        if (bydefaultstatus==0){
            View hView =  navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
            TextView header = (TextView)hView.findViewById(R.id.header);
            header.setText(username);
            loginUser();
        }
        navigationView.setNavigationItemSelectedListener(this);


        Button submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname= fullname.getText().toString();
                String emailtext = emailt.getText().toString();
                String subtext= sub.getText().toString();
                String msgtext = message.getText().toString();
                registerUser(fname,emailtext,subtext,msgtext);



            }
        });
    }



    private void loginUser() {


        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = base_api_url.USER_DETAIL + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("user");
                    System.out.print(jUser);
                    JSONObject c = jUser.getJSONObject(0);

                    String photourl = c.getString("userphoto");
                    String gender = c.getString("gender");
                    username= c.getString("username");
                    PhotoUrl = photourl;

                    ImageView slideimg=(ImageView)findViewById(R.id.user_image_slide);

                    if (!photourl.equals("")){
                        Picasso.with(getApplicationContext()).load(photourl).resize(400,400).into(slideimg);
                    }else {
                        if (gender.toString().equals("F")){
                            slideimg.setImageResource(R.drawable.femaledefault);

                        }
                        else {
                            slideimg.setImageResource(R.drawable.mandefault);

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
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
//
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void registerUser(final String name,  final String email, final String subject,final String messages) {
        // Tag used to cancel the request
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (fullname.getText().toString().equals("")) {
            Toast.makeText(Contact.this, "Please fill Your Name!", Toast.LENGTH_LONG).show();
        } else if (emailt.getText().toString().equals("")) {
            Toast.makeText(Contact.this, "Please fill Email!", Toast.LENGTH_LONG).show();
        } else if (!emailt.getText().toString().matches(emailPattern)) {
            Toast.makeText(Contact.this, "Please fill valid Email !", Toast.LENGTH_LONG).show();
        } else if (sub.getText().toString().equals("")) {
            Toast.makeText(Contact.this, "Please Specify Subject!", Toast.LENGTH_LONG).show();
        } else if (message.getText().toString().equals("")) {
            Toast.makeText(Contact.this, "Please Enter Your Message!", Toast.LENGTH_LONG).show();
        } else if (!textCaptcha.checkAnswer(captchaText.getText().toString().trim())) {
            Toast.makeText(Contact.this, "Captcha is not match", Toast.LENGTH_LONG).show();
        }
        else

            {
            captchaText.getText().clear();

            String cancel_req_tag = "register";
            progressDialog.setMessage("Sending ...");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_FOR_LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);

                        boolean error = jObj.getBoolean("error");



                        if (!error) {

                            ((EditText) findViewById(R.id.msgtext)).getText().clear();
                            ((EditText) findViewById(R.id.sub)).getText().clear();
                            ((EditText) findViewById(R.id.fullname)).getText().clear();
                            ((EditText) findViewById(R.id.email)).getText().clear();



//                            Intent intent = new Intent(getApplicationContext(), SplashPersonalDetails.class);
//                            startActivity(intent);
//                            finish();
                            String msg = jObj.getString("message");
                            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                        } else {

                            String errorMsg = jObj.getString("message");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    // Posting params to register url
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("name", name);
                    params.put("email", email);
                    params.put("subject", subject);
                    params.put("message", messages);
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
//    }

    private void showDialog() {
    if (!progressDialog.isShowing())
        progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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


    public void checklogin() {
        if (bydefaultstatus == 1) {
            Intent it = new Intent(Contact.this, Splash.class);
            startActivity(it);
        }

        else if (globallogincheck == 0) {
            Intent it = new Intent(Contact.this, TryFragmentActivity.class);
            startActivity(it);
        } else if (globallogincheck == 1) {
            Intent it = new Intent(Contact.this, Splash.class);
            startActivity(it);
        }
    }

    public void redirectsearch() {
        if (bydefaultstatus == 1) {
            Intent it = new Intent(Contact.this, Search.class);
            startActivity(it);
        }

        else if (globallogincheck == 0) {
            Intent it = new Intent(Contact.this, SearchClick.class);
            startActivity(it);
        } else if (globallogincheck == 1) {
            Intent it = new Intent(Contact.this, Search.class);
            startActivity(it);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent it = new Intent(Contact.this, Login2.class);
                        startActivity(it);
                    }
                });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (bydefaultstatus==0){
            if (id == R.id.profile) {
                Intent it=new Intent(Contact.this,TryFragmentActivity.class);
                startActivity(it);
            }
            if (id == R.id.dashboard) {
                Intent it=new Intent(Contact.this,Dashboard.class);
                startActivity(it);
            }
            else if (id == R.id.search) {
                Intent it=new Intent(Contact.this,SearchClick.class);
                startActivity(it);
            }else if (id == R.id.compose) {
                Intent it=new Intent(Contact.this,Compose.class);
                startActivity(it);
            }
            else if (id == R.id.Messenger) {
                Intent it=new Intent(Contact.this,User.class);
                startActivity(it);
            }
            else if (id == R.id.inbox) {
                Intent it=new Intent(Contact.this,Inbox.class);
                startActivity(it);


            } else if (id == R.id.sent) {
                Intent it=new Intent(Contact.this,Sent.class);
                startActivity(it);

            } else if (id == R.id.drafts) {
                Intent it=new Intent(Contact.this,Drafts.class);
                startActivity(it);

            }else if (id == R.id.isend) {
                Intent it=new Intent(Contact.this,InterestSend.class);
                startActivity(it);

            } else if (id == R.id.irecieved) {
                Intent it=new Intent(Contact.this,InterestRecieved.class);
                startActivity(it);

            } else if (id == R.id.yaccept) {
                Intent it=new Intent(Contact.this,Yaccepted.class);
                startActivity(it);

            } else if (id == R.id.accepty) {
                Intent it=new Intent(Contact.this,AcceptedY.class);
                startActivity(it);


            } else if (id == R.id.ydecline) {
                Intent it=new Intent(Contact.this,YDeclined.class);
                startActivity(it);

            }else if (id == R.id.decliney) {
                Intent it=new Intent(Contact.this,DeclinedY.class);
                startActivity(it);
            }
//            else if (id == R.id.for_accept)
//            {
//                Intent it=new Intent(Contact.this,ForAccepted.class);
//                startActivity(it);
//            }
//
//            else if (id == R.id.to_accept) {
//                Intent it=new Intent(Contact.this,ToAccept.class);
//                startActivity(it);
//
//            }
            else if (id == R.id.about) {
                Intent it=new Intent(Contact.this,About.class);
                startActivity(it);

            } else if (id == R.id.membership) {
                Intent it=new Intent(Contact.this,MemberShip_Main.class);
                startActivity(it);

            }else if (id == R.id.change) {
                Intent it=new Intent(Contact.this,ChangePassword.class);
                startActivity(it);
            }else if (id == R.id.contact) {

                Intent it=new Intent(Contact.this,Contact.class);
                startActivity(it);

            }else if (id == R.id.logout) {

                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                writeNewUser(username);
                signOut();
                bydefaultstatus=0;
                globallogincheck=1;
                Intent it=new Intent(Contact.this,Splash.class);
                it.putExtra("logout","hello");
                startActivity(it);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                    signOut();
                } else {
                    // not signed in. Show the "sign in" button and explanation.
                    // ...
                    Intent ijt=new Intent(Contact.this,Login2.class);
                    ijt.putExtra("logout","hello");
                    startActivity(ijt);


                    SharedPreferences.Editor editor = sharedpConstant.edit();
                    editor.putString("session_status", "");
                    editor.commit();

                }
                SharedPreferences.Editor editor = sharedpConstant.edit();
                editor.putString("session_status", "");
                editor.commit();

            }
        }else {
            if (id == R.id.nav_camera) {
                // Handle the camera action

                checklogin();
//
//            Intent it=new Intent(About.this,Splash.class);
//            startActivity(it);
            } else if (id == R.id.nav_gallery) {
                Intent it=new Intent(Contact.this,About.class);
                startActivity(it);
            } else if (id == R.id.nav_slideshow) {
                Intent it=new Intent(Contact.this,MemberShip_Main.class);
                startActivity(it);

            } else if (id == R.id.nav_manage) {
                Intent it=new Intent(Contact.this,Contact.class);
                startActivity(it);
            } else if (id == R.id.nav_manage1) {

                redirectsearch();

//            Intent it=new Intent(About.this,Dashboard.class);
//            startActivity(it);
            }
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void writeNewUser(String userName) {
        mDatabase.child("users").child(userName).removeValue();
        mDatabase.child("users").child(userName).child("password").removeValue();

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

