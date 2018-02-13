package com.villupuram.nayarishta;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asdf on 1/11/2018.
 */

public class InterstSend_Adapter extends RecyclerView.Adapter<InterstSend_Adapter.ViewHolder> {

private List<InterestSend_Items> listItems;
private Context context;

public InterstSend_Adapter(List<InterestSend_Items> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
        }

@Override
public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_interest_received, parent, false);

        return new ViewHolder(view);

        }

@Override
public void onBindViewHolder(ViewHolder holder, final int position) {

final InterestSend_Items listItem = listItems.get(position);
        holder.nametxt.setText(listItem.getName());
        holder.datetxt.setText(listItem.getDate());
        holder.msgtxt.setText(listItem.getId());


        holder.layout.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        ((InterestSend)v.getContext()).opendraftmsg(listItem.getId());
        }
        });
        }

@Override
public int getItemCount() {
        return listItems.size();
        }

public class ViewHolder extends RecyclerView.ViewHolder {
    public TextView nametxt, datetxt, msgtxt;
    public RelativeLayout layout, deletebtn;

    public ViewHolder(View itemView) {
        super(itemView);

        nametxt = (TextView) itemView.findViewById(R.id.user_name_inbox);
        datetxt = (TextView) itemView.findViewById(R.id.date_inbox);
        msgtxt = (TextView) itemView.findViewById(R.id.id_text);
        layout =(RelativeLayout)itemView.findViewById(R.id.interestItems);


    }
}
}



