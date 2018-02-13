package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.SplashLocationDetails.SplashLocation;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.try_One.countryname;
import static com.villupuram.nayarishta.try_One.try_one;

public class Edit_Location extends AppCompatActivity implements Spinner.OnItemSelectedListener{
    //Declaring an Spinner
    private Spinner spinner, Scity,Sstate;
    private final String TAG = "Edit_Location";
    //An ArrayList for Spinner Items
    ProgressDialog progressDialog;
    public static String channel="";
    Spinner h1, h2,m1;
    EditText residence;
        private ArrayList<String> contries;
    private ArrayList<String> state;
    private ArrayList<String> allcity;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/locationDetailsUpdate.php";
    public static String countryList="";
    public static String statuslist="";
    public static String citylist="";
    public static String country_id="";
    public static String state_id="";
    public static String city_id="";
    private static String URL_FOR_DETAIL = "",country="",county="",city="",resistatus="";
    ImageView UserPhoto;
    String residencystatus="";
    //JSON Array
    private JSONArray result;
    int currentItem = 0;
    String picturePath;
    private String UPLOAD_URL ="http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int PICK_IMAGE_REQUEST = 1;
    String one;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__location);

        //Initializing the ArrayList
        contries = new ArrayList<String>();
        state = new ArrayList<String>();
        allcity = new ArrayList<String>();

        UserPhoto = (ImageView)findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);


        spinner = (Spinner) findViewById(R.id.countrylocation);
        Sstate = (Spinner) findViewById(R.id.state);
        spinner.setOnItemSelectedListener(this);
        Scity = (Spinner) findViewById(R.id.citlocationy);
        Button save = (Button)findViewById(R.id.savebtn);

        h1 =(Spinner)findViewById(R.id.countrylocation);
        h2 =(Spinner)findViewById(R.id.state);
        m1 =(Spinner)findViewById(R.id.citlocationy);
        residence = (EditText) findViewById(R.id.residence);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);


        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic_details);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });


        spinner.setPrompt("Select Country");
        Sstate.setPrompt("Select State");
        Scity.setPrompt("Select City");

        Intent intent = getIntent();
        one = intent.getStringExtra("logintype");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resi = residence.getText().toString();
                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (preferences.getString("user_id", ""));




                registerUser(channel,country_id,state_id,city_id,resi);
            }
        });
//do anything you want getData();

        loginUser();

        getData();



        Sstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Integer value = Sstate.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(statuslist);
                    result = jsonResponse.getJSONArray("state");
                    JSONObject jsonn = result.getJSONObject(value);

                    String valusdd = jsonn.getString("id");
                    state_id = valusdd;
                    getcity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        Scity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Integer value = Scity.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(citylist);
                    result = jsonResponse.getJSONArray("city");
                    JSONObject jsonn = result.getJSONObject(value);

                    String valusdd = jsonn.getString("id");
                    city_id = valusdd;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        requestStoragePermission();




        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Edit_Location.this,TryFragmentActivity.class);
                startActivity(intent);
            }
        });

    }



    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");

        showDialog();
        SharedPreferences shared = getSharedPreferences(SplashLocation ,MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        SharedPreferences sharede = getSharedPreferences(MyPREFERENCES ,MODE_PRIVATE);
        String channell = (sharede.getString("user_id", ""));

        if (channel.toString().equals(null))
        {
            URL_FOR_DETAIL = base_api_url.USER_DETAIL + channell;
        }
        else {
            URL_FOR_DETAIL = base_api_url.USER_DETAIL + channel;
        }


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



                    country =  c.getString("countryname");
                    county =  c.getString("statename");
                    city =  c.getString("cityname");
                    resistatus =  c.getString("residencystatus");

                    String photourl = c.getString("userphoto");

                    String gender = c.getString("gender");
                    PhotoUrl = photourl;


                    if (!photourl.equals("")){

                        Picasso.with(getApplicationContext()).load(photourl).placeholder(R.drawable.placeholder).resize(80,80).into(UserPhoto);
                    }
                    else {
                        if (gender.toString().equals("F")){
                            UserPhoto.setImageResource(R.drawable.femaledefault);
                        }
                        else {
                            UserPhoto.setImageResource(R.drawable.mandefault);
                        }
                    }

                    residence.setText(resistatus);


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
        };
//         Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    public void uploadMultipart() {
        //getting name for the image
        String name ="1179";

        //getting the actual path of the image
        String path = getPath(filePath);

        //Uploading code
        try {
            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            String  channel = (shared.getString("user_id", ""));

            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "file") //Adding file
                    .addParameter("userid", channel) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 1);
    }




    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            UserPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            new ImageUploadTask().execute();
        }
    }
    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(Edit_Location.this);
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String  channel = (shared.getString("user_id", ""));
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Uploading...");
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(getApplicationContext(), uploadId, UPLOAD_URL)
                        .addFileToUpload(picturePath, "file") //Adding file
                        .addParameter("userid", channel) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload();
            } catch (Exception e) {
                // something went wrong. connection with the server error
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

            Toast.makeText(getApplicationContext(), "Image uploaded",Toast.LENGTH_LONG).show();
        }
    }
    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void registerUser(final String userid, final String countryid , final String stateid, final String cityid,final String residencystatus) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Saving...");
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
//                    String msg = jObj.getString("message");
                        String msg = jObj.getString("message");
                        Intent intent = new Intent(getApplicationContext(), EditProfile_adminMessage.class);
                        intent.putExtra("msg",msg);
                        startActivity(intent);

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
                params.put("country", countryid);
                params.put("state", stateid);
                params.put("city", cityid);
                params.put("residencystatus", residencystatus);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
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


    private void getData(){
        //Creating a string request
        state.clear();
        StringRequest stringRequest = new StringRequest(Config_location.DATA_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            countryList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY);


                            getStudents(result);
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
    private void getstates(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_STATE+"countryid="+country_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            statuslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_STATES);


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
    private void getcity(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_CITY+"stateid="+state_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            citylist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_CITY);


                            //Calling method getStudents to get the students from the JSON Array
                            getallcities(result);
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
    private void getStudents(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                contries.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



//            contries.add(0, "Select Country");

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(Edit_Location.this, android.R.layout.simple_spinner_dropdown_item, contries));
            spinner.setSelection(contries.indexOf(country));

    }
    private void getallstates(JSONArray j){
        //Traversing through all the items in the json array
        allcity.clear();
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                state.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        Sstate.setAdapter(new ArrayAdapter<String>(Edit_Location.this, android.R.layout.simple_spinner_dropdown_item, state));

            Sstate.setSelection(state.indexOf(county));


    }
    private void getallcities(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                allcity.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//
//        if (allcity.get(0)!="Select city") {
//            allcity.add(0, "Select city");
//        }

        Scity.setAdapter(new ArrayAdapter<String>(Edit_Location.this, android.R.layout.simple_spinner_dropdown_item, allcity));

        Scity.setSelection(allcity.indexOf(city));
        //Setting adapter to show the items in the spinner



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        state.clear();
        allcity.clear();

        Spinner spinnerv = (Spinner) parent;
        if(spinnerv.getId() == R.id.countrylocation) {

            Integer value = spinner.getSelectedItemPosition();

            JSONObject jsonResponse = null;
            try {
                jsonResponse = new JSONObject(countryList);
                result = jsonResponse.getJSONArray("country");
                JSONObject jsonn = result.getJSONObject(value);

                String valusd = jsonn.getString("id");
                country_id = valusd;

                getstates();
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
//        else if(spinnerv.getId() == R.id.state)
//        {
//
//
//                final Integer value = Sstate.getSelectedItemPosition();
//
//                Toast.makeText(getApplicationContext(), statuslist, Toast.LENGTH_LONG).show();
//                JSONObject jsonResponse = null;
//                try {
//                    jsonResponse = new JSONObject(statuslist);
//                    result = jsonResponse.getJSONArray("state");
//                    JSONObject jsonn = result.getJSONObject(value - 1);
//
//                    String valusdd = jsonn.getString("id");
//                    state_id = valusdd;
//                    getcity();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//            }
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        spinner.setPrompt("Select...");
    }
}
