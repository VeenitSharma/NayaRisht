package com.villupuram.nayarishta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ShowHobbies extends AppCompatActivity {

    MyCustomAdapter dataAdapter = null;
    public ArrayList<String> values;
    Button myButton;
    SharedPreferences sharedPreferences= null;
    public static final String sadsf = "MyPrefs" ;
    MyCustomAdapter.ViewHolder holder = null;
    final MyCustomAdapter.ViewHolder finalHolder = holder;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_hobbies);

       myButton = (Button) findViewById(R.id.select);

        values = new ArrayList<String>();
        //Generate list ViewSome from ArrayList
        displayListView();

        checkButtonClick();
        sharedPreferences = getSharedPreferences(sadsf, Context.MODE_PRIVATE);

//        if(sharedPreferences!=null){
//
//            if (sharedPreferences.getBoolean("checkbox", false)){ //false is default value
//                finalHolder.name.setChecked(true); //it was checked
//            }
//
//        }


        myButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
//              String[] mStringArray = new String[values.size()];
//              mStringArray = values.toArray(mStringArray);
//              for(int i = 0; i < mStringArray.length ; i++){
//

//                  Toast.makeText(getApplicationContext(),"string:"+mStringArray[i],Toast.LENGTH_LONG).show();
//

//
//              }

              Intent intent = new Intent(getApplicationContext(), Edit_yourLikes.class);
//              intent.putStringArrayListExtra("key",values);
              startActivity(intent);


          }
      });

    }

    private void displayListView() {

        //Array list of countries
        ArrayList<Country> countryList = new ArrayList<Country>();
        Country country = new Country( "Afghanistan");
        countryList.add(country);
        country = new Country("Albania");
        countryList.add(country);
        country = new Country("Algeria");
        countryList.add(country);
        country = new Country( "American Samoa");
        countryList.add(country);
        country = new Country( "Andorra");
        countryList.add(country);
        country = new Country("Angola");
        countryList.add(country);
        country = new Country( "Anguilla");
        countryList.add(country);


        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,R.layout.row_hobbies, countryList);
        ListView listView = (ListView) findViewById(R.id.listView_inbox);

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Country country = (Country) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on Row: " + country.getName(),
//                        Toast.LENGTH_LONG).show();
            }
        });



//




    }
    private class MyCustomAdapter extends ArrayAdapter<Country> {

        private ArrayList<Country> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Country> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<Country>();
            this.countryList.addAll(countryList);
              }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


//            ViewHolder holder = null;
//            final  ViewHolder finalHolder = holder;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row_hobbies, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.user_name_inbox);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkbox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Country country = (Country) cb.getTag();


                        if (finalHolder.name.isChecked()==true) {

                            values.add(cb.getText().toString());
                            System.out.print(values);
//                            Toast.makeText(getApplicationContext(),"hobbies added:"+ values, Toast.LENGTH_LONG).show();
                        }
                        else {
                            values.remove(cb.getText());
                            System.out.print(values);
//                            Toast.makeText(getApplicationContext(),"hobbies deleted:"+ values, Toast.LENGTH_LONG).show();
                        }
                        Toast.makeText(getApplicationContext(),"hobbies :"+ values, Toast.LENGTH_LONG).show();

//                        Log.d("string is",(String)mStringArray[i]);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("checkbox", finalHolder.name.isChecked());
                        editor.commit();

                    }
                }

                );
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Country country = countryList.get(position);
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);


            return convertView;

        }

    }


    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.select);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                StringBuffer responseText = new StringBuffer();
//                StringBuffer value = new StringBuffer();
//
//
////                responseText.append("The following were selected...\n");
//
//                ArrayList<Country> countryList = dataAdapter.countryList;
//                for(int i=0;i<countryList.size();i++){
//                    Country country = countryList.get(i);
//                    if(country.isSelected()){
//
//                        if ( i != countryList.size()+1){
//                            responseText.append(country.getName()+",");
//
//
//                        }
//
//                    }
//
//                }

//                value = responseText.deleteCharAt(responseText.length()-1);

//                Intent intent = new Intent(getApplicationContext(),Edit_yourLikes.class);
//                intent.putExtra("checked",values);
//                startActivity(intent);



//                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();


            }
        });

    }


}

