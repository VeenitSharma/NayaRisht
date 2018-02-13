package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;

public class ToAccept extends AppCompatActivity {


    private static final String TAG = "ToAccept";
    ListView listView;
    InboxAdapter adapter;
    private static String URL_FOR_DETAIL = "";

    ArrayList<HashMap<String, String>> inboxlist;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/getToAcceptInterest.php";
    ProgressDialog progressDialog;
    public static String channel="",jsonResult="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_accept);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        inboxlist = new ArrayList<>();

        api();
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
                            Intent intent = new Intent(ToAccept.this,BasicDetail.class);
                            intent.putExtra("id",idd);
                            intent.putExtra("type","yaccept");
                            startActivity(intent);

                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        });
    }
    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");
        showDialog();

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));
        URL_FOR_DETAIL = URL_FOR_LOGIN+"?userid="+channel;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {


                    JSONObject jobj = new JSONObject(response);

                    String error = jobj.getString("message");

                    if (error.toString().equals("There is no records."))
                    {
                        TextView textView = (TextView)findViewById(R.id.errortext);

                        textView.setText("there is no record!");
                    }

                    JSONArray jUser =   jobj.getJSONArray("toacceptInterest");

                    jsonResult=jUser.toString();
                    for (int i = 0; i < jUser.length(); i++) {
                        JSONObject c = jUser.getJSONObject(i);


                        // Phone node is JSON Object
                        String name = c.getString("toname");
                        String date = c.getString("createddate");
                        String id = c.getString("id");


                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("toname", name);
                        contact.put("createddate", date);
                        contact.put("id", id);

                        inboxlist.add(contact);

                        adapter.notifyDataSetChanged();
                        listView.invalidateViews();



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();

            }
        }) ;

        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

    public void api(){
        loginUser();

        listView=(ListView)findViewById(R.id.listView_inbox);

        int i=0;
        adapter=new InboxAdapter(getApplicationContext(), R.layout.row_interest_sent);


        final ListAdapter adapter = new SimpleAdapter(
                ToAccept.this, inboxlist,
                R.layout.row_interest_sent, new String[]{"toname","createddate","id"}, new int[]{R.id.user_name_inbox,R.id.date_inbox,R.id.id_text
        });


        listView.setAdapter(adapter);

    }

    public void back(View a){
        finish();
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
