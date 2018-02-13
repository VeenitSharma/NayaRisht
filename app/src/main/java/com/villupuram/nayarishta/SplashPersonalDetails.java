package com.villupuram.nayarishta;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
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
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Login2.user;
import static com.villupuram.nayarishta.Register.DOB;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.Register.fmonth;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;
import static com.villupuram.nayarishta.constant.base_api_url.varify_profile;

public class SplashPersonalDetails extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {



    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/personalDetailsUpdate.php";
    private static final String TAG = "SplashPersonalDetails";
    ProgressDialog progressDialog;
    Spinner mother,religious,maritalstatuss,children,height;
    public static String userid="",mstatus="";
    private ArrayList<String> Language;
    private JSONArray result;

    private ArrayList<String> religiousarray;
    private ArrayList<String> heightarray;
    private ArrayList<String> languagesarray;

    public static String heightList="";
    public static String heightID="";
    public static String religiouslist="";
    public static String languageslist="";
    public static String religious_id="";
    public static String languageid="";
    SharedPreferences sharedpreferences;
    public static final String SplasgPI = "MyPrefs";
    TextView heighttlayout,logout;
    RelativeLayout heightlay, genderlayout,datelayout;
    private GoogleApiClient mGoogleApiClient;
    String one;
    ImageButton male, female;
    static EditText datee;
    public static String Gender="";
    public static Integer fday,fmonth,fyear;
    Calendar myCalendar = Calendar.getInstance();
    public static int age;
    RadioGroup gender;
    String DOB_string="";
    public String verify_logout="";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedpreferences = getSharedPreferences(SplasgPI, Context.MODE_PRIVATE);
        setContentView(R.layout.activity_splash_personal_details);
        //Initializing the ArrayList
        religiousarray = new ArrayList<String>();
        languagesarray = new ArrayList<String>();
        heightarray = new ArrayList<String>();

        male = (ImageButton) findViewById(R.id.male);
        female = (ImageButton) findViewById(R.id.female);
        datee = (EditText) findViewById(R.id.date);
        gender = (RadioGroup)findViewById(R.id.gender);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        Language = new ArrayList<String>();
        mother = (Spinner) findViewById(R.id.languagedff);
        religious = (Spinner) findViewById(R.id.religious);
        maritalstatuss = (Spinner) findViewById(R.id.mssdfg);
        children = (Spinner) findViewById(R.id.childrenspinner);
        height = (Spinner) findViewById(R.id.heightPI);
        logout =(TextView)findViewById(R.id.logout);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RelativeLayout next = (RelativeLayout)findViewById(R.id.bottom);
        heightlay = (RelativeLayout)findViewById(R.id.heightlayout);
        genderlayout = (RelativeLayout)findViewById(R.id.genderlayout);
        datelayout = (RelativeLayout)findViewById(R.id.dated);




        Intent intentt = getIntent();
        String  typet = intentt.getStringExtra("logintype");



        if (!typet.equals("")&&!typet.equals("loginuser"))
        {
            if (typet.equals("fb")||typet.equals("google")){
                datelayout.setVisibility(View.VISIBLE);
                datee.setVisibility(View.VISIBLE);
                gender.setVisibility(View.VISIBLE);
                genderlayout.setVisibility(View.VISIBLE);
            }
            else if (typet.equals("register")){
                datee.setVisibility(View.GONE);
                gender.setVisibility(View.GONE);
                genderlayout.setVisibility(View.GONE);
                datelayout.setVisibility(View.GONE);
            }

        }



        heighttlayout = (TextView)findViewById(R.id.heighttt);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  FirebaseAuth.getInstance().signOut();



//                Firebase reference = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/users");
//            reference.child(usernamereg).child("Online").setValue("true");

                signOut();
                bydefaultstatus=1;

                globallogincheck=1;
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString("user_id", userid);
                editor.putString("verify_logout", "personal");
                editor.commit();



                verify_logout ="personal";

                Intent it=new Intent(SplashPersonalDetails.this,Login2.class);
                startActivity(it);



            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.male_gray_select);
                female.setBackgroundResource(R.drawable.female_gender_selected);
                Gender="M";
            }
        });



        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.ic_search_male);
                female.setBackgroundResource(R.drawable.female_gray_select);
                Gender="F";

            }
        });



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fday=dayOfMonth;
                fmonth=monthOfYear;
                fyear=year;
                DOB_string = String.valueOf(fday);
                updateLabel();
                updateDisplay();
            }

        };

        datee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");

            }
        });




//




        getReligious();
        getLanguage();
        getheights();


        religious.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                 Integer value = religious.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(religiouslist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("religionid");
                    religious_id = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mother.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = mother.getSelectedItemPosition();
                value = value-1;

                if (mother.getSelectedItem() != "Select language") {
                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(languageslist);
                        result = jsonResponse.getJSONArray("language");
                        JSONObject jsonn = result.getJSONObject(value);

                        String valusd = jsonn.getString("languageid");
                        languageid = valusd;


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }// to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightList);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("heightid");
                    heightID = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        maritalstatuss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String checkstatus = maritalstatuss.getSelectedItem().toString();

                if (checkstatus.equals("Never Married")){
                    RelativeLayout childrenlayout = (RelativeLayout)findViewById(R.id.children);
                    TextView childrentext = (TextView)findViewById(R.id.have);

                    childrenlayout.setVisibility(View.GONE);
                    childrentext.setVisibility(View.GONE);
                }else {
                    RelativeLayout childrenlayout = (RelativeLayout)findViewById(R.id.children);
                    TextView childrentext = (TextView)findViewById(R.id.have);

                    childrenlayout.setVisibility(View.VISIBLE);
                    childrentext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String mstatus = maritalstatuss.getSelectedItem().toString();
                String ch = children.getSelectedItem().toString();
                String heightt = height.getSelectedItem().toString();
                String mothertng_value = mother.getSelectedItem().toString();
                String religious_value = religious.getSelectedItem().toString();

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("ch", ch);
                editor.putString("languageid", languageid);
                editor.putString("mstatus", mstatus);
                editor.commit();

                DOB= fday+"-"+fmonth+"-"+fyear;

                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                userid = (preferences.getString("user_id", ""));

                if (gender.getVisibility() == View.VISIBLE) {
                    if (Gender.toString().equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Specify Gender", Toast.LENGTH_LONG).show();
                    }
                    else if (datee.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "Please select your DOB", Toast.LENGTH_LONG).show();
                    }
                    else   if (languageid.equals("")){
                        Toast.makeText(getApplicationContext(), "Please select your Mother Tongue", Toast.LENGTH_LONG).show();
                    }
                    else if (religious_id.equals("")){
                        Toast.makeText(getApplicationContext(), "Please select your Religion", Toast.LENGTH_LONG).show();
                    }
                    else if (mstatus.equals("Select")){
                        Toast.makeText(getApplicationContext(), "Please select your Marital Status", Toast.LENGTH_LONG).show();
                    }
                    else if (mstatus.equals("Never Married")){

                        if(heightt.equals("Select")){
                            Toast.makeText(getApplicationContext(), "Please select your Height", Toast.LENGTH_LONG).show();
                        }
                        else {
                            fb_registerUser(userid,languageid,religious_id,Gender,fday,fmonth,fyear,DOB,mstatus,"",heightID);
                        }

                    }
                    else if(ch.equals("select")){
                        Toast.makeText(getApplicationContext(), "Please select num. of childrens", Toast.LENGTH_LONG).show();
                    }
                    else if(heightt.equals("Select")){
                        Toast.makeText(getApplicationContext(), "Please select your Height", Toast.LENGTH_LONG).show();
                    }
                    else {
                        fb_registerUser(userid,languageid,religious_id,Gender,fday,fmonth,fyear,DOB,mstatus,ch,heightID);
                    }

                }
                else {
                    if (languageid.equals("")){
                        Toast.makeText(getApplicationContext(), "Please select your Mother Tongue", Toast.LENGTH_LONG).show();
                    }
                    else if (religious_id.equals("")){
                        Toast.makeText(getApplicationContext(), "Please select your Religion", Toast.LENGTH_LONG).show();
                    }
                    else if (mstatus.equals("Select")){
                        Toast.makeText(getApplicationContext(), "Please select your Marital Status", Toast.LENGTH_LONG).show();
                    }
                    else if (mstatus.equals("Never Married")){

                        if(heightt.equals("Select")){
                            Toast.makeText(getApplicationContext(), "Please select your Height", Toast.LENGTH_LONG).show();
                        }
                        else {
                            registerUser(userid,languageid,religious_id,mstatus,"",heightID);
                        }

                    }
                    else if(ch.equals("select")){
                        Toast.makeText(getApplicationContext(), "Please select num. of childrens", Toast.LENGTH_LONG).show();
                    }
                    else if(heightt.equals("Select")){
                        Toast.makeText(getApplicationContext(), "Please select your Height", Toast.LENGTH_LONG).show();
                    }
                    else {
                        registerUser(userid,languageid,religious_id,mstatus,ch,heightID);
                    }


                }
            }
        });


    }
    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            c.add(Calendar.YEAR,-18);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            fday = day;
            fmonth = month+1;
            fyear = year;

            DOB = fday+"/"+fmonth+"/"+fyear;

            Calendar calendar = Calendar.getInstance();
            int yeare = calendar.get(Calendar.YEAR);
            age = yeare - fyear;
            datee.setText(day + "/" + (month +1)  + "/" + year);
        }
    }


    private void updateLabel() {

        datee.setText(fday + "/"
                + (fmonth +1)  + "/" + fyear);
    }

    public void updateDisplay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - fyear;
//        datee.setText( new StringBuilder().append("The user is ")
//                .append(age).append(" years old"));
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



    private void getReligious(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.RELIGIOUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            religiouslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getListReligious(result);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getListReligious(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("religionid");

                System.out.print(reli_ID);


                religiousarray.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        religiousarray.add(0, "Select Religion");
        //Setting adapter to show the items in the spinner
        religious.setAdapter(new ArrayAdapter<String>(SplashPersonalDetails.this, android.R.layout.simple_spinner_dropdown_item, religiousarray));
    }



    private void getheights(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            heightList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getallheights(result);
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
    private void getallheights(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("heightid");

                System.out.print(reli_ID);

                String feet= json.getString("feet");
                String inch= json.getString("inch");
                String cm= json.getString("cm");


                heightarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        heightarray.add(0, "Select");
        //Setting adapter to show the items in the spinner
        height.setAdapter(new ArrayAdapter<String>(SplashPersonalDetails.this, android.R.layout.simple_spinner_dropdown_item, heightarray));
    }


    private void getLanguage(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.LANGUAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            languageslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getallLanguage(result);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getallLanguage(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//


                Language.add(json.getString("languagename"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Language.add(0, "Select language");
        //Setting adapter to show the items in the spinner
        mother.setAdapter(new ArrayAdapter<String>(SplashPersonalDetails.this, android.R.layout.simple_spinner_dropdown_item, Language));
    }



    private void registerUser(final String userid,final String mother,  final String riligious, final String trms,final String childrenn, final String heighxt) {
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

                            if (type!=""){
                                if (type.equals("fb")){

                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype","fb");
                                    startActivity(intentt);
                                }else if (type.equals("google")){
                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype","google");
                                    startActivity(intentt);
                                }
                                else if (type.equals("register")){
                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype","register");
                                    startActivity(intentt);
                               }
                                else if (type.equals("loginuser")){
                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype","loginuser");
                                    startActivity(intentt);
                                }

                            }
                            else {
                                Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                startActivity(intentt);
                            }



                        } else {

                            String errorMsg = jObj.getString("error_msg");
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
                    params.put("mothertongue", mother);
                    params.put("religionid", riligious);
                    params.put("maritalstatus", trms);
                    params.put("havechildren", childrenn);
                    params.put("heightid", heighxt);

                    return params;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        }

    private void fb_registerUser(final String userid,final String mother,  final String riligious, final String gender,final Integer day,final Integer month,final Integer year,final String dob, final String trms,final String childrenn, final String heighxt) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";



        if (age < 18) {
            Toast.makeText(SplashPersonalDetails.this, "Under 18Years are not allow!", Toast.LENGTH_LONG).show();

        } else {
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

                            if (type != "") {
                                if (type.equals("fb")) {

                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype", "fb");
                                    startActivity(intentt);
                                } else if (type.equals("google")) {
                                    Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                    intentt.putExtra("logintype", "google");
                                    startActivity(intentt);
                                }
                            } else {
                                Intent intentt = new Intent(getApplicationContext(), SplashLocationDetails.class);
                                startActivity(intentt);
                            }


                        } else {

                            String errorMsg = jObj.getString("error_msg");
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
                    params.put("mothertongue", mother);
                    params.put("religionid", riligious);
                    params.put("maritalstatus", trms);
                    params.put("havechildren", childrenn);
                    params.put("heightid", heighxt);

                    params.put("gender", gender);
                    params.put("day", String.valueOf(day));
                    params.put("month", String.valueOf(month));
                    params.put("year", String.valueOf(year));
                    params.put("DOB", String.valueOf(dob));

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


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
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
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("verify_logout", verify_logout);
        super.onSaveInstanceState(savedInstanceState);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
