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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.CastName;
import static com.villupuram.nayarishta.constant.base_api_url.Gotra;
import static com.villupuram.nayarishta.constant.base_api_url.MotherTangue;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.ReligioNname;
import static com.villupuram.nayarishta.constant.base_api_url.SubcasteName;
import static com.villupuram.nayarishta.try_One.castename;
import static com.villupuram.nayarishta.try_One.religionname;
import static com.villupuram.nayarishta.try_One.try_one;

public class Edit_Religious_Details extends AppCompatActivity {

    //Declaring an Spinner
     Spinner religious, caste,subcaste, language;
    private final String TAG = "Edit_Location";
    //An ArrayList for Spinner Items
    ProgressDialog progressDialog;
    public static String channel="";
    EditText residence, gotratext;
    private ArrayList<String> religiousarray;
    private ArrayList<String> castearray;
    private ArrayList<String> subcastearray;
    private ArrayList<String> gotraarray;

    private ArrayList<String> Language;

    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/religiousInfoUpdate.php";


    public static String religiouslist="";
    public static String castelist="";
    public static String subcastelist="";
    public static String gotralist="";
    public static String religious_id="";
    public static String caste_id="";
    public static String subcaste_id="";
    public static String gotra_id="";
    private static String URL_FOR_DETAIL = "",religintext="",castetext="",subcastetext="",gotra="",mtanguetext="";


    ImageView UserPhoto;
    //JSON Array
    private JSONArray result;


    String mothertongue="";

    private String UPLOAD_URL ="http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int PICK_IMAGE_REQUEST = 1;

    String picturePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__religious__details);

        //Initializing the ArrayList
        religiousarray = new ArrayList<String>();
        castearray = new ArrayList<String>();
        subcastearray = new ArrayList<String>();
        gotraarray = new ArrayList<String>();
        Language = new ArrayList<String>();


        religious = (Spinner) findViewById(R.id.religious);
        caste = (Spinner) findViewById(R.id.caste);
        subcaste = (Spinner) findViewById(R.id.subcaste);
        language = (Spinner) findViewById(R.id.language);

        Button save = (Button)findViewById(R.id.saveReligion) ;

        gotratext = (EditText)findViewById(R.id.gotra);

        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        UserPhoto = (ImageView)findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);

        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic_details);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });

        getData();
        getLanguage();

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

                    getsubcaste();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        subcaste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = subcaste.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(subcastelist);
                    result = jsonResponse.getJSONArray("subcaste");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("ReligionId");
                    subcaste_id = valusd;

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resi = gotratext.getText().toString();
                String lang = language.getSelectedItem().toString();

                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (preferences.getString("user_id", ""));


                registerUser(channel,religious_id,caste_id,subcaste_id,resi,lang);
            }
        });
        requestStoragePermission();
        loginUser();


        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


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
        private ProgressDialog dialog = new ProgressDialog(Edit_Religious_Details.this);
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



    private void getData(){
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
    private void getStudents(JSONArray j){
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
        religious.setAdapter(new ArrayAdapter<String>(Edit_Religious_Details.this, android.R.layout.simple_spinner_dropdown_item, religiousarray));
        religious.setSelection(religiousarray.indexOf(ReligioNname));
    }
    private void getstates(){
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
        castearray.clear();
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


        //Setting adapter to show the items in the spinner
        castearray.add(0, "Select Caste");
        caste.setAdapter(new ArrayAdapter<String>(Edit_Religious_Details.this, android.R.layout.simple_spinner_dropdown_item, castearray));
        caste.setSelection(castearray.indexOf(CastName));
    }


    private void getsubcaste(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.SUBCASTE+"?religionid="+religious_id+"&casteid="+caste_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            subcastelist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("subcaste");


                            //Calling method getStudents to get the students from the JSON Array
                            getallcaste(result);
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
    private void getallcaste(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String subcaste_id =  json.getString("religionid");

                System.out.print(caste_id);



                subcastearray.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //Setting adapter to show the items in the spinner
        religiousarray.add(0, "Select Sub Caste");
        subcaste.setAdapter(new ArrayAdapter<String>(Edit_Religious_Details.this, android.R.layout.simple_spinner_dropdown_item, subcastearray));
        subcaste.setSelection(subcastearray.indexOf(SubcasteName));
    }

    private void getLanguage(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.LANGUAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
//                            religiouslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getallLanguage(result);
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
    private void getallLanguage(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//


                Language.add(json.getString("languagename"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        Language.add(0, "Select language");
        //Setting adapter to show the items in the spinner
        language.setAdapter(new ArrayAdapter<String>(Edit_Religious_Details.this, android.R.layout.simple_spinner_dropdown_item, Language));
        language.setSelection(Language.indexOf(MotherTangue));
    }




    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");

        showDialog();
        SharedPreferences shared = getSharedPreferences(RegisterPref,MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = base_api_url.USER_DETAIL + channel;

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



                    ReligioNname =  c.getString("religionname");
                    CastName =  c.getString("castename");
                    SubcasteName =  c.getString("subcastename");
                    Gotra =  c.getString("gotra");
                    MotherTangue =  c.getString("mothertongue");
//
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
                    gotratext.setText(Gotra);


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

        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//         Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void registerUser(final String userid, final String religionid , final String casteid, final String subcasteid,final String gotra, final String language) {
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
//                        String user = jObj.getJSONObject("user").getString("name");
                        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Saved!", Toast.LENGTH_SHORT).show();


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
                params.put("religionid", religionid);
                params.put("casteid", casteid);
                params.put("subcasteid", subcasteid);
                params.put("gotra", gotra);
                params.put("language", language);


                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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


    public void back(View a){
        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
        startActivity(intent);
    }
}
