package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.global_draftmessage;

public class Drafts extends AppCompatActivity {

    private static final String TAG = "Inbox";
    ListView listView;
//    InboxAdapter adapter;
    private static String URL_FOR_DETAIL = "";
//    ArrayList<HashMap<String, String>> inboxlist;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/getdraftsPvtmsg.php";
    private static final String URL_FOR_DELETEDRAFT = "http://nayarishta.com/nayarishta-app/api/deleteDrafts.php";

    ProgressDialog progressDialog;
    public static String channel = "";
    public static String USERNAME = "";
    SharedPreferences sharedpreferencestwo;

    public static String disname="",jsonResult="";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private List<DraftItems> itemsList;
    private JSONArray result;
    public String userprofileID="";
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts);

        //        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
//        inboxlist = new ArrayList<>();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        textView = (TextView)findViewById(R.id.nummtext);
        textView.setVisibility(View.GONE);

        itemsList = new ArrayList<>();

        adapter = new DraftAdapter(itemsList, getApplicationContext());
        recyclerView.setAdapter(adapter);

//        api();



//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                // TODO Auto-generated method stub
////                Log.d("############","Items " +  MoreItems[arg2] );
//                Toast.makeText(getApplicationContext(),"id",Toast.LENGTH_LONG).show();
//            }
//
//        });

        loadDraft();

    }

    public void loadDraft()
    {
        String cancel_req_tag = "cancle";
        progressDialog.setMessage("Please wait a moment");
        showDialog();
        SharedPreferences shared = getSharedPreferences(Tryfragment, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_LOGIN+"?userid="+channel, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Boolean error = jsonObject.getBoolean("error");
                    String msg = jsonObject.getString("message");
                    if (!error){
                        if (!msg.equals("There is no records.")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("draftspvtmsg");

                            jsonResult = jsonArray.toString();

                            for (int i = 0; i<=jsonArray.length();i++){
                                JSONObject jObj = jsonArray.getJSONObject(i);
                                DraftItems draftItems = new DraftItems(jObj.getString("toname"),jObj.getString("msgtime"),jObj.getString("msgsubject"),jObj.getString("msgtext"),jObj.getString("msgid"), getApplicationContext());
                                adapter.notifyDataSetChanged();
                                itemsList.add(draftItems);

                            }

                            }
                            else {
                            textView.setVisibility(View.VISIBLE);
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



    public void deleteDraft(final String msgid)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Drafts.this);
        builder1.setMessage("Are you sure want to delete this message?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                        String cancel_req_tag = "cancle";
                        progressDialog.setMessage("Please wait a moment");
                        showDialog();


                        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_DELETEDRAFT+"?msgid="+msgid, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                hideDialog();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();


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
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public void opendraftmsg(String toname, String subject, String message){

        global_draftmessage = "1";
//        Toast.makeText(getApplicationContext(),position,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), Compose.class);
            intent.putExtra("toname", toname);
            intent.putExtra("subject", subject);
            intent.putExtra("message",message);
            startActivity(intent);
    }

    private void getDraftmsg(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(URL_FOR_LOGIN+"?userid="+userprofileID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("draftspvtmsg");
                            JSONObject c = result.getJSONObject(0);
                            String name = c.getString("toname");

                            Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();




                            //Calling method getStudents to get the students from the JSON Array
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



//    private void loginUser() {
//
//        // Tag used to cancel the request
//        String cancel_req_tag = "search";
//        progressDialog.setMessage("please wait a moment");
//        showDialog();
//        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//        String channel = (shared.getString("user_id", ""));
//        URL_FOR_DETAIL = URL_FOR_LOGIN + "?userid="+channel;
//
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                URL_FOR_DETAIL, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                hideDialog();
//                try {
//
//
//
//                    JSONObject jobj = new JSONObject(response);
//                    String error = jobj.getString("message");
//
//                    jsonResult = response;
//                    if (error.toString().equals("There is no records.")) {
//                        TextView textView = (TextView) findViewById(R.id.errortext);
//
//                        textView.setText("there is no record!");
//                    }
//
//                    JSONArray jUser = jobj.getJSONArray("draftspvtmsg");
//
//                    for (int i = 0; i < jUser.length(); i++) {
//                        JSONObject c = jUser.getJSONObject(i);
//
//
//
//                        // Phone node is JSON Object
//                        String name = c.getString("toname");
//                        String date = c.getString("msgtime");
//                        String message = c.getString("msgtext");
//
//
//                        HashMap<String, String> contact = new HashMap<>();
//
//                        // adding each child node to HashMap key => value
//                        contact.put("toname", name);
//                        contact.put("msgtime", date);
//                        contact.put("msgtext", message);
//
//                        inboxlist.add(contact);
//
//                        adapter.notifyDataSetChanged();
//                        listView.invalidateViews();
//
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.print(e);
//                }
//                adapter.notifyDataSetChanged();
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Search Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        });
//
//        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        // Adding request to request queue
//        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
//    }


//    public void api() {
//        loginUser();
//
//        listView = (ListView) findViewById(R.id.listView_draft);
//
//        int i = 0;
//        adapter = new InboxAdapter(getApplicationContext(), R.layout.row_inbox);
//
//
//        final ListAdapter adapter = new SimpleAdapter(
//                Drafts.this, inboxlist,
//                R.layout.row_inbox, new String[]{"toname", "msgtime", "msgtext"}, new int[]{R.id.user_name_inbox,
//                R.id.date_inbox, R.id.message});
//
//
//        listView.setAdapter(adapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),"f",Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }

//    public void delete(View v){
//        ListView listview1;
//        listView=(ListView)findViewById(R.id.listView_draft);
//
//        final int position = listView.getPositionForView((View) v.getParent());
////        Toast.makeText(getApplicationContext(),String.valueOf(position),Toast.LENGTH_LONG).show();
//
//    }


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

    public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {

        private List<DraftItems>listItems;
        private Context context;
        public DraftAdapter(List<DraftItems> listItems, Context context) {
            this.listItems = listItems;
            this.context = context;
        }

        @Override
        public DraftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox,parent,false);

            return new DraftAdapter.ViewHolder(view);

        }

        @Override
        public void onBindViewHolder(DraftAdapter.ViewHolder holder, final int position) {

            final DraftItems listItem = listItems.get(position);

            holder.nametxt.setText(listItem.getName());
            holder.datetxt.setText(listItem.getDate());
            holder.msgtxt.setText(listItem.getMessage());


            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Drafts)v.getContext()).opendraftmsg(listItem.getName(),listItem.getSubject(),listItem.getMessage());
                }
            });
            holder.deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Drafts.this);
                    builder1.setMessage("Are you sure want to delete this message?");
                    builder1.setCancelable(true);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    String cancel_req_tag = "cancle";
                                    progressDialog.setMessage("Please wait a moment");
                                    showDialog();


                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_FOR_DELETEDRAFT+"?msgid="+listItem.getMsgid(), new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            hideDialog();
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                listItems.remove(position);
                                                notifyItemRemoved(position);
                                                notifyItemRangeChanged(position,listItems.size());
//                                                ((Drafts)v.getContext()).deleteDraft(listItem.getMsgid());
                                                Toast.makeText(getApplicationContext(),"deleted",Toast.LENGTH_LONG).show();


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
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();


                }
            });
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            public TextView nametxt , datetxt, msgtxt;
            public RelativeLayout layout, deletebtn;
            public ViewHolder(View itemView) {
                super(itemView);

                nametxt = (TextView)itemView.findViewById(R.id.user_name_inbox);
                datetxt = (TextView)itemView.findViewById(R.id.date_inbox);
                msgtxt = (TextView)itemView.findViewById(R.id.message);
                layout = (RelativeLayout)itemView.findViewById(R.id.draftitem);
                deletebtn = (RelativeLayout)itemView.findViewById(R.id.deletebtn);


            }
        }
    }

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
