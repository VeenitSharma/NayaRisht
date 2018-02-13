package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.Check_FB_Rregistration;
import static com.villupuram.nayarishta.constant.base_api_url.FacebookloginOrnot;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;
import static com.villupuram.nayarishta.constant.base_api_url.varify_profile;

public class SplashEducationCarrier extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private ArrayList<String> Education;
    private ArrayList<String> Profession;
    private ArrayList<String> AnnualIncom;
    private JSONArray result;
    ProgressDialog progressDialog;
    private final String TAG = "SplashEducationCarrier";
    public static String userid="";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/educationDetailsUpdate.php";
    private static final String URL_FOR_SAVE_EDU_GOOGLE = "http://nayarishta.com/nayarishta-app/api/educationSocialDetailsUpdate.php";

    private GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedpreferences;
    public static final String SplashEducation = "MyPrefs";


    Spinner education,working,income;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_education_carrier);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        sharedpreferences = getSharedPreferences(SplashEducation, Context.MODE_PRIVATE);



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        //Initializing the ArrayList
        Education = new ArrayList<String>();
        Profession = new ArrayList<String>();
        AnnualIncom = new ArrayList<String>();

        RelativeLayout next = (RelativeLayout)findViewById(R.id.bottom);
        education = (Spinner)findViewById(R.id.education);
        working = (Spinner)findViewById(R.id.profession);
        income = (Spinner)findViewById(R.id.annual_incom);

        geteducation();
        getprofessionlist();
        getannualincojmlist();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edu = education.getSelectedItem().toString();
                String profess = working.getSelectedItem().toString();
                String annual = income.getSelectedItem().toString();

                Intent intent = getIntent();
                String typee = intent.getStringExtra("logintype");
                if (typee.equals("fb") || (typee.equals("google")) )
                {
                    SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    userid = (preferences.getString("user_id", ""));

                }
                else {
                    SharedPreferences preferences = getSharedPreferences(RegisterPref, MODE_PRIVATE);
                    userid = (preferences.getString("user_id", ""));
                }


                if (edu=="-Select-"){
                    Toast.makeText(getApplicationContext(),"Please specify Education",Toast.LENGTH_LONG).show();
                }
                else if(profess=="-Select-"){
                    Toast.makeText(getApplicationContext(),"Please specify Working",Toast.LENGTH_LONG).show();
                }
                else if(annual=="-Select-"){
                    Toast.makeText(getApplicationContext(),"Please specify Annual Income",Toast.LENGTH_LONG).show();
                }
                else {

                    Intent intentf = getIntent();
                    String type = intentf.getStringExtra("logintype");

                    if (type!=""){
                        if (type.equals("fb")){
                            google_case(userid,edu,profess,annual);
                        }
                        else if (type.equals("google")){
                            google_case(userid,edu,profess,annual);
                        }
                        else if (type.equals("loginuser")){
                            save_userdata(userid,edu,profess,annual);
                        }
                        else if (type.equals("register")){
                            save_userdata(userid,edu,profess,annual);
                        }
                    }
                    else {
                        save_userdata(userid,edu,profess,annual);
                    }
                }


            }
        });

        TextView logout =(TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                bydefaultstatus=1;

                globallogincheck=1;
//
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("logout", "education");
//                editor.commit();

                varify_profile ="education";

                Intent it=new Intent(SplashEducationCarrier.this,Login2.class);
                it.putExtra("logout","education");
                startActivity(it);
            }
        });





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


    //education
    private void geteducation(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.EDUCATION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("education");


                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    //education
    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                Education.add(json.getString("education"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Education.add(0, "-Select-");
        //Setting adapter to show the items in the spinner
        education.setAdapter(new ArrayAdapter<String>(SplashEducationCarrier.this, android.R.layout.simple_spinner_dropdown_item, Education));
    }


    //profession
    private void getprofessionlist(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.PROFESSION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("profession");


                            getprofession(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    //profession
    private void getprofession(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                Profession.add(json.getString("profession"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Profession.add(0, "-Select-");
        //Setting adapter to show the items in the spinner
        working.setAdapter(new ArrayAdapter<String>(SplashEducationCarrier.this, android.R.layout.simple_spinner_dropdown_item, Profession));
    }



    //profession
    private void getannualincojmlist(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.ANNUAL_INCOM_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("income");


                            getannualincojm(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    //profession
    private void getannualincojm(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                AnnualIncom.add(json.getString("annualincome"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AnnualIncom.add(0, "-Select-");
        //Setting adapter to show the items in the spinner
        income.setAdapter(new ArrayAdapter<String>(SplashEducationCarrier.this, android.R.layout.simple_spinner_dropdown_item, AnnualIncom));
    }



    private void save_userdata(final String userid, final String education, final String profession,final String annual_incom) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        SharedPreferences.Editor editor = sharedpreferences.edit();
//
        editor.putString("user_id", userid);
        editor.commit();

        progressDialog.setMessage("Please wait a moment");
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

                            Intent intentf = getIntent();
                            String type = intentf.getStringExtra("logintype");

                            if (type!="") {

                                if (type.equals("fb")) {
                                    Check_FB_Rregistration = "yes";
                                    Intent intent = new Intent(getApplicationContext(), TryFragmentActivity.class);
                                    startActivity(intent);

                                } else if (type.equals("google")) {
                                    Toast.makeText(getApplicationContext(), "google", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), TryFragmentActivity.class);
                                    startActivity(intent);

                                } else if (type.equals("register")) {
                                    Intent intentt = new Intent(getApplicationContext(), SplashCompleteProfile.class);
                                    intentt.putExtra("logintype", "register");
                                    startActivity(intentt);

                                }else if (type.equals("loginuser")) {
                                    Intent intentt = new Intent(getApplicationContext(), SplashCompleteProfile.class);
                                    intentt.putExtra("logintype", "loginuser");
                                    startActivity(intentt);
                                }

                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(),SplashCompleteProfile.class);
                                startActivity(intent);
                            }

//                            finish();
//                        }else if(one.equals("fb")){
//                            Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }

//                        }

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
                params.put("education", education);
                params.put("profession", profession);
                params.put("income", annual_incom);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void google_case(final String userid, final String education, final String profession,final String annual_incom) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";
        progressDialog.setMessage("Please wait a moment");

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("user_id", userid);
        editor.commit();

        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_SAVE_EDU_GOOGLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
//                        String user = jObj.getJSONObject("user").getString("name");

//                        Intent intenttt = getIntent();
//                        String type = intenttt.getStringExtra("logintype");
//
//                        Toast.makeText(getApplicationContext(),type,Toast.LENGTH_LONG).show();

//                        if(FacebookloginOrnot==1){
//                            Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
////                        else {
//                        String one;
//                        Intent intentc = getIntent();
//                        one = intentc.getStringExtra("logintype");
//                        Toast.makeText(getApplicationContext(),one,Toast.LENGTH_LONG).show();
//
//                        if (one.equals("")){
                        Intent intentf = getIntent();
                        String type = intentf.getStringExtra("logintype");

                        if (type!=""){
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("logintype", "social");
                            editor.commit();


                            if (type.equals("fb")){
                                Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
                                startActivity(intent);
                            }
                            else if (type.equals("google")){
                                Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
                                startActivity(intent);
                            }
                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(),SplashCompleteProfile.class);
                            startActivity(intent);
                        }

//                            finish();
//                        }else if(one.equals("fb")){
//                            Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }

//                        }


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
                params.put("education", education);
                params.put("profession", profession);
                params.put("income", annual_incom);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
