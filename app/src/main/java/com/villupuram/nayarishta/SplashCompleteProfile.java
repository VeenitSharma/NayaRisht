package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;
import static com.villupuram.nayarishta.constant.base_api_url.varify_profile;

public class SplashCompleteProfile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    
    EditText otptext;
    ProgressDialog progressDialog;
    private final String TAG = "SplashCompleteProfile";
    private GoogleApiClient mGoogleApiClient;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/active-user.php";
    private static final String URL_FOR_RESENDOTP = "http://nayarishta.com/nayarishta-app/api/resend_otp.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_complete_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RelativeLayout next = (RelativeLayout) findViewById(R.id.bottom);
        otptext = (EditText) findViewById(R.id.otpfield);
        TextView email = (TextView) findViewById(R.id.emailtext);

        SharedPreferences shared = getSharedPreferences(RegisterPref, MODE_PRIVATE);
        final String emailname = (shared.getString("email", ""));
        email.setText(emailname);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = otptext.getText().toString();
                if (otp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your OTP", Toast.LENGTH_LONG).show();
                }
                else {
                    save_userdata(otp);
                }



//                Intent intent = new Intent(getApplicationContext(),TryFragmentActivity.class);
//                startActivity(intent);
            }
        });

        TextView logout =(TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                bydefaultstatus=1;

                globallogincheck=1;


                varify_profile ="complete";
                Intent it=new Intent(SplashCompleteProfile.this,Login2.class);
                startActivity(it);

            }
        });


        Button resend = (Button)findViewById(R.id.resend);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resendotp(emailname);
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

    private void save_userdata(final String otp) {
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

                        SharedPreferences shared = getSharedPreferences(RegisterPref, MODE_PRIVATE);
                        String uname = (shared.getString("username", ""));
                        String pass = (shared.getString("password", ""));
//

                        chat(uname,pass);

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
                params.put("otp", otp);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void chat(final String Susername, final String Spassword) {

        String cancel_req_tag = "login";
        progressDialog.setMessage("Please wait a moment");
        showDialog();
        String url = "https://nayarishta-app-8d5f4.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                hideDialog();
                if (s.equals("null")) {
                    Toast.makeText(SplashCompleteProfile.this, "user not found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject obj = new JSONObject(s);

                        if (!obj.has(Susername)) {
//                                Toast.makeText(Login2.this, "user not found", Toast.LENGTH_LONG).show();
                        }
                        else if (!obj.getJSONObject(Susername).getString("password").equals("")) {
                            UsersDetails.username = Susername;
                            UsersDetails.password = Spassword;
                            startActivity(new Intent(SplashCompleteProfile.this, TryFragmentActivity.class));

                        } else {

                            Toast.makeText(SplashCompleteProfile.this, "incorrect password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
                hideDialog();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(SplashCompleteProfile.this);
        rQueue.add(request);

    }



    private void resendotp(final String email) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please wait a moment");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_RESENDOTP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
//
                       Toast.makeText(getApplicationContext(),"Sent Successfully",Toast.LENGTH_LONG).show();
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

