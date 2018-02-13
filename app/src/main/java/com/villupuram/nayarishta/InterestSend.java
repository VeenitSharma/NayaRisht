package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import java.util.List;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;

public class InterestSend extends AppCompatActivity {

    private static final String TAG = "InterestSend";
    ListView listView;
    private static String URL_FOR_DETAIL = "";

    ArrayList<HashMap<String, String>> sendlist;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/getsentInterest.php";
    ProgressDialog progressDialog;
    public static String channel = "";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<InterestSend_Items> itemsList;
    private JSONArray result;
    public String userprofileID="",jsonResult="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest_send);

        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);


        itemsList = new ArrayList<>();

        adapter = new InterstSend_Adapter(itemsList, getApplicationContext());
        recyclerView.setAdapter(adapter);

        loadInterestSend();


    }


    public void opendraftmsg(String id){

//        Toast.makeText(getApplicationContext(),position,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), BasicDetail.class);
        intent.putExtra("id", id);
        intent.putExtra("type","interest_send");
        startActivity(intent);
    }

    public void loadInterestSend()
    {
        String cancel_req_tag = "cancle";
        progressDialog.setMessage("Please wait a moment");
        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_LOGIN+"?userid="+channel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("sentinterest");

                    jsonResult = jsonArray.toString();

                    for (int i = 0; i<=jsonArray.length();i++){
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        InterestSend_Items draftItems = new InterestSend_Items(jObj.getString("toname"),jObj.getString("createddate"),jObj.getString("idrequestto"),getApplicationContext());
                        adapter.notifyDataSetChanged();
                        itemsList.add(draftItems);

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

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



    public void back(View a){
        finish();
    }
}
