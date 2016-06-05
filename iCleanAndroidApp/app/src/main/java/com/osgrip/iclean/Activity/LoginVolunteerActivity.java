package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

public class LoginVolunteerActivity extends AppCompatActivity {


    EditText usernameInput, passwordInput;
    Spinner jobSpinner;
    Button continueButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_login);
        context = this;
        usernameInput = (EditText) findViewById(R.id.mobileNumberInput);
        passwordInput = (EditText) findViewById(R.id.nameInput);
        continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        String password = passwordInput.getText().toString();
        String username = usernameInput.getText().toString();
        String errorMsg = "";
        View v = null;
        if (password.length() < 3) {
            errorMsg = "Enter a valid password";
            v = passwordInput;
        } else if (username.length() == 0) {
            errorMsg = "Mobile Number cannot be empty";
            v = usernameInput;
        } else if (!(username.charAt(0) == '7' || username.charAt(0) == '8' || username.charAt(0) == '9')) {
            errorMsg = "Mobile number must begin with 7 or 8 or 9";
            v = usernameInput;
        } else if (username.length() != 10) {
            errorMsg = "Mobile Number must be of 10 digit";
            v = usernameInput;
        }else {
            if (ServiceCall.isNetworkAvailable(this)) {
                new RegOnServer().execute(password, username);
            } else {
                ErrorDialog.displayError(this, "Error", "Not connected to the Internet, please try again later", null);
            }
        }
        if (errorMsg != "") {
            final View finalV = v;
            new AlertDialog.Builder(context)
                    .setMessage(errorMsg)
                    .setTitle("Error")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (finalV != null)
                                finalV.requestFocus();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }
    }


    private class RegOnServer extends AsyncTask<String, Void, String[]> {
        private ProgressDialog pd = new ProgressDialog(LoginVolunteerActivity.this);
        private String password, username;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Login, please wait...");
            pd.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String result[] = new String[2];
            password = params[0];
            username = params[1];
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("password", password));
            param.add(new BasicNameValuePair("username", username));
            String jsonString = new ServiceCall().makeServiceCall(Config.LOGIN_VOLUNTEER_URL, ServiceCall.POST, param);
            Log.d("Volunteer Request", jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                if (error) {
                    result[0] = "false";
                } else {
                    result[0] = "true";
                    result[1] = "volid";
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
            if (s[0].equals("true")) {
                final SessionManager session = new SessionManager(context);
                session.createVolunteer(username,password, s[1],false);
                Intent i = new Intent(getApplicationContext(), VolunteerHomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else {
                String title = "Unable to login";
                new AlertDialog.Builder(LoginVolunteerActivity.this)
                        .setMessage(s[1])
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
