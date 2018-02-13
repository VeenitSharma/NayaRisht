package com.villupuram.nayarishta;

import android.content.Context;

/**
 * Created by asdf on 12/1/2017.
 */

public class DraftItems {
    private String name, date, message, subject, msgid;
    Context context;
    public DraftItems(String name, String date, String message,String subject,String msgid, Context context
    ) {
        this.name = name;
        this.date = date;
        this.message = message;
        this.context = context;
        this.subject = subject;
        this.msgid = msgid;
    }

    public String getName() {
        return name;
    }

    public String getMsgid() {
        return msgid;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }
}
