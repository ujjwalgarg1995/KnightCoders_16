package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ErrorDialog;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewComplaintActivity extends AppCompatActivity implements View.OnClickListener{
    private String filePath = null;
    private  String imgName = null;
    ImageView complainImage;
    EditText complainText,addressText;
    Button confirmButton, cancelButton;
    Spinner wardSpinner;
    Bitmap bitmap;
    String img64;
    String complain="", address="", ward="", time="";
    ShowDetailDialog sdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='white'>New </font><font color='#0059CA'>Complain</font>"));
        complainImage = (ImageView) findViewById(R.id.complainImage);
        complainText = (EditText) findViewById(R.id.complain);
        addressText = (EditText) findViewById(R.id.address);
        wardSpinner = (Spinner) findViewById(R.id.wardSpinner);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,Config.wardList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wardSpinner.setAdapter(adapter);

        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");
        previewImage();
        imgName = i.getStringExtra("imgName");
        Log.d("imgName",imgName);
        //bitmap = ImageHandler.loadImageFromStorage(imgName);

        //complainImage.setImageBitmap(bitmap);

    }

    private void previewImage() {
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        bitmap = BitmapFactory.decodeFile(filePath, options);

        complainImage.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.confirmButton:
                validate();
                break;
            case R.id.cancelButton:
                Intent i = new Intent(getApplicationContext(),CitizenHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                break;
        }
    }

    private void validate() {
        complain = complainText.getText().toString();
        address = addressText.getText().toString();
        ward = wardSpinner.getSelectedItemId()+"";
        String errorMsg="";
        View v = null;
        if(complain.length()==0) {
            errorMsg="Complain cannot be empty";
            v=complainText;
        } else if(address.length()==0) {
            errorMsg="Location cannot be empty";
            v=addressText;
        } else if(wardSpinner.getSelectedItemId()==0) {
            errorMsg="Kindly select ward";
            v=wardSpinner;
        }
        if(errorMsg.length()>0) {
            ErrorDialog.displayError(this, "Error", errorMsg, v);
        } else {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] ba = bao.toByteArray();
            img64 = Base64.encodeToString(ba, Base64.DEFAULT);
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            if(ServiceCall.isNetworkAvailable(this)) {
                new UploadToServer().execute();
            } else {
                DBHelper db = new DBHelper(this);
                Pending pending = new Pending(complain,address,time,ward,imgName,filePath);
                db.insertIntoPending(pending);
                ShowDetailDialog sdd = new ShowDetailDialog(NewComplaintActivity.this,null,2,null);
                sdd.show();
            }
        }
    }

    private class UploadToServer extends AsyncTask<Void, Void, Complains> {
        private ProgressDialog pd = new ProgressDialog(NewComplaintActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait !!!");
            pd.show();
        }

        @Override
        protected Complains doInBackground(Void... params) {
            Complains complains = null;
            String result[] = new String[5];
            List<NameValuePair> param = new ArrayList<>();
            SessionManager sessionManager = new SessionManager(NewComplaintActivity.this);
            String mobileNo = sessionManager.getMobileNumber();
            param.add(new BasicNameValuePair("mobileNo", mobileNo));
            param.add(new BasicNameValuePair("complain",complain));
            param.add(new BasicNameValuePair("address",address));
            param.add(new BasicNameValuePair("time",time));
            param.add(new BasicNameValuePair("ward",ward));
            param.add(new BasicNameValuePair("img64", img64));
            param.add(new BasicNameValuePair("imgName",imgName));
            String jsonString = new ServiceCall().makeServiceCall(Config.FILE_UPLOAD_URL,ServiceCall.POST,param);
            Log.d("Reg Complain",jsonString);
            String message, complainID, status;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                result[0] = ""+error;
                if(error)
                {
                    message = jsonObject.getString("message");
                    result[1]=message;
                } else {
                    complainID = jsonObject.getString("complainID");
                    status = jsonObject.getString("status");
                    DBHelper db = new DBHelper(NewComplaintActivity.this);
                    complains = new Complains(complain,complainID,mobileNo , time,ward,imgName,status,"", 0 , address);
                    db.insertIntoComplains(complains);
                    result[2] = complain;
                    result[3] = complainID;
                    result[4] = status;
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
                new AlertDialog.Builder(NewComplaintActivity.this)
                        .setMessage("Something went wrong please try again later")
                        .setTitle("Error")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(),CitizenHomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setCancelable(true)
                        .show();
            } else {
                sdd = new ShowDetailDialog(NewComplaintActivity.this,s,1,null);
                sdd.show();
            }

        }
    }
}

