package com.villupuram.nayarishta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
//
//import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
////import static com.villupuram.nayarishta.Login2.userid;



public class Splash extends AppCompatActivity {



    Context mContext;
    SharedPreferences sharedpreferences;
    public static String channel="";
    String fontPath = "fonts/GOTHAM-BLACK.TTF";


    public void login(View a){
        Intent it= new Intent(this,Login2.class);
        startActivity(it);
    }

    public void register(View a){
        Intent it= new Intent(this,Register.class);
        startActivity(it);
    }

//
//public void gotoLogin(ViewSome view){
//    Intent it= new Intent(this,Login.class);
//    startActivity(it);
//}
//    public void gotoRegister(ViewSome view){
//        Intent it= new Intent(this,Register.class);
//        startActivity(it);
//    }
//    public void gotoSearch(ViewSome view){
//        Intent it= new Intent(this,Search.class);
//        startActivity(it);
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.villupuram.nayarishta",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }

        TextView txtGhost = (TextView) findViewById(R.id.makertext);
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        // Applying font
        txtGhost.setTypeface(tf);
//        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String logout = getIntent().getStringExtra("logout");

        SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
        channel = (shared.getString("session_status", ""));


        if (!channel.equals("")){


            Intent it = new Intent(Splash.this, TryFragmentActivity.class);
                startActivity(it);
                finish();

        }



//        if (logout=="") {
//
//            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//
//
//            channel = (shared.getString("user_id", ""));
//
//
//            if (!channel.equals("")) {
//
//
//                Intent it = new Intent(Splash.this, TryFragmentActivity.class);
//                startActivity(it);
//                finish();
////
//            }
//        }
////        else {
//
//            Intent it= new Intent(Splash.this, Splash.class);
//            startActivity(it);
//            finish();
//        }
        /*else {
            Intent it = new Intent(Splash.this, Splash.class);
            startActivity(it);
            finish();

        }*/
//        if (channel !="") {
//            Intent it= new Intent(Splash.this, Dashboard.class);
//            startActivity(it);
//
//        }
//
//        else {
//            Intent it = new Intent(Splash.this, Splash.class);
//            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(it);
//           // finish();
//
//        }



        RelativeLayout dsd = (RelativeLayout) findViewById(R.id.searchlayout);

        dsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it= new Intent(Splash.this, Search.class);
                startActivity(it);
            }
        });
//

        }
//





    }

