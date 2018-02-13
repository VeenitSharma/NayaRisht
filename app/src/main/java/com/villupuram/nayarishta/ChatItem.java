package com.villupuram.nayarishta;

/**
 * Created by MALIK-PC on 5/29/2017.
 */

public class ChatItem {
    private int dpic;
    private String name;
    private  String desc;
    public ChatItem(int dpic,String name,String desc){
        this.setDpic(dpic);
        this.setName(name);
        this.setDesc(desc);
    }
    public int getDpic() {
        return dpic;
    }

    public void setDpic(int dpic) {
        this.dpic = dpic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
