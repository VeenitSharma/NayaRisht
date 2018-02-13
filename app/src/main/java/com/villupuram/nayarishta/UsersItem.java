package com.villupuram.nayarishta;

import android.content.Context;

/**
 * Created by asdf on 1/12/2018.
 */

public class UsersItem {
    private String name;
    Context context;
    String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
