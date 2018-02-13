package com.villupuram.nayarishta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;


public class MyAlbum extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String channel="";
    public static String USERNAME="";

    private static final String TAG = MainActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private try_two.GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL = "http://stacktips.com/?json=get_recent_posts&count=45";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_album);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setItemIconTintList(null);



//        SharedPreferences shared = getSharedPreferences(MYUSER_NAME, MODE_PRIVATE);
//        USERNAME = (shared.getString("USERNAME", ""));
//

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");

        View header=navigationView.getHeaderView(0);
/*ViewSome view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        TextView uname = (TextView)header.findViewById(R.id.header);
        uname.setText(name);



        // Locate GridView in listview_main.xml
        GridView gridview = (GridView) findViewById(R.id.grid_view);

        // Set the ImageAdapter into GridView Adapter

//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, ViewSome view,
//                                    int position, long id) {
//                Toast.makeText(MyAlbum.this, "You Clicked ", Toast.LENGTH_SHORT).show();
//
//            }
//        });






        RelativeLayout relativeclic1 =(RelativeLayout)findViewById(R.id.go_profile);
        RelativeLayout relativeclic2 =(RelativeLayout)findViewById(R.id.go_ps);


        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(MyAlbum.this,MyProfile.class), 0);
            }
        });



        relativeclic2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(MyAlbum.this,PartnerSetting.class), 0);
            }
        });


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
            Intent it=new Intent(MyAlbum.this,MyProfile.class);
            startActivity(it);
        } else if (id == R.id.compose) {


        } else if (id == R.id.inbox) {


        } else if (id == R.id.sent) {

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
            Intent it=new Intent(MyAlbum.this,About.class);
            startActivity(it);

        } else if (id == R.id.membership) {
            Intent it=new Intent(MyAlbum.this,MemberShip_Main.class);
            startActivity(it);

        }else if (id == R.id.contact) {

            Intent it=new Intent(MyAlbum.this,Contact.class);
            startActivity(it);

        }else if (id == R.id.logout) {
            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            channel = (shared.getString("user_id", ""));


            Intent it=new Intent(MyAlbum.this,Splash.class);
            channel="";
            it.putExtra("logout","hello");
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
