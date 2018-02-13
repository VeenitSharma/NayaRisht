package com.villupuram.nayarishta;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
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
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.villupuram.nayarishta.ApiCall.AppSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static com.villupuram.nayarishta.constant.base_api_url.globallogincheck;

public class Register extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "http://nayarishta.com/nayarishta-app/api/registerUser.php";
    ProgressDialog progressDialog;
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/loginUser.php";



    private EditText username;
    private EditText emailreg;
    private EditText passwordreg;
    private EditText cpasswordreg;
    private EditText mobilereg;
    private static EditText datee;
    private EditText namef;
    private Button btnSignUp;
    private Button btnLinkLogin;
    private RadioGroup genderRadioGroup;
    Spinner spiner;
    DatePickerDialog datePickerDialog;
    ImageButton male, female;
    FirebaseAuth auth;
    public static String Gender="",DOB="",UserName="",channel="";
    public static Integer fday,fmonth,fyear;
    static String user="", pass="", email="",cpass="",mobilenum="";
    String[] number = new String[]{
            "Male",
            "Female",
    };
    public static final String RegisterPref = "MyPrefs";

    SharedPreferences sharedpreferences;

    static Calendar myCalendar = Calendar.getInstance();
    public static int age;
    public static int loginvalue=0;
    Calendar c;
    public int mYear, mMonth,mDay;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    EditText profileselect;
    RelativeLayout pselect;
    private FirebaseAuth mAuth;
    TextView termspage, conditionlink;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedpreferences = getSharedPreferences(RegisterPref, Context.MODE_PRIVATE);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        username = (EditText) findViewById(R.id.user);
        emailreg = (EditText) findViewById(R.id.email);
        passwordreg = (EditText) findViewById(R.id.password);
        cpasswordreg = (EditText) findViewById(R.id.cpass);
        mobilereg = (EditText) findViewById(R.id.mobile);
        namef = (EditText) findViewById(R.id.namefield);

        male = (ImageButton) findViewById(R.id.male);
        female = (ImageButton) findViewById(R.id.female);


        btnSignUp = (Button) findViewById(R.id.register);
        btnLinkLogin = (Button) findViewById(R.id.login);

        genderRadioGroup = (RadioGroup) findViewById(R.id.gender);
         pselect = (RelativeLayout)findViewById(R.id.pselect);
        profileselect = (EditText)findViewById(R.id.profileselect);

        datee = (EditText) findViewById(R.id.date);
        termspage = (TextView)findViewById(R.id.terms);
        conditionlink = (TextView)findViewById(R.id.conditionpage);

        checkBox = (CheckBox)findViewById(R.id.check);
        Firebase.setAndroidContext(this);

        auth= FirebaseAuth.getInstance();



        Firebase.setAndroidContext(this);

        profileselect.setInputType(InputType.TYPE_NULL);
        profileselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(Register.this, pselect);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.profile_created_for, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
//                        Toast.makeText(Register.this, "You Clicked : ", Toast.LENGTH_SHORT).show();
                        profileselect.setText(item.getTitle());

                        return true;
                    }
                });

                popup.show(); //showing popup menu

            }
        }); //closing



        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.ic_search_male);
                female.setBackgroundResource(R.drawable.ic_search_female);
                Gender="M";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.ic_search_male_unselected);
                female.setBackgroundResource(R.drawable.female_gender_selected);
                Gender="F";

            }
        });


//        spiner.setPrompt("select profile for");

// Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(R.layout.custom_textview_for_profile);
// Apply the adapter to the spinner
//        //on spinner item selected
//        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//            @Override
//            public void onItemSelected(AdapterView<?> parent, ViewSome view,
//                                       int position, long id) {
//                // TODO Auto-generated method stub
//
//                Toast.makeText(Register.this, spiner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                // TODO Auto-generated method stub
//
//            }
//        });



        //date picker
// perform click event on edit text



//        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                // TODO Auto-generated method stub
//
//                myCalendar.set(Calendar.YEAR, year);
//                myCalendar.set(Calendar.MONTH, monthOfYear);
//                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//
//                fday=dayOfMonth;
//                fmonth=monthOfYear;
//                fyear=year;
//                updateLabel();
//                updateDisplay();
//            }
//
//        };

        datee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });



//        datee.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//
////
////                new DatePickerDialog(Register.this, date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//
//
//            }
//        });

        btnLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login2.class);
                        startActivity(intent);
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitForm();

                          }
        });



        termspage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://nayarishta.com/terms-condition.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        conditionlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://nayarishta.com/privacy-policy.php";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });



        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(checkBox.isChecked()){
                    System.out.println("Checked");
                }else{
                    System.out.println("Un-Checked");
                }
            }
        });
    }
    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = myCalendar.get(Calendar.YEAR);
            int month = myCalendar.get(Calendar.MONTH);
            int day = myCalendar.get(Calendar.DAY_OF_MONTH);
            myCalendar.add(Calendar.YEAR,-20);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {

            fday = day;
            fmonth = month+1;
            fyear = year;
            myCalendar.set(Calendar.YEAR, 2000);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);

            datee.setText(day + "/" + (month +1)  + "/" + year);
        }
    }
    private void updateLabel() {

        datee.setText(fday + "/"
                + (fmonth +1)  + "/" + fyear);
    }

    public void updateDisplay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - fyear;
//        datee.setText( new StringBuilder().append("The user is ")
//                .append(age).append(" years old"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_profile, menu);//Menu Resource, Menu
        return true;

    }


    public void submitForm() {


            String gender = Gender;


            String uname = username.getText().toString();
            String em = emailreg.getText().toString();
            String pas = passwordreg.getText().toString();
            String cp = cpasswordreg.getText().toString();
            String pro = profileselect.getText().toString();
            String mob = mobilereg.getText().toString();
            String usname = namef.getText().toString();



            System.out.print(mob);

            DOB= fday+"-"+fmonth+"-"+fyear;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        age = year - fyear;
        if (age<18){
            Toast.makeText(getApplicationContext(),"You are under 18years",Toast.LENGTH_SHORT).show();
        }else {
            registerUser(uname,em,pas,usname,cp,pro,gender,fday,fmonth,fyear,DOB,mob);

        }
    }

//        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
//
    private void registerUser(final String uname,  final String email, final String password,final String Name,final String cpassword, final String profile,
                              final String gender,final Integer day,final Integer month,final Integer year,final String DOB, final String mobile) {
        // Tag used to cancel the request
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (username.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill username!", Toast.LENGTH_LONG).show();
        } else if (namef.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill Name!", Toast.LENGTH_LONG).show();
        } else if (emailreg.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill Email!", Toast.LENGTH_LONG).show();
        } else if (!emailreg.getText().toString().matches(emailPattern)) {
            Toast.makeText(Register.this, "Please fill valid Email !", Toast.LENGTH_LONG).show();
        } else if (passwordreg.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill Password !", Toast.LENGTH_LONG).show();
        } else if (passwordreg.getText().toString().length() < 6) {
            Toast.makeText(Register.this, "Please fill minimum 6 digits Password !", Toast.LENGTH_LONG).show();
        } else if (passwordreg.getText().toString().length() > 15) {
            Toast.makeText(Register.this, "Please fill maximum 15 digits Password !", Toast.LENGTH_LONG).show();
        } else if (cpasswordreg.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill confirm Password !", Toast.LENGTH_LONG).show();
        } else if (!cpasswordreg.getText().toString().equals(passwordreg.getText().toString())) {
            Toast.makeText(Register.this, "confirm Password dosen't match !", Toast.LENGTH_LONG).show();
        } else if (profileselect.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please select Profile Created For !", Toast.LENGTH_LONG).show();
        } else if (datee.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill DOB !", Toast.LENGTH_LONG).show();
        } else if (mobilereg.getText().toString().equals("")) {
            Toast.makeText(Register.this, "Please fill your Mobile Number !", Toast.LENGTH_LONG).show();
        } else if (mobilereg.getText().toString().length() < 10) {
            Toast.makeText(Register.this, "Please fill correct Mobile Number !", Toast.LENGTH_LONG).show();
        } else if (mobilereg.getText().toString().length() > 10) {
            Toast.makeText(Register.this, "Please fill correct Mobile Number !", Toast.LENGTH_LONG).show();
        }
        else if (checkBox.isChecked()==false){
            Toast.makeText(Register.this, "Please read and confirm acceptance of our terms and conditions", Toast.LENGTH_LONG).show();
        }
        else {

            String cancel_req_tag = "register";
            progressDialog.setMessage("Please wait a moment");
            showDialog();
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_FOR_REGISTRATION, new Response.Listener<String>() {





                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "Register Response: " + response.toString());
                    hideDialog();



                    try {
                        JSONObject jObj = new JSONObject(response);

                        boolean error = jObj.getBoolean("error");

                      if (!error) {

                            String userid = jObj.getString("userid");


                            String em = emailreg.getText().toString();
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            user = username.getText().toString();
                            pass = passwordreg.getText().toString();
                            final String agevalue = String.valueOf(age);

                            editor.putString("user_id", userid);
                            editor.putString("username", uname);
                            editor.putString("email", em);
                            editor.putString("password",pass);
                            editor.putString("agevalue", agevalue);
                            editor.putInt("loginvalue", globallogincheck);
                            editor.commit();

//                            Intent intent = new Intent(getApplicationContext(), SplashPersonalDetails.class);
//                            startActivity(intent);
//                            finish();

                            chat(user,pass);


                        } else {

                          JSONObject jObdj = new JSONObject(response);
                          String msg = jObdj.getString("message");

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                    params.put("username", uname);
                    params.put("email", email);
                    params.put("password", password);
                    params.put("firstname", Name);
                    params.put("cpassword", cpassword);
                    params.put("profilecreatedfor", profile);

                    params.put("gender", gender);
                    params.put("day", String.valueOf(day));
                    params.put("month", String.valueOf(month));
                    params.put("year", String.valueOf(year));
                    params.put("DOB", DOB);

                    params.put("mobile", mobile);
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
//    }


    private void chat(final String usernamereg,final String passwordreg){

//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//        editor.putString("age", String.valueOf(age));
//        editor.commit();
        final ProgressDialog pd = new ProgressDialog(Register.this);
            pd.setMessage("Please wait a moment");
            pd.show();


            String url = "https://nayarishta-app-8d5f4.firebaseio.com/users.json";


            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                @Override
                public void onResponse(String s) {
                    Firebase reference = new Firebase("https://nayarishta-app-8d5f4.firebaseio.com/users");

                    if(s.equals("null")) {
                        reference.child(usernamereg).child("password").setValue(passwordreg);
                        Intent intent = new Intent(getApplicationContext(),SplashPersonalDetails.class);
                        startActivity(intent);

                        Toast.makeText(Register.this, "registration success", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject obj = new JSONObject(s);

                            if (!obj.has(usernamereg)) {

                                reference.child(usernamereg).child("password").setValue(passwordreg);

                                Intent intent = new Intent(getApplicationContext(),SplashPersonalDetails.class);
                                intent.putExtra("logintype","register");
                                startActivity(intent);


                            } else {
                                Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    pd.dismiss();
                }

            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError );
                    pd.dismiss();
                }
            });

            request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue rQueue = Volley.newRequestQueue(Register.this);
            rQueue.add(request);
        }



//    private void loginUser(final String email, final String password) {
//
//        // Tag used to cancel the request
//        String cancel_req_tag = "login";
//        progressDialog.setMessage("Logging you in...");
//
//
//            showDialog();
//            StringRequest strReq = new StringRequest(Request.Method.POST,
//                    URL_FOR_LOGIN, new Response.Listener<String>() {
//
//                @Override
//                public void onResponse(String response) {
//                    Log.d(TAG, "Register Response: " + response.toString());
//
//                    try {
//                        JSONObject jarray = new JSONObject(response);
//
//                        JSONArray jUser = jarray.getJSONArray("user");
//                        System.out.print(jUser);
//                        JSONObject c = jUser.getJSONObject(0);
//                        String userid = c.getString("UserProfileId");
//
//                        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//                        editor.putString("user_id", userid);
//                        editor.commit();
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    hideDialog();
//                    try {
//                        JSONObject jObj = new JSONObject(response);
//                        boolean error = jObj.getBoolean("error");
//
//                        if (!error) {
//
//
//                            SharedPreferences shared = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
//                            channel = (shared.getString("user_id", ""));
//
//                            chat(user,pass);
////                        else{
////                            Intent intent = new Intent(Login2.this, MyProfile.class);
////                            startActivity(intent);
////                            finish();
////
////                        }
//
//
////                        String user = jObj.getJSONObject("user").getString("name");
//                            // Launch User activity
//
//                        } else {
//
//                            String errorMsg = jObj.getString("error_msg");
//                            Toast.makeText(getApplicationContext(),
//                                    errorMsg, Toast.LENGTH_LONG).show();
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        System.out.print(e);
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(TAG, "Login Error: " + error.getMessage());
//                    Toast.makeText(getApplicationContext(),
//                            error.getMessage(), Toast.LENGTH_LONG).show();
//                    hideDialog();
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    // Posting params to login url
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("email", email);
//                    params.put("password", password);
//                    return params;
//                }
//            };
//            // Adding request to request queue
//            AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
//
//    }




    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void back(View v){
        finish();
    }
    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}