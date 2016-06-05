package com.osgrip.iclean.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;

import com.osgrip.iclean.Adapter.MyComplainAdapter;
import com.osgrip.iclean.Config;
import com.osgrip.iclean.Dialog.ErrorDialog;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.DBHelper;
import com.osgrip.iclean.utils.ServiceCall;
import com.osgrip.iclean.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyComplaintActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefreshLayout;
    private MyComplainAdapter adapter;
    private RecyclerView recyclerView;
    private List<Complains> complains = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_complaint);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='white'>My </font><font color='#0059CA'>Complaints</font>"));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new MyComplainAdapter(complains);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        refreshList();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void refreshList(){
        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
        DBHelper db = new DBHelper(this);
        complains = db.getComplains();
        adapter = new MyComplainAdapter(complains);
        recyclerView.setAdapter(adapter);
        if(complains.size()!=0) {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setBackgroundResource(R.drawable.empty);
        }
    }

    @Override
    public void onRefresh() {
        if(isNetworkAvailable()){
            new RefreshComplain().execute();
        } else {
            ErrorDialog.displayError(this,"Error","No Internet connection available",null);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class RefreshComplain extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            List<NameValuePair> param = new ArrayList<>();
            SessionManager sessionManager = new SessionManager(MyComplaintActivity.this);
            String mobileNo = sessionManager.getMobileNumber();
            param.add(new BasicNameValuePair("mobileNo", mobileNo));
            String jsonString = new ServiceCall().makeServiceCall(Config.REFRESH_MY_COMPLAIN_URL,ServiceCall.POST,param);
            if(jsonString.equals("no"))
            {
                return null;
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(jsonString);
                    String id[] = new String[jsonArray.length()];
                    String status[] = new String[jsonArray.length()];
                    String comment[] = new String[jsonArray.length()];
                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        id[i] = jsonObject.getString("id");
                        status[i] = jsonObject.getString("status");
                        comment[i] = jsonObject.getString("comment");
                    }
                    DBHelper dbHelper = new DBHelper(MyComplaintActivity.this);
                    dbHelper.updateComplain(id,status,comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            refreshList();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
