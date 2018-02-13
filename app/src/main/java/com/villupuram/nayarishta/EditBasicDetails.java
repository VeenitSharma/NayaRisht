package com.villupuram.nayarishta;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
//import static com.villupuram.nayarishta.Login2.MyPREFERENCES;

import static android.content.Context.MODE_PRIVATE;
import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.Register.fmonth;
import static com.villupuram.nayarishta.SplashPersonalDetails.SplasgPI;
import static com.villupuram.nayarishta.constant.base_api_url.HeightID;
import static com.villupuram.nayarishta.constant.base_api_url.HeightText;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.try_One.try_one;

public class EditBasicDetails extends AppCompatActivity {

    EditText firstname,lastname;

    private final String TAG = "EditBasicDetails";
    private static String URL_FOR_SEARCH = "",DOB="";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/basicdetailsUpdate.php";
    ProgressDialog progressDialog;
    DatePickerDialog datePickerDialog;
    EditText date;
    Spinner heightBD,m1,children;
    public static String channel="",childdsp="";
   static TextView datebox;
    public static Integer fday=null,fmonth=null ,fyear=null;
    Calendar myCalendar = Calendar.getInstance();
    private static final String MaritalStatus = "http://nayarishta.com/nayarishta-app/api/maritalstatus.php";
    private JSONArray result;
    private ArrayList<String> Mstatus;
    private ArrayList<String> Religiouss;
    private static String URL_FOR_DETAIL = "",marital="",heightt="",maritalll="",dob="";
    public static String heightList="";
    public static String heightID="";
    private ArrayList<String> heightarray;
    ImageView UserPhoto;
    public static int age;
    private ArrayList<String> Childrenarray;
    private String UPLOAD_URL ="http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int PICK_IMAGE_REQUEST = 1;
    static String datetext = "";
    String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_basic_details);

        Mstatus = new ArrayList<String>();
        heightarray = new ArrayList<String>();
        Childrenarray = new ArrayList<String>();

        UserPhoto = (ImageView)findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);
        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);

        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });
        heightBD =(Spinner)findViewById(R.id.heightBD);
        m1 =(Spinner)findViewById(R.id.s2);
        firstname= (EditText)findViewById(R.id.fnamefield);
        lastname= (EditText)findViewById(R.id.lnamefield);
        Button save = (Button)findViewById(R.id.savebasic);
        datebox = (TextView) findViewById(R.id.date);
        children = (Spinner) findViewById(R.id.childrenspinner);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Childrenarray.add(0,"Select");
        Childrenarray.add(1,"0");
        Childrenarray.add(2,"1");
        Childrenarray.add(3,"2");
        Childrenarray.add(4,"3");
        Childrenarray.add(5,"4");
        children.setAdapter(new ArrayAdapter<String>(EditBasicDetails.this, android.R.layout.simple_spinner_dropdown_item, Childrenarray));




        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fday=dayOfMonth;
                fmonth=monthOfYear;
                fyear=year;

                updateLabel();
                updateDisplay();

            }

        };
//        datebox.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                new DatePickerDialog(EditBasicDetails.this, date, myCalendar
//                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//            }
//        });
        datebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });




//        Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();

        heightBD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = heightBD.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightList);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value);

                    String valusd = jsonn.getString("heightid");
                    HeightID = valusd;

                    String hname= heightBD.getSelectedItem().toString();


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        m1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                maritalll = m1.getSelectedItem().toString();
                if (maritalll.equals("Never Married")){
                    RelativeLayout childrenlayout = (RelativeLayout)findViewById(R.id.children);
                    TextView childrentext = (TextView)findViewById(R.id.have);

                    childrenlayout.setVisibility(View.GONE);
                    childrentext.setVisibility(View.GONE);
                }else {
                    RelativeLayout childrenlayout = (RelativeLayout)findViewById(R.id.children);
                    TextView childrentext = (TextView)findViewById(R.id.have);

                    childrenlayout.setVisibility(View.VISIBLE);
                    childrentext.setVisibility(View.VISIBLE);
                }


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        loginUser();
        getheights();
        getData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String havechildren = "0";
                String ch=null;
//                String occupation = h2.getSelectedItem().toString();
                children = (Spinner) findViewById(R.id.childrenspinner);

                String maritalstatus = m1.getSelectedItem().toString();
                if (!children.getSelectedItem().toString().equals(null))
                {
                    ch = children.getSelectedItem().toString();

                }



                datetext= datebox.getText().toString();

                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (preferences.getString("user_id", ""));

                SharedPreferences child = getSharedPreferences(SplasgPI, MODE_PRIVATE);
                childdsp = (child.getString("ch", ""));

                String input = datebox.getText().toString();

                String mainchild;
                if (ch.equals("")){
                    mainchild=childdsp;
                }
                else {
                    mainchild=ch;
                }



                String yearr="",monthh="",days="";

                if (!datetext.equals("")){
                    String s[] = datetext.split("/");
                    days = s[0];
                     monthh = s[1];
                     yearr = s[2];

                    datetext = days+"-"+monthh+"-"+yearr;

                    age = Integer.parseInt(yearr);

                    registerUser(channel, Integer.valueOf(days), Integer.valueOf(monthh), Integer.valueOf(yearr),datetext,firstname.getText().toString(),lastname.getText().toString(),maritalstatus,HeightID,mainchild);
                }else {

                        registerUser(channel, fday, fmonth, fyear,datetext,firstname.getText().toString(),lastname.getText().toString(),maritalstatus,HeightID,mainchild);
                    }
            }
        });

        requestStoragePermission();


        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


//

    }
    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
//            c.add(Calendar.YEAR,-18);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
//            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {

            fday = day;
            fmonth = month+1;
            fyear = year;
            datetext= fyear+"-"+fmonth+1+"-"+fday;

            datebox.setText(day + "/" + (month +1)  + "/" + year);
        }
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
        private ProgressDialog dialog = new ProgressDialog(EditBasicDetails.this);
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




    private void updateLabel() {

        datebox.setText(fday + "/"
                + (fmonth +1)  + "/" + fyear);
    }

    public void updateDisplay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - fyear;
//        datee.setText( new StringBuilder().append("The user is ")
//                .append(age).append(" years old"));
    }




    private void getheights(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            heightList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getallheights(result);
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

        //Adding request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getallheights(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("heightid");

                System.out.print(reli_ID);

                String feet= json.getString("feet");
                String inch= json.getString("inch");
                String cm= json.getString("cm");

//                HeightText = feet+"'"+""+inch+"'"+"-"+cm+"cm";




                heightarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



//        heightarray.add(0, "Select");

        //Setting adapter to show the items in the spinner
        heightBD.setAdapter(new ArrayAdapter<String>(EditBasicDetails.this, android.R.layout.simple_spinner_dropdown_item, heightarray));
        heightBD.setSelection(heightarray.indexOf(HeightText));


    }

    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(MaritalStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("mstatus");

                            for(int i=0;i<result.length();i++){
                                try {
                                    //Getting json object

                                    Mstatus.add(result.getString(i));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                            //Setting adapter to show the items in the spinner
                            m1.setAdapter(new ArrayAdapter<String>(EditBasicDetails.this, android.R.layout.simple_spinner_dropdown_item, Mstatus));
                            m1.setSelection(Mstatus.indexOf(marital));


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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

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
                    String userid = c.getString("profileid");
                    String userName = c.getString("firstname");

                    HeightText =  c.getString("feet")+"'"+""+c.getString("inch")+"'"+"-"+c.getString("cm")+"cm";
                    String Name = c.getString("firstname") +" "+c.getString("lastname");
                    String Lastname = c.getString("lastname");
                    String childrsden = c.getString("havechildren");

//
                    dob =  c.getString("day")+ "/"+c.getString("month")+"/"+c.getString("year");
                    marital=  c.getString("maritalstatus");
                    String photourl = c.getString("userphoto");
                    String dateob = c.getString("dateofbirth");

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


                    firstname.setText(userName);
                    lastname.setText(Lastname);
                    datebox.setText(dob);


//                    Toast.makeText(getApplicationContext(),heightt,Toast.LENGTH_LONG).show();
//                    if (heightt!=""){
                    if (!childrsden.equals("")){

                        children.setSelection(Childrenarray.indexOf(childrsden));
                    }
                    if (!HeightText.equals("")){

                        heightBD.setSelection(heightarray.indexOf(HeightText));
                    }


//                    }
//                    else {
//                        heightarray.add(0, "Select");
//                    }

                         m1.setSelection(Mstatus.indexOf(marital));


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
//         Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void registerUser(final String userid, final Integer day , final Integer month, final Integer year,final String dob,final String firstname,
                              final String lastname,final String maritalstatus,final String height,final String havechildren) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        if (!String.valueOf(fyear).equals(null)&& age < 18) {
            Toast.makeText(EditBasicDetails.this, "Under 18Years are not allow!", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("please wait a moment");
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
                    params.put("userid", String.valueOf(userid));
                    params.put("day", String.valueOf(day));
                    params.put("month", String.valueOf(month));
                    params.put("year", String.valueOf(year));
                    params.put("dob", dob);
                    params.put("firstname", firstname);
                    params.put("lastname", lastname);
                    params.put("maritalstatus", maritalstatus);
                    params.put("heightid", height);
                    params.put("havechildren", havechildren);


                    return params;
                }
            };

            strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
        }
    }

    private void registerUseremptydate(final Integer userid, final String dob, final String firstname,
                                       final String lastname, final String maritalstatus, final String height, final String havechildren) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Saving..");
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
                        Toast.makeText(getApplicationContext(),"Saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                        startActivity(intent);


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
                params.put("userid", String.valueOf(userid));
                params.put("dateofbirth",dob);
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("maritalstatus", maritalstatus);
                params.put("heightid", height);
                params.put("havechildren", havechildren);


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
