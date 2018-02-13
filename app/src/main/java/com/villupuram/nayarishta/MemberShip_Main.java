package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
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

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

public class MemberShip_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MemberShip_Main";
    private ViewPager _mViewPager;
    private ViewPagerAdapter _adapter;
    private Button _btn1, _btn2;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    Button loginbtn;
    private GoogleApiClient mGoogleApiClient;
    public static String disname="";
    ProgressDialog progressDialog;
    private static String URL_FOR_DETAIL = "";
    private FirebaseAuth mAuth;
    public static String channel = "";
    /**
     * Called when the activity is first created.
     */
    SharedPreferences sharedpConstant;

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_ship__main);
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences shared = getSharedPreferences(Tryfragment, MODE_PRIVATE);
        disname=(shared.getString("username",""));
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (bydefaultstatus==1){
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_search_results_drawer);
//            navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
        }

        if (bydefaultstatus==0){
            navigationView.inflateMenu(R.menu.activity_dashboard_drawer);
            View hView =  navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
            TextView header = (TextView)hView.findViewById(R.id.header);
            header.setText(disname);
            loginUser();
        }

        navigationView.setNavigationItemSelectedListener(this);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);



        // layouts of all welcome sliders
        // add few more layouts if you want
        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                };



        loginbtn = (Button)findViewById(R.id.loginbutton);

        // adding bottom dots
        addBottomDots(0);

        // making notification bar transparent

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);



    }

    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("wait..");

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

                    PhotoUrl = photourl;

                    ImageView slideimg=(ImageView)findViewById(R.id.user_image_slide);

                    if (!photourl.equals("")){
                        Picasso.with(getApplicationContext()).load(photourl).resize(400,400).into(slideimg);
                    } else {
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



    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dots[i].setTextColor(colorsInactive[currentPage]);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    };


    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        startActivity(new Intent(MemberShip_Main.this, MemberShip_Main.class));
        finish();
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Making notification bar transparent
     */


    /**
     * ViewSome pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);

            switch (position) {
                case 0:
                    view = layoutInflater.inflate(R.layout.welcome_slide1, null);
                    Button bb = (Button) view.findViewById(R.id.loginbutton);
                    Button continuebtn = (Button) view.findViewById(R.id.continuebtn);

                    if (bydefaultstatus==0){
                        continuebtn.setVisibility(View.VISIBLE);
                        bb.setVisibility(View.GONE);
                    }

                    bb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       Intent intent = new Intent(getApplicationContext(),Login2.class);
                        startActivity(intent);
                        }
                    });

                    break;

            }
            container.addView(view);
            

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void checklogin(){
        if (bydefaultstatus==1){
            Intent it=new Intent(MemberShip_Main.this,Splash.class);
            startActivity(it);
        }

        else if (globallogincheck==0){
            Intent it=new Intent(MemberShip_Main.this,TryFragmentActivity.class);
            startActivity(it);
        }
        else if(globallogincheck==1){
            Intent it=new Intent(MemberShip_Main.this,Splash.class);
            startActivity(it);
        }
    }

    public void redirectsearch(){

        if (bydefaultstatus==1){
            Intent it=new Intent(MemberShip_Main.this,Search.class);
            startActivity(it);
        }

       else if (globallogincheck==0){
            Intent it=new Intent(MemberShip_Main.this,SearchClick.class);
            startActivity(it);
        }
        else if(globallogincheck==1){
            Intent it=new Intent(MemberShip_Main.this,Search.class);
            startActivity(it);
        }
    }



    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent it=new Intent(MemberShip_Main.this,Login2.class);
                        startActivity(it);
                    }
                });
    }


    private void writeNewUser(String userName) {
        mDatabase.child("users").child(userName).removeValue();
        mDatabase.child("users").child(userName).child("password").removeValue();

    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (bydefaultstatus==0){
            if (id == R.id.profile) {
                Intent it=new Intent(MemberShip_Main.this,TryFragmentActivity.class);
                startActivity(it);
            }
            if (id == R.id.dashboard) {
                Intent it=new Intent(MemberShip_Main.this,Dashboard.class);
                startActivity(it);
            }

            else if (id == R.id.search) {
                Intent it=new Intent(MemberShip_Main.this,SearchClick.class);
                startActivity(it);
            }else if (id == R.id.compose) {
                Intent it=new Intent(MemberShip_Main.this,Compose.class);
                startActivity(it);
            }

            else if (id == R.id.Messenger) {
                Intent it=new Intent(MemberShip_Main.this,User.class);
                startActivity(it);
            }

            else if (id == R.id.inbox) {
                Intent it=new Intent(MemberShip_Main.this,Inbox.class);
                startActivity(it);


            } else if (id == R.id.sent) {
                Intent it=new Intent(MemberShip_Main.this,Sent.class);
                startActivity(it);

            } else if (id == R.id.drafts) {
                Intent it=new Intent(MemberShip_Main.this,Drafts.class);
                startActivity(it);

            }else if (id == R.id.isend) {
                Intent it=new Intent(MemberShip_Main.this,InterestSend.class);
                startActivity(it);

            } else if (id == R.id.irecieved) {
                Intent it=new Intent(MemberShip_Main.this,InterestRecieved.class);
                startActivity(it);

            } else if (id == R.id.yaccept) {
                Intent it=new Intent(MemberShip_Main.this,Yaccepted.class);
                startActivity(it);



            } else if (id == R.id.accepty) {
                Intent it=new Intent(MemberShip_Main.this,AcceptedY.class);
                startActivity(it);



            } else if (id == R.id.ydecline) {
                Intent it=new Intent(MemberShip_Main.this,YDeclined.class);
                startActivity(it);

            }else if (id == R.id.decliney) {
                Intent it=new Intent(MemberShip_Main.this,DeclinedY.class);
                startActivity(it);
            }
//            else if (id == R.id.for_accept)
//            {
//                Intent it=new Intent(MemberShip_Main.this,ForAccepted.class);
//                startActivity(it);
//            }
//
//            else if (id == R.id.to_accept) {
//                Intent it=new Intent(MemberShip_Main.this,ToAccept.class);
//                startActivity(it);
//
//            }
            else if (id == R.id.about) {
                Intent it=new Intent(MemberShip_Main.this,About.class);
                startActivity(it);

            } else if (id == R.id.membership) {
                Intent it=new Intent(MemberShip_Main.this,MemberShip_Main.class);
                startActivity(it);

            } else if (id == R.id.change) {
                Intent it=new Intent(MemberShip_Main.this,ChangePassword.class);
                startActivity(it);

            }else if (id == R.id.contact) {

                Intent it=new Intent(MemberShip_Main.this,Contact.class);
                startActivity(it);

            }else if (id == R.id.logout)
            {
                FirebaseAuth.getInstance().signOut();

                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();

                bydefaultstatus=1;

                globallogincheck=1;
                writeNewUser(disname);
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    // signed in. Show the "sign out" button and explanation.
                    // ...
                    signOut();
                } else {
                    // not signed in. Show the "sign in" button and explanation.
                    // ...
                    Intent it=new Intent(MemberShip_Main.this,Login2.class);
                    channel="";
                    it.putExtra("logout","hello");
                    startActivity(it);

                }


                SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (shared.getString("user_id", ""));

                SharedPreferences.Editor editor = sharedpConstant.edit();
                editor.putString("session_status", "");
                editor.commit();


            }
        }else {if (id == R.id.nav_camera) {
            // Handle the camera action
            checklogin();

        } else if (id == R.id.nav_gallery) {
            Intent it=new Intent(MemberShip_Main.this,About.class);
            startActivity(it);
        } else if (id == R.id.nav_slideshow) {
            Intent it=new Intent(MemberShip_Main.this,MemberShip_Main.class);
            startActivity(it);

        } else if (id == R.id.nav_manage) {
            Intent it=new Intent(MemberShip_Main.this,Contact.class);
            startActivity(it);
        } else if (id == R.id.nav_manage1) {

            redirectsearch();
        }}



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public void onClick(View v)
//    {
//        Intent it=new Intent(MemberShip_Main.this,Login2.class);
//        startActivity(it);
//    }

}