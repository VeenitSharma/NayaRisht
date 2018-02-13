package com.villupuram.nayarishta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinit on 7/13/2017.
 */

public class Row_hobbies extends ArrayAdapter {
    List list=new ArrayList();
    private boolean checked = false;

    public Row_hobbies(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }
    static class DataHandler
    {
        TextView name;
        TextView date_inbox;
        TextView message;
        CheckBox checkBox;

    }
    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return this.list.get(position);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row=convertView;
        Row_hobbies.DataHandler handler;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.row_inbox,parent,false);
            handler=new Row_hobbies.DataHandler();
            handler.name=(TextView)row.findViewById(R.id.user_name_inbox);
            handler.date_inbox=(TextView)row.findViewById(R.id.date_inbox);
            handler.message=(TextView)row.findViewById(R.id.message);
//            handler.checkBox = (CheckBox) row.findViewById(R.id.checkbox1);
            row.setTag(handler);


        }
        else {
            handler=(Row_hobbies.DataHandler)row.getTag();
        }
        SearchInbox item;
        item=(SearchInbox) this.getItem(position);
        handler.name.setText(item.getname());
        handler.date_inbox.setText(item.getDate());
        handler.message.setText(item.getMessage());
        handler.message.setText(item.getMessage());
//        handler.checkBox.setChecked(true);

        return row;
    }



}

