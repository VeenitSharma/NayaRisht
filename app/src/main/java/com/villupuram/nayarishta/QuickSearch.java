package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appyvet.rangebar.RangeBar;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.constant.base_api_url.CastName;

public class QuickSearch extends AppCompatActivity {

    private static final String TAG = "QuickSearch";
    private static String URL_FOR_SEARCH = "";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/full-search.php";
    private static final String MaritalStatus = "http://nayarishta.com/nayarishta-app/api/maritalstatus.php";
    private static final String Religious = "http://nayarishta.com/nayarishta-app/api/religious.php";

    private JSONArray result;
    private ArrayList<String> Religiouss;
    private ArrayList<String> heightfromarray;
    private ArrayList<String> heitoarray;
    private ArrayList<String> castearray;
    final ArrayList<MaritalClass> maritallist = new ArrayList<MaritalClass>();

    ProgressDialog progressDialog;
    Spinner maritalstatus, religious,caste,heightto,heightfrom;
    String Gender="";
    final ArrayList<ColorVO> colorList = new ArrayList<ColorVO>();
    TextView mothertext,maritalstatustext;
    TextView seekbartext,seekbar_right;
    RangeBar rangeBar;
    public static String religiouslist="";
    public static String heightlist="";
    public static String heightidfrom="",heightidto="";
    public static String gotralist="";
    public static String castelist="";
    public static String religious_id="";
    public static String caste_id="";
    public static String subcaste_id="";
    CrystalRangeSeekbar rangeSeekbar;

    Button searchbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_search);

        religious =(Spinner)findViewById(R.id.religious);
        caste =(Spinner)findViewById(R.id.castespinner);
        heightto =(Spinner)findViewById(R.id.heightto);
        heightfrom =(Spinner)findViewById(R.id.heightfrom);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Religiouss = new ArrayList<String>();
        castearray = new ArrayList<String>();
        heightfromarray = new ArrayList<String>();
        heitoarray = new ArrayList<String>();

        seekbartext = (TextView)findViewById(R.id.seekbartext);
        seekbar_right = (TextView)findViewById(R.id.seekbar_right);
        maritalstatustext = (TextView) findViewById(R.id.maritalstatus);
        searchbtn = (Button)findViewById(R.id.searchbtn);
        RelativeLayout maritalstatuslayout = (RelativeLayout) findViewById(R.id.addmstatusss);

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.seekbar2);


        // String array for alert dialog multi choice items
        final String[] colors = new String[]{
                "Dosen't Matter",
                "Hindi",
                "Marathi",
                "Bengali",
                "Gujrati",
                "malayalam"
        };
        // Boolean array for initial selected items
        final boolean[] checkedColors = new boolean[]{
                false,false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false // Olive

        };


        final String[] mstatuss = new String[]{
                "Dosen't Matter",
                "Never Married",
                "Divorced",
                "Awaiting Divorced",

        };
        final boolean[] checkedsmstatus = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue

        };

        getReligious();

        Intent i = getIntent();
        ArrayList<String> list = i.getStringArrayListExtra("key");
        if (list!=null){

            Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_LONG).show();

        }


//        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
//                                              int rightPinIndex,
//                                              String leftPinValue, String rightPinValue) {
//
//
//                seekbartext.setText(leftPinValue);
//                seekbar_right.setText(rightPinValue);
//
//            }
//        });
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
//                tvMin.setText(String.valueOf(minValue));
//                tvMax.setText(String.valueOf(maxValue));
                seekbartext.setText(String.valueOf(minValue));
                seekbar_right.setText(String.valueOf(maxValue));

            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
        caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = caste.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(castelist);
                    result = jsonResponse.getJSONArray("religion");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("religionid");
                    caste_id = valusd;

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        religious.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = religious.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(religiouslist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("religionid");
                    religious_id = valusd;

                    getstates();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        heightfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("heightid");
                    heightidfrom = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        heightto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("heightid");
                    heightidto = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        maritalstatuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuickSearch.this);

                // make a list to hold state of every color
                for (int i = 0; i < mstatuss.length; i++) {
                    MaritalClass MaritalClass = new MaritalClass();
                    MaritalClass.setName(mstatuss[i]);
                    MaritalClass.setSelected(checkedsmstatus[i]);
                    maritallist.add(MaritalClass);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(mstatuss, checkedsmstatus, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedsmstatus.length; i++) {
                                checkedsmstatus[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                maritallist.get(which).setSelected(isChecked);
                                maritallist.get(i).setSelected(false);


                            }



                        } else {
                            checkedsmstatus[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            maritallist.get(which).setSelected(isChecked);
                            maritallist.get(0).setSelected(false);
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Marital Status");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        maritalstatustext.setText("");

                        // save state of selected vos
                        ArrayList<MaritalClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < maritallist.size(); i++) {
                            MaritalClass IncomeClass = maritallist.get(i);
                            mstatuss[i] = IncomeClass.getName();
                            checkedsmstatus[i] = IncomeClass.isSelected();
                            if (IncomeClass.isSelected()) {
                                selectedList.add(IncomeClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                maritalstatustext.setText(maritalstatustext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                maritalstatustext.setText(maritalstatustext.getText() + selectedList.get(i).getName());
                        }
                        maritallist.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        maritallist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


//        getheights();
//        getfromgeight();
        heightfrom();
        heightto();



        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String agrfrm = seekbartext.getText().toString();
//                String agrto = seekbar_right.getText().toString();
//                String hid = heightidfrom;
//                String hto = heightidto;
//                String mstatus =maritalstatustext.getText().toString();
//                String religionid = religious_id;
//                String caste_idc = caste_id;
//
//                loginUser(agrfrm,agrto,hid,hto,mstatus,religionid,caste_idc);

                searfdch();

            }
        });

    }

    public void searfdch(){

        String agrfrm = seekbartext.getText().toString();
        String agrto = seekbar_right.getText().toString();
        String hid = heightidfrom;
        String hto = heightidto;
        String mstatus ="";
        String religionid = religious_id;
        String caste_idc = caste_id;

        if (maritalstatustext.getText().toString().equals("Dosen't Matter"))
        {
            mstatus="";
        }
        else {
            mstatus =maritalstatustext.getText().toString();
        }
//        loginUser(agrfrm,agrto,hid,hto,mstatus,religionid,caste_idc);
       if (heightidfrom.equals("")||heightidto.equals(""))
        {
            Toast.makeText(getApplicationContext(),"Please select Height",Toast.LENGTH_LONG).show();
        }else {
           loginUser(agrfrm,agrto,hid,hto,mstatus,religionid,caste_idc);
       }

    }


    private void getstates(){
        castearray.clear();
        progressDialog.setMessage("Please wait a moment");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.CASTE+"?religionid="+religious_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        JSONObject j = null;
                        try {
                            castelist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("religion");


                            //Calling method getStudents to get the students from the JSON Array
                            getallstates(result);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getallstates(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String caste_id =  json.getString("religionid");

                System.out.print(caste_id);


                castearray.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        castearray.add(0, "Select");
        //Setting adapter to show the items in the spinner
        caste.setAdapter(new ArrayAdapter<String>(QuickSearch.this, android.R.layout.simple_spinner_dropdown_item, castearray));
    }

    private class ColorVO {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private class MaritalClass {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }


    }




    private void getReligious(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.RELIGIOUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            religiouslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getAllReligious(result);
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getAllReligious(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));

                Religiouss.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Religiouss.add(0, "Select");
        //Setting adapter to show the items in the spinner
        religious.setAdapter(new ArrayAdapter<String>(QuickSearch.this, android.R.layout.simple_spinner_dropdown_item, Religiouss));
    }

    private void heightfrom(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            heightlist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");

                            for(int i=0;i<result.length();i++){

                                try {

                                    JSONObject json = result.getJSONObject(i);

                                    String feet= json.getString("feet");
                                    String inch= json.getString("inch");
                                    String cm= json.getString("cm");

                                    //Getting json object

                                    heightfromarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            heightfromarray.add(0, "Select");
                            //Setting adapter to show the items in the spinner
                            heightfrom.setAdapter(new ArrayAdapter<String>(QuickSearch.this, android.R.layout.simple_spinner_dropdown_item, heightfromarray));


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

    private void heightto(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            heightlist = response;
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");

                            for(int i=0;i<result.length();i++){

                                try {

                                    JSONObject json = result.getJSONObject(i);

                                    String feet= json.getString("feet");
                                    String inch= json.getString("inch");
                                    String cm= json.getString("cm");

                                    //Getting json object

                                    heitoarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            heitoarray.add(0, "Select");
                            //Setting adapter to show the items in the spinner
                            heightto.setAdapter(new ArrayAdapter<String>(QuickSearch.this, android.R.layout.simple_spinner_dropdown_item, heitoarray));


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




    private void getcaste(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.CASTE+"?religionid="+religious_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array

                            result = j.getJSONArray("religion");



                            getAllcaste(result);
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
    private void getAllcaste(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));

                Religiouss.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        Religiouss.add(0, "Select");
        //Setting adapter to show the items in the spinner
        religious.setAdapter(new ArrayAdapter<String>(QuickSearch.this, android.R.layout.simple_spinner_dropdown_item, Religiouss));
    }



    private void loginUser(final String agefrom, final String ageto, final String heightfrom,final String heightto,final String maritalstatus,final String religionid,final String casteid) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";

        if (Integer.parseInt(heightidfrom) > Integer.parseInt(heightidto)) {
            Toast.makeText(getApplicationContext(), "invalid age range", Toast.LENGTH_LONG).show();
        } else {



            progressDialog.setMessage("Searching..");



            showDialog();

            URL_FOR_SEARCH = URL_FOR_LOGIN + "?agefrom=" + agefrom + "&ageto=" + ageto + "&heightfrom=" + heightfrom + "&heightto=" + heightto + "&maritalstatus=" + maritalstatus + "&religionid=" + religionid + "&casteid=" + casteid;

        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        String urlEncoded = Uri.encode(URL_FOR_SEARCH, ALLOWED_URI_CHARS);



            StringRequest strReq = new StringRequest(Request.Method.GET,
                    urlEncoded, new Response.Listener<String>() {



                @Override
                public void onResponse(String response) {
                    hideDialog();
                    try {
                        JSONObject jsonObj = new JSONObject(response);

                        String msg = jsonObj.getString("message");

                        if (msg.equals("There is no profile matched for the selected criteria.")) {
                            Toast.makeText(QuickSearch.this, "No profile matched for the selected criteria.", Toast.LENGTH_LONG).show();
                        } else {

                            JSONArray jObj = jsonObj.getJSONArray("users");
                            Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                            intent.putExtra("json", jObj.toString());
                            intent.putExtra("identity", "quicksearch");
                            intent.putExtra("agefrom", agefrom);
                            intent.putExtra("ageto", ageto);
                            intent.putExtra("heightfrom", heightfrom);
                            intent.putExtra("heightto", heightto);
                            intent.putExtra("maritalstatus", maritalstatus);
                            intent.putExtra("religionid", religionid);
                            intent.putExtra("casteid", casteid);



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



    public void back(View a)
    {
        finish();
    }


}
