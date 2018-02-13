package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.EducationText;
import static com.villupuram.nayarishta.constant.base_api_url.FavoriteCuisine_values;
import static com.villupuram.nayarishta.constant.base_api_url.FavoriteReads_valuew;
import static com.villupuram.nayarishta.constant.base_api_url.Interests;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.PreferredDressStyle_value;
import static com.villupuram.nayarishta.constant.base_api_url.PreferredMovies_values;
import static com.villupuram.nayarishta.constant.base_api_url.SportsFitnessActivities_values;
import static com.villupuram.nayarishta.constant.base_api_url.music_values;


public class Edit_yourLikes extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static String hoby = "";
    private final String TAG = "Edit_Location";
    Spinner interest, music, f_reads, p_movies, sports, cousine,dressS;
    private JSONArray result, res_music;
    public static String interest_id="";
    public static String interestList="";
    private ArrayList<String> contries;
    private ArrayList<String> musics;
    private ArrayList<String> FREADS_ARRAY;
    private ArrayList<String> P_MUSIC_ARRAY;
    private ArrayList<String> SPORTS_ARRAY;
    private ArrayList<String> CUISINE_ARRAY;
    private ArrayList<String> DRESS_ARRAY;
    ProgressDialog progressDialog;
    public static String DRESS_ID="";
    public static String DRESS_LIST="";
    public static String COUSINE_ID="";
    public static String COUSINE_LIST="";
    public static String SPORTS_ID="";
    public static String SPORTS_LIST="";
    public static String MOVIES_ID="";
    public static String MOVIES_LIST="";
    public static String f_reads_id="";
    public static String f_reads_list="";
    public static String music_id="";
    public static String music_list="";
    private static String URL_FOR_DETAIL = "";
    private static String GET_YOUR_LIKES = "http://nayarishta.com/nayarishta-app/api/getYourLikes.php?userid=";
    private static String FOR_UPDATE_YOURLIKES = "http://nayarishta.com/nayarishta-app/api/YourLikeUpdate.php";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/getInterestslike.php";
    private static final String Music = "http://nayarishta.com/nayarishta-app/api/getFavoriteMusic.php";
    private static final String FREADS = "http://nayarishta.com/nayarishta-app/api/getFavoriteReads.php";
    private static final String P_MOVIES = "http://nayarishta.com/nayarishta-app/api/getPreferredMovies.php";
    private static final String SPORTS = "http://nayarishta.com/nayarishta-app/api/getSportsFitnessActivities.php";
    private static final String CUISINE = "http://nayarishta.com/nayarishta-app/api/getFavoriteCuisine.php";
    private static final String DRESSSTYLE = "http://nayarishta.com/nayarishta-app/api/getPreferredDressStyle.php";
    int currentIndex=0;
     private TextView hobiesText;
     private ArrayList<String> urls= null;
    private RelativeLayout addbut;
    ImageView UserPhoto;
    private String UPLOAD_URL ="http://nayarishta.com/nayarishta-app/api/photoUpload.php";
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;
    private Bitmap bitmap;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private int PICK_IMAGE_REQUEST = 1;
    Button save;
    String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_likes);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        contries = new ArrayList<String>();
        musics = new ArrayList<String>();
        FREADS_ARRAY = new ArrayList<String>();
        P_MUSIC_ARRAY = new ArrayList<String>();
        SPORTS_ARRAY = new ArrayList<String>();
        CUISINE_ARRAY = new ArrayList<String>();
        DRESS_ARRAY = new ArrayList<String>();

        UserPhoto = (ImageView)findViewById(R.id.user_image);

        UserPhoto.setBackgroundResource(R.drawable.addphotocircle);
        hobiesText = (TextView)findViewById(R.id.hobiesText);
        interest = (Spinner) findViewById(R.id.interest);
        music = (Spinner) findViewById(R.id.music);
        f_reads = (Spinner) findViewById(R.id.f_reads);
        f_reads = (Spinner) findViewById(R.id.f_reads);
        p_movies = (Spinner) findViewById(R.id.p_movies);
        sports = (Spinner) findViewById(R.id.sports);
        cousine = (Spinner) findViewById(R.id.CUSINE);
        dressS = (Spinner) findViewById(R.id.dress_STYLE);

        save = (Button)findViewById(R.id.save);
        RelativeLayout basic =(RelativeLayout)findViewById(R.id.basic_details);
        addbut =(RelativeLayout)findViewById(R.id.addbutton);

        RelativeLayout addphoto = (RelativeLayout) findViewById(R.id.uploadphoto);
        TextView t = (TextView) findViewById(R.id.picturebtn);
        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        final ArrayList<ColorVO> colorList = new ArrayList<ColorVO>();
        // String array for alert dialog multi choice items
        final String[] colors = new String[]{
                "Dosen't Matter",
                "Acting",
                "Astrology / Palmistry / Numerology",
                "Collectibles",
                "DIY(do it yourself) projects",
                "Gardening / Landscaping",
                "Home / Interior decoration",
                "Painting / Drawing",
                "Singing","Animal breeding"," Astronomy","Cooking","Film-making","Graphology","Hunting","Photography",
                "Solving Crosswords, Puzzles","Animal breeding","Astronomy","Cooking","Film-making","Graphology","Hunting",
                "Photography","Solving Crosswords, Puzzles"




        };
        // Boolean array for initial selected items
        final boolean[] checkedColors = new boolean[]{
               false, false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false,// Olive
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false, false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false,// Olive
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false, false, // Green
                false, // Blue
                false, // Purple
                false  // Olive

        };


        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });


        getYourLikes();

        interest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = interest.getSelectedItemPosition();
                if (interest.getSelectedItem().toString().equals("Select")){
                    interest_id="";
                }
                else {
                    interest_id =interest.getSelectedItem().toString();
                }
//

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        music.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = music.getSelectedItemPosition();

                if (interest.getSelectedItem().toString().equals("Select")){
                    music_id="";
                }
                else {
                    music_id =music.getSelectedItem().toString();
                }
//


            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        f_reads.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = f_reads.getSelectedItemPosition();
                if (f_reads.getSelectedItem().toString().equals("Select")){
                    f_reads_id="";
                }
                else {
                    f_reads_id =f_reads.getSelectedItem().toString();
                }
//
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        p_movies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = interest.getSelectedItemPosition();
                if (p_movies.getSelectedItem().toString().equals("Select")){
                    MOVIES_ID="";
                }
                else {
                    MOVIES_ID =p_movies.getSelectedItem().toString();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        sports.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {


                final Integer value = interest.getSelectedItemPosition();

                if (sports.getSelectedItem().toString().equals("Select")){
                    SPORTS_ID="";
                }
                else {
                    SPORTS_ID =sports.getSelectedItem().toString();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        cousine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = interest.getSelectedItemPosition();
                if (cousine.getSelectedItem().toString().equals("Select")){
                    COUSINE_ID="";
                }
                else {
                    COUSINE_ID =cousine.getSelectedItem().toString();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        dressS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = interest.getSelectedItemPosition();
                if (dressS.getSelectedItem().toString().equals("Select")){
                    DRESS_ID="";
                }
                else {
                    DRESS_ID =dressS.getSelectedItem().toString();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        getData();
        getMusic();
        getFReads();
        getPMovies();
        getSports();
        getCuisine();
        getdress();


        addbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_yourLikes.this);

                // make a list to hold state of every color
                for (int i = 0; i < colors.length; i++) {
                    ColorVO colorVO = new ColorVO();
                    colorVO.setName(colors[i]);
                    colorVO.setSelected(checkedColors[i]);
                    colorList.add(colorVO);

                }

//                Toast.makeText(getApplicationContext(),"hii",Toast.LENGTH_LONG).show();

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
//                        colorList.get(which).setSelected(isChecked);



                        int i = 1;
                            if (which == 0 && isChecked == true) {

                                for (i = 1; i < checkedColors.length; i++) {
                                    checkedColors[i] = false;
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    colorList.get(which).setSelected(isChecked);
                                    colorList.get(i).setSelected(false);



                                }


                            } else {
                                checkedColors[0] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                                colorList.get(which).setSelected(isChecked);
                                colorList.get(0).setSelected(false);
                            }

                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Your preferred Hobbies?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hobiesText.setText("");

                        // save state of selected vos
                        ArrayList<ColorVO> selectedList = new ArrayList<>();
                        for (int i = 0; i < colorList.size(); i++) {
                            ColorVO colorVO = colorList.get(i);
                            colors[i] = colorVO.getName();
                            checkedColors[i] = colorVO.isSelected();
                            if (colorVO.isSelected()) {
                                selectedList.add(colorVO);
                            }
                        }
                        colorList.clear();

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                hobiesText.setText(hobiesText.getText() + selectedList.get(i).getName() + " ,");
                            else
                                hobiesText.setText(hobiesText.getText() + selectedList.get(i).getName());
                        }
                        colorList.clear();
                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        colorList.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
                String channel = (shared.getString("user_id", ""));

                String hobbies_value= hobiesText.getText().toString();

                if (hobbies_value.equals("Dosen't Matter"))
                {
                    hobbies_value = "";
                    update_yourlikes(channel,hobbies_value,interest_id,music_id,f_reads_id,MOVIES_ID,SPORTS_ID,COUSINE_ID,DRESS_ID);
                }
                else {
                    update_yourlikes(channel,hobbies_value,interest_id,music_id,f_reads_id,MOVIES_ID,SPORTS_ID,COUSINE_ID,DRESS_ID);
                }




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
        }    }
    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private ProgressDialog dialog = new ProgressDialog(Edit_yourLikes.this);
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

            Toast.makeText(getApplicationContext(), "Image uploaded"+result,Toast.LENGTH_LONG).show();
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
        SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
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


    private void update_yourlikes(final String userid,final String hobbies,final String interest,final String favmusic,final String favreads,final String favmovies,final String sportsactvity,final String favcusine,final String dessstyle ) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        final StringRequest strReq = new StringRequest(Request.Method.POST,
                FOR_UPDATE_YOURLIKES, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {
                    JSONObject jarray = new JSONObject(response);

                    boolean error = jarray.getBoolean("error");
                    if (!error) {
//                        String user = jObj.getJSONObject("user").getString("name");
                        String msg = jarray.getString("message");
                        Toast.makeText(getApplicationContext(),
                                msg, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                        startActivity(intent);

                    } else {

                        String errorMsg = jarray.getString("message");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
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
        })
        {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("hobbies", hobbies);
                params.put("interest", interest);
                params.put("favmusic", favmusic);
                params.put("favreads", favreads);
                params.put("favmovies", favmovies);
                params.put("sportsactvity", sportsactvity);
                params.put("favcusine", favcusine);
                params.put("dessstyle", dessstyle);
                return params;
            }
        };


        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//         Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void getYourLikes() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(Tryfragment,MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = GET_YOUR_LIKES + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("hobbies");
                    System.out.print(jUser);
                    JSONObject c = jUser.getJSONObject(0);

                    String msg = jarray.getString("message");
                    if (!msg.equals("There is no records."))

//{
                    {
                        String Hobbies = c.getString("Hobbies");
                        Interests = c.getString("Interests");
                        music_values = c.getString("FavoriteMusic");
                        FavoriteReads_valuew = c.getString("FavoriteReads");
                        PreferredMovies_values = c.getString("PreferredMovies");
                        SportsFitnessActivities_values = c.getString("SportsFitnessActivities");
                        FavoriteCuisine_values = c.getString("FavoriteCuisine");
                        PreferredDressStyle_value = c.getString("PreferredDressStyle");


                        hobiesText.setText(Hobbies);

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


    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
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


    private void getData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(URL_FOR_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            interestList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("interests");


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




                contries.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        contries.add(0, "Select");
        //Setting adapter to show the items in the spinner
        interest.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, contries));
        interest.setSelection(contries.indexOf(Interests));
    }

    private void getMusic(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Music,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            music_list = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("favmusic");


                            getStudentsss(res_music);
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
    private void getStudentsss(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                musics.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        musics.add(0, "Select");
        //Setting adapter to show the items in the spinner
        music.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, musics));
        music.setSelection(musics.indexOf(music_values));
    }

    private void getFReads(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(FREADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            f_reads_list = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("favreads");


                            getAllFReads(res_music);
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
    private void getAllFReads(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                FREADS_ARRAY.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        FREADS_ARRAY.add(0, "Select");
        //Setting adapter to show the items in the spinner
        f_reads.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, FREADS_ARRAY));
        f_reads.setSelection(FREADS_ARRAY.indexOf(FavoriteReads_valuew));
    }

    private void getPMovies(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(P_MOVIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            MOVIES_LIST = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("favmovies");


                            getAllPMovies(res_music);
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
    private void getAllPMovies(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                P_MUSIC_ARRAY.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        P_MUSIC_ARRAY.add(0, "Select");
        //Setting adapter to show the items in the spinner
        p_movies.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, P_MUSIC_ARRAY));
        p_movies.setSelection(P_MUSIC_ARRAY.indexOf(PreferredMovies_values));
    }

    private void getSports(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(SPORTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            SPORTS_LIST = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("sportsactivity");


                            getAllsports(res_music);
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
    private void getAllsports(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                SPORTS_ARRAY.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        SPORTS_ARRAY.add(0, "Select");
        //Setting adapter to show the items in the spinner
        sports.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, SPORTS_ARRAY));
        sports.setSelection(SPORTS_ARRAY.indexOf(SportsFitnessActivities_values));
    }

    private void getCuisine(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(CUISINE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            COUSINE_LIST = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("favcuisine");


                            getAllCusine(res_music);
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
    private void getAllCusine(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                CUISINE_ARRAY.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        CUISINE_ARRAY.add(0, "Select");
        //Setting adapter to show the items in the spinner
        cousine.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, CUISINE_ARRAY));
        cousine.setSelection(CUISINE_ARRAY.indexOf(FavoriteCuisine_values));
    }

    private void getdress(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(DRESSSTYLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {

                            DRESS_LIST = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            res_music = j.getJSONArray("dresstyle");


                            getAllDress(res_music);
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
    private void getAllDress(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString("name");

                System.out.print(CityID);


                DRESS_ARRAY.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        DRESS_ARRAY.add(0, "Select");
        //Setting adapter to show the items in the spinner
        dressS.setAdapter(new ArrayAdapter<String>(Edit_yourLikes.this, android.R.layout.simple_spinner_dropdown_item, DRESS_ARRAY));
        dressS.setSelection(DRESS_ARRAY.indexOf(PreferredDressStyle_value));
    }

    public void back(View a){
        Intent intent = new Intent(getApplicationContext(),EditProfile.class);
        startActivity(intent);

    }
}
