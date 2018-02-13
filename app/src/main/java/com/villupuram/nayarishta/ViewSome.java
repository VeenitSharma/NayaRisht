package com.villupuram.nayarishta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewSome extends AppCompatActivity {

    TextView topmsg,msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        topmsg = (TextView)findViewById(R.id.topmsg);
        msg=(TextView)findViewById(R.id.msg);


        Intent intent = getIntent();
        String msgtext = intent.getStringExtra("msgtext");
        String name = intent.getStringExtra("name");

        topmsg.setText(name);
        msg.setText(msgtext);
        RelativeLayout back =(RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
