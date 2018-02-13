package com.villupuram.nayarishta;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.appyvet.rangebar.RangeBar;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.villupuram.nayarishta.ApiCall.AppSingleton;
import com.villupuram.nayarishta.constant.base_api_url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdvanceSearch extends AppCompatActivity {

    private static final String TAG = "AdvanceSearch";
    private static String URL_FOR_SEARCH = "";
    TextView seekbartext, seekbar_right;
    RangeBar rangeBar;
    Spinner maritalstatus, religious, caste, heighttoo, heightfromm, countryspinner, statespinner, physicalspinner;
    private JSONArray result;
    private ArrayList<String> heightfromarray;
    private ArrayList<String> heitoarray;
    public static String heightlist = "";
    private static final String URL_FOR_LOGIN = "http://nayarishta.com/nayarishta-app/api/full-search.php";

    public static String heightidfrom = "",heightidto="";
    Button send;
    ProgressDialog progressDialog;
    TextView maritalstatustext, addeducationtext,addprofessiontext,incometext,sstatustext,mothertanguetext;
    final ArrayList<ColorVO> colorList = new ArrayList<ColorVO>();
    final ArrayList<EducationClass> edulist = new ArrayList<EducationClass>();
    final ArrayList<ProfessionClass> professionlist = new ArrayList<ProfessionClass>();
    final ArrayList<IncomeClass> incomelist = new ArrayList<IncomeClass>();
    final ArrayList<MaritalClass> maritallist = new ArrayList<MaritalClass>();

    private ArrayList<String> religiousarray;
    public static String religiouslist = "";
    public static String religious_id = "";
    private ArrayList<String> castearray;
    public static String castelist = "";
    public static String caste_id = "";
    private ArrayList<String> contries;
    private ArrayList<String> state;

    public static String countryList = "";
    public static String statuslist = "";
    public static String citylist = "";
    public static String country_id = "";
    public static String state_id = "";
    public static String city_id = "";
    TextView mothertangue;
    CrystalRangeSeekbar rangeSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_search);

        religiousarray = new ArrayList<String>();
        castearray = new ArrayList<String>();
        heightfromarray = new ArrayList<String>();
        heitoarray = new ArrayList<String>();
        //Initializing the ArrayList
        contries = new ArrayList<String>();
        state = new ArrayList<String>();

        seekbartext = (TextView) findViewById(R.id.seekbartext);
        addeducationtext = (TextView) findViewById(R.id.educationtext);
        seekbar_right = (TextView) findViewById(R.id.seekbar_right);
        heighttoo = (Spinner) findViewById(R.id.heightto);
        heightfromm = (Spinner) findViewById(R.id.heightfrom);

        countryspinner = (Spinner) findViewById(R.id.countriesspinner);
        statespinner = (Spinner) findViewById(R.id.state);
        physicalspinner = (Spinner) findViewById(R.id.physicalspinner);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        religious = (Spinner) findViewById(R.id.religious);
        caste = (Spinner) findViewById(R.id.castespinner);

        rangeSeekbar = (CrystalRangeSeekbar)findViewById(R.id.seekbar2);

        send = (Button) findViewById(R.id.search);
        RelativeLayout addmothertaungue = (RelativeLayout) findViewById(R.id.addbutton);
        mothertanguetext = (TextView) findViewById(R.id.mothertangue);
        addprofessiontext = (TextView) findViewById(R.id.professiontext);
        incometext = (TextView) findViewById(R.id.incometext);
        maritalstatustext = (TextView) findViewById(R.id.maritalstatus);
         mothertangue = (TextView)findViewById(R.id.mothertangue);


//        RelativeLayout mspinner = (RelativeLayout) findViewById(R.id.addmstatus);
        RelativeLayout addeducation = (RelativeLayout) findViewById(R.id.educatrionlayout);
        RelativeLayout maritalstatuslayout = (RelativeLayout) findViewById(R.id.addmstatusss);
        RelativeLayout addprofession = (RelativeLayout) findViewById(R.id.addprofession);
        RelativeLayout addincome = (RelativeLayout) findViewById(R.id.addincome);


        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final String[] colors = new String[]{
                "Dosen't Matter",
                "English",
                "Hindi",
                "Tamil",
                "Bengali",
                "Gujrati", "Malayalam", "Telugu", "Urdu", "kanada"
        };


        final String[] mstatuss = new String[]{
                "Dosen't Matter",
                "Never Married",
                "Divorced",
                "Awaiting Divorced",

        };


        // Boolean array for initial selected items
        final boolean[] checkedsmstatus = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue

        };



        // Boolean array for initial selected items
        final boolean[] checkedColors = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue
                false, // Purple
                false, false, false, false, false// Olive

        };

        final String[] educaions = new String[]{
                "Dosen't Matter",
                "Masters",
                "Doctorate",
                "Polytechnic",
                "Undergraduate",
                "Unschooled", "High school", "ITI"
        };

        // Boolean array for initial selected items
        final boolean[] checkedEducaiton = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue
                false, // Purple
                false, false, false// Olive

        };

        final String[] professions = new String[]{
                "Dosen't Matter",
                "Unemployed",
                "IT/Telecom Professional",
                "Air Hostess",
                "Architect",
                "Non-mainstreem professional", "Accountant", "Acting Professional", "Actor", "Administression Professional",
                "Advertising Professional","Artisan","Audiologist","Banker","Beautician","Doctor"
        };
        final boolean[] checkedprofession = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue
                false, // Purple
                false, false, false,false, // Red
                false, false, // Green
                false, // Blue
                false, // Purple
                false, false, false// Olive

        };

        final String[] incomes = new String[]{
                "Dosen't Matter",
                "GBP 5K or less",
                "GBP 6K to 15K",
                "GBP 16K to 25K",
                "GBP 26K to 40K",
                "GBP 40K+"
        };

        final boolean[] checkedincome = new boolean[]{
                false, // Red
                false, false, // Green
                false, // Blue
                false, // Purple
                false

        };


        //date
//        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
//            @Override
//            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
//                                              int rightPinIndex,
//                                              String leftPinValue, String rightPinValue) {
//
//
//                seekbartext.setText(leftPinValue);
//                seekbar_right.setText(rightPinValue);
//
//            }
//        });
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

        final String[] MststusStrings = new String[]{
                "Doesn't Matter",
                "Never Married",
                "Divorced",
                "Awaiting Divorced",
        };
        final boolean[] checkedMststusStrings = new boolean[]{
                false, false, // Green
                false, // Blue
                false, // Purple


        };

        heightfrom();
        heightto();
        getData();
        getCountry();

        religious.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = religious.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(religiouslist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("religionid");
                    religious_id = valusd;

                    getstates();
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        caste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = caste.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(castelist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value-1);

                    String valusd = jsonn.getString("religionid");
                    caste_id = valusd;

                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        countryspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    Integer value = countryspinner.getSelectedItemPosition();

                    JSONObject jsonResponse = null;
                    try {
                        jsonResponse = new JSONObject(countryList);
                        result = jsonResponse.getJSONArray("country");
                        JSONObject jsonn = result.getJSONObject(value-1);

                        String valusd = jsonn.getString("id");
                        country_id = valusd;

                        getrealstate();
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        physicalspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String physical = physicalspinner.getSelectedItem().toString();


            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        heightfromm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusd = jsonn.getString("heightid");
                    heightidfrom = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        heighttoo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer value = parent.getSelectedItemPosition();

                JSONObject jsonResponse = null;
                try {
                    jsonResponse = new JSONObject(heightlist);
                    result = jsonResponse.getJSONArray("language");
                    JSONObject jsonn = result.getJSONObject(value - 1);

                    String valusd = jsonn.getString("heightid");
                    heightidto = valusd;


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addmothertaungue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceSearch.this);

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
                        mothertanguetext.setText("");
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
                                mothertanguetext.setText(mothertanguetext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                mothertanguetext.setText(mothertanguetext.getText() + selectedList.get(i).getName());
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

        addeducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceSearch.this);

                // make a list to hold state of every color
                for (int i = 0; i < educaions.length; i++) {
                    EducationClass colorVO = new EducationClass();
                    colorVO.setName(educaions[i]);
                    colorVO.setSelected(checkedEducaiton[i]);
                    edulist.add(colorVO);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(educaions, checkedEducaiton, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list

//                            int i = 1;
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedEducaiton.length; i++) {
                                checkedEducaiton[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                edulist.get(which).setSelected(isChecked);
                                edulist.get(i).setSelected(false);



                            }
                        } else {
                            checkedEducaiton[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            edulist.get(which).setSelected(isChecked);
                            edulist.get(0).setSelected(false);
                        }


                    }


                });
                builder.setCancelable(false);

                builder.setTitle("Select Education");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addeducationtext.setText("");
                        // save state of selected vos
                        ArrayList<EducationClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < edulist.size(); i++) {
                            EducationClass colorVO = edulist.get(i);
                            educaions[i] = colorVO.getName();
                            checkedEducaiton[i] = colorVO.isSelected();
                            if (colorVO.isSelected()) {
                                selectedList.add(colorVO);
                            }
                        }
                        edulist.clear();
                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                addeducationtext.setText(addeducationtext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                addeducationtext.setText(addeducationtext.getText() + selectedList.get(i).getName());
                        }
                        edulist.clear();

                    }
                });


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        edulist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        addprofession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceSearch.this);

                // make a list to hold state of every color
                for (int i = 0; i < professions.length; i++) {
                    ProfessionClass colorVO = new ProfessionClass();
                    colorVO.setName(professions[i]);
                    colorVO.setSelected(checkedprofession[i]);
                    professionlist.add(colorVO);
                }

                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(professions, checkedprofession, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list

//                            int i = 1;
                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedprofession.length; i++) {
                                checkedprofession[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                professionlist.get(which).setSelected(isChecked);
                                professionlist.get(i).setSelected(false);


                            }

                        } else {
                            checkedprofession[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            professionlist.get(which).setSelected(isChecked);
                            professionlist.get(0).setSelected(false);
                        }


                    }


                });
                builder.setCancelable(false);

                builder.setTitle("Select Profession");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addprofessiontext.setText("");
                        // save state of selected vos
                        ArrayList<ProfessionClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < professionlist.size(); i++) {
                            ProfessionClass colorVO = professionlist.get(i);
                            professions[i] = colorVO.getName();
                            checkedprofession[i] = colorVO.isSelected();
                            if (colorVO.isSelected()) {
                                selectedList.add(colorVO);
                            }
                        }
                        professionlist.clear();
                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it
                            if (i != selectedList.size() - 1)
                                addprofessiontext.setText(addprofessiontext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                addprofessiontext.setText(addprofessiontext.getText() + selectedList.get(i).getName());
                        }
                        professionlist.clear();

                    }
                });


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        professionlist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        addincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceSearch.this);
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


        maritalstatuslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdvanceSearch.this);


                // make a list to hold state of every color
                for (int i = 0; i < mstatuss.length; i++) {
                    MaritalClass EatingClass = new MaritalClass();
                    EatingClass.setName(mstatuss[i]);
                    EatingClass.setSelected(checkedsmstatus[i]);
                    maritallist.add(EatingClass);
                }


                // Do something here to pass only arraylist on this both arrays ('colors' & 'checkedColors')
                builder.setMultiChoiceItems(mstatuss, checkedsmstatus, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        // set state to vo in list

                        if (which == 0 && isChecked == true) {

                            for (int i = 1; i < checkedsmstatus.length; i++) {
                                checkedsmstatus[i] = false;
                                ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                                maritallist.get(which).setSelected(isChecked);
                                maritallist.get(i).setSelected(false);

                            }


                        } else {
                            checkedsmstatus[0] = false;
                            ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                            maritallist.get(which).setSelected(isChecked);
                            maritallist.get(0).setSelected(false);
                        }


                    }
                });

                builder.setCancelable(false);

                builder.setTitle("Select Marital Status");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        maritalstatustext.setText("");

                        // save state of selected vos
                        ArrayList<MaritalClass> selectedList = new ArrayList<>();
                        for (int i = 0; i < maritallist.size(); i++) {
                            MaritalClass EatingClass = maritallist.get(i);
                            mstatuss[i] = EatingClass.getName();
                            checkedsmstatus[i] = EatingClass.isSelected();


                            if (EatingClass.isSelected()) {
                                selectedList.add(EatingClass);
                            }
                        }

                        for (int i = 0; i < selectedList.size(); i++) {
                            // if element is last then not attach comma or attach it



                            if (i != selectedList.size() - 1)
                                maritalstatustext.setText(maritalstatustext.getText() + selectedList.get(i).getName() + " ,");
                            else
                                maritalstatustext.setText(maritalstatustext.getText() + selectedList.get(i).getName());
                        }
                        maritallist.clear();
                    }
                });



                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // make sure to clear list that duplication dont formed here
                        maritallist.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                advance();
            }
        });

    }


    public void advance(){

        String agrfrm = seekbartext.getText().toString();
        String agrto =seekbar_right.getText().toString();
        String hid = heightidfrom;
        String hto = heightidto;
        String mstatus="";
        String religionid = religious_id;
        String country = country_id;
        String languiage ="";
        String occu = "";
        String income = "";


        if (!heightidfrom.equals("")&& Integer.parseInt(heightidfrom) >Integer.parseInt(heightidto))
            {
                Toast.makeText(getApplicationContext(),"invalid height range",Toast.LENGTH_LONG).show();
            }
        else {
            if (maritalstatustext.getText().toString().equals("Dosen't Matter")) {
                mstatus = "";
            } else {
                mstatus = maritalstatustext.getText().toString();
            }

            if (mothertangue.getText().toString().equals("Dosen't Matter")) {
                languiage = "";
            } else {
                languiage = mothertangue.getText().toString();
            }
            if (addprofessiontext.getText().toString().equals("Dosen't Matter")) {
                occu = "";
            } else {
                occu = addprofessiontext.getText().toString();
            }
            if (incometext.getText().toString().equals("Dosen't Matter")) {
                income = "";
            } else {
                income = incometext.getText().toString();
            }

            loginUser(agrfrm, agrto, hid, hto, mstatus, religionid, languiage, country, occu, income);
//
        }
    }

    private void getData() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.RELIGIOUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            religiouslist = response;

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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        //Traversing through all the items in the json array

        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);


                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String reli_ID = json.getString("religionid");

                System.out.print(reli_ID);


                religiousarray.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        religiousarray.add(0, "Select Religion");
        //Setting adapter to show the items in the spinner
        religious.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, religiousarray));
    }


    private void getstates(){
        castearray.clear();
        progressDialog.setMessage("please wait a moment");
        showDialog();
        //Creating a string request
        StringRequest stringRequest = new StringRequest(base_api_url.CASTE+"?religionid="+religious_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        JSONObject j = null;
                        try {
                            castelist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("religion");


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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                String caste_id =  json.getString("religionid");

                System.out.print(caste_id);


                castearray.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        castearray.add(0, "Select");
        //Setting adapter to show the items in the spinner
        caste.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, castearray));
    }
    private void getCountry() {
        //Creating a string request
        state.clear();
        StringRequest stringRequest = new StringRequest(Config_location.DATA_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            countryList = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY);



                            getallcountry(result);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getallcountry(JSONArray j) {
        //Traversing through all the items in the json array

        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);


                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID = json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                contries.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


            contries.add(0, "Select Country");

        //Setting adapter to show the items in the spinner
        countryspinner.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, contries));

    }

    private void getrealstate() {
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Config_location.DATA_STATE + "countryid=" + country_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            statuslist = response;

                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(Config_location.JSON_ARRAY_STATES);


                            //Calling method getStudents to get the students from the JSON Array
                            getallrealstatw(result);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getallrealstatw(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);


                //Adding the name of the student to array list
//                cities.add(json.getString(Config_location.TAG_id));
                String CityID = json.getString(Config_location.TAG_id);

                System.out.print(CityID);


                state.add(json.getString(Config_location.TAG_NAME));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        statespinner.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, state));


    }

     private void loginUser(final String agefrom, final String ageto, final String heightfrom,final String heightto,final String maritalstatus,final String religionid,final String Language,final String countryid,final String occupation,final String income) {

        // Tag used to cancel the request

        String cancel_req_tag = "search";
        progressDialog.setMessage("Searching..");
         showDialog();
        URL_FOR_SEARCH = URL_FOR_LOGIN+"?agefrom="+agefrom+"&ageto="+ageto+"&heightfrom="+heightfrom+"&heightto="+heightto+"&maritalstatus="+maritalstatus+"&religionid="+religionid+"&languageid="+Language+"&countryid="+countryid+"&occupation="+occupation+"&income="+income;

         String ALLOWED_URI_CHARSss = "@#&=*+-_.,:!?()/~'%";
        String AdvancewSearch = Uri.encode(URL_FOR_SEARCH, ALLOWED_URI_CHARSss);


        StringRequest strReq = new StringRequest(Request.Method.GET,
                AdvancewSearch, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();
                try {



                    JSONObject jsonObj = new JSONObject(response);
//
//
                    String msg =  jsonObj.getString("message");

                    if (msg.equals("There is no profile matched for the selected criteria."))
                    {
                        Toast.makeText(AdvanceSearch.this,"No profile matched for the selected criteria.",Toast.LENGTH_LONG).show();
                    }

                    else {


                        JSONArray jObj = jsonObj.getJSONArray("users");
                        Intent intent=new Intent(getApplicationContext(),SearchResults.class);

                        intent.putExtra("json",jObj.toString());
                        intent.putExtra("identity","advance");
                        intent.putExtra("agefrom",agefrom);
                        intent.putExtra("ageto",ageto);
                        intent.putExtra("heightfrom",heightfrom);
                        intent.putExtra("heightto",heightto);
                        intent.putExtra("maritalstatus",maritalstatus);
                        intent.putExtra("religionid",religionid);
                        intent.putExtra("languageid",Language);

                        intent.putExtra("occupation",occupation);
                        intent.putExtra("countryid",countryid);
                        intent.putExtra("income",income);
                        startActivity(intent);
//                    }







                } }catch (JSONException e) {
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

        strReq.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
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


    private class EducationClass {
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


    private class ProfessionClass {
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

    private class IncomeClass {
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

    private class MaritalClass {
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

        private void heightfrom() {
            //Creating a string request
            StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject j = null;
                            try {

                                heightlist = response;
                                //Parsing the fetched Json String to JSON Object
                                j = new JSONObject(response);

                                //Storing the Array of JSON String to our JSON Array
                                result = j.getJSONArray("language");

                                for (int i = 0; i < result.length(); i++) {

                                    try {

                                        JSONObject json = result.getJSONObject(i);

                                        String feet = json.getString("feet");
                                        String inch = json.getString("inch");
                                        String cm = json.getString("cm");

                                        //Getting json object

                                        heightfromarray.add(feet + "'" + "" + inch + "'" + "-" + cm + "cm");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                heightfromarray.add(0, "Select");
                                //Setting adapter to show the items in the spinner
                                heightfromm.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, heightfromarray));


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

        private void heightto() {
            //Creating a string request
            StringRequest stringRequest = new StringRequest(base_api_url.HEIGHTS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JSONObject j = null;
                            try {
                                heightlist = response;

                                //Parsing the fetched Json String to JSON Object
                                j = new JSONObject(response);

                                //Storing the Array of JSON String to our JSON Array
                                result = j.getJSONArray("language");

                                for (int i = 0; i < result.length(); i++) {

                                    try {

                                        JSONObject json = result.getJSONObject(i);

                                        String feet = json.getString("feet");
                                        String inch = json.getString("inch");
                                        String cm = json.getString("cm");

                                        //Getting json object

                                        heitoarray.add(feet + "'" + "" + inch + "'" + "-" + cm + "cm");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                heitoarray.add(0, "Select");
                                //Setting adapter to show the items in the spinner
                                heighttoo.setAdapter(new ArrayAdapter<String>(AdvanceSearch.this, android.R.layout.simple_spinner_dropdown_item, heitoarray));


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




    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

        public void back(View v) {
            finish();
        }

    }
