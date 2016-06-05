package com.osgrip.iclean.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.osgrip.iclean.Activity.CitizenHomeActivity;
import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ShowDetailDialog;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.Modals.Pending;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.DBHelper;
import com.osgrip.iclean.utils.ImageHandler;
import com.osgrip.iclean.utils.ServiceCall;
import com.osgrip.iclean.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranjal on 15-Jan-16.
 */
public class PendingGridAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Pending> pending;
    public PendingGridAdapter(Activity activity, List<Pending> pending) {
        this.activity = activity;
        this.pending = pending;
    }
    @Override
    public int getCount() {
        return pending.size();
    }

    @Override
    public Object getItem(int position) {
        return pending.get(position);
    }

    @Override
    public long getItemId(int position) {
        return pending.get(position).getId();
    }

    @Override
    public View getView(int position, View cView, ViewGroup parent) {
        View convertView =null;
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.pending_grid_view,null);
        final Pending p = pending.get(position);
        ImageView gridImage = (ImageView) convertView.findViewById(R.id.gridImage);
        String path = ImageHandler.loadImagePath(p.getImage());
        if(path!=null){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(ImageHandler.loadImagePath(p.getImage()), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/50, photoH/50);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            Bitmap bitmap = BitmapFactory.decodeFile(ImageHandler.loadImagePath(p.getImage()), bmOptions);
            gridImage.setImageBitmap(bitmap);
        } else {
            gridImage.setImageResource(R.drawable.no_photo);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    new UploadToServer().execute(p.getComplain(),p.getAddress(),p.getTime(),p.getWard(),p.getImage(),p.getImgPath(),p.getId()+"");
                } else {
                    new AlertDialog.Builder(activity)
                            .setMessage("Sorry, you are not connected to the Internet.\nPlease try again later.")
                            .setTitle("Error")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(true)
                            .show();
                }
            }
        });
        return convertView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class UploadToServer extends AsyncTask<String,Void,Complains> {
        private ProgressDialog pd = new ProgressDialog(activity);
        Bitmap bitmap;
        String img64;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait !!!");
            pd.show();
        }

        @Override
        protected Complains doInBackground(String... data) {
            Complains complains = null;
            List<NameValuePair> param = new ArrayList<>();
            SessionManager sessionManager = new SessionManager(activity);
            String mobileNo = sessionManager.getMobileNumber();
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(data[5], options);
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            img64 = Base64.encodeToString(ba, Base64.DEFAULT);
            param.add(new BasicNameValuePair("mobileNo", mobileNo));
            param.add(new BasicNameValuePair("complain",data[0]));
            param.add(new BasicNameValuePair("address",data[1]));
            param.add(new BasicNameValuePair("time",data[2]));
            param.add(new BasicNameValuePair("ward",data[3]));
            param.add(new BasicNameValuePair("img64", img64));
            param.add(new BasicNameValuePair("imgName",data[4]));
            String jsonString = new ServiceCall().makeServiceCall(Config.FILE_UPLOAD_URL,ServiceCall.POST,param);
            String message, complainID, status;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                if(error)
                {
                    message = jsonObject.getString("message");
                } else {
                    complainID = jsonObject.getString("complainID");
                    status = jsonObject.getString("status");
                    DBHelper db = new DBHelper(activity);
                    complains = new Complains(data[0],complainID,"" , data[2],data[3],data[4],status,"", 0, data[1]);
                    db.insertIntoComplains(complains);
                    db.deleteFromPending(Integer.parseInt(data[6]));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return complains;
        }

        @Override
        protected void onPostExecute(Complains s) {
            super.onPostExecute(s);

            pd.dismiss();
            if(s==null) {
                new AlertDialog.Builder(activity)
                        .setMessage("Something went wrong please try again later")
                        .setTitle("Error")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(activity,CitizenHomeActivity.class);
                                activity.startActivity(i);
                                activity.finish();
                            }
                        })
                        .setCancelable(true)
                        .show();
            } else {
                ShowDetailDialog sdd = new ShowDetailDialog(activity,s,1,null);
                sdd.show();
            }

        }
    }
}
