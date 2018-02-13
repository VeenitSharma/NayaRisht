package com.villupuram.nayarishta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditProfile_adminMessage extends AppCompatActivity {

    TextView except, secondmsg;
    RelativeLayout crossbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin_message);



        except = (TextView) findViewById(R.id.except);
        secondmsg = (TextView) findViewById(R.id.secline);
        crossbtn = (RelativeLayout)findViewById(R.id.crossbtn);



        Intent intent = getIntent();
        String msg = intent.getStringExtra("msg");



        String part1="",msg1="",msg2="";
        if (!msg.equals(""))
        {

                 except.setText(msg);




        }



        crossbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });




    }
}
