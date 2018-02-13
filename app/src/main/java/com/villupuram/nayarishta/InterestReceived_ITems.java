package com.villupuram.nayarishta;

import android.content.Context;

import com.firebase.tubesock.ThreadInitializer;

/**
 * Created by asdf on 1/11/2018.
 */

public class InterestReceived_ITems {
    private String name, date, id;
    Context context;
    public InterestReceived_ITems(String name, String date, String id,Context context
    ) {
        this.name = name;
        this.date = date;
        this.id = id;
        this.context = context;

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }




}
