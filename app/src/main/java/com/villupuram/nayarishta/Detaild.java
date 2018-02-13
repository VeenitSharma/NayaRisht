package com.villupuram.nayarishta;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import static com.villupuram.nayarishta.Register.RegisterPref;

public class Detaild extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaild);



        String positionValue=getIntent().getStringExtra("value");
        String jsonResult= getIntent().getStringExtra("jsonResult");

//        Toast.makeText(getApplicationContext(),positionValue,Toast.LENGTH_LONG).show();


        try {
            JSONArray json=new JSONArray(jsonResult);
            int pos = Integer.parseInt(positionValue);
            for (int i = 0; i < json.length(); i++) {
                if(i==pos) {
                    JSONObject c = json.getJSONObject(i);
                    String userName = c.getString("username");

//
//                    if (!image.equals("")){
//                        Picasso.with(getApplicationContext()).load(image).resize(400,400).into(userimage);
//                    }


//
//
//                    TextView profileText = (TextView) findViewById(R.id.profileTxt);
//                    TextView dateText = (TextView) findViewById(R.id.datetext);
//                    TextView username = (TextView) findViewById(R.id.username);
//                    TextView profileId = (TextView) findViewById(R.id.profileId);
//                    TextView heighttext = (TextView) findViewById(R.id.heighttext);
//                    TextView m_status = (TextView) findViewById(R.id.m_status);
////                    TextView no_children = (TextView) findViewById(R.id.no_children);


//
//                    profileId.setText(userid);
//                    username.setText(Name);
//                    profileText.setText(profilecreatedfor);
//                    dateText.setText(dob);
//                    riligioustext.setText(religion);
//                    mothertanguetext.setText(mtangue);
//                    professiontext.setText(occupation);
//                    mobiletext.setText(mobile);
//                    educationtext.setText(educaion);
//                    annualtext.setText(income);
//                    aboutfamilytext.setText(family);

                    Toast.makeText(getApplicationContext(),userName,Toast.LENGTH_LONG).show();

//                    if (!height.equals("'''()")){
//                        heighttext.setText(height);
//                    }
//                    else {
//                        heighttext.setText("None");
//                    }
//                    m_status.setText(maritalstatus);
////                    no_children.setText(havechildren);


                    break;
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


//            String positionValue=getIntent().getStringExtra("value");
//
//
//                try {
//                    JSONArray json=new JSONArray(jsonResult);
//                    int pos = Integer.parseInt(positionValue);
//                    for (int i = 0; i < json.length(); i++) {
//                        if(i==pos) {
//                            JSONObject c = json.getJSONObject(i);
//
//
//                    JSONArray jUser = jarray.getJSONArray("user");
//                    System.out.print(jUser);
//                    JSONObject c = jUser.getJSONObject(0);
//                    String userid = c.getString("profileid");
//                    String userName = c.getString("username");
//
//                    String Name = c.getString("firstname") +" "+c.getString("lastname");
//                    lastname = c.getString("lastname");
//                    profilecreatedfor =  c.getString("profilecreatedfor");
//                    gender =  c.getString("gender");
//                    day = (c.getString("day"));
//                    month =(c.getString("month"));
//                    int year = Integer.parseInt((c.getString("year")));
//                    String dob =  c.getString("day")+ "-"+c.getString("month")+"-"+c.getString("year");
//                    dateofbirth=  c.getString("dateofbirth");
//                    religionname =  c.getString("religionname");
//                    languagename =  c.getString("languagename");
//                    mobile =  c.getString("mobile");
//
//                    String heightt =  c.getString("feet")+ "'"+c.getString("inch")+"'-"+c.getString("cm")+"cm";
////                    height =  c.getString("height");
//                    maritalstatuss=  c.getString("maritalstatus");
//                    havechildren =  c.getString("havechildren");
//                    educationname =  c.getString("educationname");
//                    professionname =  c.getString("professionname");
//                    annualincome =  c.getString("annualincome");
//                    countryname =  c.getString("countryname");
//                    statename =  c.getString("statename");
//                    cityname =  c.getString("cityname");
//                    residencystatus =  c.getString("residencystatus");
//                    religionn =  c.getString("religion");
//                    mothertongue =  c.getString("mothertongue");
//                    persninfo =  c.getString("personalityinfo");
//                    countrytext.setText(c.getString("countryname"));
//                    countytext.setText(c.getString("statename"));
//                    citytext.setText(c.getString("cityname"));
//                    resitext.setText(c.getString("residencystatus"));
//                    specialcasestext.setText(c.getString("specialcases"));
//                    bodytypetext.setText(c.getString("bodytype"));
//                    complexiontext.setText(c.getString("complexion"));
//                    educationtext.setText(c.getString("educationname"));
//                    branchtext.setText(c.getString("branchname"));
//                    professiontext.setText(c.getString("professionname"));
//                    annualtext.setText(c.getString("annualincome"));
//                    riligioustext.setText(c.getString("religion"));
//                    castetext.setText(c.getString("castename"));
//                    subcastetext.setText(c.getString("subcastename"));
//                    gotratext.setText(c.getString("gotra"));
//                    mothertanguetext.setText(c.getString("mothertongue"));
//                    mothertext.setText(c.getString("motheris"));
//                    fathertext.setText(c.getString("fatheris"));
//                    totalsistertext.setText(c.getString("sister"));
//                    totalbrotest.setText(c.getString("brothers"));
//                    totalmarriedsister.setText(c.getString("sismarried"));
//                    totalbromarried.setText(c.getString("bromarried"));
//                    famvaluestext.setText(c.getString("familyvalues"));
//                    famtypes.setText(c.getString("familytype"));
//                    aboutfamilytext.setText(c.getString("aboutmyfamily"));
//                    mobiletext.setText(c.getString("mobileisd")+""+""+c.getString("mobilenumber"));
//                    landlinetext.setText(c.getString("phoneisd")+""+""+c.getString("phonestd")+""+""+c.getString("phonelandline"));
//                    conpersontext.setText(c.getString("contactperson"));
//                    suitablecalltext.setText(c.getString("calltime"));
//                    displayoptiontext.setText(c.getString("displayoption"));
//                    diettext.setText(c.getString("diet"));
//                    drinktext.setText(c.getString("drink"));
//                    smoketext.setText(c.getString("smoke"));
//                    bloodtext.setText(c.getString("bloodgroup"));
////                    hobbiestext.setText(c.getString("bloodgroup"));
//
//                    Calendar calendar = Calendar.getInstance();
//                    int cyear = calendar.get(Calendar.YEAR);
//                    Age = String.valueOf((cyear - year));
//
//                    TextView profileText = (TextView) getView().findViewById(R.id.ptext);
//                    TextView dateText = (TextView) getView().findViewById(R.id.dtext);
//                    TextView agetext = (TextView) getView().findViewById(R.id.atext);
//
//                    TextView heighttext = (TextView) getView().findViewById(R.id.htext);
//                    TextView m_status = (TextView) getView().findViewById(R.id.mtext);
//                    TextView no_children = (TextView) getView().findViewById(R.id.no_childr);
//                    TextView pinfo = (TextView) getView().findViewById(R.id.personalinfo);
////
//
//                    profileText.setText(profilecreatedfor);
//                    dateText.setText(dob);
//                    heighttext.setText(heightt);
//                    m_status.setText(maritalstatuss);
//
//                    if (havechildren=="-select"){
//                        no_children.setText("");
//                    }else {
//                        no_children.setText(havechildren);
//                    }
//
//                    pinfo.setText(persninfo);
//
//
//
//
//                    agetext.setText(Age);
//
//
//
//
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString("userName", userName);
//                    editor.putString("dateofbirth", dateofbirth);
//                    editor.putString("countryname", countryname);
//                    editor.putString("mothertongue", mothertongue);
//                    editor.putString("educationname", educationname);
//                    editor.putString("professionname", professionname);
//                    editor.putString("annualincome", annualincome);
//                    editor.putString("maritalstatus", maritalstatuss);
//                    editor.putString("residencystatus", residencystatus);
//                    editor.putString("religionn", religionn);
//                    editor.putString("lastname", lastname);
//                    editor.commit();
//
//
////                    agetext.setText(Age);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    System.out.print(e);
//                }
    }


}



