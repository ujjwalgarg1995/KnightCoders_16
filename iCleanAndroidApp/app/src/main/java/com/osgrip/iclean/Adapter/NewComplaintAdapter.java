package com.osgrip.iclean.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.R;

import java.util.List;

/**
 * Created by adarsh2k on 6/5/16.
 */
public class NewComplaintAdapter extends RecyclerView.Adapter<NewComplaintAdapter.MyViewHolder> {

    List<Complains> complainsList;
    Context context;
    OnItemClickListener itemClickListener;


    public NewComplaintAdapter(List<Complains> complainsList, Context context) {
        this.complainsList = complainsList;
        this.context = context;
    }

    @Override
    public NewComplaintAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_complain_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewComplaintAdapter.MyViewHolder holder, int position) {
        Complains complains = complainsList.get(position);
        holder.complainText.setText(Html.fromHtml("<b>Complain : </b>"+complains.getComplain()));
        holder.addressText.setText(Html.fromHtml("<b>Address : </b>"+complains.getAddress()));
        holder.mobileNoText.setText(Html.fromHtml("<b>Mobile No : </b>"+complains.getMobileNo()));
        Glide.with(context)
                .load(complains.getImage())
                .thumbnail(1f)
                .crossFade()
                .placeholder(R.drawable.no_photo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return complainsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView complainText,addressText,mobileNoText;
        Button acceptButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            complainText = (TextView) itemView.findViewById(R.id.complainText);
            addressText= (TextView) itemView.findViewById(R.id.addressText);
            mobileNoText = (TextView) itemView.findViewById(R.id.mobileNoText);
            acceptButton = (Button) itemView.findViewById(R.id.acceptButton);
            acceptButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null) {
                itemClickListener.onItemClick(v,getPosition());
            }
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public void setItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
