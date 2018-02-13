package com.villupuram.nayarishta;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.squareup.picasso.Picasso;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.R.attr.bitmap;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.R.drawable.camera;
import static com.villupuram.nayarishta.R.id.addtext;

public class try_two extends Fragment {

    private static final String TAG = "try_two";

    private GridView mGridView;
    private ProgressBar mProgressBar;
//    private ImageAdapter_My_Album mGridAdapter;
//    private ArrayList<GridItemnew> mGridData;
    ProgressDialog progressDialog;
    SharedPreferences sharedpreferencestwo;
    private static String URL_FOR_DETAIL = "",URL_FOR_UPLOAD_PHOTO = "";
    private static String ALBUM = "";
    private String DELETE_PHOTO ="http://nayarishta.com/nayarishta-app/api/deleteGalleryInfo.php";
    private String UPLOAD_PHOTO ="http://nayarishta.com/nayarishta-app/api/deleteGalleryInfo.php";
    private int PICK_IMAGE_REQUEST = 1;
    public static String  profilephoto ="";
    Context mcontext;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    private static final int STORAGE_PERMISSION_CODE = 123;
    String encodedString;
    String imgPath, fileName;
    Bitmap bitmap;

    private static int RESULT_LOAD_IMG = 1;
    private static int RESULT_OK = 1;
    private ImageView imageView;
    private String WEB_NAME="http://nayarishta.com/";
    private Uri filePath;
    public static String gallery_id="";
    // Declare Variables
    try_two mContext;
    private JSONArray result;
    JSONObject response;
    Button addphoto;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItemnew> mGridData;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_try_two,
                container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        mGridView = (GridView) view.findViewById(R.id.grid_view);
        //Initialize with empty data
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        addphoto = (Button)view.findViewById(R.id.button);





        mGridData = new ArrayList<>();
//        mGridAdapter = new ImageAdapter_My_Album(getActivity(), R.layout.row_gridview_item, mGridData);
        mGridAdapter = new GridViewAdapter(getActivity(),mContext,R.layout.row_gridview_item, mGridData);

        mGridView.setAdapter(mGridAdapter);

        //Start download

        SharedPreferences shared = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));


        URL_FOR_DETAIL = base_api_url.USER_GALLERY;

        ALBUM = URL_FOR_DETAIL + channel;


        new AsyncHttpTask().execute(ALBUM);
        mProgressBar.setVisibility(View.VISIBLE);






        return view;

    }


    public void name (){

//        Toast.makeText(getActivity(),"fsdf",Toast.LENGTH_LONG).show();
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
            }
            catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());

            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(getActivity(), "No Records!", Toast.LENGTH_SHORT).show();
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
            response = new JSONObject(result);

            if (!response.getString("message").equals("There is no records.")) {
                JSONArray posts = response.optJSONArray("Gallery");
                GridItemnew item;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    item = new GridItemnew();

                    item.setImage(c.getString("gallerypath"));


                    mGridData.add(item);
                }

            }else {
                Toast.makeText(getApplicationContext(),"No records Found.",Toast.LENGTH_LONG).show();
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public  class GridViewAdapter extends ArrayAdapter<GridItemnew>{
        private Context mContext;
        private int layoutResourceId;
        try_two try_two;
        private ArrayList<GridItemnew> mGridData = new ArrayList<GridItemnew>();

        public GridViewAdapter(Context mContext,try_two myFragmentNew, int layoutResourceId, ArrayList<GridItemnew> mGridData) {
            super(mContext, layoutResourceId, mGridData);
            this.layoutResourceId = layoutResourceId;
            this.mContext = mContext;
            this.mGridData = mGridData;
            this.try_two = myFragmentNew;
        }

        /**
         * Updates grid data and refresh grid items.
         * @param mGridData
         */
        public void setGridData(ArrayList<GridItemnew> mGridData) {
            this.mGridData = mGridData;

            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
                holder.imageButton=(ImageButton)row.findViewById(R.id.cross);
                holder.profilebtn=(ImageButton)row.findViewById(R.id.profilepic);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }

            int itemCount = mGridData.size();
            GridItemnew item = mGridData.get(position);
            holder.imageButton.setTag(Integer.valueOf(position));

            Picasso.with(mContext).load(item.getImage()).resize(400, 600)
                    .onlyScaleDown() .into(holder.imageView);
            holder.imageButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View arg1)
                {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Do you want to delete this image?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                    JSONObject jsonResponse = null;
                                    try {
                                        result = response.getJSONArray("Gallery");
                                        JSONObject jsonn = result.getJSONObject(position);

                                        String valusd = jsonn.getString("galleryid");
                                        gallery_id = valusd;

                                        g_login();
                                        mGridData.remove(position);
                                        notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();



                    // list.remove(position);

                }
            });


            return row;
        }



        class ViewHolder {
            TextView titleTextView;
            ImageView imageView;
            ImageButton imageButton, profilebtn;
        }
    }




    private void g_login() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        URL_FOR_DETAIL = DELETE_PHOTO+"?galleryid="+gallery_id;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {

                    JSONObject jsonObj = new JSONObject(response);

                    Toast.makeText(getActivity(),jsonObj.getString("message"),Toast.LENGTH_LONG);


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
        }) ;
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void uploadphoto(final String userid, final String file) {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        SharedPreferences shared = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));


        StringRequest strReq = new StringRequest(Request.Method.POST,
                UPLOAD_PHOTO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {

                    JSONObject jsonObj = new JSONObject(response);

                    Toast.makeText(getApplicationContext(),jsonObj.getString("message"),Toast.LENGTH_LONG);


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
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("file", file);

                return params;
            }
        };

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





//

}

