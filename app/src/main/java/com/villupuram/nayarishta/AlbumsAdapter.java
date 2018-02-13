package com.villupuram.nayarishta;

/**
 * Created by vinit on 6/21/2017.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;
/**
 * Created by Ravi Tamada on 18/05/16.
 */
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
public class  AlbumsAdapter extends ArrayAdapter<Album> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<Album> mGridData = new ArrayList<Album>();

    public AlbumsAdapter(Context mContext, int layoutResourceId, ArrayList<Album> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;

    }
    /**
     * Updates grid data and refresh grid items.
     * @param mGridData
     */
    public void setGridData(ArrayList<Album> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        final ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.titleTextView = (TextView) row.findViewById(R.id.grid_item_title);
            holder.dis = (TextView) row.findViewById(R.id.dis);
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        final Album item = mGridData.get(position);
        holder.titleTextView.setText(Html.fromHtml(item.getName()));
        holder.dis.setText(Html.fromHtml(item.getNumOfSongs()));


        if (item.getThumbnail().isEmpty()) {
            if (item.getGender().toString().equals("F"))
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
            Picasso.with(holder.imageView.getContext()).load(item.getThumbnail()).placeholder(R.drawable.user).resize(170,180).onlyScaleDown().into(holder.imageView);
        }
        return row;
    }
    static class ViewHolder {
        TextView titleTextView, dis;
        ImageView imageView;
    }
}
