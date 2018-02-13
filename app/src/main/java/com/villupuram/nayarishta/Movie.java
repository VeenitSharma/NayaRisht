package com.villupuram.nayarishta;

import java.util.ArrayList;

/**
 * Created by vinit on 8/21/2017.
 */

public class Movie {
    private String name,desc, address,detail,thumbnailUrl;


    public Movie() {
    }

    public Movie(String name, String thumbnailUrl,String desc,String address,String detail,
                 ArrayList<String> genre) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.desc = desc;
        this.address = address;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
