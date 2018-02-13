package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;

public class ResultLetsSearch extends AppCompatActivity {
    private final String TAG = "ResultLetsSearch";
    TextView edit,personalinfo,agetext,countrytext,countytext,citytext,resitext,specialcasestext, bodytypetext, complexiontext,educationtext,branchtext,professiontext,annualtext,
            riligioustext,castetext,subcastetext,gotratext,mothertanguetext,mothertext,fathertext,
            totalsistertext,totalmarriedsister,totalbrotest,totalbromarried,famvaluestext,famtypes,aboutfamilytext,mobiletext,landlinetext,conpersontext,
            suitablecalltext,displayoptiontext,diettext,drinktext,smoketext,bloodtext,hobbiestext,interesttext,favoritemusictext,
            readstext,
            moviestext,activitytext,cuisinetext,dresstext;
    ImageView userimage;
    Button interestSend;
    String userid , expid;

    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/interestSend.php";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_lets_search);


        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userimage = (ImageView)findViewById(R.id.user_image);
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
        personalinfo = (TextView) findViewById(R.id.personalinfo);

        userimage.setBackgroundResource(R.drawable.addphotocircle);
        interestSend = (Button)findViewById(R.id.interestsend);

        String positionValue=getIntent().getStringExtra("value");
        String jsonResult= getIntent().getStringExtra("jsonResult");

        try {
            JSONArray json=new JSONArray(jsonResult);
            int pos = Integer.parseInt(positionValue);
            for (int i = 0; i < json.length(); i++) {
                if(i==pos) {
                    JSONObject c = json.getJSONObject(i);
                    expid = c.getString("userprofileid");
                    String Name = c.getString("userprofilefirstname") +" "+c.getString("userprofilelastname");
                    String dob =  c.getString("userprofiledob");
                    int year = Integer.parseInt((c.getString("userprofileyear")));
//                    String havechildren =  c.getString("havechildren");
                    String image = c.getString("userprofilephoto");
                    String religion = c.getString("religion");
                    String occupation = c.getString("userprofileoccupation");

                    Calendar calendar = Calendar.getInstance();
                    int cyear = calendar.get(Calendar.YEAR);
                   String Age = String.valueOf((cyear - year));



                    if (!image.equals("")){
                        Picasso.with(getApplicationContext()).load(image).resize(400,400).into(userimage);
                    }

                    TextView agetext = (TextView) findViewById(R.id.agetext);
                    TextView dateText = (TextView) findViewById(R.id.datetext);
                    TextView username = (TextView) findViewById(R.id.username);
                    TextView profileId = (TextView) findViewById(R.id.profileId);

                    profileId.setText(userid);
                    username.setText(Name);
                    agetext.setText(Age);
                    dateText.setText(dob);
                    riligioustext.setText(religion);
                    professiontext.setText(occupation);

                    break;
                }
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }



        interestSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                String uid = (preferences.getString("user_id", ""));


                registerUser(uid,expid);
            }
        });



    }
    private void registerUser(final String userid, final String expressid) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Sending...");
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
////                        String user = jObj.getJSONObject("user").getString("name");
//                        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Sent Successfully!", Toast.LENGTH_SHORT).show();


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
                params.put("expressid", expressid);

                return params;
            }
        };
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

    public void back(View v)
    {
        finish();
    }

}
