package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appyvet.rangebar.RangeBar;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.villupuram.nayarishta.Login2.MyPREFERENCES;

public class try_three extends Fragment {

    RangeBar rangeBar;
    TextView seekbartext,seekbar_right;
    private RadioGroup radioGroup1;
    public static String countryList="";
    public static String country_id="";
    public static String realCountrilist="";
    public static String realCountriId="";
    private final String TAG = "try_three";
    public static String channel="";

    public static String statuslist="";
    public static String state_id="";
    public static String realstatelist="";
    public static String realstateid="";
   static String currentItem;
    Button save;
    public static String heightList="",heightid_from="",heightid_to="";
    public static String heightID="";
    private ArrayList<String> heightarray;
    private ArrayList<String> heitoarray;


    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/partnerPreferencesUpdate.php";


    private JSONArray result;
    private ArrayList<String> contries;
    private ArrayList<String> caste_array;
    private ArrayList<String> realcountries_array;
    private ArrayList<String> realcountrieslist;
    ProgressDialog progressDialog;
    private ArrayList<String> citiesstate;



    Spinner getreligion,caste,countries,state,education,heightfrom,heightto,marital,drinking,smoking;
    static String religion_id="",caste_id="";
    TextView mothertext,profilefield,bodytypetext,complexiontext,educationtext,occupationtext,incometext,eatingtsxt;


    ArrayList<ColorVO> colorList = new ArrayList<ColorVO>();
    final ArrayList<ProfileClass> profileList = new ArrayList<ProfileClass>();
    final ArrayList<bodyTypeList> bodytypeList = new ArrayList<bodyTypeList>();
    final ArrayList<ComplexionClass> complexionList = new ArrayList<ComplexionClass>();
    final ArrayList<EducationClass> educationList = new ArrayList<EducationClass>();
    final ArrayList<OccupationClass> occupationlist = new ArrayList<OccupationClass>();
    final ArrayList<IncomeClass> incomelist = new ArrayList<IncomeClass>();
    final ArrayList<EatingClass> eatinglist = new ArrayList<EatingClass>();


    ArrayList<ColorVO> testlist = new ArrayList<ColorVO>();

    CrystalRangeSeekbar rangeSeekbar;
    String age_from="18",age_to="70";
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_try_three,
                container, false);

        heightarray = new ArrayList<String>();
        heitoarray = new ArrayList<String>();

        contries = new ArrayList<String>();
        caste_array = new ArrayList<String>();
        realcountries_array = new ArrayList<String>();
        realcountrieslist = new ArrayList<String>();
        citiesstate = new ArrayList<String>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
         save = (Button)view.findViewById(R.id.savepp);


        final String[] eatings = new String[]{
                "Doesn't Matter",
                "Veg",
                "Non-Veg",
                "Jain",
        };

        // Boolean array for initial selected items
        final boolean[] checkedeating = new boolean[]{
                false,false, // Green
                false, // Blue
                false, // Purple

        };


        final List<String> ListEatings = Arrays.asList(eatings);


        final String[] incomes = new String[]{
                "Doesn't Matter",
                "0 GPB to 5 GPB",
                "5 GPB to 15 GPB",
                "15 GPB to 25 GPB",
                "25 GPB to 40 GPB","40+ GPB"
        };
        // Boolean array for initial selected items
        final boolean[] checkedincome = new boolean[]{
                false,false, // Green
                false, // Blue
                false, // Purple
                false,false

        };


        // String array for alert dialog multi choice items
        final String[] occupations = new String[]{
                "Dosen't Matter",
                "Unemployed",
                "IT / Telecom Professional",
                "Air Hostess",
                "Architect",
                "Non-mainstream professional","Accountant","Acting Professional","Actor","Administration Professional","Advertising Professional","Artisan",
                "Audiologist","Banker","Beautician","Biologist / Botanist","Business Person","Chartered Accountant","Civil Engineer","Clerical Official",
                "Commercial Pilot","Company Secretary","Computer Professional","Consultant","Contractor","Cost Accountant","Creative Person","Customer Support Professional",
                "Defense Employee","Dentist","Designer","Doctor","Economist","Engineer","catering"

        };
        // Boolean array for initial selected items
        final boolean[] checkedoccupation = new boolean[]{
                false,false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false,false,false,false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false,false,false,false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false,false,false,false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false// Olive// Olive// Olive// Olive

        };

        // String array for alert dialog multi choice items
        final String[] colors = new String[]{
                "Dosen't Matter",
                "English",
                "Hindi",
                "Tamil",
                "Bengali",
                "Gujrati","Malayalam","Telugu","Urdu","kanada"
        };
        // Boolean array for initial selected items
        final boolean[] checkedColors = new boolean[]{
                false, // Red
                false,false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false,false// Olive

        };
        // String array for alert dialog multi choice items
        final String[] profiles = new String[]{
                "Dosen't Matter",
                "Self",
                "Son",
                "Daughter",
                "Brother","Sister"
        };
        // Boolean array for initial selected items
        final boolean[] checkedProfiles = new boolean[]{
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false,false,

        };

        final String[] bodylist = new String[]{
                "Dosen't Matter",
                "Slim",
                "Average/Athletic",
                "Heavy",
        };
        // Boolean array for initial selected items
        final boolean[] checkbodylist = new boolean[]{
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple

        };

        final String[] complexions = new String[]{
                "Doesn't Matter",
                "Fair",
                "Wheatish",
                "Dark",
        };
        // Boolean array for initial selected items
        final boolean[] checkedcomplexion = new boolean[]{
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple

        };


        final String[] educations = new String[]{
                "Doesn't Matter",
                "Bachelores",
                "Masters",
                "Doctorate",
                "Diploma","Undergraduate","Associate degree","Honours degree","Trade School","High School","Less than igh school","master of philosophy"
        };
        // Boolean array for initial selected items
        final boolean[] checkededucation = new boolean[]{
                false, // Red
                false, // Green
                false, // Blue
                false, // Purple
                false,false,false,false,false, false, // Red
                false, // Green
                false, // Blue false, // Red
        };



        seekbartext = (TextView)view.findViewById(R.id.seekbartext);
        seekbar_right = (TextView)view.findViewById(R.id.seekbar_right);
        mothertext = (TextView)view.findViewById(R.id.mothertangue);
        profilefield = (TextView)view.findViewById(R.id.profilefield);
        bodytypetext = (TextView)view.findViewById(R.id.bodyfield);
        complexiontext = (TextView)view.findViewById(R.id.complexiontext);
        educationtext = (TextView)view.findViewById(R.id.sducationfield);
        occupationtext = (TextView)view.findViewById(R.id.occupationfield);
        incometext = (TextView)view.findViewById(R.id.incomefield);
        eatingtsxt = (TextView)view.findViewById(R.id.eatingfield);
        heightfrom = (Spinner)view.findViewById(R.id.heightfrom);
        heightto = (Spinner)view.findViewById(R.id.heightto);
        marital = (Spinner)view.findViewById(R.id.maritalstatus);
        drinking = (Spinner)view.findViewById(R.id.drinkingspinner);
        smoking = (Spinner)view.findViewById(R.id.smokingspinner);
        state = (Spinner)view.findViewById(R.id.state);


        rangeSeekbar = (CrystalRangeSeekbar)view.findViewById(R.id.seekbar2);
        RelativeLayout mspinner = (RelativeLayout)view.findViewById(R.id.addbutton);
        final RelativeLayout addprofile = (RelativeLayout)view.findViewById(R.id.profileadd);
        RelativeLayout bodytype = (RelativeLayout)view.findViewById(R.id.addbodytype);
        final RelativeLayout complexion = (RelativeLayout)view.findViewById(R.id.addcomplexion);
        RelativeLayout education = (RelativeLayout)view.findViewById(R.id.addeducation);
        RelativeLayout occupation = (RelativeLayout)view.findViewById(R.id.addoccupation);
        RelativeLayout income = (RelativeLayout)view.findViewById(R.id.addincome);
        RelativeLayout eating = (RelativeLayout)view.findViewById(R.id.addhabit);




        getreligion = (Spinner)view.findViewById(R.id.getreligion);
        caste = (Spinner)view.findViewById(R.id.caste);
        countries = (Spinner)view.findViewById(R.id.countriesspinner);





        // Checked change Listener for RadioGroup 1
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
//                tvMin.setText(String.valueOf(minValue));
//                tvMax.setText(String.valueOf(maxValue));
                seekbartext.setText(String.valueOf(minValue));
                seekbar_right.setText(String.valueOf(maxValue));

            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });


        getData();
        getCountries();
        getheights();
        getfromgeight();

        getreligion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = getreligion.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(countryList);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusdd = jsonn.getString("religionid");
                    religion_id = valusdd;
                    getstates();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                Integer value = caste.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(statuslist);
                    result = jsonResponse.getJSONArray("religion");
                    JSONObject jsonn = result.getJSONObject(value);

                    String valusdd = jsonn.getString("religionid");
                    caste_id = valusdd;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        countries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                final Integer value = countries.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(realCountrilist);
                    result = jsonResponse.getJSONArray("country");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusdd = jsonn.getString("id");
                    realCountriId = valusdd;
                    getcountryStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        eating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                // make a list to hold state of every color
                for (int i = 0; i < eatings.length; i++) {
                    EatingClass EatingClass = new EatingClass();
                    EatingClass.setName(eatings[i]);
                    EatingClass.setSelected(checkedeating[i]);
                    eatinglist.add(EatingClass);
                }


                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(eatings, checkedeating, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list

                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedeating.length; i++) {
                                checkedeating[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                eatinglist.get(which).setSelected(isChecked);
                                eatinglist.get(i).setSelected(false);


                            }


                        } else {
                            checkedeating[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            eatinglist.get(which).setSelected(isChecked);
                            eatinglist.get(0).setSelected(false);
                        }
                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Eating Habits");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        eatingtsxt.setText("");

                        // save state of selected vos
                        ArrayList<EatingClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < eatinglist.size(); i++) {
                            EatingClass EatingClass = eatinglist.get(i);
                            eatings[i] = EatingClass.getName();
                            checkedeating[i] = EatingClass.isSelected();


                            if (EatingClass.isSelected()) {
                                selectedList.add(EatingClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it



                            if (i != selectedList.size() - 1)
                                eatingtsxt.setText(eatingtsxt.getText() + selectedList.get(i).getName() + " ,");
                            else
                                eatingtsxt.setText(eatingtsxt.getText() + selectedList.get(i).getName());
                        }
                        eatinglist.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        eatinglist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // make a list to hold state of every color
                for (int i = 0; i < incomes.length; i++) {
                    IncomeClass IncomeClass = new IncomeClass();
                    IncomeClass.setName(incomes[i]);
                    IncomeClass.setSelected(checkedincome[i]);
                    incomelist.add(IncomeClass);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(incomes, checkedincome, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedincome.length; i++) {
                                checkedincome[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                incomelist.get(which).setSelected(isChecked);
                                incomelist.get(i).setSelected(false);


                            }



                        } else {
                            checkedincome[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            incomelist.get(which).setSelected(isChecked);
                            incomelist.get(0).setSelected(false);
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Anual Income");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        incometext.setText("");

                        // save state of selected vos
                        ArrayList<IncomeClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < incomelist.size(); i++) {
                            IncomeClass IncomeClass = incomelist.get(i);
                            incomes[i] = IncomeClass.getName();
                            checkedincome[i] = IncomeClass.isSelected();
                            if (IncomeClass.isSelected()) {
                                selectedList.add(IncomeClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                incometext.setText(incometext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                incometext.setText(incometext.getText() + selectedList.get(i).getName());
                        }
                        incomelist.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        incomelist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // make a list to hold state of every color
                for (int i = 0; i < occupations.length; i++) {
                    OccupationClass OccupationClass = new OccupationClass();
                    OccupationClass.setName(occupations[i]);
                    OccupationClass.setSelected(checkedoccupation[i]);
                    occupationlist.add(OccupationClass);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(occupations, checkedoccupation, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedoccupation.length; i++) {
                                checkedoccupation[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                occupationlist.get(which).setSelected(isChecked);
                                occupationlist.get(i).setSelected(false);


                            }



                        } else {
                            checkedoccupation[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            occupationlist.get(which).setSelected(isChecked);
                            occupationlist.get(0).setSelected(false);
                        }

                    }
                });


                builder.setCancelable(false);

                builder.setTitle("Select Occupation");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        occupationtext.setText("");

                        // save state of selected vos
                        ArrayList<OccupationClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < occupationlist.size(); i++) {
                            OccupationClass OccupationClass = occupationlist.get(i);
                            occupations[i] = OccupationClass.getName();
                            checkedoccupation[i] = OccupationClass.isSelected();
                            if (OccupationClass.isSelected()) {
                                selectedList.add(OccupationClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                occupationtext.setText(occupationtext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                occupationtext.setText(occupationtext.getText() + selectedList.get(i).getName());
                        }
                        occupationlist.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        occupationlist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // make a list to hold state of every color
                for (int i = 0; i < educations.length; i++) {
                    EducationClass EducationClass = new EducationClass();
                    EducationClass.setName(educations[i]);
                    EducationClass.setSelected(checkededucation[i]);
                    educationList.add(EducationClass);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(educations, checkededucation, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkededucation.length; i++) {
                                checkededucation[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                educationList.get(which).setSelected(isChecked);
                                educationList.get(i).setSelected(false);


                            }


                        } else {
                            checkededucation[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            educationList.get(which).setSelected(isChecked);
                            educationList.get(0).setSelected(false);
                        }

                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Education");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        educationtext.setText("");

                        // save state of selected vos
                        ArrayList<EducationClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < educationList.size(); i++) {
                            EducationClass EducationClass = educationList.get(i);
                            educations[i] = EducationClass.getName();
                            checkededucation[i] = EducationClass.isSelected();
                            if (EducationClass.isSelected()) {
                                selectedList.add(EducationClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                educationtext.setText(educationtext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                educationtext.setText(educationtext.getText() + selectedList.get(i).getName());
                        }
                        educationList.clear();
                    }
                });


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        educationList.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });




            mspinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // make a list to hold state of every color
                    for (int i = 0; i < colors.length; i++) {
                        ColorVO colorVO = new ColorVO();
                        colorVO.setName(colors[i]);
                        colorVO.setSelected(checkedColors[i]);
                        colorList.add(colorVO);
                    }

                    // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                    builder.setMultiChoiceItems(colors, checkedColors, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // set state to vo in list

//                            int i = 1;
                            if (which == 0 && isChecked == true) {
                                for (int i = 1; i < checkedColors.length; i++) {
                                    checkedColors[i] = false;
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    colorList.get(which).setSelected(isChecked);
                                    colorList.get(i).setSelected(false);
                                }

                            } else {
                                checkedColors[0] = false;
                               ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                                // make a list to hold state of every color

                                colorList.get(which).setSelected(isChecked);
                                colorList.get(0).setSelected(false);
                            }


                        }


                    });
                    builder.setCancelable(false);

                    builder.setTitle("Select Mother Tongue");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mothertext.setText("");
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
                                    mothertext.setText(mothertext.getText() + selectedList.get(i).getName() + " ,");
                                else
                                    mothertext.setText(mothertext.getText() + selectedList.get(i).getName());
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

        complexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // make a list to hold state of every color
                for (int i = 0; i < complexions.length; i++) {
                    ComplexionClass ComplexionClass = new ComplexionClass();
                    ComplexionClass.setName(complexions[i]);
                    ComplexionClass.setSelected(checkedcomplexion[i]);
                    complexionList.add(ComplexionClass);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(complexions, checkedcomplexion, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list


                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedcomplexion.length; i++) {
                                checkedcomplexion[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                complexionList.get(which).setSelected(isChecked);
                                complexionList.get(i).setSelected(false);


                            }


                        } else {
                            checkedcomplexion[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            // make a list to hold state of every color
                            complexionList.get(which).setSelected(isChecked);
                            complexionList.get(0).setSelected(false);
                        }
                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Complexion");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        complexiontext.setText("");

                        // save state of selected vos
                        ArrayList<ComplexionClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < complexionList.size(); i++) {
                            ComplexionClass ComplexionClass = complexionList.get(i);
                            complexions[i] = ComplexionClass.getName();
                            checkedcomplexion[i] = ComplexionClass.isSelected();
                            if (ComplexionClass.isSelected()) {
                                selectedList.add(ComplexionClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                complexiontext.setText(complexiontext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                complexiontext.setText(complexiontext.getText() + selectedList.get(i).getName());
                        }
                        complexionList.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        complexionList.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
            addprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // make a list to hold state of every color
                    for (int i = 0; i < profiles.length; i++) {
                        ProfileClass ProfileClass = new ProfileClass();
                        ProfileClass.setName(profiles[i]);
                        ProfileClass.setSelected(checkedProfiles[i]);
                        profileList.add(ProfileClass);
                    }

                    // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                    builder.setMultiChoiceItems(profiles, checkedProfiles, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            // set state to vo in list
                            if (which == 0 && isChecked == true) {

                                for (int i = 1; i < checkedProfiles.length; i++) {
                                    checkedProfiles[i] = false;
                                    ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                    profileList.get(which).setSelected(isChecked);
                                    profileList.get(i).setSelected(false);



                                }


                            } else {
                                checkedProfiles[0] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                                profileList.get(which).setSelected(isChecked);
                                profileList.get(0).setSelected(false);
                            }
                        }
                    });

                    builder.setCancelable(false);

                    builder.setTitle("Profile Created For");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            profilefield.setText("");

                            // save state of selected vos
                            ArrayList<ProfileClass> selectedList = new ArrayList<>();
                            for (int i = 0; i < profileList.size(); i++) {
                                ProfileClass ProfileClass = profileList.get(i);
                                profiles[i] = ProfileClass.getName();
                                checkedProfiles[i] = ProfileClass.isSelected();
                                if (ProfileClass.isSelected()) {
                                    selectedList.add(ProfileClass);
                                }
                            }

                            for (int i = 0; i < selectedList.size(); i++) {
                                // if element is last then not attach comma or attach it
                                if (i != selectedList.size() - 1)
                                    profilefield.setText(profilefield.getText() + selectedList.get(i).getName() + " ,");
                                else
                                    profilefield.setText(profilefield.getText() + selectedList.get(i).getName());
                            }
                            profileList.clear();
                        }
                    });


                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // make sure to clear list that duplication dont formed here
                            profileList.clear();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        bodytype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // make a list to hold state of every color
                for (int i = 0; i < bodylist.length; i++) {
                    bodyTypeList bodyTypeList = new bodyTypeList();
                    bodyTypeList.setName(bodylist[i]);
                    bodyTypeList.setSelected(checkbodylist[i]);
                    bodytypeList.add(bodyTypeList);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(bodylist, checkbodylist, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkbodylist.length; i++) {
                                checkbodylist[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                bodytypeList.get(which).setSelected(isChecked);
                                bodytypeList.get(i).setSelected(false);


                            }

                        } else {
                            checkbodylist[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            bodytypeList.get(which).setSelected(isChecked);
                            bodytypeList.get(0).setSelected(false);
                        }


                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Body Type");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bodytypetext.setText("");

                        // save state of selected vos
                        ArrayList<bodyTypeList> selectedList = new ArrayList<>();
                        for (int i = 0; i < bodytypeList.size(); i++) {
                            bodyTypeList bodyTypeList = bodytypeList.get(i);
                            bodylist[i] = bodyTypeList.getName();
                            checkbodylist[i] = bodyTypeList.isSelected();
                            if (bodyTypeList.isSelected()) {
                                selectedList.add(bodyTypeList);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                bodytypetext.setText(bodytypetext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                bodytypetext.setText(bodytypetext.getText() + selectedList.get(i).getName());
                        }
                        bodytypeList.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        bodytypeList.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        heightfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightList);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("heightid");
                    heightid_from = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        heightto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightList);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("heightid");
                    heightid_to = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });





        return view;

    }

    private void getheights(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            heightList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getallheights(result);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getallheights(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("heightid");

                System.out.print(reli_ID);

                String feet= json.getString("feet");
                String inch= json.getString("inch");
                String cm= json.getString("cm");


                heightarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        heightarray.add(0, "Select");
        //Setting adapter to show the items in the spinner
        heightfrom.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, heightarray));
    }

    private void getfromgeight(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            heightList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


                            getfromtoheight(result);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getfromtoheight(JSONArray j)   {
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID =  json.getString("heightid");

                System.out.print(reli_ID);

                String feet= json.getString("feet");
                String inch= json.getString("inch");
                String cm= json.getString("cm");


                heitoarray.add(feet+"'"+""+inch+"'"+"-"+cm+"cm");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        heitoarray.add(0, "Select");
        //Setting adapter to show the items in the spinner
        heightto.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, heitoarray));
    }


    private void savedata(){
        age_from = seekbartext.getText().toString();
        age_to = seekbar_right.getText().toString();
        String hfrom = heightfrom.getSelectedItem().toString();
        String hto = heightto.getSelectedItem().toString();
        String ms = marital.getSelectedItem().toString();
        String religion = getreligion.getSelectedItem().toString();
        String getcaste = caste.getSelectedItem().toString();
        String mothertangue = mothertext.getText().toString();
        String profile = profilefield.getText().toString();
        String body = bodytypetext.getText().toString();
        String complexiontedxt = complexiontext.getText().toString();
        String country = countries.getSelectedItem().toString();
        String education = educationtext.getText().toString();
        String statee = state.getSelectedItem().toString();
        String occupation = occupationtext.getText().toString();
        String income = incometext.getText().toString();
        String eating = eatingtsxt.getText().toString();
        String drinkidng = drinking.getSelectedItem().toString();
        String smokidng = smoking.getSelectedItem().toString();




        SharedPreferences preferences = getActivity().getSharedPreferences(MyPREFERENCES, getContext().MODE_PRIVATE);
        channel = (preferences.getString("user_id", ""));

        String value = channel ;

        if (heightid_from.equals("")||heightid_to.equals(""))
        {
            Toast.makeText(getActivity(), "Please select Height!", Toast.LENGTH_SHORT).show();
        }
        else {
            registerUser(value, age_from,age_to,heightid_from,heightid_to,ms,religion,getcaste,mothertangue,profile,body,complexiontedxt,
                    country,education,statee,occupation,income,eating,drinkidng,smokidng
            );
        }

    }





//    private void registerUser(final Integer userid , final String agefrom, final String ageto, final String heightfrom, final String heightto, final String maritalstatus, final String religion, final String mothertongue, final String profilecreatedfor, final String countryid, final String stateid, final String education, final String profession, final String annualincome, final String diet, final String complexion, final String bodytype, final String drink, final String smoke  ) {
//        // Tag used to cancel the request
//        String cancel_req_tag = "try_three";
//
//        progressDialog.setMessage("Saving...");
//        showDialog();
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                URL_FOR_LOGIN, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d(TAG, "Register Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//
//                    if (!error) {
////                        String user = jObj.getJSONObject("user").getString("name");
//                        Toast.makeText(getActivity(),"Saved!", Toast.LENGTH_SHORT).show();
//
//
//                    } else {
//
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getActivity(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to register url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("userid", String.valueOf(userid));
//                params.put("agefrom", agefrom);
//                params.put("ageto", ageto);
//                params.put("heightfrom", heightfrom);
//                params.put("heightto", heightto);
//                params.put("maritalstatus", maritalstatus);
//                params.put("religion", religion);
//                params.put("mothertongue", mothertongue);
//                params.put("profilecreatedfor", profilecreatedfor);
//                params.put("countryid", countryid);
//                params.put("stateid", stateid);
//                params.put("education", education);
//                params.put("profession", profession);
//                params.put("annualincome", annualincome);
//                params.put("diet", diet);
//                params.put("complexion", complexion);
//                params.put("bodytype", bodytype );
//                params.put("drink", drink);
//                params.put("smoke", smoke);
//
//                return params;
//            }
//        };
//        // Adding request to request queue
//        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
//    }

private void registerUser(final String userid, final String agefrom,final String ageto,final String hfrom,final String hto,final String ms,final String religion,final String getcaste,final String mothertangue,final String profile,final String body,final String complexiontedxt,
                          final String country,final String education,
                          final String statee,final String occupation,final String income,final String eating,final String drinkidng,final String smokidng) {
    // Tag used to cancel the request
    String cancel_req_tag = "register";

    if (Integer.parseInt(heightid_from) > Integer.parseInt(heightid_to)) {
        Toast.makeText(getActivity(), "invalid height range", Toast.LENGTH_LONG).show();
    } else {


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
//                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getActivity(), "Saved!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),Dashboard.class);
                        startActivity(intent);


                    } else {

                        String errorMsg = jObj.getString("message");
                        Toast.makeText(getActivity(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("agefrom", agefrom);
                params.put("ageto", ageto);
                params.put("hfrom", hfrom);
                params.put("hto", hto);
                params.put("ms", ms);
                params.put("religion", religion);
                params.put("getcaste", getcaste);
                params.put("mothertangue", mothertangue);
                params.put("profile", profile);
                params.put("body", body);
                params.put("complexiontedxt", complexiontedxt);
                params.put("country", country);
                params.put("education", education);
                params.put("statee", statee);
                params.put("occupation", occupation);
                params.put("income", income);
                params.put("eating", eating);
                params.put("drinkidng", drinkidng);
                params.put("smokidng", smokidng);


                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(strReq, cancel_req_tag);
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

    private class EatingClass {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public  void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }


    private class IncomeClass {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public  void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private class OccupationClass {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public  void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

    private class EducationClass {
        private String name;
        private boolean selected;

        public String getName() {
            return name;
        }

        public  void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
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



    private class ProfileClass {
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
    private class bodyTypeList {
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

    private class ComplexionClass {
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
        StringRequest stringRequest = new StringRequest(base_api_url.RELIGIOUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            countryList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("language");


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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
        contries.add(0, "Select religion");
        //Setting adapter to show the items in the spinner
        getreligion.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, contries));
    }

    //caste
    private void getstates(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.CASTE+"?religionid="+religion_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            statuslist = response;
//
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                                result = j.getJSONArray("religion");


                            //Storing the Array of JSON String to our JSON Array



                            //Calling method getStudents to get the students from the JSON Array
                            getallstates(result);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getallstates(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));



                caste_array.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        caste.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, caste_array));
    }


    //countries
    private void getCountries(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            realCountrilist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("country");


                            getAllCountries(result);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getAllCountries(JSONArray j){
        //Traversing through all the items in the json array

        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                realcountries_array.add(json.getString("name"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        realcountries_array.add(0, "-Select-");
        //Setting adapter to show the items in the spinner
        countries.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, realcountries_array));
    }

    private void getcountryStatus(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_STATE+"countryid="+realCountriId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            realstatelist = response;
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_STATES);

                            //Calling method getStudents to get the students from the JSON Array
                            getallCountryStatus(result);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getallCountryStatus(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);



                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID =  json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                citiesstate.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        citiesstate.add(0, "Select County");

        //Setting adapter to show the items in the spinner
        state.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citiesstate));
    }







}
