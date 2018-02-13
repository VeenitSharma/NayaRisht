package com.villupuram.nayarishta;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by vinit on 8/24/2017.
 */

public class SearchAdapter  extends ArrayAdapter<SearchItem> {

    private static final int MAX_ROW_DISPLAY = 1;
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<SearchItem> mListData = new ArrayList<SearchItem>();
    int i =2;
    int num = 1;
    public SearchAdapter(Context mContext, int layoutResourceId, ArrayList<SearchItem> mListData) {
        super(mContext, layoutResourceId, mListData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mListData = mListData;
    }
    @Override
    public int getCount() {

        if(num*8 > mListData.size()){
            return mListData.size();
        }else{
            return num*8;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = convertView;
        ViewHolder holder;


        if (row == null) {
            row = inflater.inflate(R.layout.row_layout, null);

            holder = new ViewHolder();
            holder.id = (TextView) row.findViewById(R.id.idf);
            holder.name = (TextView) row.findViewById(R.id.name);
            holder.detail = (TextView) row.findViewById(R.id.info_layout);
            holder.imageView = (ImageView) row.findViewById(R.id.user_image);
            holder.more = (Button) row.findViewById(R.id.more);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }



        SearchItem item = mListData.get(position);

        holder.id.setText(Html.fromHtml(item.getId()));
        holder.name.setText(Html.fromHtml(item.getName()));
        holder.detail.setText(Html.fromHtml(item.getDetail()));
//        holder.id.setText(Html.fromHtml(item.getId()));



        if (item.getImage().isEmpty()) {

            if (item.getGender().toString().equals("F")||item.getGender().toString().equals("f"))
            {
                holder.imageView.setImageResource(R.drawable.femaledefault);
            }
            else if (item.getGender().toString().equals("M"))
            {
                holder.imageView.setImageResource(R.drawable.mandefault);
            }
            else {
                holder.imageView.setImageResource(R.drawable.user1);
            }

        } else{
            Picasso.with(mContext).load(item.getImage()).into(holder.imageView);
        }

//        Picasso.with(mContext).load(item.getImage()).into(holder.imageView);

        return row;
    }

    static class ViewHolder {
        TextView id,name,detail;
        ImageView imageView;
        Button more;
    }

}
