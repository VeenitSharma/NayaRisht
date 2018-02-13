package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;

public class MyProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MyPREFERENCESs = "inbox" ;
    private static final String TAG = "MyProfile";
    private static String URL_FOR_SEARCH = "";
    private static String URL_FOR_DETAIL = "";
    ProgressDialog progressDialog;
    public static String channel="";
    public static String USERNAME="";
    SharedPreferences sharedpreferencestwo;
    public static final String MYUSER_NAME = "MY_USER" ;
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


//        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sharedpreferences = getSharedPreferences(MyPREFERENCESs, Context.MODE_PRIVATE);

        navigationView.setItemIconTintList(null);




        TextView t = (TextView) findViewById(R.id.edit);


        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        RelativeLayout relativeclic1 = (RelativeLayout) findViewById(R.id.go_album);
        RelativeLayout relativeclic2 = (RelativeLayout) findViewById(R.id.go_ps);

        relativeclic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfile.this, MyAlbum.class);
                intent.putExtra("name", USERNAME);
                startActivity(intent);
            }
        });

        relativeclic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MyProfile.this, PartnerSetting.class), 0);
            }
        });


        loginUser();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = base_api_url.USER_DETAIL + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "MyProfile Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("user");
                    System.out.print(jUser);
                    JSONObject c = jUser.getJSONObject(0);
                    String userid = c.getString("profileid");
                    String Name = c.getString("firstname") +" "+c.getString("lastname");
                    String profilecreatedfor =  c.getString("profilecreatedfor");
                    String dob =  c.getString("day")+ "-"+c.getString("month")+"-"+c.getString("year");
                    String height =  c.getString("feet")+ "'"+c.getString("inch")+"''("+c.getString("cm")+")";
                    String maritalstatus =  c.getString("maritalstatus");
//                    String havechildren =  c.getString("havechildren");


                    TextView profileText = (TextView) findViewById(R.id.ptext);
                    TextView dateText = (TextView) findViewById(R.id.dtext);
                    TextView username = (TextView) findViewById(R.id.utext);
                    TextView profileId = (TextView) findViewById(R.id.pidtext);
                    TextView heighttext = (TextView) findViewById(R.id.htext);
                    TextView m_status = (TextView) findViewById(R.id.mtext);
//                    TextView no_children = (TextView) findViewById(R.id.no_children);

                    USERNAME = Name;
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    editor.putString("username", USERNAME);
                    editor.commit();




                    TextView header = (TextView)findViewById(R.id.header);



                    profileId.setText(userid);
                    username.setText(Name);
                    profileText.setText(profilecreatedfor);
                    dateText.setText(dob);
                    heighttext.setText(height);
                    m_status.setText(maritalstatus);
//                    no_children.setText(havechildren);


                    header.setText(Name);



//
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
//                params.put("profileid", profileid);
//                params.put("firstname", firstname);
//                params.put("lastname", lastname);
//                return params;
//            }
//        };
        // Adding request to request queue
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











    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent it=new Intent(MyProfile.this,MyProfile.class);
            startActivity(it);
        } else if (id == R.id.compose) {


        } else if (id == R.id.inbox) {
            Intent it=new Intent(MyProfile.this,Inbox.class);
            startActivity(it);

        } else if (id == R.id.sent) {
            Intent it=new Intent(MyProfile.this,Sent.class);
            startActivity(it);

        } else if (id == R.id.drafts) {

        }else if (id == R.id.isend) {

        } else if (id == R.id.irecieved) {

        } else if (id == R.id.yaccept) {

        } else if (id == R.id.accepty) {

        } else if (id == R.id.ydecline) {

        }else if (id == R.id.decliney) {

        }
//        else if (id == R.id.for_accept) {
//
//        } else if (id == R.id.to_accept) {
//
//        }
        else if (id == R.id.about) {
            Intent it=new Intent(MyProfile.this,About.class);
            startActivity(it);

        } else if (id == R.id.membership) {
            Intent it=new Intent(MyProfile.this,MemberShip_Main.class);
            startActivity(it);

        }else if (id == R.id.contact) {

            Intent it=new Intent(MyProfile.this,Contact.class);
            startActivity(it);

        }else if (id == R.id.logout) {


            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            channel = (shared.getString("user_id", ""));


            Intent it=new Intent(MyProfile.this,Splash.class);
            channel="";
            it.putExtra("logout","hello");
            startActivity(it);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

