package com.villupuram.nayarishta;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.androidquery.AQuery;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import retrofit.http.POST;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.SplashEducationCarrier.SplashEducation;
import static com.villupuram.nayarishta.constant.base_api_url.Email;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
public class TryFragmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener, AsyncTaskCompleteListener {


    private static final String TAG = "TryFragmentActivity";

    ProgressDialog progressDialog;
    private static String URL_FOR_DETAIL = "";
    public static String channel = "";
    public static String USERNAME = "", ImageName = "";
    SharedPreferences sharedpreferencestwo;
    public static final String MYUSER_NAME = "MY_USER";
    RelativeLayout add_photo;
    private GoogleApiClient mGoogleApiClient;
    public static Integer age;
    ImageView UserPhoto;
    private String UPLOAD_URL = "http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    public static String fb_intent = "hello";
    static Integer day, month, year;
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private String filePath;
    private Bitmap bitmap;
    TextView addtext;
    ImageView camera;
    private FirebaseAuth mAuth;
    public static int logincheck;
    SharedPreferences sharedpreferences;
    private int PICK_IMAGE_REQUEST = 1;
    public static String profilephoto = "";
    Context mcontext;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    Firebase conRef;

    public static final String Tryfragment = "MyPrefs";
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    StorageReference storageRef;
    FirebaseStorage storage;
    String imageName;
    String userName;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    private Fragment mFragment;
    SharedPreferences sharedpConstant;
    private ParseContent parseContent;
    private final int GALLERY = 1;
    private AQuery aQuery;
    ImageView slideimg;
    TextView headertext;
    byte[] multipartBody;
    String picturePath;
    private static final String IMAGE_DIRECTORY = "/nayarishta_upload_gallery";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_fragment);


        storage = FirebaseStorage.getInstance();

        bydefaultstatus = 0;

        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(Tryfragment, Context.MODE_PRIVATE);
        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.getHeaderView(0);

         slideimg=(ImageView)headerLayout.findViewById(R.id.user_image_slide);
        headertext = (TextView)headerLayout.findViewById(R.id.header);
        SharedPreferences sharedd = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String logintypee = (sharedd.getString("logintype", ""));


        if (logintypee.equals("fb") || logintypee.equals("google")) {
            Menu menuNav = navigationView.getMenu();
            MenuItem logoutItem = menuNav.findItem(R.id.change);
            logoutItem.setVisible(false);
        } else if (logintypee.equals("simplelogin")) {
            Menu menuNav = navigationView.getMenu();
            MenuItem logoutItem = menuNav.findItem(R.id.change);
            logoutItem.setVisible(true);
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        requestStoragePermission();

        TextView t = (TextView) findViewById(R.id.edit);
        TextView utext = (TextView) findViewById(R.id.utext);
        addtext = (TextView) findViewById(R.id.addtext);
        camera = (ImageView) findViewById(R.id.camera);

        RelativeLayout searchicon = (RelativeLayout) findViewById(R.id.searchbtn);

        final RelativeLayout addphotoalbum = (RelativeLayout) findViewById(R.id.addphotoalbum);

        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        add_photo = (RelativeLayout) findViewById(R.id.addphoto);
        UserPhoto = (ImageView) findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);


        SharedPreferences.Editor editor = sharedpConstant.edit();
        editor.putString("session_status", "logout");
        editor.commit();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

// create bitmap from resource
//        Bitmap bm = BitmapFactory.decodeResource(getResources(),
//                R.drawable.addphotocircle);
//        // set circle bitmap
//        ImageView UserPhoto = (ImageView) findViewById(R.id.user_image);
//        UserPhoto.setImageBitmap(getCircleBitmap(bm));

        add_photo.setOnClickListener(this);

        RelativeLayout relativeclic1 = (RelativeLayout) findViewById(R.id.go_profile);
        RelativeLayout relativeclic2 = (RelativeLayout) findViewById(R.id.go_album);
        RelativeLayout relativeclic3 = (RelativeLayout) findViewById(R.id.go_ps);

        Button btn = (Button) findViewById(R.id.button);
        final TextView profiletext = (TextView) findViewById(R.id.profile);
        final TextView albumtext = (TextView) findViewById(R.id.album);
        final TextView pstext = (TextView) findViewById(R.id.ps);


        final ImageView ps = (ImageView) findViewById(R.id.pic3);
        final ImageView mp = (ImageView) findViewById(R.id.pic1);
        final ImageView ma = (ImageView) findViewById(R.id.pic2);


        try_two myFragment = (try_two) getSupportFragmentManager().findFragmentByTag("HELLOTWO");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
        }


        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchClick.class);
                startActivity(intent);
            }
        });

        relativeclic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ps.setBackgroundResource(R.drawable.ic_psetting_unselected);
                ma.setBackgroundResource(R.drawable.ic_album_unselected);
                mp.setBackgroundResource(R.drawable.ic_profile_selected);

                albumtext.setTextColor(Color.parseColor("#cecece"));
                pstext.setTextColor(Color.parseColor("#cecece"));
                profiletext.setTextColor(Color.parseColor("#ffb902"));

                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try_One hello = new try_One();
                fragmentTransaction.add(R.id.main_fragment, hello, "HELLO");
                fragmentTransaction.commit();
                addphotoalbum.setVisibility(View.INVISIBLE);

            }
        });

        relativeclic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                albumtext.setTextColor(Color.parseColor("#ffb902"));
                pstext.setTextColor(Color.parseColor("#cecece"));
                profiletext.setTextColor(Color.parseColor("#cecece"));

                ps.setBackgroundResource(R.drawable.ic_psetting_unselected);
                ma.setBackgroundResource(R.drawable.ic_album_selected);
                mp.setBackgroundResource(R.drawable.profileunselected);

                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try_two hello = new try_two();
                fragmentTransaction.add(R.id.main_fragment, hello, "HELLOTWO");
                fragmentTransaction.commit();

                addphotoalbum.setVisibility(View.VISIBLE);
            }
        });


        relativeclic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                albumtext.setTextColor(Color.parseColor("#cecece"));
                pstext.setTextColor(Color.parseColor("#ffb902"));
                profiletext.setTextColor(Color.parseColor("#cecece"));
                ps.setBackgroundResource(R.drawable.ic_psetting_selected);
                ma.setBackgroundResource(R.drawable.ic_album_unselected);
                mp.setBackgroundResource(R.drawable.profileunselected);
                FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try_three hello = new try_three();
                fragmentTransaction.add(R.id.main_fragment, hello, "HELLO");
                fragmentTransaction.commit();
                addphotoalbum.setVisibility(View.INVISIBLE);
            }
        });
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        channel = (shared.getString("user_id", ""));


        Intent fb = getIntent();
        String fbid = fb.getStringExtra("userid");
        String fbname = fb.getStringExtra("userName");

        System.out.print(fbid);

        add_photo.setOnClickListener(this);


        loginUser();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });


    }



    public void refereah() {


        progressDialog.setMessage("Please wait a moment");
        showDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
                System.out.print("Refresh");
                FragmentTransaction fragmentManager = getSupportFragmentManager().beginTransaction();
//                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try_two hello = new try_two();
                fragmentManager.replace(R.id.main_fragment, hello, "HELLOTWO");
                fragmentManager.addToBackStack(null);

                fragmentManager.commit();
            }
        }, 7000);


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

//            File file = new File(picturePath);
//            long length = file.length() / 1024;
//            if ((length < 2000))
//            {
            UserPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//            Picasso.with(TryFragmentActivity.this).load("file:" + picturePath).placeholder(R.drawable.placeholder).into(UserPhoto);
//                bitmap = getResizedBitmap(bitmap, 60);
                camera.setVisibility(View.GONE);
                addtext.setVisibility(View.GONE);
                new ImageUploadTask().execute();
//            }
//            else {
//                Toast.makeText(getApplicationContext(),"Invalid file Size or Type",Toast.LENGTH_LONG).show();
//            }


        }
    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(TryFragmentActivity.this);

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

            Toast.makeText(getApplicationContext(), "file uploaded",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        AndyUtils.removeSimpleProgressDialog();
        Log.d("res", response.toString());
        switch (serviceCode) {

            case GALLERY:
                if (parseContent.isSuccess(response)) {
                    String url = parseContent.getURL(response);
                    aQuery.id(UserPhoto).image(url);
                }
        }
    }

    private void writeNewUser(String userName) {
        mDatabase.child("users").child(userName).removeValue();
        mDatabase.child("users").child(userName).child("password").removeValue();

    }
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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


                //myprofile
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
                                String gender = c.getString("gender");

                                String Name = c.getString("firstname") +" "+c.getString("lastname");
                                String profilecreatedfor =  c.getString("profilecreatedfor");
//                     day = Integer.valueOf(c.getString("day"));
//                     month = Integer.valueOf(c.getString("month"));
//                     year = Integer.valueOf(c.getString("year"));
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

                                SharedPreferences.Editor editor = sharedpConstant.edit();
                                editor.putString("username", userName);
                                editor.commit();





                                if (!photourl.equals("")){
//
                                    addtext.setVisibility(View.GONE);
                                    camera.setVisibility(View.GONE);
                                    Picasso.with(getApplicationContext()).load(photourl).placeholder(R.drawable.placeholder).resize(75,75).into(UserPhoto);
                                    Picasso.with(getApplicationContext()).load(photourl).placeholder(R.drawable.placeholder).resize(75,75).into(slideimg);
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


                                UsersDetails.username=userName;
//
//
                                profileId.setText(userid);
                                username.setText(userName);
//                    profileText.setText(profilecreatedfor);
//                    dateText.setText(dob);
//                    heighttext.setText(height);
//                    m_status.setText(maritalstatus);
////                    no_children.setText(havechildren);
                                headertext.setText(userName);


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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent it=new Intent(TryFragmentActivity.this,TryFragmentActivity.class);
            startActivity(it);
        }
        if (id == R.id.dashboard) {
            Intent it=new Intent(TryFragmentActivity.this,Dashboard.class);
            startActivity(it);
        }
        else if (id == R.id.search) {
            Intent it=new Intent(TryFragmentActivity.this,SearchClick.class);
            startActivity(it);
        }
        else if (id == R.id.compose) {
            Intent it=new Intent(TryFragmentActivity.this,Compose.class);
            startActivity(it);
        }
        else if (id == R.id.Messenger) {
            Intent it=new Intent(TryFragmentActivity.this,User.class);
            startActivity(it);
        }
        else if (id == R.id.inbox) {
            Intent it=new Intent(TryFragmentActivity.this,Inbox.class);
            startActivity(it);


        } else if (id == R.id.sent) {
            Intent it=new Intent(TryFragmentActivity.this,Sent.class);
            startActivity(it);

        } else if (id == R.id.drafts) {
            Intent it=new Intent(TryFragmentActivity.this,Drafts.class);
            startActivity(it);

        }else if (id == R.id.isend) {
            Intent it=new Intent(TryFragmentActivity.this,InterestSend.class);
            startActivity(it);

        } else if (id == R.id.irecieved) {
            Intent it=new Intent(TryFragmentActivity.this,InterestRecieved.class);
            startActivity(it);

        } else if (id == R.id.yaccept) {
            Intent it=new Intent(TryFragmentActivity.this,Yaccepted.class);
            startActivity(it);

        } else if (id == R.id.accepty) {
            Intent it=new Intent(TryFragmentActivity.this,AcceptedY.class);
            startActivity(it);


        } else if (id == R.id.ydecline) {
            Intent it=new Intent(TryFragmentActivity.this,YDeclined.class);
            startActivity(it);

        }else if (id == R.id.decliney) {
            Intent it=new Intent(TryFragmentActivity.this,DeclinedY.class);
            startActivity(it);
        }
// else if (id == R.id.for_accept)
//        {
//            Intent it=new Intent(TryFragmentActivity.this,ForAccepted.class);
//            startActivity(it);
//        }
//
//        else if (id == R.id.to_accept) {
//            Intent it=new Intent(TryFragmentActivity.this,ToAccept.class);
//            startActivity(it);
//
//        }
        else if (id == R.id.about) {
            Intent it=new Intent(TryFragmentActivity.this,About.class);
            startActivity(it);

        } else if (id == R.id.membership) {
            Intent it=new Intent(TryFragmentActivity.this,MemberShip_Main.class);
            startActivity(it);

        }
        else if (id == R.id.change) {
            Intent it=new Intent(TryFragmentActivity.this,ChangePassword.class);
            startActivity(it);
        }else if (id == R.id.contact) {

            Intent it=new Intent(TryFragmentActivity.this,Contact.class);
            startActivity(it);

        }else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();

            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();

            bydefaultstatus=1;

            globallogincheck=1;
            writeNewUser(userName);
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                // signed in. Show the "sign out" button and explanation.
                // ...
                signOut();
            } else {
                // not signed in. Show the "sign in" button and explanation.
                // ...
                Intent it=new Intent(TryFragmentActivity.this,Login2.class);
                channel="";
                it.putExtra("logout","hello");
                startActivity(it);

            }


            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            channel = (shared.getString("user_id", ""));

            SharedPreferences.Editor editor = sharedpConstant.edit();
            editor.putString("session_status", "");
            editor.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
