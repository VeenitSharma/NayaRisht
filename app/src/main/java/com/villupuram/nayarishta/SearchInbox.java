package com.villupuram.nayarishta;

/**
 * Created by vinit on 7/13/2017.
 */

public class SearchInbox {
    private String name;
    private  String date_inbox;
    private String message;

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String getDate() {
       return date_inbox;
    }

    public void setDate(String date_inbox) {
        this.date_inbox = date_inbox;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SearchInbox(String name,String date_inbox,String message)
    {
        this.setname(name);
        this.setDate(date_inbox);
        this.setMessage(message);
    }


}


