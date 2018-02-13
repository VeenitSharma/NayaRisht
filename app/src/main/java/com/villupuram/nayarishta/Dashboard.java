package com.villupuram.nayarishta;

import android.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

//import static com.villupuram.nayarishta.Login2.MyPREFERENCES;



public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,  View.OnClickListener,GoogleApiClient.OnConnectionFailedListener  {



    private ExpandableHeightGridView  mGridView;
    private static final String TAG = Dashboard.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private ProgressBar mProgressBar;
    private AlbumsAdapter mGridAdapter;
    private ArrayList<Album> mGridData;
    private String FEED_URL = "http://nayarishta.com/nayarishta-app/api/mymatches.php?userid=";
    private String GETACCOUNTDETAILS_URL = "http://nayarishta.com/nayarishta-app/api/account-details.php?userid=";

    TextView hiddertext, updatedtext, profiletext,counttext;
    static String JSON_RESULT="";
    ProgressDialog progressDialog;
    private static String URL_FOR_DETAIL = "",userName="";
    ImageView UserPhoto;
    RelativeLayout updateprofile;
    private Uri filePath;
    private Bitmap bitmap;
    public static String channel="";
    private String UPLOAD_URL ="http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private int PICK_IMAGE_REQUEST = 1;
    int age;
    SharedPreferences sharedpConstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        channel = (shared.getString("user_id", ""));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        navigationView.setItemIconTintList(null);

        mGridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        UserPhoto = (ImageView)findViewById(R.id.user_image);
        updateprofile = (RelativeLayout)findViewById(R.id.updatelayout);
        hiddertext =(TextView)findViewById(R.id.hiddentext);
        updatedtext = (TextView)findViewById(R.id.dtext);
        profiletext = (TextView)findViewById(R.id.ptext);
        counttext = (TextView)findViewById(R.id.atext);
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new AlbumsAdapter(this, R.layout.album_card, mGridData);
        mGridView.setAdapter(mGridAdapter);
        mGridView.setExpanded(true);

        RelativeLayout searchicon = (RelativeLayout)findViewById(R.id.searchbtn);

        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SearchClick.class);
                startActivity(intent);
            }
        });
        //Start download

        SharedPreferences.Editor editor = sharedpConstant.edit();
        editor.putString("session_status", "logout");
        editor.commit();

//        new AsyncHttpTask().execute(FEED_URL+channel);
//        mProgressBar.setVisibility(View.VISIBLE);
        loginUser();
        getAcountDetail();
        dashboard();

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                //Get item at position

                try {
                    JSONArray json=new JSONArray(JSON_RESULT);
                    String poss = String.valueOf(position);
                    for (int i = 0; i < json.length(); i++) {
                        if(i==position) {
                            JSONObject c = json.getJSONObject(i);
                            String idd  = c.getString("userprofileid");

                            Intent intent = new Intent(Dashboard.this,BasicDetail.class);
                            intent.putExtra("id",idd);
                            intent.putExtra("type","dashboard");
                            startActivity(intent);

                            break;
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //Start details activity
            }
        });



        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
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

            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                    .addFileToUpload(path, "file") //Adding file
                    .addParameter("userid", channel) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
                final int sizeInBytes = bitmap.getByteCount();
                UserPhoto.setImageBitmap(bitmap);



                uploadMultipart();

            } catch (IOException e) {
                e.printStackTrace();
            }
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



    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "wait";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
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
                    userName = c.getString("username");
                    String photourl = c.getString("userphoto");

                    String Name = c.getString("firstname") +" "+c.getString("lastname");
                    String profilecreatedfor =  c.getString("profilecreatedfor");
//                     day = Integer.valueOf(c.getString("day"));
//                     month = Integer.valueOf(c.getString("month"));
//                     year = Integer.valueOf(c.getString("year"));
                    String gender = c.getString("gender");
                    String dob =  c.getString("day")+ "-"+c.getString("month")+"-"+c.getString("year");
                    String height =  c.getString("feet")+ "'"+c.getString("inch")+"'-"+c.getString("cm")+"cm";
                    String maritalstatus =  c.getString("maritalstatus");
//                    String havechildren =  c.getString("havechildren");



                    TextView profileText = (TextView) findViewById(R.id.ptext);
                    TextView dateText = (TextView) findViewById(R.id.dtext);
                    TextView username = (TextView) findViewById(R.id.utext);
                    TextView profileId = (TextView) findViewById(R.id.pidtext);
                    TextView heighttext = (TextView) findViewById(R.id.htext);
                    TextView m_status = (TextView) findViewById(R.id.mtext);
//                    TextView no_children = (TextView) findViewById(R.id.no_children);

                    PhotoUrl = photourl;


                    TextView header = (TextView)findViewById(R.id.header);
                    ImageView slideimg=(ImageView)findViewById(R.id.user_image_slide);


                    if (!photourl.equals("")){
//
                        Picasso.with(getApplicationContext()).load(photourl).resize(100,100).into(UserPhoto);
                        Picasso.with(getApplicationContext()).load(photourl).resize(100,100).into(slideimg);
                    }
                    else {
                        if (gender.toString().equals("F")){
                            UserPhoto.setImageResource(R.drawable.femaledefault);
                            slideimg.setImageResource(R.drawable.femaledefault);

                        }
                        else {
                            UserPhoto.setImageResource(R.drawable.mandefault);
                            slideimg.setImageResource(R.drawable.mandefault);

                        }
                    }


//
//
                    profileId.setText(userid);
                    username.setText(userName);
//                    profileText.setText(profilecreatedfor);
//                    dateText.setText(dob);
//                    heighttext.setText(height);
//                    m_status.setText(maritalstatus);
////                    no_children.setText(havechildren);
                    header.setText(userName);


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
//
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void getAcountDetail() {

        // Tag used to cancel the request
        String cancel_req_tag = "wait";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = GETACCOUNTDETAILS_URL + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {
                    String createddate="";

                    JSONObject jarray = new JSONObject(response);

                    JSONObject jsonObject = jarray.getJSONObject("account");
                    String mtype = jsonObject.getString("membertype");
                    String pcount = jsonObject.getString("profilecount");



                    JSONArray array =  jsonObject.getJSONArray("0");

                    for(int i = 0; i<array.length() ; i++){
                        String productInfo = String.valueOf(array.get(i));

                        JSONObject object = new JSONObject(productInfo );
                         createddate = String.valueOf(object.get("CreatedDateTime"));
                    }
                    String string = createddate;
                    String[] One = string.split(" ");
                    String date = One[0]; // 004
                    updatedtext.setText(date);
                    profiletext.setText(mtype);
                    counttext.setText(pcount);

//                    JSONArray c = jsonObject.getJSONArray("0");
//                    JSONObject jsonObject11 = c.getJSONObject("")
//                    String pcount = c.getString("profilecount");
//                    String mtype = c.getString("membertype");




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
//
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



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.logout:
                signOut();
                break;
            case R.id.addphoto:
                showFileChooser();
                break;
        }

    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));

                int statusCode = httpResponse.getStatusLine().getStatusCode();

                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                Log.d(TAG, err);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(Dashboard.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }



    /**
     * Parsing the feed results and get the list
     * @param result
     */
    private void parseResult(String result) {
        try {



            JSONObject response = new JSONObject(result);



            Boolean error = response.getBoolean("error");



            if (!error) {
                hiddertext.setVisibility(View.GONE);
                JSONArray posts = response.optJSONArray("users");
                JSON_RESULT = posts.toString();
                Album item;
                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);


                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.optJSONObject(i);
                    String agevalue = post.getString("userprofileyear");
                    age = year - Integer.valueOf(agevalue);

                    String title = post.optString("userprofilefirstname");
                    String dis = post.optString("userprofilemaritalstatus")+","+(post.getString("userprofileoccupation"));
                    String photourl = post.optString("userprofilephoto");



                    item = new Album();

                    item.setName(title);
                    item.setNumOfSongs(age+","+dis);


                    item.setThumbnail(photourl);
//                JSONArray attachments = post.getJSONArray("attachments");
//                if (null != attachments && attachments.length() > 0) {
//                    JSONObject attachment = attachments.getJSONObject(0);
//                    if (attachment != null)
//                        item.setThumbnail(attachment.getString("url"));
//                }
                    mGridData.add(item);
                }
            }
            else {
                String msg = response.getString("message");
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                hiddertext.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void dashboard() {
        // Tag used to cancel the request
        String cancel_req_tag = "wait";
        progressDialog.setMessage("please wait a moment");
        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));
        URL_FOR_DETAIL = FEED_URL+channel;
        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("users");
                    JSON_RESULT = jUser.toString();
                    Album item;
                    Calendar calendar = Calendar.getInstance();
                    Integer year = calendar.get(Calendar.YEAR);

                    for (int i = 0; i < jUser.length(); i++) {
                        JSONObject post = jUser.optJSONObject(i);
                        String agevalue = post.getString("userprofileyear");
                        age = year - Integer.valueOf(agevalue);


                        String title = post.optString("userprofilefirstname");
                        String dis = post.optString("userprofilemaritalstatus")+","+(post.getString("userprofileoccupation"));
                        String photourl = post.optString("userprofilephoto");

                        String gender = post.optString("userprofilegender");

                        item = new Album();

                        item.setName(title);
                        item.setNumOfSongs(age+","+dis);
                        item.setGender(gender);
                        if (!photourl.equals(null)){
                            item.setThumbnail(photourl);
                        }
                        else{

                            item.setThumbnail(null);
                        }
                        mGridData.add(item);
                        mGridAdapter.notifyDataSetChanged();

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
//
        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }















    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent intent = new Intent(getApplicationContext(),Login2.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent it=new Intent(Dashboard.this,TryFragmentActivity.class);
            startActivity(it);
        }
        if (id == R.id.dashboard) {

        }
        else if (id == R.id.search) {
            Intent it=new Intent(Dashboard.this,SearchClick.class);
            startActivity(it);
        }
        else if (id == R.id.compose) {
            Intent it=new Intent(Dashboard.this,Compose.class);
            startActivity(it);
        }
        else if (id == R.id.Messenger) {
            Intent it=new Intent(Dashboard.this,User.class);
            startActivity(it);
        }
        else if (id == R.id.inbox) {
            Intent it=new Intent(Dashboard.this,Inbox.class);
            startActivity(it);


        } else if (id == R.id.sent) {
            Intent it=new Intent(Dashboard.this,Sent.class);
            startActivity(it);

        } else if (id == R.id.drafts) {
            Intent it=new Intent(Dashboard.this,Drafts.class);
            startActivity(it);

        }else if (id == R.id.isend) {
            Intent it=new Intent(Dashboard.this,InterestSend.class);
            startActivity(it);

        } else if (id == R.id.irecieved) {
            Intent it=new Intent(Dashboard.this,InterestRecieved.class);
            startActivity(it);

        } else if (id == R.id.yaccept) {
            Intent it=new Intent(Dashboard.this,Yaccepted.class);
            startActivity(it);

        } else if (id == R.id.accepty) {
            Intent it=new Intent(Dashboard.this,AcceptedY.class);
            startActivity(it);


        } else if (id == R.id.ydecline) {
            Intent it=new Intent(Dashboard.this,YDeclined.class);
            startActivity(it);

        }else if (id == R.id.decliney) {
            Intent it=new Intent(Dashboard.this,DeclinedY.class);
            startActivity(it);
        }
//        else if (id == R.id.for_accept)
//        {
//            Intent it=new Intent(Dashboard.this,ForAccepted.class);
//            startActivity(it);
//        }
//
//        else if (id == R.id.to_accept) {
//            Intent it=new Intent(Dashboard.this,ToAccept.class);
//            startActivity(it);
//
//        }
        else if (id == R.id.about) {
            Intent it=new Intent(Dashboard.this,About.class);
            startActivity(it);

        } else if (id == R.id.membership) {
            Intent it=new Intent(Dashboard.this,MemberShip_Main.class);
            startActivity(it);

        }
        else if (id == R.id.change) {
            Intent it=new Intent(Dashboard.this,ChangePassword.class);
            startActivity(it);
        }else if (id == R.id.contact) {

            Intent it=new Intent(Dashboard.this,Contact.class);
            startActivity(it);

        }else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();

            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            bydefaultstatus=1;

            globallogincheck=1;

            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                // signed in. Show the "sign out" button and explanation.
                // ...
                signOut();
            } else {
                // not signed in. Show the "sign in" button and explanation.
                // ...
                Intent it=new Intent(Dashboard.this,Login2.class);
                it.putExtra("logout","hello");
                startActivity(it);

            }
            SharedPreferences.Editor editor = sharedpConstant.edit();
            editor.putString("session_status", "");
            editor.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
