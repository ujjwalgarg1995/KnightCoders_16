package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.osgrip.iclean.Adapter.PendingGridAdapter;
import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ShowDetailDialog;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.Modals.Pending;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.DBHelper;
import com.osgrip.iclean.utils.ServiceCall;
import com.osgrip.iclean.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class PendingActivity extends AppCompatActivity implements View.OnClickListener{
    private GridView pendingUploadGrid;
    private LinearLayout uploadButton;
    private PendingGridAdapter adapter;
    private TextView noPendingText;
    private DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='white'>Pending </font><font color='#0059CA'>Uploads</font>"));
        db = new DBHelper(this);
        pendingUploadGrid = (GridView) findViewById(R.id.pendingUploadGrid);
        uploadButton = (LinearLayout) findViewById(R.id.uploadButton);
        noPendingText = (TextView) findViewById(R.id.noPendingText);
        uploadButton.setOnClickListener(this);
        refreshGrid();
    }

    private void refreshGrid() {
        List<Pending> pendingList = db.getPending();
        if(pendingList.size()!=0)
        {
            uploadButton.setVisibility(View.VISIBLE);
            pendingUploadGrid.setVisibility(View.VISIBLE);
            noPendingText.setVisibility(View.GONE);
            adapter = new PendingGridAdapter(this,pendingList);
            pendingUploadGrid.setAdapter(adapter);
        } else {
            noPendingText.setVisibility(View.VISIBLE);
            uploadButton.setVisibility(View.GONE);
            pendingUploadGrid.setVisibility(View.GONE);
        }
    }



    @Override
    public void onClick(View v) {
        if(ServiceCall.isNetworkAvailable(this)){
            List<Pending> pendingList = db.getPending();
            if(pendingList.size() == 0){
                new AlertDialog.Builder(this)
                        .setMessage("No Pending Complain")
                        .setTitle("Error")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(true)
                        .show();
            } else {
                new UploadToServer().execute(pendingList);

            }
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("No Internet Connection Available, Please try again later!")
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

    private class UploadToServer extends AsyncTask<List<Pending>,Integer,String[]> {
        private ProgressDialog pd = new ProgressDialog(PendingActivity.this);
        Bitmap bitmap;
        String img64;
        int size,uploaded=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait !!!");
            pd.show();
        }

        @Override
        protected String[] doInBackground(List<Pending>... params) {
            size = params[0].size();
            publishProgress(uploaded,size);
            DBHelper db = new DBHelper(PendingActivity.this);
            for(int i=0;i<size;i++)
            {
                Pending p = params[0].get(i);

                List<NameValuePair> param = new ArrayList<>();
                SessionManager sessionManager = new SessionManager(PendingActivity.this);
                String mobileNo = sessionManager.getMobileNumber();
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;

                bitmap = BitmapFactory.decodeFile(p.getImgPath(), options);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                byte[] ba = bao.toByteArray();
                img64 = Base64.encodeToString(ba, Base64.DEFAULT);
                param.add(new BasicNameValuePair("mobileNo", mobileNo));
                param.add(new BasicNameValuePair("complain",p.getComplain()));
                param.add(new BasicNameValuePair("address",p.getAddress()));
                param.add(new BasicNameValuePair("time",p.getTime()));
                param.add(new BasicNameValuePair("ward",p.getWard()));
                param.add(new BasicNameValuePair("img64", img64));
                param.add(new BasicNameValuePair("imgName",p.getImage()));
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

                        Complains complains = new Complains(p.getComplain(),complainID, "", p.getTime(),p.getWard(),p.getImage(),status,"",0 , p.getAddress());
                        db.insertIntoComplains(complains);
                        db.deleteFromPending(p.getId());
                        uploaded++;
                        publishProgress(uploaded,size);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return new String[]{""+uploaded,""+size};
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pd.setMessage("Please wait!!!\n" + values[0] + " out of " + values[1] + " uploaded.");
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            pd.dismiss();
            ShowDetailDialog sdd = new ShowDetailDialog(PendingActivity.this,null,3,new int[]{uploaded,size});
            sdd.show();

        }
    }
}
