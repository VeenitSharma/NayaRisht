package com.villupuram.nayarishta;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by vinit on 6/17/2017.
 */

public class LayoutOne extends Fragment {

    public static Fragment newInstance(Context context) {
        LayoutOne f = new LayoutOne();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.layout_one, null);



        return root;
    }

}