package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.Register.UserName;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.basicdetails;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;

public class BasicDetail extends AppCompatActivity{

    private final String TAG = "BasicDetail";


    private static final String BasicDetail = "BasicDetail";
    public static String USERNAME="", ImageName="",profilecreatedfor="",Age="",lastname="",religionname="",gender="",maritalstatus="",dateofbirth="",languagename="",height="",occupationn="",mobile="",maritalstatuss=""
            ,havechildren="",educationname="",branchname="",professionname="",annualincome="",countryname="",statename="",cityname="",residencystatus=""
            ,religionn="",castename="",mothertongue="",isonline="",persninfo="",day="",month="",year="";


    TextView usernamem,useridd, edit,personalinfo,agetext,countrytext,countytext,citytext,resitext,specialcasestext, bodytypetext, complexiontext,educationtext,branchtext,professiontext,annualtext,
            riligioustext,castetext,subcastetext,gotratext,mothertanguetext,mothertext,fathertext,
            totalsistertext,totalmarriedsister,totalbrotest,totalbromarried,famvaluestext,famtypes,aboutfamilytext,mobiletext,landlinetext,conpersontext,
            suitablecalltext,displayoptiontext,diettext,drinktext,smoketext,bloodtext,hobbiestext,interesttext,favoritemusictext,
            readstext,
            moviestext,activitytext,cuisinetext,dresstext;
    ImageView userimage;
    ImageButton interestSend;
    String userid , expid;
    SharedPreferences sharedpreferences;
    private static String URL_FOR_DETAIL = "",idddd="";

    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/interestSend.php";
    private static final String URLFORFAV = "http://nayarishta.com/nayarishta-app/api/addFavorites.php";
    private static final String ACCEPT_URL = "http://nayarishta.com/nayarishta-app/api/acceptinterest.php";
    private static final String DECLINE_URL = "http://nayarishta.com/nayarishta-app/api/declineinterest.php";
    ProgressDialog progressDialog;
    String iddd="";
    ImageButton favbtn;
    String userName;
    ArrayList<String> al = new ArrayList<>();
    Button login;
    public static String channel="";
    LinearLayout Accept_BtnLayout;
    RelativeLayout button_layout, acceptBtn, declineBtn, accepttoast;
    SharedPreferences sharedpConstant;
    String type,VerifyStatus="", basiclogin,basic_gender,basic_agefrom,basic_ageto,basic_heightfrom,basic_heightto,basic_riligion,basic_occupation,json;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.result_letssearch);


        sharedpreferences = getSharedPreferences(BasicDetail, Context.MODE_PRIVATE);
        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);

        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userimage = (ImageView)findViewById(R.id.user_image);
        agetext = (TextView) findViewById(R.id.atext);

        final ImageButton chatbtn = (ImageButton)findViewById(R.id.chatbtn);

        login = (Button)findViewById(R.id.profile_login);
        countrytext = (TextView) findViewById(R.id.ctect);
        countytext = (TextView) findViewById(R.id.county);
        citytext = (TextView) findViewById(R.id.city);
        resitext = (TextView) findViewById(R.id.residency);
        specialcasestext = (TextView) findViewById(R.id.cases);
        bodytypetext = (TextView) findViewById(R.id.bodytype);
        complexiontext = (TextView) findViewById(R.id.complexion);
        educationtext = (TextView) findViewById(R.id.Heducation);
        branchtext = (TextView) findViewById(R.id.branchname);
        professiontext = (TextView) findViewById(R.id.Profession);
        annualtext = (TextView) findViewById(R.id.annualincome);
        riligioustext = (TextView) findViewById(R.id.religous);
        castetext = (TextView) findViewById(R.id.caste);
        subcastetext = (TextView) findViewById(R.id.subcaste);
        gotratext = (TextView) findViewById(R.id.gotra);
        mothertanguetext = (TextView) findViewById(R.id.mtangue);
        mothertext = (TextView) findViewById(R.id.motheris);
        fathertext = (TextView) findViewById(R.id.fatheris);
        totalsistertext = (TextView) findViewById(R.id.totalsister);
        totalmarriedsister = (TextView) findViewById(R.id.totalMarriedsister);
        totalbrotest = (TextView) findViewById(R.id.totalbrother);
        totalbromarried = (TextView) findViewById(R.id.totalMarriedbrother);
        famvaluestext = (TextView) findViewById(R.id.familyvalues);
        famtypes = (TextView) findViewById(R.id.familytype);
        aboutfamilytext = (TextView) findViewById(R.id.aboutfamily);
        mobiletext = (TextView) findViewById(R.id.mobile);
        landlinetext = (TextView) findViewById(R.id.landline);
        conpersontext = (TextView) findViewById(R.id.contactperson);
        suitablecalltext = (TextView) findViewById(R.id.calltime);
        displayoptiontext = (TextView) findViewById(R.id.displayoption);
        diettext = (TextView) findViewById(R.id.diet);
        drinktext = (TextView) findViewById(R.id.drink);
        smoketext = (TextView) findViewById(R.id.smoke);
        bloodtext = (TextView) findViewById(R.id.blood);
        hobbiestext = (TextView) findViewById(R.id.hobbies);
        interesttext = (TextView) findViewById(R.id.interests);
        favoritemusictext = (TextView) findViewById(R.id.music);
        readstext = (TextView) findViewById(R.id.freads);
        moviestext = (TextView) findViewById(R.id.movies);
        activitytext = (TextView) findViewById(R.id.activity);
        cuisinetext = (TextView) findViewById(R.id.Textcuision);
        dresstext = (TextView) findViewById(R.id.dress);
        usernamem = (TextView)findViewById(R.id.username);
        useridd =(TextView)findViewById(R.id.userid);
        favbtn = (ImageButton)findViewById(R.id.favbtn);
        interestSend = (ImageButton)findViewById(R.id.interestsend);
        Accept_BtnLayout=(LinearLayout)findViewById(R.id.accept_btn_layout);
        button_layout = (RelativeLayout)findViewById(R.id.right);
        acceptBtn = (RelativeLayout)findViewById(R.id.accept);
        declineBtn= (RelativeLayout)findViewById(R.id.decline);
        accepttoast=(RelativeLayout)findViewById(R.id.requestaccept);

        Intent intent = getIntent();
         type = intent.getStringExtra("type");

         Intent intent_basiclogin = getIntent();
        basic_gender = intent_basiclogin.getStringExtra("Gender");
        basic_agefrom = intent_basiclogin.getStringExtra("agefrom");
        basic_ageto = intent_basiclogin.getStringExtra("ageto");
        basic_heightfrom = intent_basiclogin.getStringExtra("heightfrom");
        basic_heightto = intent_basiclogin.getStringExtra("heightto");
        basic_riligion = intent_basiclogin.getStringExtra("riligion");
        basic_occupation = intent_basiclogin.getStringExtra("occupation");
        json = intent_basiclogin.getStringExtra("json");
        if (type.toString().equals("advance")||type.toString().equals("quicksearch")||type.toString().equals("dashboard")||type.toString().equals("pid")||type.toString().equals("interest_received")||type.toString().equals("interest_send")||type.toString().equals("viewprofile")
                ||type.toString().equals("yaccept"))
        {
            login.setVisibility(View.GONE);
        }
        else {
            login.setVisibility(View.VISIBLE);
        }
        if (type.toString().equals("yaccept"))
        {
            interestSend.setVisibility(View.GONE);
        }
        SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
        channel = (shared.getString("session_status", ""));
        if (!channel.equals("")){
            login.setVisibility(View.GONE);
        }
        if (type.toString().equals("viewprofile")){
            bydefaultstatus = 0;
              basic_gender = intent_basiclogin.getStringExtra("Gender");
              basic_agefrom = intent_basiclogin.getStringExtra("agefrom");
              basic_ageto = intent_basiclogin.getStringExtra("ageto");
              basic_heightfrom = intent_basiclogin.getStringExtra("heightfrom");
              basic_heightto = intent_basiclogin.getStringExtra("heightto");
              basic_riligion = intent_basiclogin.getStringExtra("riligion");
              basic_occupation = intent_basiclogin.getStringExtra("occupation");
              json = intent_basiclogin.getStringExtra("json");
              login.setVisibility(View.GONE);
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("type")) {
                String typevalue = extras.getString("type", "");
                if (typevalue.equals("interest_send")){
                    interestSend.setVisibility(View.GONE);
                }
                else if (typevalue.equals("interest_received")){
                    button_layout.setVisibility(View.GONE);
                    Accept_BtnLayout.setVisibility(View.VISIBLE);
                }

                // TODO: Do something with the value of isNew.
            }
        }
        personalinfo = (TextView) findViewById(R.id.personalinfo);

        TextView m_status = (TextView) findViewById(R.id.mtext);
        TextView no_children = (TextView) findViewById(R.id.no_childr);
        SharedPreferences preferences =getSharedPreferences(BasicDetail,MODE_PRIVATE);
        String  mstats = preferences.getString("mstatus", "");
        String  ch = preferences.getString("ch", "");

        no_children.setText(ch);
        m_status.setText(mstats);
        loginUser();
        checkstatus();


//        Intent intent = getIntent();
//        String accepttest =intent.getStringExtra("accept");
//
//        if (!accepttest.toString().equals(null)&& accepttest.toString().equals("accept"))
//        {
//            interestSend.setVisibility(View.GONE);
//        }

        SharedPreferences preferencesds = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String uid = (preferencesds.getString("user_id", ""));




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basicdetails="basic";
                Intent intenst = getIntent();
                String idddd=intenst.getStringExtra("id");

                SharedPreferences.Editor editor = sharedpConstant.edit();
                editor.putString("basicdetail", "basicdetail");
                editor.putString("id", idddd);
//                editor.putString("session_status", "logout");

                editor.commit();

                Intent intent = new Intent(getApplicationContext(),Login2.class);
                intent.putExtra("basicdetail","basicdetail");
                intent.putExtra("type","quicksearch");
                intent.putExtra("Gender",basic_gender);
                intent.putExtra("agefrom",basic_agefrom);
                intent.putExtra("ageto",basic_ageto);
                intent.putExtra("heightfrom",basic_heightfrom);
                intent.putExtra("heightto",basic_heightto);
                intent.putExtra("riligion",basic_riligion);
                intent.putExtra("occupation",basic_occupation);
                intent.putExtra("json",json);
                intent.putExtra("loginfrom","basicdetails");
                startActivity(intent);
            }
        });




        interestSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String uid = (preferences.getString("user_id", ""));

                Intent intent = getIntent();
                String idddd=intent.getStringExtra("id");

                SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                channel = (shared.getString("session_status", ""));

                if (!channel.equals("")){
                    registerUser(uid,idddd);
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please logged in to send interest",Toast.LENGTH_LONG).show();

                }


            }
        });

        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.toString().equals("viewprofile")){

                    Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                    intent.putExtra("json", json);
                    intent.putExtra("identity", "search");
                    intent.putExtra("Gender", basic_gender);
                    intent.putExtra("agefrom", basic_agefrom);
                    intent.putExtra("ageto", basic_ageto);
                    intent.putExtra("heightfrom", basic_heightfrom);
                    intent.putExtra("heightto", basic_heightto);
                    intent.putExtra("riligion", basic_riligion);
                    intent.putExtra("occupation", basic_occupation);
                    startActivity(intent);
                }
                else {
                    finish();
                }
            }
        });


        favbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String uid = (preferences.getString("user_id", ""));

                Intent intent = getIntent();
                String idddd=intent.getStringExtra("id");

                SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                channel = (shared.getString("session_status", ""));

                if (!channel.equals("")){
                    makefav(uid,idddd);
                }else {
                    Toast.makeText(getApplicationContext(),"Please logged in to add as favorite",Toast.LENGTH_LONG).show();

                }


            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String uid = (preferences.getString("user_id", ""));

                Intent intent = getIntent();
                String idddd=intent.getStringExtra("id");

                acceptinterest(uid,idddd);
            }
        });
        declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String uid = (preferences.getString("user_id", ""));

                Intent intent = getIntent();
                String idddd=intent.getStringExtra("id");

                declineinterest(uid,idddd);
            }
        });



        String url = "https://nayarishta-app-8d5f4.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(BasicDetail.this);
        rQueue.add(request);



        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> list = new ArrayList<>();

                String[] mStringArray = new String[al.size()];
                mStringArray = al.toArray(mStringArray);

                SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                channel = (shared.getString("session_status", ""));

                if (!channel.equals("")){
//                  if(!Arrays.asList(mStringArray).contains(userName)){
//                        Toast.makeText(getApplicationContext(),"User is not added in firebase database",Toast.LENGTH_LONG).show();
//                    }
                    if (!VerifyStatus.equals("Verified")){
                        Toast.makeText(getApplicationContext(),"Your profile is waiting for Admin approval",Toast.LENGTH_LONG).show();
                    }
                    else {

                        SharedPreferences sharedd = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
                       String disname=(sharedd.getString("username",""));

                        UsersDetails.chatWith = userName;
                        UsersDetails.username = disname;
                        startActivity(new Intent(BasicDetail.this, Chat.class));
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please logged in to send message",Toast.LENGTH_LONG).show();
                }


//                if (Arrays.asList(mStringArray).contains(userName)) {
//
//                    UsersDetails.chatWith = userName;
//                    startActivity(new Intent(BasicDetail.this, Chat.class));
//                    // found a match to "software"
//                }
//                else {
//                    Toast.makeText(getApplicationContext(),"User not found",Toast.LENGTH_LONG).show();
//                }


//
//                for(int i = 0; i < mStringArray.length ; i++){
//                    Log.d("string is",(String)mStringArray[i]);
//
//                    String s =mStringArray[i];
//
//                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//
//                    if (s.equals(userName)){
//
//
//                        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
//
//                        finish();
//                    }
//                }


//                    UsersDetails.chatWith = al.get(i)
//                UsersDetails.chatWith = UsersDetails.username;
//                startActivity(new Intent(BasicDetail.this, Chat.class));
            }
        });

    }
    private void makefav(final String userid, final String favid) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("please wait a moment");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                URLFORFAV, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


////

                        String errorMsg = jObj.getString("message");
                        if (errorMsg.equals("Successfully added favourites!")){
                            favbtn.setBackgroundResource(R.drawable.favicon);
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }
                        else {
                            favbtn.setBackgroundResource(R.drawable.unfav);
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();
                        }


                    } else {

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {

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
                params.put("userid", userid);
                params.put("favid", favid);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void acceptinterest(final String userid, final String profileid) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("please wait a moment");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                ACCEPT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


                        String msg = jObj.getString("message");
                        accepttoast.setVisibility(View.VISIBLE);
                        Accept_BtnLayout.setVisibility(View.GONE);
                        interestSend.setVisibility(View.GONE);
                        button_layout.setVisibility(View.VISIBLE);
                        final Thread thread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        accepttoast.setVisibility(View.GONE);
                                        // Do some stuff
                                    }

                                });

                            }
                        };
                        thread.start();
                        accepttoast.setVisibility(View.GONE);
                    } else {
                        accepttoast.setVisibility(View.VISIBLE);
                        Accept_BtnLayout.setVisibility(View.GONE);
                        interestSend.setVisibility(View.GONE);
                        button_layout.setVisibility(View.VISIBLE);
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {

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
                params.put("userid", userid);
                params.put("profileid", profileid);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    private void declineinterest(final String userid, final String profileid) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("please wait a moment");
        showDialog();



        StringRequest strReq = new StringRequest(Request.Method.POST,
                DECLINE_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {


////

                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                        Accept_BtnLayout.setVisibility(View.GONE);
                        button_layout.setVisibility(View.VISIBLE);

                    } else {

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }}, new Response.ErrorListener() {

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
                params.put("userid", userid);
                params.put("profileid", profileid);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void registerUser(final String userid, final String expressid) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("please wait a moment");
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
////
                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();

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
                params.put("userid", userid);
                params.put("expressid", expressid);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }




    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));
        Intent intent = getIntent();
        String idddd=intent.getStringExtra("id");

        URL_FOR_DETAIL = base_api_url.USER_DETAIL + idddd;



        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);


                    JSONArray jUser = jarray.getJSONArray("user");

                    JSONObject c = jUser.getJSONObject(0);
                    String userid = c.getString("profileid");
                    userName = c.getString("firstname");

                    String Name = c.getString("firstname") +" "+c.getString("lastname");
                    lastname = c.getString("lastname");
                    profilecreatedfor =  c.getString("profilecreatedfor");
                    gender =  c.getString("gender");
                    day = (c.getString("day"));
                    month =(c.getString("month"));
                    int year = Integer.parseInt((c.getString("year")));
                    String dob =  c.getString("day")+ "-"+c.getString("month")+"-"+c.getString("year");
                    dateofbirth=  c.getString("dateofbirth");
                    religionname =  c.getString("religionname");
                    languagename =  c.getString("languagename");
                    mobile =  c.getString("mobile");



                    TextView heighttext = (TextView)findViewById(R.id.htext);

                    if (c.getString("feet").equals("")&&c.getString("inch").equals("")&&c.getString("cm").equals("")){
                        heighttext.setText("None");
                    }
                    else{
                        String heightt =  c.getString("feet")+ "'"+c.getString("inch")+"'-"+c.getString("cm")+"cm";
                        heighttext.setText(heightt);
                    }

                    usernamem.setText(userName);
                    useridd.setText(userid);

//                    height =  c.getString("height");
                    maritalstatuss=  c.getString("maritalstatus");
                    havechildren =  c.getString("havechildren");
                    educationname =  c.getString("educationname");
                    professionname =  c.getString("professionname");
                    annualincome =  c.getString("annualincome");
                    countryname =  c.getString("countryname");
                    statename =  c.getString("statename");
                    cityname =  c.getString("cityname");
                    residencystatus =  c.getString("residencystatus");
                    religionn =  c.getString("religion");
                    mothertongue =  c.getString("mothertongue");
                    persninfo =  c.getString("personalityinfo");

                    if (!c.getString("userphoto").equals("")){
                        Picasso.with(getApplicationContext()).load(c.getString("userphoto")).placeholder(getApplicationContext().getResources().getDrawable(R.drawable.user)).resize(400,400).into(userimage);
                    }
                    else if (c.getString("gender").equals("F")){
                        userimage.setImageResource(R.drawable.femaledefault);
                    }
                    else if (c.getString("gender").equals("M")){
                        userimage.setImageResource(R.drawable.mandefault);
                    }


                    countrytext.setText(c.getString("countryname"));
                    countytext.setText(c.getString("statename"));
                    citytext.setText(c.getString("cityname"));
                    resitext.setText(c.getString("residencystatus"));
                    specialcasestext.setText(c.getString("specialcases"));
                    bodytypetext.setText(c.getString("bodytype"));
                    complexiontext.setText(c.getString("complexion"));
                    educationtext.setText(c.getString("educationname"));
                    branchtext.setText(c.getString("branchname"));
                    professiontext.setText(c.getString("professionname"));
                    annualtext.setText(c.getString("annualincome"));
                    riligioustext.setText(c.getString("religion"));
                    castetext.setText(c.getString("castename"));
                    subcastetext.setText(c.getString("subcastename"));
                    gotratext.setText(c.getString("gotra"));
                    mothertanguetext.setText(c.getString("mothertongue"));
                    mothertext.setText(c.getString("motheris"));
                    fathertext.setText(c.getString("fatheris"));
                    totalsistertext.setText(c.getString("sister"));
                    totalbrotest.setText(c.getString("brothers"));
                    totalmarriedsister.setText(c.getString("sismarried"));
                    totalbromarried.setText(c.getString("bromarried"));
                    famvaluestext.setText(c.getString("familyvalues"));
                    famtypes.setText(c.getString("familytype"));
                    aboutfamilytext.setText(c.getString("aboutmyfamily"));
                    mobiletext.setText(c.getString("mobileisd")+""+""+c.getString("mobilenumber"));
                    landlinetext.setText(c.getString("phoneisd")+""+""+c.getString("phonestd")+""+""+c.getString("phonelandline"));
                    conpersontext.setText(c.getString("contactperson"));
                    suitablecalltext.setText(c.getString("calltime"));
                    displayoptiontext.setText(c.getString("displayoption"));
                    diettext.setText(c.getString("diet"));
                    drinktext.setText(c.getString("drink"));
                    smoketext.setText(c.getString("smoke"));
                    bloodtext.setText(c.getString("bloodgroup"));
//                    hobbiestext.setText(c.getString("bloodgroup"));

                    Calendar calendar = Calendar.getInstance();
                    int cyear = calendar.get(Calendar.YEAR);
                    Age = String.valueOf((cyear - year));

                    TextView profileText = (TextView) findViewById(R.id.ptext);
                    TextView dateText = (TextView) findViewById(R.id.dtext);
                    TextView agetext = (TextView) findViewById(R.id.atext);



                    TextView m_status = (TextView) findViewById(R.id.mtext);
                    TextView no_children = (TextView) findViewById(R.id.no_childr);
                    TextView pinfo = (TextView) findViewById(R.id.personalinfo);
//




                    profileText.setText(profilecreatedfor);
                    dateText.setText(dob);

                    m_status.setText(maritalstatuss);

                    if (havechildren=="-select"){
                        no_children.setText("");
                    }else {
                        no_children.setText(havechildren);
                    }

                    pinfo.setText(persninfo);




                    agetext.setText(Age);





                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("dateofbirth", dateofbirth);
                    editor.putString("countryname", countryname);
                    editor.putString("mothertongue", mothertongue);
                    editor.putString("educationname", educationname);
                    editor.putString("professionname", professionname);
                    editor.putString("annualincome", annualincome);
                    editor.putString("maritalstatus", maritalstatuss);
                    editor.putString("residencystatus", residencystatus);
                    editor.putString("religionn", religionn);
                    editor.putString("lastname", lastname);
                    editor.commit();


//                    agetext.setText(Age);


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
        {
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to login url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("profileid", profileid);
//                params.put("firstname", firstname);
//                params.put("lastname", lastname);
//                return params;
//            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//         Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    private void checkstatus() {

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

                    String verifystatus = c.getString("verifystatus");

                    VerifyStatus = verifystatus;
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


    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UsersDetails.username)) {
                    al.add(key);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void back(View v)
    {


}



}
