package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;



public class ForgotPassword extends AppCompatActivity {

    private static final String TAG = "ForgotPassword";
    private static final String URL_FORGOTPASSWORD = "http://nayarishta.com/nayarishta-app/api/forgotpassword.php";
    ProgressDialog progressDialog;
    EditText email, pass, cpass, otp;
    private static final String CHANGEPASSWORD = "http://nayarishta.com/nayarishta-app/api/change-password.php";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/verify_otp.php?otp=";

    TextView successtext;
    RelativeLayout otplayout,newlayout,confirmlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RelativeLayout back = (RelativeLayout)findViewById(R.id.cback);
        final Button submit = (Button)findViewById(R.id.submit);
        final Button settext = (Button)findViewById(R.id.setpassword);
        email = (EditText)findViewById(R.id.fpemail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pass = (EditText)findViewById(R.id.newpassword);
        otp = (EditText)findViewById(R.id.otp);
        cpass = (EditText)findViewById(R.id.confirmpasswoed);
        successtext = (TextView)findViewById(R.id.successmessage);
        otplayout = (RelativeLayout)findViewById(R.id.otpp);
        newlayout = (RelativeLayout)findViewById(R.id.newpas);
        confirmlayout = (RelativeLayout)findViewById(R.id.cnfrmpas);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String etext= email.getText().toString();

                registerUser(etext);
            }
        });

        settext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String etext = email.getText().toString();
                String padss =pass.getText().toString();
                String otpp = otp.getText().toString();

                String conpass = cpass.getText().toString();

                if(etext.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter Email",Toast.LENGTH_LONG).show();
                }

                else if(otpp.equals("")){
                    Toast.makeText(getApplicationContext(),"Please verify OTP",Toast.LENGTH_LONG).show();
                }

//                else if(padss.equals("")){
//                    Toast.makeText(getApplicationContext(),"Please enter new Password",Toast.LENGTH_LONG).show();
//                }
//                else if(padss.length()<6){
//                    Toast.makeText(getApplicationContext(),"Please fill minimum 6 digits Password !",Toast.LENGTH_LONG).show();
//                }
//                else if(padss.length()>15){
//                    Toast.makeText(getApplicationContext(),"Please fill maximum 15 digits Password !",Toast.LENGTH_LONG).show();
//                }
//                else if(conpass.equals("")){
//                    Toast.makeText(getApplicationContext(),"Please enter confirm Password !",Toast.LENGTH_LONG).show();
//                }
//                else if(!padss.equals(conpass)){
//                    Toast.makeText(getApplicationContext(),"Confirm Password dosen't match !",Toast.LENGTH_LONG).show();
//                }
                else{

                    checkotp(otpp);
                }




            }
        });


    }

    private void registerUser(final String email) {
        // Tag used to cancel the request

        if (email.toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Your Email", Toast.LENGTH_LONG).show();
        }
        else {
            String cancel_req_tag = "register";
            progressDialog.setMessage("Seding O.T.P....");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_FORGOTPASSWORD, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);

                        boolean error = jObj.getBoolean("error");


                        if (!error) {

                            final Button submit = (Button) findViewById(R.id.submit);
                            final Button settext = (Button) findViewById(R.id.setpassword);

                            Toast.makeText(getApplicationContext(), "OTP sent Successfully", Toast.LENGTH_LONG).show();
                            successtext.setVisibility(View.VISIBLE);
                            otplayout.setVisibility(View.VISIBLE);
                            settext.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.GONE);


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
                    params.put("email", email);
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


    private void checkotp(final String otpp) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please wait a moment");
        showDialog();

        String otppp = otp.getText().toString();
        String check_otp = URL_FOR_LOGIN+otppp;
        StringRequest strReq = new StringRequest(Request.Method.GET,
                check_otp, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String etext = email.getText().toString();
                        String padss =pass.getText().toString();
                        newlayout.setVisibility(View.VISIBLE);
                        confirmlayout.setVisibility(View.VISIBLE);

                        changepassword(etext,padss);

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
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("otp", otpp);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }






    private void changepassword(final String emailv, final String password) {
        // Tag used to cancel the request


        String etext = email.getText().toString();
        String padss = pass.getText().toString();
        String otpp = otp.getText().toString();
        String conpass = cpass.getText().toString();

        if (padss.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter new Password", Toast.LENGTH_LONG).show();
        } else if (padss.length() < 6) {
            Toast.makeText(getApplicationContext(), "Please fill minimum 6 digits Password !", Toast.LENGTH_LONG).show();
        } else if (padss.length() > 15) {
            Toast.makeText(getApplicationContext(), "Please fill maximum 15 digits Password !", Toast.LENGTH_LONG).show();
        } else if (conpass.equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter confirm Password !", Toast.LENGTH_LONG).show();
        } else if (!padss.equals(conpass)) {
            Toast.makeText(getApplicationContext(), "Confirm Password dosen't match !", Toast.LENGTH_LONG).show();
        }
        else
            {

            String cancel_req_tag = "register";
            progressDialog.setMessage("Please wait a moment");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    CHANGEPASSWORD, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);

                        boolean error = jObj.getBoolean("error");


                        if (!error) {
//                        Toast.makeText(getApplicationContext(), "Changed!", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Password updated successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), Login2.class);
                            startActivity(intent);

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
                    params.put("email", emailv);
                    params.put("password", password);
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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }





}
