package com.osgrip.iclean.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osgrip.iclean.Config;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.ImageHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Pranjal on 01-Feb-16.
 */
public class MyComplainAdapter extends RecyclerView.Adapter<MyComplainAdapter.MyViewHolder> {
    List<Complains> complainsList;

    public class MyViewHolder  extends RecyclerView.ViewHolder{
        TextView minuteText, pictureTakenText, statusText, complainText, wardText,commentText;
        ImageView garbageImage;
        public MyViewHolder(View view){
            super(view);
            minuteText = (TextView) view.findViewById(R.id.minuteText);
            pictureTakenText = (TextView) view.findViewById(R.id.pictureTakenText);
            statusText = (TextView) view.findViewById(R.id.statusText);
            complainText = (TextView) view.findViewById(R.id.complainText);
            wardText = (TextView) view.findViewById(R.id.wardText);
            commentText = (TextView) view.findViewById(R.id.commentText);
            garbageImage = (ImageView) view.findViewById(R.id.garbageImage);
        }

    }
    public MyComplainAdapter(List<Complains> complainsList) {
        this.complainsList = complainsList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.complain_list_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Complains c = complainsList.get(position);
        String str_date=c.getTime();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String minago = DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS).toString();
        holder.minuteText.setText(minago);
        holder.pictureTakenText.setText(Html.fromHtml("<strong>Picture taken at </strong>" + c.getTime()));
        holder.complainText.setText(Html.fromHtml("<strong>Complain :</strong>" + c.getComplain()));
        //addressText.setText(c.getAddress());
        holder.wardText.setText(Html.fromHtml("<strong>" + Config.wardList[Integer.parseInt(c.getAreaCode())] + "</strong>"));
        holder.commentText.setText(Html.fromHtml("<strong>ReplyBack : </strong>"+c.getComment()));
        String path = ImageHandler.loadImagePath(c.getImage());
        if(path!=null){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(ImageHandler.loadImagePath(c.getImage()), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/50, photoH/50);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImagePath(c.getImage()), bmOptions);
            holder.garbageImage.setImageBitmap(bitmap);
        } else {
            holder.garbageImage.setImageResource(R.drawable.no_photo);
        }
        if(c.getStatus().equals("0")){
            holder.statusText.setText("Submitted");
            holder.statusText.setBackgroundResource(R.drawable.status_pending);
        } else if (c.getStatus().equals("1")) {
            holder.statusText.setText("Rejected");
            holder.statusText.setBackgroundResource(R.drawable.status_rejected);
        } if(c.getStatus().equals("2")){
            holder.statusText.setText("Processing");
            holder.statusText.setBackgroundResource(R.drawable.status_processing);
        } else if (c.getStatus().equals("3")) {
            holder.statusText.setText("Resolved");
            holder.statusText.setBackgroundResource(R.drawable.status_resolved);
        } else if (c.getStatus().equals("4")) {
            holder.statusText.setText("Accepted");
            holder.statusText.setBackgroundResource(R.drawable.status_accepted);
        }
    }

    @Override
    public int getItemCount() {
        return complainsList.size();
    }
}
