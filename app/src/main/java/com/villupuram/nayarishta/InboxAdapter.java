package com.villupuram.nayarishta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vinit on 7/13/2017.
 */

public class InboxAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public InboxAdapter(@NonNull Context context, @LayoutRes int resource ) {
        super(context, resource);
    }

    static class DataHandler
    {

        TextView name;
        TextView date_inbox;
        TextView message;
        Button deletebtn;
        RelativeLayout relativeLayout;

    }
    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }



    public String getName(String name) {
        return name;
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row=convertView;
        InboxAdapter.DataHandler handler;
        if(convertView==null)
        {

            LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.row_inbox,parent,false);
            handler=new InboxAdapter.DataHandler();
            handler.name=(TextView)row.findViewById(R.id.user_name_inbox);
            handler.date_inbox=(TextView)row.findViewById(R.id.date_inbox);
            handler.message=(TextView)row.findViewById(R.id.message);
            handler.relativeLayout=(RelativeLayout) row.findViewById(R.id.draftitem);



            row.setTag(handler);

        }
        else {
            handler=(InboxAdapter.DataHandler)row.getTag();
        }
        SearchInbox item;
        item=(SearchInbox) this.getItem(position);
        handler.name.setText(item.getname());
        handler.date_inbox.setText(item.getDate());
        handler.message.setText(item.getMessage());


        return row;
    }




}

