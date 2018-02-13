package com.villupuram.nayarishta;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MALIK-PC on 5/29/2017.
 */


public class ChatAdapter extends ArrayAdapter {
    List list=new ArrayList();
    public ChatAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    static class DataHandler1
    {
        ImageView dpic;
        TextView name;
        TextView message;
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
        ChatAdapter.DataHandler1 handler;
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.chat_layout,parent,false);
            handler=new ChatAdapter.DataHandler1();
            handler.dpic=(ImageView) row.findViewById(R.id.user_image);
            handler.name=(TextView)row.findViewById(R.id.name1);
            handler.message=(TextView)row.findViewById(R.id.message);
            row.setTag(handler);

        }
        else {
            handler=(ChatAdapter.DataHandler1)row.getTag();
        }
        ChatItem item;
        item=(ChatItem) this.getItem(position);
        handler.name.setText(item.getName());
        handler.message.setText(item.getDesc());


        Picasso.with(this.getContext()).load(item.getDpic()).into(handler.dpic);

        return row;
    }


}
