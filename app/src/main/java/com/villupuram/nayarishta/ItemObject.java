package com.villupuram.nayarishta;

import android.content.Context;

/**
 * Created by vinit on 7/20/2017.
 */

public class ItemObject {
    private String name;

    public ItemObject(String name) {
        this.name = name;
    }

    public ItemObject(Context applicationContext, int row_hobbies) {
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

