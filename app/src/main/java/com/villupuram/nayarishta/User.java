package com.villupuram.nayarishta;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.global_draftmessage;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

public class User extends AppCompatActivity {
    private static final String TAG = "Inbox";
    ListView listView;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    private JSONArray result;
    public String userprofileID="",jsonResult="";
    private static final String URL_FOR_GETUSERS = "http://nayarishta.com/nayarishta-app/api/getOnlineUsers.php";

    RecyclerView recyclerView;
    UsersAdapter adapter;
    private ArrayList<UsersItem> mListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_users);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        listView=(ListView)findViewById(R.id.listView);

        noUsersText = (TextView)findViewById(R.id.noUsersText);

        mListData = new ArrayList<>();

        adapter = new UsersAdapter(getApplicationContext(), R.layout.user_name_list, mListData);
        listView.setAdapter(adapter);


        loadCurrentUsers();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = listView.getItemAtPosition(position).toString();
                System.out.print(value);

                try {
                    JSONArray json=new JSONArray(jsonResult);
                    String poss = String.valueOf(position);
                    for (int i = 0; i < json.length(); i++) {
                        if(i==position) {
                            JSONObject c = json.getJSONObject(i);

                            String idd  = c.getString("id");
                            String name = c.getString("name");
                            Toast.makeText(getApplicationContext(),name,Toast.LENGTH_SHORT).show();
                            UsersDetails.chatWith = name;
                            startActivity(new Intent(User.this, Chat.class));


                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });

////        pd = new ProgressDialog(User.this);
//        pd.setMessage("Loading...");
//        pd.show();
//
//        String url = "https://nayarishta-app-8d5f4.firebaseio.com/users.json";
//
//        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
//            @Override
//            public void onResponse(String s) {
//                doOnSuccess(s);
//            }
//        },new Response.ErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                System.out.println("" + volleyError);
//            }
//        });
//
//        RequestQueue rQueue = Volley.newRequestQueue(User.this);
//        rQueue.add(request);
//
//
//        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                UsersDetails.chatWith = al.get(position);
//                String name = al.get(position).toString();
//
//
//                startActivity(new Intent(User.this, Chat.class));
//            }
//        });


        RelativeLayout back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    public void loadCurrentUsers()
    {
        String cancel_req_tag = "cancle";
        progressDialog.setMessage("Please wait a moment");
        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_GETUSERS+"?userid="+channel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Boolean error = jsonObject.getBoolean("error");
                    String msg = jsonObject.getString("message");
                    UsersItem msgitems;

                    if (!error){
                        if (!msg.equals("There is no records.")){
                            JSONArray jsonArray = jsonObject.getJSONArray("users");
                            jsonResult = jsonArray.toString();
                            for (int i = 0; i<=jsonArray.length();i++){
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                msgitems = new UsersItem();
                                msgitems.setName(jObj.getString("name"));
                                msgitems.setImage(jObj.getString("avatar"));
                                adapter.notifyDataSetChanged();
                                mListData.add(msgitems);

                            }

                        }
                        else {
                            noUsersText.setVisibility(View.VISIBLE);
                        }

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });



        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, cancel_req_tag);

    }

    public void openChat(String toname){

        UsersDetails.chatWith = toname;
        startActivity(new Intent(User.this, Chat.class));

    }

//    public void doOnSuccess(String s){
//        try {
//            JSONObject obj = new JSONObject(s);
//
//            Iterator i = obj.keys();
//            String key = "";
//
//            while(i.hasNext()){
//                key = i.next().toString();
//
//                if(!key.equals(UsersDetails.username)) {
//                    al.add(key);
//                }
//
//                totalUsers++;
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        if(totalUsers <=1){
//            noUsersText.setVisibility(View.VISIBLE);
////            usersList.setVisibility(View.GONE);
//        }
//        else{
//            noUsersText.setVisibility(View.GONE);
////            usersList.setVisibility(View.VISIBLE);
//            listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
//        }
//
////        pd.dismiss();
//    }



    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
