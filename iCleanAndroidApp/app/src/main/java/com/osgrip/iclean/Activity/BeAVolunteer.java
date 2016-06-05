package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ErrorDialog;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.ServiceCall;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BeAVolunteer extends AppCompatActivity {

    Button submitButton;
    EditText vName, vDOB, vMobileNo, vEmail;
    Spinner vJob,vArea;
    TextView headingText;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_be_avolunteer);
        vName = (EditText) findViewById(R.id.vName);
        vDOB = (EditText) findViewById(R.id.vDOB);
        vMobileNo = (EditText) findViewById(R.id.vMobileNo);
        vEmail = (EditText) findViewById(R.id.vEmail);
        vJob = (Spinner) findViewById(R.id.vJob);
        headingText = (TextView) findViewById(R.id.headingText);
        headingText.requestFocus();
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = dayOfMonth+"-"+(monthOfYear+1)+"-"+year;
                vDOB.setText(date);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        vArea = (Spinner)findViewById(R.id.vArea);
        submitButton = (Button) findViewById(R.id.submitButton);
        vDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewForm();
            }
        });
        ArrayAdapter vJobAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Config.vJobList);
        vJobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vJob.setAdapter(vJobAdapter);
        ArrayAdapter vAreaAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Config.wardList);
        vJobAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vArea.setAdapter(vAreaAdapter);
    }

    private void reviewForm() {
        String name,dob,mobileNo,email="",job,area, errorMSG=null;
        name=vName.getText().toString();
        dob=vDOB.getText().toString();
        mobileNo=vMobileNo.getText().toString();
        email=vEmail.getText().toString();
        job=vJob.getSelectedItem().toString();
        area=vArea.getSelectedItemId()+"";
        View v=null;

        if(name==null || name.length()<3) {
            errorMSG = "Name cannot be empty, kindly enter your name";
            v=vName;
        } else if (dob==null || dob==""){
            errorMSG = "Date of Birth cannot be empty, kindly enter your Date of Birth";
            v=vDOB;
        } else if(mobileNo.length()==0) {
            errorMSG = "Mobile Number cannot be empty";
            v=vMobileNo;
        } else if(!(mobileNo.charAt(0)=='7'||mobileNo.charAt(0)=='8'||mobileNo.charAt(0)=='9')) {
            errorMSG = "Mobile number must begin with 7 or 8 or 9";
            v=vMobileNo;
        } else if(mobileNo.length()!=10){
            errorMSG = "Mobile Number must be of 10 digit";
            v=vMobileNo;
        } else if(vJob.getSelectedItemId()==0) {
            errorMSG = "Kindly select the profession";
            v=vJob;
        } else if(vArea.getSelectedItemId()==0) {
            errorMSG = "Kindly select a ward";
            v=vArea;
        }

        if(errorMSG!=null) {
            ErrorDialog.displayError(this,"Error",errorMSG,v);
            return;
        } else {
            if(isNetworkAvailable()) {
                new UploadToServer().execute(name,dob,mobileNo,email,area,job);
            } else {
                ErrorDialog.displayError(this,"Error","No Internet Connection Available, Please try again later!",null);
            }
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class UploadToServer extends AsyncTask<String, Void, String[]> {
        private ProgressDialog pd = new ProgressDialog(BeAVolunteer.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Please wait !!!");
            pd.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String result[] = new String[2];
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("name", params[0]));
            param.add(new BasicNameValuePair("dob", params[1]));
            param.add(new BasicNameValuePair("mobile_no", params[2]));
            param.add(new BasicNameValuePair("email", params[3]));
            param.add(new BasicNameValuePair("area_code", params[4]));
            param.add(new BasicNameValuePair("job", params[5]));
            String jsonString = new ServiceCall().makeServiceCall(Config.VOLUNTEER_REQUEST_URL, ServiceCall.POST, param);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                String msg = jsonObject.getString("message");
                if (error) {
                    result[0] = "true";
                } else {
                    result[1] = "false";
                }
                result[1] = "" + msg;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);
            String title="Registered for Volunteer";
            if (s[0] == "false") {
                title="Unable to Register";
            }
            pd.dismiss();
            new AlertDialog.Builder(BeAVolunteer.this)
                .setMessage(s[1])
                .setTitle(title)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent i = new Intent(getApplicationContext(), CitizenHomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);*/
                        finish();
                    }
                })
                .setCancelable(true)
                .show();
        }
    }

}
