package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.Register.pass;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.Email;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;

public class ChangePassword extends AppCompatActivity {



    private static final String TAG = "ForgotPassword";
    private static final String URL_FORGOTPASSWORD = "http://nayarishta.com/nayarishta-app/api/change-password.php";
    private static String URL_FOR_DETAIL = "";
    EditText cpassword, newpassword,confirmpassword;
    ProgressDialog progressDialog;
    public static String emailaddress="",passwordaddress="";
    Button submit;
    TextView newtext, cnfrtext;
    RelativeLayout newpass, conpass;
    String email = "";
    @Override



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);



        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        RelativeLayout back = (RelativeLayout)findViewById(R.id.cback);
        submit=(Button) findViewById(R.id.submit);
        cpassword = (EditText)findViewById(R.id.currentpassword);
        newpassword = (EditText)findViewById(R.id.newpassword);
        confirmpassword = (EditText)findViewById(R.id.confirmpasswoed);




        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        newtext = (TextView)findViewById(R.id.newtext);
        cnfrtext = (TextView)findViewById(R.id.confrm);
        newpass = (RelativeLayout)findViewById(R.id.newpas);
        conpass = (RelativeLayout)findViewById(R.id.cnfrmpas);



        newtext.setVisibility(View.GONE);
        newpass.setVisibility(View.GONE);
        cnfrtext.setVisibility(View.GONE);
        conpass.setVisibility(View.GONE);



        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String emailtext =shared.getString("email","");
        final String passwordtext=shared.getString("password","");


        getEmail();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override



            public void onClick(View v) {
                String pass = cpassword.getText().toString();
                String newp = newpassword.getText().toString();
                String conpassd = confirmpassword.getText().toString();
                String matchpas=passwordtext;

                if (pass.equals("123456")){



                    newtext.setVisibility(View.VISIBLE);
                    newpass.setVisibility(View.VISIBLE);
                    cnfrtext.setVisibility(View.VISIBLE);
                    conpass.setVisibility(View.VISIBLE);

                    if(pass.equals("")){
                        Toast.makeText(getApplicationContext(),"Please enter old Password",Toast.LENGTH_LONG).show();
                    }


                    else if(newp.equals("")){
                        Toast.makeText(getApplicationContext(),"Please enter new Password",Toast.LENGTH_LONG).show();
                    }

                    else if(newp.length()<6){
                        Toast.makeText(getApplicationContext(),"Please fill minimum 6 digits Password!",Toast.LENGTH_LONG).show();
                    }
                    else if(newp.length()>15){
                        Toast.makeText(getApplicationContext(),"Please fill maximum 15 digits Password!",Toast.LENGTH_LONG).show();
                    }

                    else if(conpass.equals("")){
                        Toast.makeText(getApplicationContext(),"Please enter confirm Password!",Toast.LENGTH_LONG).show();
                    }

                    else if(!newp.equals(conpassd)){
                        Toast.makeText(getApplicationContext(),"Confirm Password dosen't match!",Toast.LENGTH_LONG).show();
                    }

                    else{
                        registerUser(email,newp);
                    }

                }

                else {

                    Toast.makeText(getApplicationContext(),"Current Password dosen't match!",Toast.LENGTH_LONG).show();
                }

            }
        });

            }

            private void registerUser(final String email, final String password) {
                // Tag used to cancel the request



                String cancel_req_tag = "register";
                progressDialog.setMessage("please wait a moment");
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

                                Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),Login2.class);
                                startActivity(intent);
                                finish();


                            } else {

                                String errorMsg = jObj.getString("messgae");
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

             private void getEmail() {

        // Tag used to cancel the request
        String cancel_req_tag = "wait";
        progressDialog.setMessage("please wait a moment");

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
                    email = c.getString("email");


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
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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






        }