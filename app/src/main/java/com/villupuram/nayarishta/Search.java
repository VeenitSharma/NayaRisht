package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.villupuram.nayarishta.constant.base_api_url.ProfessionName;

public class Search extends AppCompatActivity{


    private static final String TAG = "SearchActivity";
    private static String URL_FOR_SEARCH = "";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/search.php";
    ProgressDialog progressDialog;
    Button search;
    ImageButton fbtn, mbtn;
    String male, female, Gender="F";
    SeekBar seekBar;
    TextView seekbartext,seekbar_right;
    RangeBar rangeBar;
    private JSONArray result;
    public static String religiouslist="", occupationlist="";
    private ArrayList<String> religiousarray;
    private ArrayList<String> Profession;
//    ArrayList<HashMap<String, String>> contactList;
    public static String heightidfrom = "",heightidto="";

    Spinner s1, s2, heightfromm, heighttoo;
    public static String religious_id=null, occupation_id=null;
    private ArrayList<String> heightfromarray;
    private ArrayList<String> heitoarray;
    public static String heightlist = "";

    public void back(View view){
        finish();
}
    String agefrom="18", ageto="70";
    CrystalRangeSeekbar rangeSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search);
//        contactList = new ArrayList<>();


        heightfromarray = new ArrayList<String>();
        heitoarray = new ArrayList<String>();
        religiousarray = new ArrayList<String>();
        Profession = new ArrayList<String>();
        s1 =(Spinner)findViewById(R.id.spinnerone);
        s2 =(Spinner)findViewById(R.id.spinner2);
        search = (Button) findViewById(R.id.search);
        seekbartext = (TextView)findViewById(R.id.seekbartext);
        seekbar_right = (TextView)findViewById(R.id.seekbar_right);
        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.seekbar2);
//        rangeBar = (RangeBar)findViewById(R.id.seekbar2);


        heighttoo = (Spinner) findViewById(R.id.heightto);
        heightfromm = (Spinner) findViewById(R.id.heightfrom);

        fbtn = (ImageButton) findViewById(R.id.female_icon);
        mbtn = (ImageButton) findViewById(R.id.male_icon);



        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbtn.setBackgroundResource(R.drawable.female_selected);
                mbtn.setBackgroundResource(R.drawable.male_unslected);
                Gender ="F";

            }
        });

        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbtn.setBackgroundResource(R.drawable.female_unselected);
                mbtn.setBackgroundResource(R.drawable.male_selected);

                Gender ="M";
            }
        });

//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
//
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progress = progress / 10;
//                progress = progress * 10;
//                seekbartext.setText(String.valueOf(progress));
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });


        heightfromm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusd = jsonn.getString("heightid");
                        heightidfrom = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        heighttoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusd = jsonn.getString("heightid");
                    heightidto = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
//                                              int rightPinIndex,
//                                              String leftPinValue, String rightPinValue) {
//
//                System.out.print(String.valueOf(leftPinIndex));
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

        System.out.print(Gender);


//        / Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


//        toolbar.setNavigationIcon(R.drawable.arrowwhite);

//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.actionbar_search);
//        ViewSome view =getSupportActionBar().getCustomView();

        search.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

//                submitForm();
                 agefrom = seekbartext.getText().toString();
                 ageto = seekbar_right.getText().toString();
                String profess = s2.getSelectedItem().toString();
                System.out.print(Gender);
//                String hid = heightidfrom;
//                String hto = heightidto;
//




//                 if (religious_id.equals("")&&(profess.equals("")))
//                {
//                    String occupation=null;
//                    String religious = null;
//
//                    loginUser(Gender,agefrom,ageto,heightidfrom,heightidto,religious,occupation);
//                }
//                else if(religious_id.equals("")){
//                    String religious = null;
//                    loginUser(Gender,agefrom,ageto,heightidfrom,heightidto,religious,profess);
//                }
//                else if(profess.equals("Select Occupation"))
//                {
//                    String occupation=null;
//                    loginUser(Gender,agefrom,ageto,heightidfrom,heightidto,religious_id,occupation);
//                }
//
//                else {
//                    String religious = religious_id;
                    loginUser(Gender,agefrom,ageto,heightidfrom,heightidto,religious_id,occupation_id);
//                }

            }
        });

//        loginUser(Gender,agefrom,ageto,religious,profess);
        getReligious();
        getprofessionlist();


        heightfrom();
        heightto();
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                String text = s1.getSelectedItem().toString();

                if (text.equals("Select Religion")){
                    religious_id=null;
                }
                else {

                    Integer value = s1.getSelectedItemPosition();

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(religiouslist);
                        result = jsonResponse.getJSONArray("language");
                        JSONObject jsonn = result.getJSONObject(value-1);

                        String valusd = jsonn.getString("religionid");
                        religious_id = text;


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                String text = s2.getSelectedItem().toString();

                if (text.equals("Select Occupation")){
                    occupation_id=null;
                }
                else {

                    Integer value = s1.getSelectedItemPosition();

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(occupationlist);
                        result = jsonResponse.getJSONArray("profession");
                        JSONObject jsonn = result.getJSONObject(value-1);

                        String valusd = jsonn.getString("id");
                        occupation_id = text;


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

    }



    private void submitForm() {



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


                            getListReligious(result);
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
    private void getListReligious(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("religionid");

                System.out.print(reli_ID);


                religiousarray.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        religiousarray.add(0, "Select Religion");
        //Setting adapter to show the items in the spinner
        s1.setAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_dropdown_item, religiousarray));
    }


    //profession
    private void getprofessionlist(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.PROFESSION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            occupationlist = response;
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("profession");


                            getprofession(result);
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
    //profession
    private void getprofession(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                Profession.add(json.getString("profession"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        Profession.add(0, "Select Occupation");
        //Setting adapter to show the items in the spinner
        s2.setAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_dropdown_item, Profession));
        s2.setSelection(Profession.indexOf(ProfessionName));
    }

    private void loginUser(final String Gender,final String agefrom,final String ageto, final String heightfrom,final String heightto,final String riligion, final String occupation) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");
        if (!heightfrom.equals("")&&!heightto.equals("") && Integer.parseInt(heightfrom) > Integer.parseInt(heightto)) {
            Toast.makeText(getApplicationContext(), "invalid height range", Toast.LENGTH_LONG).show();
        } else {
            URL_FOR_SEARCH = URL_FOR_LOGIN + "?lookingfor=" + Gender + "&agefrom=" + agefrom + "&ageto=" + ageto + "&heightfrom=" + heightfrom + "&heightto=" + heightto + "&religion=" + riligion + "&occupation=" + occupation;

            String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
          String  BasicSearch = Uri.encode(URL_FOR_SEARCH, ALLOWED_URI_CHARS);
          showDialog();
            StringRequest strReq = new StringRequest(Request.Method.GET,
                    BasicSearch, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    hideDialog();
                    try {

                        JSONObject jsonObj = new JSONObject(response);


                        String message = jsonObj.getString("message");

                        if (message.equals("There is no profile matched for the selected criteria.")) {

                            Toast.makeText(getApplicationContext(), "No Records Found!", Toast.LENGTH_LONG).show();
                        } else {
                            JSONArray jObj = jsonObj.getJSONArray("users");

                            Intent intent = new Intent(getApplicationContext(), SearchResults.class);
                            intent.putExtra("json", jObj.toString());
                            intent.putExtra("identity", "search");
                            intent.putExtra("Gender", Gender);
                            intent.putExtra("agefrom", agefrom);
                            intent.putExtra("ageto", ageto);
                            intent.putExtra("heightfrom", heightfrom);
                            intent.putExtra("heightto", heightto);
                            intent.putExtra("riligion", riligion);
                            intent.putExtra("occupation", occupation);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void heightfrom() {
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

                            for (int i = 0; i < result.length(); i++) {

                                try {

                                    JSONObject json = result.getJSONObject(i);

                                    String feet = json.getString("feet");
                                    String inch = json.getString("inch");
                                    String cm = json.getString("cm");

                                    //Getting json object

                                    heightfromarray.add(feet + "'" + "" + inch + "'" + "-" + cm + "cm");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            heightfromarray.add(0, "Select");
                            //Setting adapter to show the items in the spinner
                            heightfromm.setAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_dropdown_item, heightfromarray));


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

    private void heightto() {
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

                            for (int i = 0; i < result.length(); i++) {

                                try {

                                    JSONObject json = result.getJSONObject(i);

                                    String feet = json.getString("feet");
                                    String inch = json.getString("inch");
                                    String cm = json.getString("cm");

                                    //Getting json object

                                    heitoarray.add(feet + "'" + "" + inch + "'" + "-" + cm + "cm");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            heitoarray.add(0, "Select");
                            //Setting adapter to show the items in the spinner
                            heighttoo.setAdapter(new ArrayAdapter<String>(Search.this, android.R.layout.simple_spinner_dropdown_item, heitoarray));


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




    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }


}
