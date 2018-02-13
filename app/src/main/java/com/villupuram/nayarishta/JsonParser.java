package com.villupuram.nayarishta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinit on 7/20/2017.
 */

public class JsonParser {
    public static List Parse(String json){

        try {
            JSONArray jsonArray = new JSONArray(json);
            List contactList = new ArrayList<>();

            int i = 0;
            while (i < jsonArray.length()){
                JSONObject jb = jsonArray.getJSONObject(i);

                contactList.add(jb.getString("name"));
                i++;
            }

            return contactList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

}
