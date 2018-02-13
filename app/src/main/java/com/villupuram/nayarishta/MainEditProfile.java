package com.villupuram.nayarishta;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainEditProfile extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_edit_profile,
                container, false);




        RelativeLayout basic = (RelativeLayout)view.findViewById(R.id.basic_details);
        basic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditProfile.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"this",Toast.LENGTH_LONG).show();
            }
        });

        return view;




    }
}
