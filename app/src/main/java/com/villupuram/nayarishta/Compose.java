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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.global_draftmessage;

public class Compose extends AppCompatActivity {


    EditText email, message, subject;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/messageSend.php";
    private static final String URL_FOR_SAVEDRAFT = "http://nayarishta.com/nayarishta-app/api/saveDrafts.php";

    ProgressDialog progressDialog;
    private final String TAG = "Edit_About_me";
    Button drafts;
    private DatabaseReference mDatabase;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        Button button = (Button)findViewById(R.id.send);
        email = (EditText)findViewById(R.id.emailtext);
        message = (EditText)findViewById(R.id.message);
        subject = (EditText)findViewById(R.id.subject);
        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        drafts = (Button)findViewById(R.id.drafts);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (global_draftmessage.equals("1")){
            Intent intent = getIntent();
            String tonametxt = intent.getStringExtra("toname");
            String subtext = intent.getStringExtra("subject");
            String msgtxt = intent.getStringExtra("message");

            email.setText(tonametxt);
            subject.setText(msgtxt);
            message.setText(subtext);
        }


        drafts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
                String userid = (shared.getString("user_id", ""));
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String emaail="",subjecttext="",msgbody="";
                emaail = email.getText().toString();
                subjecttext = subject.getText().toString();
                msgbody = message.getText().toString();

                if (emaail.equals("")&&subjecttext.equals("")&&msgbody.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your email,subject and message",Toast.LENGTH_LONG).show();
                }
                else if (!email.getText().toString().matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(), "Please fill valid Email !", Toast.LENGTH_LONG).show();
                }
                else if (emaail.equals("")){
                    Toast.makeText(getApplicationContext(),"enter your email",Toast.LENGTH_LONG).show();
                }
                else if(subjecttext.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your subject",Toast.LENGTH_LONG).show();
                }
                else if(msgbody.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your message",Toast.LENGTH_LONG).show();
                }
                else {
                    email.getText().clear();
                    subject.getText().clear();
                    message.getText().clear();
                    save_drafts(userid,emaail,subjecttext,msgbody);
                }

            }
        });


        button.setOnClickListener(  new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
                String userid = (shared.getString("user_id", ""));
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String emaail="",subjecttext="",msgbody="";
                 emaail = email.getText().toString();
                subjecttext = subject.getText().toString();
                msgbody = message.getText().toString();

                if (emaail.equals("")&&subjecttext.equals("")&&msgbody.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your email,subject and message",Toast.LENGTH_LONG).show();
                }
                else if (!email.getText().toString().matches(emailPattern))
                {
                    Toast.makeText(getApplicationContext(), "Please fill valid Email !", Toast.LENGTH_LONG).show();
                }
                else if (emaail.equals("")){
                    Toast.makeText(getApplicationContext(),"enter your email",Toast.LENGTH_LONG).show();
                }
                else if(subjecttext.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your subject",Toast.LENGTH_LONG).show();
                }
                else if(msgbody.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"enter your message",Toast.LENGTH_LONG).show();
                }
                else {
                    email.getText().clear();
                    subject.getText().clear();
                    message.getText().clear();

                    sendmsg(userid,emaail,subjecttext,msgbody);
                }




            }
        });





    }


    private void sendmsg(final String userid, final String toaddress,final String subject,final String messagebody) {
        // Tag used to cancel the request
        String cancel_req_tag = "compose";

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
//
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

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
                params.put("toaddress", toaddress);
                params.put("subject", subject);
                params.put("messagebody", messagebody);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void save_drafts(final String userid, final String toaddress,final String subject,final String messagebody) {
        // Tag used to cancel the request
        String cancel_req_tag = "compose";

        progressDialog.setMessage("Please wait a moment");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_SAVEDRAFT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
//
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

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
                params.put("toaddress", toaddress);
                params.put("subject", subject);
                params.put("messagebody", messagebody);
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

}
