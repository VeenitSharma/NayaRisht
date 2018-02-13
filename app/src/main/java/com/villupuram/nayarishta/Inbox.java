package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;


public class Inbox extends AppCompatActivity {

    private static final String TAG = "Inbox";
    ListView listView;
    InboxAdapter adapter;
    private static String URL_FOR_DETAIL = "";

    ArrayList<HashMap<String, String>> inboxlist;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/getinboxPvtmsg.php";
    ProgressDialog progressDialog;
    public static String channel="",jsonResult="";
    public static String USERNAME="";
    SharedPreferences sharedpreferencestwo;
    public static final String MYUSER_NAME = "MY_USER" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        inboxlist = new ArrayList<>();



        api();

        listView=(ListView)findViewById(R.id.listView_inbox);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONArray json=new JSONArray(jsonResult);
                    String poss = String.valueOf(position);
                    for (int i = 0; i < json.length(); i++) {
                        if(i==position) {
                            JSONObject c = json.getJSONObject(i);
                            String msgtext  = c.getString("msgtext");
                            String name  = c.getString("fromname");
                            Intent intent = new Intent(Inbox.this,ViewSome.class);
                            intent.putExtra("msgtext",msgtext);
                            intent.putExtra("name",name);
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
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();
                try {

                    JSONObject jobj = new JSONObject(response);
                    String error = jobj.getString("message");

                    if (error.toString().equals("There is no records."))
                    {
                        TextView textView = (TextView)findViewById(R.id.errortext);

                        textView.setText("there is no record!");
                    }



                    JSONArray jUser = jobj.getJSONArray("inboxpvtmsg");
                    jsonResult = jUser.toString();

                    for (int i = 0; i < jUser.length(); i++) {
                        JSONObject c = jUser.getJSONObject(i);


                        // Phone node is JSON Object
                        String name = c.getString("fromname");
                        String date = c.getString("msgtime") ;
                        String message = c.getString("msgtext");


                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("fromname", name);
                        contact.put("msgtime", date);
                        contact.put("msgtext", message);

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
        adapter=new InboxAdapter(getApplicationContext(), R.layout.row_inbox);


        final ListAdapter adapter = new SimpleAdapter(
                Inbox.this, inboxlist,
                R.layout.row_inbox, new String[]{"fromname", "msgtime","msgtext"}, new int[]{R.id.user_name_inbox,
                R.id.date_inbox, R.id.message});
                listView.setAdapter(adapter);

    }

    public class InboxAdapter extends ArrayAdapter {
        List list=new ArrayList();
        private Context mContext;

        public InboxAdapter(@NonNull Context mContext, @LayoutRes int resource ) {
            super(mContext, resource);
            this.mContext = mContext;

        }

        class DataHandler
        {
            TextView name;
            TextView date_inbox;
            TextView message;
            Button deletebtn;
        }
        @Override
        public void add(@Nullable Object object) {
            super.add(object);
            list.add(object);
        }





        public String getName(String name) {
            return name;
        }

        @Override
        public int getCount() {
            return this.list.size();
        }




        @Nullable
        @Override
        public Object getItem(int position) {
            return this.list.get(position);
        }



        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row;
            row=convertView;
            DataHandler handler;
            if(convertView==null)
            {

                LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.row_inbox,parent,false);
                handler=new DataHandler();
                handler.name=(TextView)row.findViewById(R.id.user_name_inbox);
                handler.date_inbox=(TextView)row.findViewById(R.id.date_inbox);
                handler.message=(TextView)row.findViewById(R.id.message);
                handler.deletebtn=(Button) row.findViewById(R.id.deletebtn);



                row.setTag(handler);

            }
            else {
                handler=(DataHandler)row.getTag();
            }
            handler.deletebtn.setTag(Integer.valueOf(position));

            SearchInbox item;
            item=(SearchInbox) this.getItem(position);
            handler.name.setText(item.getname());
            handler.date_inbox.setText(item.getDate());
            handler.message.setText(item.getMessage());
            Button deleteBtn = (Button)row.findViewById(R.id.deletebtn);

            return row;
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


    public void back(View a) {
        finish();
    }
    public void delete(View v){
        ListView listview1;
        listView=(ListView)findViewById(R.id.listView_inbox);

        final int position = listView.getPositionForView((View) v.getParent());
//        Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();

    }

//
//    private void showDialog() {
//        if (!progressDialog.isShowing())
//            progressDialog.show();
//    }
//    private void hideDialog() {
//        if (progressDialog.isShowing())
//            progressDialog.dismiss();
//    }
}
