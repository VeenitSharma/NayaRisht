package com.villupuram.nayarishta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;


public class PartnerSetting extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RadioGroup radioGroup1;
    public static String channel="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_setting);


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

        RelativeLayout relativeclic6 =(RelativeLayout)findViewById(R.id.go_album);
        RelativeLayout relativeclic7 =(RelativeLayout)findViewById(R.id.go_profile);

        final RadioButton btn1 = (RadioButton)findViewById(R.id.bridebtn);
        final RadioButton btn2 = (RadioButton)findViewById(R.id.groombtn);


        relativeclic6.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(PartnerSetting.this,MyAlbum.class), 0);
            }
        });



        relativeclic7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivityForResult(new Intent(PartnerSetting.this,MyProfile.class), 0);
            }
        });



        //radio Btn
        radioGroup1 = (RadioGroup) findViewById(R.id.radiobtngrp);

        // Checked change Listener for RadioGroup 1
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                switch (checkedId)
                {
                    case R.id.bridebtn:

                        btn1.setBackgroundResource(R.drawable.profile_setting_check_circle);
                        btn2.setBackgroundResource(R.drawable.profile_setting_uncheck_cricle);

                        break;
                    case R.id.groombtn:

                        btn1.setBackgroundResource(R.drawable.profile_setting_uncheck_cricle);
                        btn2.setBackgroundResource(R.drawable.profile_setting_check_circle);

                        break;
                    default:
                        break;
                }
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
            Intent it=new Intent(PartnerSetting.this,MyProfile.class);
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

            Intent it=new Intent(PartnerSetting.this,About.class);
            startActivity(it);
        } else if (id == R.id.membership) {

            Intent it=new Intent(PartnerSetting.this,MemberShip_Main.class);
            startActivity(it);
        }else if (id == R.id.contact) {
            Intent it=new Intent(PartnerSetting.this,Contact.class);
            startActivity(it);

        }else if (id == R.id.logout) {
           SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
           channel = (shared.getString("user_id", ""));


           Intent it=new Intent(PartnerSetting.this,Splash.class);
           channel="";
           it.putExtra("logout","hello");
           startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
