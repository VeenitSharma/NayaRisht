package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.Search;
import com.villupuram.nayarishta.constant.base_api_url;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;
import static com.villupuram.nayarishta.constant.base_api_url.MyCOnstantPreference;
import static com.villupuram.nayarishta.constant.base_api_url.PhotoUrl;
import static com.villupuram.nayarishta.constant.base_api_url.bydefaultstatus;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

public class    SearchResults extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    Context context;
    ImageView search;
    int start = 0;
    int limit = 10;
    boolean loadingMore = false;
    View loadMoreView;
//    ItemAdapter adapter;
   final String Gender="";
    private static final String TAG = "SearchResults";
    private GoogleApiClient mGoogleApiClient;
    public static String disname="",jsonResult="";
    ProgressDialog progressDialog;
    private static String URL_FOR_BASICSEARCH = "",URL_FOR_DETAIL="",URL_FOR_QUICKSEARCH="",URL_FOR_ADVANCESEARCH="";
    ImageView userimage;
//    private List<Movie> movieList = new ArrayList<Movie>();
//    private CustomListAdapter adapter;
    private static final String URL_FOR_Searsh = "http://nayarishta.com/nayarishta-app/api/search.php?";
    private static final String URL_QUICKSEARCH = "http://nayarishta.com/nayarishta-app/api/full-search.php";
//    ProgressDialog progressDialog;
    private static String URL_FOR_SEARCH = "";
//    String[]data;
//    ArrayList<HashMap<String, String>> contactList;

    private ArrayList<SearchItem> mListData;
    private SearchAdapter mListAdapter;

    ArrayList<String> al = new ArrayList<>();

    private int bootCounter=0;
    private int maxRecords = 400;
    String BasicSearch,QuickSEarch,AdvancewSearch;
    ArrayList arrayList2 = new ArrayList();
    int num = 1;
    int positionn = 0;
    SharedPreferences sharedpConstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        search = (ImageView)findViewById(R.id.searchicon);
        listView=(ListView)findViewById(R.id.listView);

        Button morebtn =(Button)findViewById(R.id.more);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shared = getSharedPreferences(MyCOnstantPreference, MODE_PRIVATE);
               String channel = (shared.getString("session_status", ""));
                if (!channel.equals("")){
                    Intent intent = new Intent(getApplicationContext(),SearchClick.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),Search.class);
                    startActivity(intent);
                }

            }
        });

        mListData = new ArrayList<>();

        sharedpConstant = getSharedPreferences(MyCOnstantPreference, Context.MODE_PRIVATE);




        mListAdapter = new SearchAdapter(getApplicationContext(), R.layout.row_layout, mListData);
        listView.setAdapter(mListAdapter);


//
//        contactList = new ArrayList<>();


         jsonResult=getIntent().getExtras().getString("json");
        final String IDENTITY =getIntent().getExtras().getString("identity");

        final String Gender =getIntent().getExtras().getString("Gender");
        final String agefrom =getIntent().getExtras().getString("agefrom");
        final String ageto =getIntent().getExtras().getString("ageto");
        final String riligion =getIntent().getExtras().getString("riligion");
        final String occupation =getIntent().getExtras().getString("occupation");

        final String heightfrom =getIntent().getExtras().getString("heightfrom");

        final String heightto =getIntent().getExtras().getString("heightto");
        final String maritalstatus =getIntent().getExtras().getString("maritalstatus");
        final String religionid =getIntent().getExtras().getString("religionid");
        final String casteid =getIntent().getExtras().getString("casteid");

        final String languageid =getIntent().getExtras().getString("languageid");
        final String countryid =getIntent().getExtras().getString("countryid");
        final String income =getIntent().getExtras().getString("income");

        String encodedurl = "";
        URL_FOR_BASICSEARCH = URL_FOR_Searsh+"lookingfor=" + Gender + "&agefrom=" + agefrom + "&ageto=" + ageto + "&heightfrom=" + heightfrom + "&heightto=" +heightto+ "&religion=" + riligion + "&occupation=" + occupation;


        String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
        BasicSearch = Uri.encode(URL_FOR_BASICSEARCH, ALLOWED_URI_CHARS);

        URL_FOR_QUICKSEARCH=URL_QUICKSEARCH+"?agefrom="+agefrom+"&ageto="+ageto+"&heightfrom="+heightfrom+"&heightto="+heightto+"&maritalstatus="+maritalstatus+"&religionid="+religionid+"&casteid="+casteid;
        String ALLOWED_URI_CHARSs = "@#&=*+-_.,:!?()/~'%";
        QuickSEarch = Uri.encode(URL_FOR_QUICKSEARCH, ALLOWED_URI_CHARSs);


        URL_FOR_ADVANCESEARCH = URL_QUICKSEARCH+"?agefrom="+agefrom+"&ageto="+ageto+"&heightfrom="+heightfrom+"&heightto="+heightto+"&maritalstatus="+maritalstatus+"&religionid="+religionid+"&languageid="+languageid+"&countryid="+countryid+"&occupation="+occupation+"&income="+income;
        String ALLOWED_URI_CHARSss = "@#&=*+-_.,:!?()/~'%";
        AdvancewSearch = Uri.encode(URL_FOR_ADVANCESEARCH, ALLOWED_URI_CHARSss);


//        new AsyncHttpTask().execute(encodedurl);
        progressDialog.setMessage("Please wait...");
        showDialog();

        if (IDENTITY.toString().equals("search")){
            Showdataa();
        }
        else if(IDENTITY.toString().equals("quicksearch"))
        {
            ShowQuicksearch();

        }
        else if(IDENTITY.toString().equals("advance")){
            ShowAdvanceksearch();
        }

        SharedPreferences shared = getSharedPreferences(Tryfragment, MODE_PRIVATE);
        disname=(shared.getString("username",""));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);



        if (bydefaultstatus==0){
            navigationView.getMenu().clear(); //clear old inflated items.
            navigationView.inflateMenu(R.menu.activity_dashboard_drawer);
//            navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
        }
        if (bydefaultstatus==0){
            View hView =  navigationView.inflateHeaderView(R.layout.nav_header_dashboard);
            TextView header = (TextView)hView.findViewById(R.id.header);
            header.setText(disname);
            loginUser();
        }

        navigationView.setNavigationItemSelectedListener(this);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = listView.getItemAtPosition(position).toString();
                System.out.print(value);

                try {
                    JSONArray json=new JSONArray(jsonResult);
                    String poss = String.valueOf(position);
                    for (int i = 0; i < json.length(); i++) {
                        if(i==position) {
                            JSONObject c = json.getJSONObject(i);

                            String idd  = c.getString("userprofileid");
                            String gender = c.getString("userprofilegender");
                            Intent intent = new Intent(SearchResults.this,BasicDetail.class);
                            intent.putExtra("id",idd);
                            intent.putExtra("json",jsonResult);
                            intent.putExtra("gender",gender);
                            intent.putExtra("type",IDENTITY);
                            intent.putExtra("Gender",Gender);
                            intent.putExtra("agefrom",agefrom);
                            intent.putExtra("ageto",ageto);
                            intent.putExtra("heightfrom",heightfrom);
                            intent.putExtra("heightto",heightto);
                            intent.putExtra("riligion",riligion);
                            intent.putExtra("occupation",occupation);
                            startActivity(intent);



                            break;
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                }

        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {



            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {




            }

        });


        int visibleChildCount = (listView.getLastVisiblePosition() - listView.getFirstVisiblePosition()) + 1;


//        if((mListAdapter.num)*8 == mListData.size() ){
//            System.out.print("hii");
//            morebtn.setVisibility(View.VISIBLE);
//            Toast.makeText(getApplicationContext(),"Hii",Toast.LENGTH_LONG).show();

//        else {
//            morebtn.setVisibility(View.GONE);
//            Toast.makeText(getApplicationContext(),"Hello"+mListAdapter.num,Toast.LENGTH_LONG).show();
//        }



        morebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((mListAdapter.num)*8 < mListData.size())
                {
                    progressDialog.setMessage("Please wait a moment");
                    showDialog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 5000);
                    mListAdapter.num = mListAdapter.num +1;
                    mListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(),"No more records found.",Toast.LENGTH_LONG).show();
                }

            }

        });

    }




    private void loginUser() {
        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("wait..");
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
                    String photourl = c.getString("userphoto");
                    String gender = c.getString("gender");

                    PhotoUrl = photourl;
                    ImageView slideimg=(ImageView)findViewById(R.id.user_image_slide);
                    if (!photourl.equals("")){
                        Picasso.with(getApplicationContext()).load(photourl).resize(400,400).into(slideimg);
                    }
                    else {
                        if (gender.toString().equals("F")){
                            slideimg.setImageResource(R.drawable.femaledefault);

                        }
                        else {
                            slideimg.setImageResource(R.drawable.femaledefault);

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
//
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void Showdataa() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));


        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_BASICSEARCH, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);

                    String message = jarray.getString("message");

                    if (message.equals("There is no profile matched for the selected criteria.")) {

                        Toast.makeText(getApplicationContext(), "No Records Found!", Toast.LENGTH_LONG).show();
                    }
                    else {
                        SearchItem item;

                        JSONArray jUser = jarray.getJSONArray("users");
                        for (int i = 0; i < jUser.length(); i++) {
                            JSONObject c = jUser.getJSONObject(i);
                            item = new SearchItem();

                            item.setId(c.getString("userprofileid"));
                            item.setGender(c.getString("userprofilegender"));
                            item.setName(c.getString("userprofilefirstname"));
                            item.setDetail(c.getString("userprofileheightft") + "'" + c.getString("userprofileheightinch") + "'-" + c.getString("userprofileheightcm") + "cm" + ", " + (c.getString("religion") + ", " + (c.getString("userprofileoccupation"))));
                            item.setImage(c.getString("userprofilephoto"));

                            mListAdapter.notifyDataSetChanged();

                            mListData.add(item);
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
//
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void ShowQuicksearch() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();



        final StringRequest strReq = new StringRequest(Request.Method.GET,
                QuickSEarch, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);
                    SearchItem item;
                    int age;
                    JSONArray jUser = jarray.getJSONArray("users");
                    for (int i = 0; i < jUser.length(); i++) {
                        JSONObject c = jUser.getJSONObject(i);
                        item = new SearchItem();
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR)  ;

                        item.setId(c.getString("userprofileid"));
                        Integer getyear = Integer.valueOf(c.getString("userprofileyear"));

                        age = year-getyear;
                        item.setGender(c.getString("userprofilegender"));
                        item.setName(c.getString("userprofilefirstname"));
                        item.setDetail(c.getString("userprofileheightft")+ "'"+c.getString("userprofileheightinch")+"'-"+c.getString("userprofileheightcm")+"cm"+","+(c.getString("religion")+","+(c.getString("userprofileoccupation"))));

                        if (c.getString("userprofilephoto").equals(null))
                        {

                        }
                        item.setImage(c.getString("userprofilephoto"));

                        mListAdapter.notifyDataSetChanged();

                        mListData.add(item);
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    private void ShowAdvanceksearch() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("Please wait a moment");

        showDialog();
        SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));


        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_ADVANCESEARCH, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {

                    JSONObject jarray = new JSONObject(response);
                    SearchItem item;
                    int age;
                    JSONArray jUser = jarray.getJSONArray("users");
                    for (int i = 0; i < jUser.length(); i++) {
                        JSONObject c = jUser.getJSONObject(i);
                        item = new SearchItem();
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR)  ;

                        item.setId(c.getString("userprofileid"));
                        item.setGender(c.getString("userprofilegender"));
                        item.setName(c.getString("userprofilefirstname"));
                        item.setDetail(c.getString("userprofileheightft")+ "'"+c.getString("userprofileheightinch")+"'-"+c.getString("userprofileheightcm")+"cm"+","+(c.getString("religion")+","+(c.getString("userprofileoccupation"))));
                        item.setImage(c.getString("userprofilephoto"));

                        mListAdapter.notifyDataSetChanged();

//                            Picasso.with(getApplicationContext()).load(item.getImage()).into(holder.imageView);



                        mListData.add(item);
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
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }





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
                Log.d(TAG, e.getLocalizedMessage());
                String err = (e.getMessage()==null)?"SD Card failed":e.getMessage();
                Log.e("sdcard-err2:",err);
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {


            } else {
                Toast.makeText(SearchResults.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            hideDialog();
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


    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            if (!response.getString("message").equals("There is no profile matched for the selected criteria.")) {
                JSONArray posts = response.optJSONArray("users");
                SearchItem item;
                  int age;
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject c = posts.getJSONObject(i);
                    item = new SearchItem();

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR)  ;

                    item.setId(c.getString("userprofileid"));
                    Integer getyear = Integer.valueOf(c.getString("year"));

                    age = year-getyear;

                    item.setName(c.getString("userprofilefirstname"));
                    item.setDetail(c.getString(age+","+"cityname"+","+"countryname"));

                    item.setImage(c.getString("userprofilephoto"));



//                            Picasso.with(getApplicationContext()).load(item.getImage()).into(holder.imageView);



                    mListData.add(item);
                }

            }else {
                Toast.makeText(getApplicationContext(),"No records Found.",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.search_results, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
        public void checklogin(){
            if (bydefaultstatus==1){
                Intent it=new Intent(SearchResults.this,Splash.class);
                startActivity(it);
            }
       else if (globallogincheck==0){
            Intent it=new Intent(SearchResults.this,TryFragmentActivity.class);
            startActivity(it);
        }
        else if(globallogincheck==1){
            Intent it=new Intent(SearchResults.this,Splash.class);
            startActivity(it);
        }}
    public void redirectsearch(){
        if (bydefaultstatus==1){
            Intent it=new Intent(SearchResults.this,Search.class);
            startActivity(it);
        }

       else if (globallogincheck==0){
            Intent it=new Intent(SearchResults.this,SearchClick.class);
            startActivity(it);
        }
        else if(globallogincheck==1){
            Intent it=new Intent(SearchResults.this,Search.class);
            startActivity(it);
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Intent it=new Intent(SearchResults.this,Login2.class);
                        startActivity(it);
                    }
                });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        if (bydefaultstatus==0){
            if (id == R.id.profile) {
                Intent it=new Intent(SearchResults.this,TryFragmentActivity.class);
                startActivity(it);
            }

            if (id == R.id.dashboard) {
                Intent it=new Intent(SearchResults.this,Dashboard.class);
                startActivity(it);
            }
            else if (id == R.id.search) {
                Intent it=new Intent(SearchResults.this,SearchClick.class);
                startActivity(it);
            }else if (id == R.id.compose) {

            }
            else if (id == R.id.Messenger) {
                Intent it=new Intent(SearchResults.this,User.class);
                startActivity(it);
            }

            else if (id == R.id.inbox) {
                Intent it=new Intent(SearchResults.this,Inbox.class);
                startActivity(it);


            } else if (id == R.id.sent) {
                Intent it=new Intent(SearchResults.this,Sent.class);
                startActivity(it);

            } else if (id == R.id.drafts) {
                Intent it=new Intent(SearchResults.this,Drafts.class);
                startActivity(it);

            }else if (id == R.id.isend) {
                Intent it=new Intent(SearchResults.this,InterestSend.class);
                startActivity(it);

            } else if (id == R.id.irecieved) {
                Intent it=new Intent(SearchResults.this,InterestRecieved.class);
                startActivity(it);

            } else if (id == R.id.yaccept) {
                Intent it=new Intent(SearchResults.this,Yaccepted.class);
                startActivity(it);

            } else if (id == R.id.accepty) {
                Intent it=new Intent(SearchResults.this,AcceptedY.class);
                startActivity(it);


            } else if (id == R.id.ydecline) {
                Intent it=new Intent(SearchResults.this,YDeclined.class);
                startActivity(it);

            }else if (id == R.id.decliney) {
                Intent it=new Intent(SearchResults.this,DeclinedY.class);
                startActivity(it);
            }
//            else if (id == R.id.for_accept)
//            {
//                Intent it=new Intent(SearchResults.this,ForAccepted.class);
//                startActivity(it);
//            }
//
//            else if (id == R.id.to_accept) {
//                Intent it=new Intent(SearchResults.this,ToAccept.class);
//                startActivity(it);
//
//            }
            else if (id == R.id.about) {
                Intent it=new Intent(SearchResults.this,About.class);
                startActivity(it);

            } else if (id == R.id.membership) {
                Intent it=new Intent(SearchResults.this,MemberShip_Main.class);
                startActivity(it);

            } else if (id == R.id.change) {
                Intent it=new Intent(SearchResults.this,ChangePassword.class);
                startActivity(it);
            }

            else if (id == R.id.contact) {

                Intent it=new Intent(SearchResults.this,Contact.class);
                startActivity(it);

            }else if (id == R.id.logout) {

                FirebaseAuth.getInstance().signOut();
                FacebookSdk.sdkInitialize(getApplicationContext());

                LoginManager.getInstance().logOut();

                bydefaultstatus=1;

                globallogincheck=1;

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                    signOut();
                } else {
                    // not signed in. Show the "sign in" button and explanation.
                    // ...
                    Intent it=new Intent(SearchResults.this,Login2.class);
                    it.putExtra("logout","hello");
                    startActivity(it);


                    SharedPreferences.Editor editor = sharedpConstant.edit();
                    editor.putString("session_status", "");
                    editor.commit();

                }
            }
        }
        else{ if (id == R.id.nav_camera) {
            // Handle the camera action
            checklogin();
        } else if (id == R.id.nav_gallery) {
            Intent it=new Intent(SearchResults.this,About.class);
            startActivity(it);
        } else if (id == R.id.nav_slideshow) {
            Intent it=new Intent(SearchResults.this,MemberShip_Main.class);
            startActivity(it);

        } else if (id == R.id.nav_manage) {
            Intent it=new Intent(SearchResults.this,Contact.class);
            startActivity(it);
        } else if (id == R.id.nav_manage1) {
            redirectsearch();
        }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
