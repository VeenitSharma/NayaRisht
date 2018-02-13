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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;
import static com.villupuram.nayarishta.constant.base_api_url.varify_profile;

public class SplashLocationDetails extends AppCompatActivity implements Spinner.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {



    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/locationUpdate.php";
    private static final String TAG = "SplashPersonalDetails";
    ProgressDialog progressDialog;
    Spinner mother,religious,maritalstatuss,children,height;
    private JSONArray result;
    public static String userid="";
    EditText residence;
    TextView logout;
    public static String countryList="";
    public static String statuslist="";
    public static String citylist="";
    public static String country_id="";
    public static String state_id="";
    public static String city_id="";
    private ArrayList<String> state;
    private ArrayList<String> allcity;
    private ArrayList<String> contries;
    private Spinner spinner, Scity,Sstate;

    private GoogleApiClient mGoogleApiClient;
    SharedPreferences sharedpreferences;
    public static final String SplashLocation = "MyPrefs";
    String one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_location_details);
        contries = new ArrayList<String>();
        state = new ArrayList<String>();
        allcity = new ArrayList<String>();


        spinner = (Spinner) findViewById(R.id.countrylocation);
        Sstate = (Spinner) findViewById(R.id.state);
        spinner.setOnItemSelectedListener(this);
        Scity = (Spinner) findViewById(R.id.citlocationy);
        residence = (EditText)findViewById(R.id.residence);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        spinner.setPrompt("Select Country");
        Sstate.setPrompt("Select County");
        Scity.setPrompt("Select City");

        sharedpreferences = getSharedPreferences(SplashLocation, Context.MODE_PRIVATE);


        RelativeLayout next = (RelativeLayout)findViewById(R.id.bottom);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        getCountry();
        logout =(TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               FirebaseAuth.getInstance().signOut();

//                Firebase reference = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/users");
//            reference.child(usernamereg).child("Online").setValue("true");

//                Intent intent = getIntent();
//                one = intent.getStringExtra("logintype");
                signOut();
                bydefaultstatus=1;

                globallogincheck=1;
                varify_profile ="location";
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("logout", "location");
//                editor.commit();



                Intent it=new Intent(SplashLocationDetails.this,Login2.class);
                startActivity(it);
//                }


            }
        });


        Sstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Integer value = Sstate.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(statuslist);
                    result = jsonResponse.getJSONArray("state");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusdd = jsonn.getString("id");
                    state_id = valusdd;
                    getcity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Scity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Integer value = Scity.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(citylist);
                    result = jsonResponse.getJSONArray("city");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusdd = jsonn.getString("id");
                    city_id = valusdd;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resi = residence.getText().toString();

                Intent intent = getIntent();
                String type = intent.getStringExtra("logintype");
                if (type.equals("fb") || (type.equals("google")) )
                {
                    SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                    userid = (preferences.getString("user_id", ""));

                }
                else {
                    SharedPreferences preferences = getSharedPreferences(RegisterPref, MODE_PRIVATE);
                    userid = (preferences.getString("user_id", ""));
                }




                if(country_id.toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Please specify Country living in",Toast.LENGTH_LONG).show();
                }
                else   if(state_id.toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Please specify county",Toast.LENGTH_LONG).show();
                } else   if(city_id.toString().equals("")) {
                    Toast.makeText(getApplicationContext(),"Please specify city",Toast.LENGTH_LONG).show();
                }
                else   if(resi.equals("")) {
                    Toast.makeText(getApplicationContext(),"Please specify residency status",Toast.LENGTH_LONG).show();
                }
                else {

                    registerUser(userid,country_id,state_id,city_id,residence.getText().toString());
                }


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

    private void getCountry(){
        //Creating a string request
        state.clear();
        StringRequest stringRequest = new StringRequest(Config_location.DATA_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            countryList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY);


                            listCountry(result);
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
    private void listCountry(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                contries.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        contries.add(0, "Select Country");
        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(SplashLocationDetails.this, android.R.layout.simple_spinner_dropdown_item, contries));
    }
    private void getstates(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_STATE+"countryid="+country_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            statuslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_STATES);


                            //Calling method getStudents to get the students from the JSON Array
                            getallstates(result);
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
    private void getallstates(JSONArray j){
        //Traversing through all the items in the json array
        allcity.clear();
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                state.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        state.add(0, "Select county");

        //Setting adapter to show the items in the spinner
        Sstate.setAdapter(new ArrayAdapter<String>(SplashLocationDetails.this, android.R.layout.simple_spinner_dropdown_item, state));
    }

    private void getcity(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_CITY+"stateid="+state_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            citylist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_CITY);


                            //Calling method getStudents to get the students from the JSON Array
                            getallcities(result);
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
    private void getallcities(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                allcity.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (allcity.get(0)!="Select city") {
            allcity.add(0, "Select city");
        }

        Scity.setAdapter(new ArrayAdapter<String>(SplashLocationDetails.this, android.R.layout.simple_spinner_dropdown_item, allcity));


        //Setting adapter to show the items in the spinner



    }




    private void registerUser(final String userid,final String country,  final String state, final String city,final String residencystatus) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

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

                        Intent intent = getIntent();
                        String type = intent.getStringExtra("logintype");

                        if (type!="") {

                            if (type.equals("fb")) {
                                Intent intentt = new Intent(getApplicationContext(), SplashEducationCarrier.class);
                                intentt.putExtra("logintype", "fb");
                                startActivity(intentt);
                            } else if (type.equals("google")) {

                                Intent intentt = new Intent(getApplicationContext(), SplashEducationCarrier.class);
                                intentt.putExtra("logintype", "google");
                                startActivity(intentt);
                            } else if (type.equals("register")) {
                                Intent intentt = new Intent(getApplicationContext(), SplashEducationCarrier.class);
                                intentt.putExtra("logintype", "register");
                                startActivity(intentt);
                            }
                            else if (type.equals("loginuser")) {
                                Intent intentt = new Intent(getApplicationContext(), SplashEducationCarrier.class);
                                intentt.putExtra("logintype", "loginuser");
                                startActivity(intentt);
                            }
                        }
                        else {
                            Intent intentt = new Intent(getApplicationContext(),SplashEducationCarrier.class);
                            startActivity(intentt);
                        }

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
                params.put("countryid", country);
                params.put("stateid", state);
                params.put("cityid", city);
                params.put("residencystatus", residencystatus);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        state.clear();
        allcity.clear();

        Spinner spinnerv = (Spinner) parent;
        if (spinnerv.getId() == R.id.countrylocation) {

            Integer value = spinner.getSelectedItemPosition();

            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(countryList);
                result = jsonResponse.getJSONArray("country");
                JSONObject jsonn = result.getJSONObject(value - 1);

                String valusd = jsonn.getString("id");
                country_id = valusd;

                getstates();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
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
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
