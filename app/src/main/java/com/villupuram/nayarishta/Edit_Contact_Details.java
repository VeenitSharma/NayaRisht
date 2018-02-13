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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class Edit_Contact_Details extends AppCompatActivity {

    private final String TAG = "Edit_Contact_";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/contactInfoUpdate.php";
    ProgressDialog progressDialog;
    EditText mobile_isd,mobile,phone_isd,phone_std,landline,contacttxt;
    public static String channel="";
    Spinner display,frmtyme,amfrom,totyme,amto;
    private static String URL_FOR_DETAIL = "",mobileisd="",mobilenumber="",phoneisd="",phonestd="",phonelandline="",contactperson="",displayoption="";
    private ArrayList<String> displayopthion;
    ImageView UserPhoto;
    private ArrayList<String> fromtyme;
    private ArrayList<String> fromtymeAM;
    private ArrayList<String> totymearray;
    private ArrayList<String> totymearrayAM;
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
        setContentView(R.layout.activity_edit__contact__details);

        displayopthion = new ArrayList<String>();
        fromtyme = new ArrayList<String>();
        fromtymeAM = new ArrayList<String>();
        totymearray = new ArrayList<String>();
        totymearrayAM = new ArrayList<String>();


        mobile_isd = (EditText) findViewById(R.id.mobile_isd);
        mobile = (EditText) findViewById(R.id.mobile);
        phone_isd = (EditText) findViewById(R.id.phone_isd);
        phone_std = (EditText) findViewById(R.id.phone_std);
        landline = (EditText) findViewById(R.id.landline);
        contacttxt = (EditText) findViewById(R.id.contact);
        display = (Spinner)findViewById(R.id.display_option);
        frmtyme = (Spinner)findViewById(R.id.fromtime);
        amfrom = (Spinner)findViewById(R.id.am);
        totyme = (Spinner)findViewById(R.id.toname);
        amto = (Spinner)findViewById(R.id.pm);
        UserPhoto = (ImageView)findViewById(R.id.user_image);
        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);

        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        Button save = (Button)findViewById(R.id.save_contact);

        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);




        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic_details);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });

        loginUser();
        requestStoragePermission();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String havechildren = "0";

                SharedPreferences preferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                channel = (preferences.getString("user_id", ""));
                String sstd=  mobile_isd.getText().toString();
                String mnumber=  mobile.getText().toString();
                String pisd=  phone_isd.getText().toString();
                String pstd=  phone_std.getText().toString();
                String plandline=  landline.getText().toString();
                String ctext=  contacttxt.getText().toString();
                String ftym=  frmtyme.getSelectedItem().toString();
                String amfrm=  amfrom.getSelectedItem().toString();
                String totym=  totyme.getSelectedItem().toString();
                String amtm=  amto.getSelectedItem().toString();
                String dis=  display.getSelectedItem().toString();

                saveContact(channel,sstd,mnumber,pisd,pstd,plandline,ctext,ftym,amfrm, totym,amtm,dis);
            }
        });


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

//            File file = new File(picturePath);
//            long length = file.length() / 1024;
//            if ((length < 2000))
//            {
            UserPhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));

//            Picasso.with(TryFragmentActivity.this).load("file:" + picturePath).placeholder(R.drawable.placeholder).into(UserPhoto);
//                bitmap = getResizedBitmap(bitmap, 60);
            new ImageUploadTask().execute();
//            }
//            else {
//                Toast.makeText(getApplicationContext(),"Invalid file Size or Type",Toast.LENGTH_LONG).show();
//            }

        }
    }
    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(Edit_Contact_Details.this);
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


    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

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



                    mobileisd =  c.getString("mobileisd");
                    mobilenumber =  c.getString("mobilenumber");
                    phoneisd =  c.getString("phoneisd");
                    phonestd =  c.getString("phonestd");
                    phonelandline =  c.getString("phonelandline");


                    String from = c.getString("calltime");

                    String part1="";
                    String part2 = "",one="",two="";
                    if (!from.equals("")){
                        String s[] = from.split(" to ");
                        String p1 = s[0];
                        String p2 = s[1];


                        String a[]= p1.split(" ");
                        part1 = a[0];
                        part2= a[1];

                        String b[]= p2.split(" ");
                         one = b[0];
                         two = b[1];


                    }






                    contactperson =  c.getString("contactperson");
                    displayoption =  c.getString("displayoption");

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
                    mobile_isd.setText(mobileisd);
                    mobile.setText(mobilenumber);
                    phone_isd.setText(phoneisd);
                    phone_std.setText(phonestd);

                    landline.setText(phonelandline);
                    contacttxt.setText(contactperson);


                    fromtyme.add(0,"1");
                    fromtyme.add(1,"2");
                    fromtyme.add(2,"3");
                    fromtyme.add(3,"4");
                    fromtyme.add(4,"5");
                    fromtyme.add(5,"6");
                    fromtyme.add(6,"7");
                    fromtyme.add(7,"8");
                    fromtyme.add(8,"9");
                    fromtyme.add(9,"10");
                    fromtyme.add(10,"11");
                    fromtyme.add(11,"12");

                    fromtymeAM.add(0,"AM");
                    fromtymeAM.add(1,"PM");

                    amfrom.setAdapter(new ArrayAdapter<String>(Edit_Contact_Details.this, android.R.layout.simple_spinner_dropdown_item, fromtymeAM));
                    amfrom.setSelection(fromtymeAM.indexOf(part2));

                    frmtyme.setAdapter(new ArrayAdapter<String>(Edit_Contact_Details.this, android.R.layout.simple_spinner_dropdown_item, fromtyme));
                    frmtyme.setSelection(fromtyme.indexOf(part1));

                    totymearray.add(0,"1");
                    totymearray.add(1,"2");
                    totymearray.add(2,"3");
                    totymearray.add(3,"4");
                    totymearray.add(4,"5");
                    totymearray.add(5,"6");
                    totymearray.add(6,"7");
                    totymearray.add(7,"8");
                    totymearray.add(8,"9");
                    totymearray.add(9,"10");
                    totymearray.add(10,"11");
                    totymearray.add(11,"12");

                    totymearrayAM.add(0,"AM");
                    totymearrayAM.add(1,"PM");

                    amto.setAdapter(new ArrayAdapter<String>(Edit_Contact_Details.this, android.R.layout.simple_spinner_dropdown_item, totymearrayAM));
                    amto.setSelection(totymearrayAM.indexOf(two));

                    totyme.setAdapter(new ArrayAdapter<String>(Edit_Contact_Details.this, android.R.layout.simple_spinner_dropdown_item, totymearray));
                    totyme.setSelection(totymearray.indexOf(one));











                    displayopthion.add(0,"Show to All Paid Members");
                    displayopthion.add(1,"Show to only Members I Accept / Express Interest In");

                    display.setAdapter(new ArrayAdapter<String>(Edit_Contact_Details.this, android.R.layout.simple_spinner_dropdown_item, displayopthion));
                    display.setSelection(displayopthion.indexOf(displayoption));


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




    private void saveContact(final String userid, final String phone_mobile_isd, final String phone_mobile_mobile, final String phone_res_isd, final String phone_res_std, final String phone_res_landline, final String contactperson, final String ftime, final String fftime, final String etime, final String eetime,final String contact_status ) {
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
                params.put("phone_mobile_isd", phone_mobile_isd);
                params.put("phone_mobile_mobile", phone_mobile_mobile);
                params.put("phone_res_isd", phone_res_isd);
                params.put("phone_res_std", phone_res_std);
                params.put("phone_res_landline", phone_res_landline);
                params.put("contactperson", contactperson);
                params.put("ftime", ftime);
                params.put("fftime", fftime);
                params.put("etime", etime);
                params.put("eetime", eetime);
                params.put("contact_status", contact_status);


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
