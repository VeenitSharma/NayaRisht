package com.villupuram.nayarishta;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by asdf on 12/1/2017.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {

    private List<DraftItems>listItems;
    private Context context;
    public DraftAdapter(List<DraftItems> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_inbox,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final DraftItems listItem = listItems.get(position);

        holder.nametxt.setText(listItem.getName());
        holder.datetxt.setText(listItem.getDate());
        holder.msgtxt.setText(listItem.getMessage());


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Drafts)v.getContext()).opendraftmsg(listItem.getName(),listItem.getSubject(),listItem.getMessage());
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                listItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,listItems.size());
                ((Drafts)v.getContext()).deleteDraft(listItem.getMsgid());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       public TextView nametxt , datetxt, msgtxt;
        public RelativeLayout layout, deletebtn;
        public ViewHolder(View itemView) {
            super(itemView);

            nametxt = (TextView)itemView.findViewById(R.id.user_name_inbox);
            datetxt = (TextView)itemView.findViewById(R.id.date_inbox);
            msgtxt = (TextView)itemView.findViewById(R.id.message);
            layout = (RelativeLayout)itemView.findViewById(R.id.draftitem);
            deletebtn = (RelativeLayout)itemView.findViewById(R.id.deletebtn);


        }
    }
}
