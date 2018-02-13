package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Calendar;

import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;


public class ProfileIdSearch extends AppCompatActivity {
    private final String TAG = "ProfileIdSearch";

    EditText idtext;
    ProgressDialog progressDialog;
    public static String URL_FOR_DETAIL="", id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_id_search);

        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        idtext=(EditText)findViewById(R.id.idtext);
        Button button = (Button)findViewById(R.id.search);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser();
            }
        });

        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });

    }

    private void loginUser() {

        id = idtext.getText().toString();
        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(Tryfragment, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));
        Intent intent = getIntent();
        String idddd = intent.getStringExtra("id");

        if (!channel.equals(id)) {
            URL_FOR_DETAIL = base_api_url.USER_DETAIL + id;


            final StringRequest strReq = new StringRequest(Request.Method.GET,
                    URL_FOR_DETAIL, new Response.Listener<String>() {


                @Override
                public void onResponse(String response) {

                    Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                    hideDialog();
                    try {

                        JSONObject jarray = new JSONObject(response);
                        String msg = jarray.getString("message");
                        if (msg.equals("There is no records.")) {
                            Toast.makeText(getApplicationContext(), "There is no record.", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), BasicDetail.class);
                            intent.putExtra("id", idtext.getText().toString());
                            intent.putExtra("type","pid");
                            startActivity(intent);
                        }

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
            }
            ;
            strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//         Adding request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        }
        else {
            hideDialog();
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
