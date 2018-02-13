package com.villupuram.nayarishta;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asdf on 1/12/2018.
 */

public class UsersAdapter extends ArrayAdapter<UsersItem> {

    private static final int MAX_ROW_DISPLAY = 1;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<UsersItem> mListData = new ArrayList<UsersItem>();
    int i =2;
    int num = 1;
    public UsersAdapter(Context mContext, int layoutResourceId, ArrayList<UsersItem> mListData) {
        super(mContext, layoutResourceId, mListData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mListData = mListData;
    }
    @Override
    public int getCount() {
            return mListData.size();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        ViewHolder holder;


        if (row == null) {
            row = inflater.inflate(R.layout.user_name_list, null);

            holder = new ViewHolder();
            holder.userimage = (ImageView) row.findViewById(R.id.user_image);
            holder.nametxt = (TextView) row.findViewById(R.id.currentusername);
            holder.layout = (RelativeLayout)row.findViewById(R.id.userlayout);

            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }



        UsersItem item = mListData.get(position);

        holder.nametxt.setText(Html.fromHtml(item.getName()));
//        holder.id.setText(Html.fromHtml(item.getId()));




            Picasso.with(mContext).load(item.getImage()).into(holder.userimage);

//        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        public TextView nametxt;
        public ImageView userimage;
        public RelativeLayout layout, deletebtn;
    }

}
