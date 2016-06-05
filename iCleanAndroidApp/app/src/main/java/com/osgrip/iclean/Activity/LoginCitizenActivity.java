package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ErrorDialog;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.ServiceCall;
import com.osgrip.iclean.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginCitizenActivity extends AppCompatActivity {

    EditText mobileNumberInput, nameInput;
    Button continueButton,loginAsVolunteer;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mobileNumberInput = (EditText) findViewById(R.id.mobileNumberInput);
        nameInput = (EditText) findViewById(R.id.nameInput);
        continueButton = (Button) findViewById(R.id.continueButton);
        loginAsVolunteer = (Button) findViewById(R.id.volunteerLoginButton);
        loginAsVolunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginCitizenActivity.this,LoginVolunteerActivity.class);
                startActivity(intent);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate(){
        String name = nameInput.getText().toString();
        String mno = mobileNumberInput.getText().toString();
        String errorMsg ="";
        View v=null;
        if (name.length()<3) {
            errorMsg = "Enter a valid name";
            v=nameInput;
        } else if(mno.length()==0) {
            errorMsg = "Mobile Number cannot be empty";
            v=mobileNumberInput;
        } else if(!(mno.charAt(0)=='7'||mno.charAt(0)=='8'||mno.charAt(0)=='9')) {
            errorMsg = "Mobile number must begin with 7 or 8 or 9";
            v=mobileNumberInput;
        } else if(mno.length()!=10){
            errorMsg = "Mobile Number must be of 10 digit";
            v=mobileNumberInput;
        } else {
            if(ServiceCall.isNetworkAvailable(this)) {
                new RegOnServer().execute(name,mno);
            } else {
                ErrorDialog.displayError(this,"Error","Not connected to the Internet, please try again later",null);
            }
        }
        if(errorMsg!=""){
            final View finalV = v;
            new AlertDialog.Builder(context)
                    .setMessage(errorMsg)
                    .setTitle("Error")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(finalV!=null)
                            finalV.requestFocus();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }



    private class RegOnServer extends AsyncTask<String,Void,String[]> {
        private ProgressDialog pd = new ProgressDialog(LoginCitizenActivity.this);
        private String name, mobile;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Registering, please wait...");
            pd.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String result[]=new String[2];
            name   = params[0];
            mobile = params[1];
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("name",name));
            param.add(new BasicNameValuePair("mobile_no",mobile));
            String jsonString = new ServiceCall().makeServiceCall(Config.LOGIN_CITIZEN_URL,ServiceCall.POST,param);
            Log.d("Citizen Request", jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                if (error) {
                    result[0] = "false";
                } else {
                    result[0] = "true";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String s[]) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s[0].equals( "true")) {
                final SessionManager session = new SessionManager(context);
                session.createCitizen(name,mobile,false);

                Intent i = new Intent(getApplicationContext(), CitizenHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else {
                String title="Unable to register";
                new AlertDialog.Builder(LoginCitizenActivity.this)
                        .setTitle(title)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(true)
                        .show();
            }
        }
    }
}
