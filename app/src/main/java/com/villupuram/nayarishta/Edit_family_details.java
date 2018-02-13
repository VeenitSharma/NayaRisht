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
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;

public class Edit_family_details  extends AppCompatActivity {

    private ArrayList<String> Mother;
    private ArrayList<String> Father;
    private ArrayList<String> Fvalues;
    private ArrayList<String> Ftype;

    private final String TAG = "EditPersonalInformation";
    private static String URL_FOR_SEARCH = "",DOB="";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/familyInfoUpdate.php";
    ProgressDialog progressDialog;
    Spinner mother, father,fam_value,fam_type;
    public static String channel="";
    EditText sister,Msister,brother,Mbrother,abouttext;
    private JSONArray result;
    private static String URL_FOR_DETAIL = "",motheris="",fatheris="",brothers="",sisters="",bromarried="",sismarried="",familyvalues="",familytype="",aboutmyfamily="";
    ImageView UserPhoto;

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
        setContentView(R.layout.activity_edit_family_details);

        mother =(Spinner)findViewById(R.id.mother);
        father =(Spinner)findViewById(R.id.father);
        fam_value =(Spinner)findViewById(R.id.family_values);
        fam_type =(Spinner)findViewById(R.id.family_type);

        sister = (EditText)findViewById(R.id.no_sister);
        Msister = (EditText)findViewById(R.id.sister_married);
        brother = (EditText)findViewById(R.id.no_brother);
        Mbrother = (EditText)findViewById(R.id.brother_married);
        abouttext = (EditText)findViewById(R.id.aboutText);

        UserPhoto = (ImageView)findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);

        Button save = (Button)findViewById(R.id.saveFD);

        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        //Initializing the ArrayList
        Mother = new ArrayList<String>();
        Father = new ArrayList<String>();
        Fvalues = new ArrayList<String>();
        Ftype = new ArrayList<String>();
        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);

        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic_details);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });

        getMother();
        getbranchlist();


        //saveData
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String edu = mother.getSelectedItem().toString();
                String bran = father.getSelectedItem().toString();
                String profess = fam_value.getSelectedItem().toString();
                String annual = fam_type.getSelectedItem().toString();

                String stext = sister.getText().toString();
                String mstext = Msister.getText().toString();
                String btext = brother.getText().toString();
                String bmtext = Mbrother.getText().toString();
                String abt = abouttext.getText().toString();

                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (preferences.getString("user_id", ""));

                save_userdata(channel,bran,edu,btext,bmtext,stext,mstext,abt,annual,profess);
            }
        });

        loginUser();

        requestStoragePermission();


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
        private ProgressDialog dialog = new ProgressDialog(Edit_family_details.this);
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


    //education
    private void getMother(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.MOTHER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);
                            JSONArray jsonArray = j.getJSONArray("mother");




//                            Mother.add(0,"-select-");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Getting json object

                                Mother.add(jsonArray.optString(i));




                                //Seting adapter to show the items in the spinner
                                mother.setAdapter(new ArrayAdapter<String>(Edit_family_details.this, android.R.layout.simple_spinner_dropdown_item, Mother));
                            }
                            Mother.add(0, "Select");


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
    //branch
    private void getbranchlist(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.FATHER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            j = new JSONObject(response);
                            JSONArray jsonArray = j.getJSONArray("father");



//                            Father.add(0,"-select-");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                //Getting json object

                                Father.add(jsonArray.optString(i));

                                //Seting adapter to show the items in the spinner
                                father.setAdapter(new ArrayAdapter<String>(Edit_family_details.this, android.R.layout.simple_spinner_dropdown_item, Father));
                            }

                            Father.add(0, "Select");
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
    //branch


    //save selected data
    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Saving..");

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



                    motheris =  c.getString("motheris");
                    fatheris =  c.getString("fatheris");
                    brothers =  c.getString("brothers");
                    sisters =  c.getString("sister");
                    bromarried =  c.getString("bromarried");


                    sismarried =  c.getString("sismarried");
                    familyvalues =  c.getString("familyvalues");
                    familytype =  c.getString("familytype");
                    aboutmyfamily =  c.getString("aboutmyfamily");

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
//
                    sister.setText(sisters);
                    brother.setText(brothers);
                    Mbrother.setText(bromarried);
                    Msister.setText(sismarried);

                    Fvalues.add(0,"Select");
                    Fvalues.add(1,"Traditional");
                    Fvalues.add(2,"Moderate");
                    Fvalues.add(3,"Liberal");

                    Ftype.add(0,"Select");
                    Ftype.add(1,"Joint Family");
                    Ftype.add(2,"Nuclear Family");
                    Ftype.add(3,"Others");



                    fam_value.setAdapter(new ArrayAdapter<String>(Edit_family_details.this, android.R.layout.simple_spinner_dropdown_item, Fvalues));
                    fam_value.setSelection(Fvalues.indexOf(familyvalues));

                    fam_type.setAdapter(new ArrayAdapter<String>(Edit_family_details.this, android.R.layout.simple_spinner_dropdown_item, Ftype));
                    fam_type.setSelection(Ftype.indexOf(familytype));
                    abouttext.setText(aboutmyfamily);


                    if (!motheris.equals("")){
                        mother.setSelection(Mother.indexOf(motheris));
                    }


                    if (!fatheris.equals("")){
                        father.setSelection(Father.indexOf(fatheris));
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


    private void save_userdata(final String userid, final String father , final String mother, final String brothers,final String bmarried,final String sister, final String smarried, final String aboutmyfamily,final String familytype, final String familyvalues) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Please wait a moment.");
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

                      String msg = jObj.getString("message");
                        Intent intent = new Intent(getApplicationContext(), EditProfile_adminMessage.class);
                        intent.putExtra("msg",msg);
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
                params.put("userid", userid);
                params.put("father", father);
                params.put("mother", mother);
                params.put("brothers", brothers);
                params.put("bmarried", bmarried);
                params.put("sister", sister);
                params.put("smarried", smarried);
                params.put("aboutmyfamily", aboutmyfamily);
                params.put("familytype", familytype);
                params.put("familyvalues", familyvalues);

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
