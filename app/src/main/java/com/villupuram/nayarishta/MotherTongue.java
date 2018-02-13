package com.villupuram.nayarishta;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MotherTongue extends AppCompatActivity{

   CheckBox c1,c2,c3,c4,c5;
    Button btn;
    TextView hindi,marathi,bamgalif,bamgali,gujratitext,malyalamtext;
    ArrayList<String> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mother_tongue);

        list = new ArrayList<String>();
        TextView t = (TextView) findViewById(R.id.reset);
         hindi = (TextView) findViewById(R.id.hinditext);
            marathi = (TextView) findViewById(R.id.marathitext);
        bamgalif = (TextView) findViewById(R.id.bengalitext);
         gujratitext = (TextView) findViewById(R.id.gujratitext);
         malyalamtext = (TextView) findViewById(R.id.malyalamtext);
        ImageButton cross = (ImageButton) findViewById(R.id.cross);





        t.setPaintFlags(t.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        btn = (Button)findViewById(R.id.cancleimage);
        c1=(CheckBox)findViewById(R.id.hindi);
        c2=(CheckBox)findViewById(R.id.marathi);
        c3=(CheckBox)findViewById(R.id.telgu);
        c4=(CheckBox)findViewById(R.id.Gujrati);
        c5=(CheckBox)findViewById(R.id.punjabi);

        RelativeLayout reset = (RelativeLayout)findViewById(R.id.resetview1);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()==true) {

                    String hindii = hindi.getText().toString();

                    list.add(hindii);
                }
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()==true) {
                    String marathii = marathi.getText().toString();

                    list.add(marathii);
                }
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()==true) {
                    String bangalli = bamgalif.getText().toString();

                    list.add(bangalli);
                }
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()==true) {
                    String gujarati = gujratitext.getText().toString();

                    list.add(gujarati);
                }
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (c1.isChecked()==true) {
                    String malayalamm = malyalamtext.getText().toString();

                    list.add(malayalamm);

                }
            }
        });







//        System.out.print(array);


        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QuickSearch.class);
                intent.putStringArrayListExtra("key", list);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),list.toString(),Toast.LENGTH_LONG).show();

            }});



    }

//    public void back (ViewSome a)
//    {
//        Toast.makeText(getApplicationContext(),,Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(getApplicationContext(),QuickSearch.class);
//        startActivity(intent);
//    }


    public void cancle(View v) {
        c1.setChecked(false);
        c2.setChecked(false);
        c3.setChecked(false);
        c4.setChecked(false);
        c5.setChecked(false);



    }

    public void value(){
        if (c1.isChecked()==true){

        }
    }



}
