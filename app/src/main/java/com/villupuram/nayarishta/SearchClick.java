package com.villupuram.nayarishta;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SearchClick extends AppCompatActivity {

    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_search_click);
        final TextView tv1,tv2,tv3;

        tv1=(TextView)findViewById(R.id.qSearch);
        tv2=(TextView)findViewById(R.id.advSearch);
        tv3=(TextView)findViewById(R.id.pidSearch);



        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });





        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv1.setBackgroundResource(R.drawable.circlebg);
                tv1.setTextColor(Color.parseColor("#000000"));
                tv2.setBackgroundResource(R.drawable.circle);
                tv2.setTextColor(Color.parseColor("#ffffff"));
                tv3.setBackgroundResource(R.drawable.circle);
                tv3.setTextColor(Color.parseColor("#ffffff"));
                Intent intent = new Intent(SearchClick.this,QuickSearch.class);
                startActivity(intent);

            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv2.setBackgroundResource(R.drawable.circlebg);
                tv2.setTextColor(Color.parseColor("#000000"));

                tv1.setBackgroundResource(R.drawable.circle);
                tv1.setTextColor(Color.parseColor("#ffffff"));
                tv3.setBackgroundResource(R.drawable.circle);
                tv3.setTextColor(Color.parseColor("#ffffff"));

                Intent intent = new Intent(SearchClick.this,AdvanceSearch.class);
                startActivity(intent);

            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tv1.setBackgroundResource(R.drawable.circle);
                tv1.setTextColor(Color.parseColor("#ffffff"));
                tv2.setBackgroundResource(R.drawable.circle);
                tv2.setTextColor(Color.parseColor("#ffffff"));
                tv3.setBackgroundResource(R.drawable.circlebg);
                tv3.setTextColor(Color.parseColor("#000000"));

                Intent intent = new Intent(SearchClick.this,ProfileIdSearch.class);
                startActivity(intent);


            }
        });



    }



}
