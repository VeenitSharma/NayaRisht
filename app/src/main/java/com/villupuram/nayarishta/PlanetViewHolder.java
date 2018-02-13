package com.villupuram.nayarishta;

import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by vinit on 7/19/2017.
 */

public class PlanetViewHolder {
    private CheckBox checkBox;
    private TextView textView;

    public PlanetViewHolder()
    {
    }

    public PlanetViewHolder(TextView textView, CheckBox checkBox)
    {
        this.checkBox = checkBox;
        this.textView = textView;
    }

    public CheckBox getCheckBox()
    {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox)
    {
        this.checkBox = checkBox;
    }

    public TextView getTextView()
    {
        return textView;
    }

    public void setTextView(TextView textView)
    {
        this.textView = textView;
    }
}
