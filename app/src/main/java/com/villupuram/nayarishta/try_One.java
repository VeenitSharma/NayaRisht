package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.villupuram.nayarishta.Login2.MyPREFERENCES;
import static com.villupuram.nayarishta.Register.RegisterPref;
import static com.villupuram.nayarishta.TryFragmentActivity.Tryfragment;

public class try_One extends Fragment {
    public static final String try_one = "MyPrefs";

    private static final String TAG = "try_One";
    SharedPreferences sharedpreferences;
    ProgressDialog progressDialog;
    private static String URL_FOR_DETAIL = "",RESPONSE="",URL_FOR_YOUR_LIKES="";
    public static String channel="",Agepref="";
    public static String YourLikes="http://nayarishta.com/nayarishta-app/api/getYourLikes.php?userid=";
    public static String USERNAME=""; public static String ImageName="";
    public static String profilecreatedfor="";
    public static String Age="";
    public static String lastname="";
    public static String religionname="";
    public static String gender="";
    public static String maritalstatus="";
    public static String dateofbirth="";
    public static String languagename="";
    public static String height="";
    public static String occupationn="";
    public static String mobile="";
    public static String maritalstatuss="";
            public static String havechildren="";
    public static String educationname="";
    public static String branchname="";
    public static String professionname="";
    public static String annualincome="";
    public static String countryname="";
    public static String statename="";
    public static String cityname="";
    public static String residencystatus="";
            public static String religionn="";
    public static String castename="";
    public static String mothertongue="";
    public static String isonline="";
    public static String persninfo="";
    public static String day="";
    public static String month="";
    public static String year;

     TextView edit,personalinfo,agetext,countrytext,countytext,citytext,resitext,specialcasestext, bodytypetext, complexiontext,educationtext,branchtext,professiontext,annualtext,
             riligioustext,castetext,subcastetext,gotratext,mothertanguetext,mothertext,fathertext,
    totalsistertext,totalmarriedsister,totalbrotest,totalbromarried,famvaluestext,famtypes,aboutfamilytext,mobiletext,landlinetext,conpersontext,
             suitablecalltext,displayoptiontext,diettext,drinktext,smoketext,bloodtext,hobbiestext,interesttext,favoritemusictext,
             readstext,
    moviestext,activitytext,cuisinetext,dresstext;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_try__one,
                container, false);

        sharedpreferences = getActivity().getSharedPreferences(try_one, Context.MODE_PRIVATE);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        edit = (TextView) view.findViewById(R.id.edit);
        agetext = (TextView) view.findViewById(R.id.atext);


        countrytext = (TextView) view.findViewById(R.id.ctect);
        countytext = (TextView) view.findViewById(R.id.county);
        citytext = (TextView) view.findViewById(R.id.city);
        resitext = (TextView) view.findViewById(R.id.residency);
        specialcasestext = (TextView) view.findViewById(R.id.cases);
        bodytypetext = (TextView) view.findViewById(R.id.bodytype);
        complexiontext = (TextView) view.findViewById(R.id.complexion);
        educationtext = (TextView) view.findViewById(R.id.Heducation);
        branchtext = (TextView) view.findViewById(R.id.branchname);
        professiontext = (TextView) view.findViewById(R.id.Profession);
        annualtext = (TextView) view.findViewById(R.id.annualincome);
        riligioustext = (TextView) view.findViewById(R.id.religous);
        castetext = (TextView) view.findViewById(R.id.caste);
        subcastetext = (TextView) view.findViewById(R.id.subcaste);
        gotratext = (TextView) view.findViewById(R.id.gotra);
        mothertanguetext = (TextView) view.findViewById(R.id.mtangue);
        mothertext = (TextView) view.findViewById(R.id.motheris);
        fathertext = (TextView) view.findViewById(R.id.fatheris);
        totalsistertext = (TextView) view.findViewById(R.id.totalsister);
        totalmarriedsister = (TextView) view.findViewById(R.id.totalMarriedsister);
        totalbrotest = (TextView) view.findViewById(R.id.totalbrother);
        totalbromarried = (TextView) view.findViewById(R.id.totalMarriedbrother);
        famvaluestext = (TextView) view.findViewById(R.id.familyvalues);
        famtypes = (TextView) view.findViewById(R.id.familytype);
        aboutfamilytext = (TextView) view.findViewById(R.id.aboutfamily);
        mobiletext = (TextView) view.findViewById(R.id.mobile);
        landlinetext = (TextView) view.findViewById(R.id.landline);
        conpersontext = (TextView) view.findViewById(R.id.contactperson);
        suitablecalltext = (TextView) view.findViewById(R.id.calltime);
        displayoptiontext = (TextView) view.findViewById(R.id.displayoption);
        diettext = (TextView) view.findViewById(R.id.diet);
        drinktext = (TextView) view.findViewById(R.id.drink);
        smoketext = (TextView) view.findViewById(R.id.smoke);
        bloodtext = (TextView) view.findViewById(R.id.blood);
        hobbiestext = (TextView) view.findViewById(R.id.hobbies);
        interesttext = (TextView) view.findViewById(R.id.interests);
        favoritemusictext = (TextView) view.findViewById(R.id.music);
        readstext = (TextView) view.findViewById(R.id.freads);
        moviestext = (TextView) view.findViewById(R.id.movies);
        activitytext = (TextView) view.findViewById(R.id.activity);
        cuisinetext = (TextView) view.findViewById(R.id.Textcuision);
        dresstext = (TextView) view.findViewById(R.id.dress);

        personalinfo = (TextView) view.findViewById(R.id.personalinfo);

        edit.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                startActivity(intent);
            }
        });

        TextView agetext = (TextView) view.findViewById(R.id.atext);

//        SharedPreferences shared =getActivity().getSharedPreferences(RegisterPref,getContext().MODE_PRIVATE);
//        Agepref = (shared.getString("agevalue",""));
//        agetext.setText(Agepref);
        TextView m_status = (TextView) view.findViewById(R.id.mtext);
        TextView no_children = (TextView) view.findViewById(R.id.no_childr);
        SharedPreferences preferences =getActivity().getSharedPreferences(try_one,getContext().MODE_PRIVATE);
        String  mstats = preferences.getString("mstatus", "");
        String  ch = preferences.getString("ch", "");

//        no_children.setText(ch);
//        m_status.setText(mstats);
        loginUser();
        yourlikes();


        return view;




    }


    private void loginUser() {

        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getActivity().getSharedPreferences(MyPREFERENCES,getContext().MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_DETAIL = base_api_url.USER_DETAIL + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_DETAIL, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {
                    RESPONSE = response;


                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("user");


                    System.out.print(jUser);
                    JSONObject c = jUser.getJSONObject(0);
                    String userid = c.getString("profileid");
                    String userName = c.getString("username");

                    String Name = c.getString("firstname") +" "+c.getString("lastname");
                    lastname = c.getString("lastname");
                    profilecreatedfor =  c.getString("profilecreatedfor");
                    gender =  c.getString("gender");
                     day = (c.getString("day"));
                     month =(c.getString("month"));
                    year = ((c.getString("year")));
                    String dob =  c.getString("day")+ "-"+c.getString("month")+"-"+c.getString("year");
                    dateofbirth=  c.getString("dateofbirth");
                    religionname =  c.getString("religionname");
                    languagename =  c.getString("languagename");
                    mobile =  c.getString("mobile");


                    TextView heighttext = (TextView) getView().findViewById(R.id.htext);

                    if (c.getString("feet").equals("")&&c.getString("inch").equals("")&&c.getString("cm").equals("")){
                        heighttext.setText("None");
                    }
                    else{
                        String heightt =  c.getString("feet")+ "'"+c.getString("inch")+"'-"+c.getString("cm")+"cm";
                        heighttext.setText(heightt);
                    }


//                    height =  c.getString("height");
                    maritalstatuss=  c.getString("maritalstatus");
                    havechildren =  c.getString("havechildren");
                    educationname =  c.getString("educationname");
                    professionname =  c.getString("professionname");
                    annualincome =  c.getString("annualincome");
                    countryname =  c.getString("countryname");
                    statename =  c.getString("statename");
                    cityname =  c.getString("cityname");
                    residencystatus =  c.getString("residencystatus");
                    religionn =  c.getString("religion");
                    mothertongue =  c.getString("mothertongue");
                    persninfo =  c.getString("personalityinfo");
                    countrytext.setText(c.getString("countryname"));
                    countytext.setText(c.getString("statename"));
                    citytext.setText(c.getString("cityname"));
                    resitext.setText(c.getString("residencystatus"));
                    specialcasestext.setText(c.getString("specialcases"));
                    bodytypetext.setText(c.getString("bodytype"));
                    complexiontext.setText(c.getString("complexion"));
                    educationtext.setText(c.getString("educationname"));
                    branchtext.setText(c.getString("branchname"));
                    professiontext.setText(c.getString("professionname"));
                    annualtext.setText(c.getString("annualincome"));
                    riligioustext.setText(c.getString("religion"));
                    castetext.setText(c.getString("castename"));
                    subcastetext.setText(c.getString("subcastename"));
                    gotratext.setText(c.getString("gotra"));
                    mothertanguetext.setText(c.getString("mothertongue"));
                    mothertext.setText(c.getString("motheris"));
                    fathertext.setText(c.getString("fatheris"));
                    totalsistertext.setText(c.getString("sister"));
                    totalbrotest.setText(c.getString("brothers"));
                    totalmarriedsister.setText(c.getString("sismarried"));
                    totalbromarried.setText(c.getString("bromarried"));
                    famvaluestext.setText(c.getString("familyvalues"));
                    famtypes.setText(c.getString("familytype"));
                    aboutfamilytext.setText(c.getString("aboutmyfamily"));
                    mobiletext.setText(c.getString("mobileisd")+""+""+c.getString("mobilenumber"));
                    landlinetext.setText(c.getString("phoneisd")+""+""+c.getString("phonestd")+""+""+c.getString("phonelandline"));
                    conpersontext.setText(c.getString("contactperson"));
                    suitablecalltext.setText(c.getString("calltime"));
                    displayoptiontext.setText(c.getString("displayoption"));
                    diettext.setText(c.getString("diet"));
                    drinktext.setText(c.getString("drink"));
                    smoketext.setText(c.getString("smoke"));
                    bloodtext.setText(c.getString("bloodgroup"));
//                    hobbiestext.setText(c.getString("bloodgroup"));

                    Calendar calendar = Calendar.getInstance();
                    int cyear = calendar.get(Calendar.YEAR);

                    if (!year.equals("")){
                        final int a = Integer.parseInt(year);
                        Age = String.valueOf(((cyear - a)));
                    }



                    TextView profileText = (TextView) getView().findViewById(R.id.ptext);
                    TextView dateText = (TextView) getView().findViewById(R.id.dtext);
                    TextView agetext = (TextView) getView().findViewById(R.id.atext);


                    TextView m_status = (TextView) getView().findViewById(R.id.mtext);
                    TextView no_children = (TextView) getView().findViewById(R.id.no_childr);
                    TextView pinfo = (TextView) getView().findViewById(R.id.personalinfo);
//

                    profileText.setText(profilecreatedfor);
                    dateText.setText(dateofbirth);

                    m_status.setText(maritalstatuss);

                    if (havechildren=="-select"){
                        no_children.setText("");
                    }else {
                        no_children.setText(havechildren);
                    }

                    pinfo.setText(persninfo);




                        agetext.setText(Age);




                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("userName", userName);
                    editor.putString("dateofbirth", dateofbirth);
                    editor.putString("countryname", countryname);
                    editor.putString("mothertongue", mothertongue);
                    editor.putString("educationname", educationname);
                    editor.putString("professionname", professionname);
                    editor.putString("annualincome", annualincome);
                    editor.putString("maritalstatus", maritalstatuss);
                    editor.putString("residencystatus", residencystatus);
                    editor.putString("religionn", religionn);
                    editor.putString("lastname", lastname);
                    editor.putString("jsonarray",RESPONSE);
                    editor.commit();


//                    agetext.setText(Age);


                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getActivity(),
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
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void yourlikes() {
        // Tag used to cancel the request
        String cancel_req_tag = "search";
        progressDialog.setMessage("please wait a moment");

        showDialog();
        SharedPreferences shared = getActivity().getSharedPreferences(Tryfragment,getContext().MODE_PRIVATE);
        String channel = (shared.getString("user_id", ""));

        URL_FOR_YOUR_LIKES = YourLikes + channel;

        final StringRequest strReq = new StringRequest(Request.Method.GET,
                URL_FOR_YOUR_LIKES, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {

                Log.d(TAG, "TryFragmentActivity Response: " + response.toString());

                hideDialog();
                try {
                    RESPONSE = response;


                    JSONObject jarray = new JSONObject(response);

                    JSONArray jUser = jarray.getJSONArray("hobbies");


                    System.out.print(jUser);
                    JSONObject c = jUser.getJSONObject(0);
                    String userid = c.getString("userid");
                    String Hobbies = c.getString("Hobbies");
                    String Interests = c.getString("Interests");
                    String FavoriteMusic = c.getString("FavoriteMusic");
                    String FavoriteReads = c.getString("FavoriteReads");
                    String PreferredMovies = c.getString("PreferredMovies");
                    String SportsFitnessActivities = c.getString("SportsFitnessActivities");
                    String FavoriteCuisine = c.getString("FavoriteCuisine");
                    String PreferredDressStyle = c.getString("PreferredDressStyle");



                    hobbiestext.setText(Hobbies);
                    interesttext.setText(Interests);
                    favoritemusictext.setText(FavoriteMusic);
                    readstext.setText(FavoriteReads);
                    moviestext.setText(PreferredMovies);
                    activitytext.setText(SportsFitnessActivities);
                    cuisinetext.setText(FavoriteCuisine);
                    dresstext.setText(PreferredDressStyle);

//                    hobbiestext.setText(c.getString("bloodgroup"));






                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.print(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Search Error: " + error.getMessage());
                Toast.makeText(getActivity(),
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
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



}