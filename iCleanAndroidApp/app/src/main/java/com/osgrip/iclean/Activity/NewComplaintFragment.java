package com.osgrip.iclean.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osgrip.iclean.Adapter.NewComplaintAdapter;
import com.osgrip.iclean.Config;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.R;
import com.osgrip.iclean.utils.ServiceCall;
import com.osgrip.iclean.utils.SessionManager;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewComplaintFragment extends Fragment {

    RecyclerView recyclerView;
    NewComplaintAdapter adapter;
    public NewComplaintFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_new_complaint, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fetchNew();
        return v;
    }

    private void fetchNew() {
        SessionManager sm = new SessionManager(getActivity());
        new FetchNewComplain().execute(sm.getMobileNumber());
    }
    private class FetchNewComplain extends AsyncTask<String, Void, List<Complains>> {
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.setTitle("fetching please wait..");
            pd.show();
        }

        @Override
        protected List<Complains> doInBackground(String... params) {
            String mobile = params[0];
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("mobileNo",mobile));
            List<Complains>  newComplainList = new ArrayList<>();
            String jsonString = new ServiceCall().makeServiceCall(Config.FETCH_NEW_URL, ServiceCall.POST, param);
            Log.d("Fetch Request", jsonString);
            try {
                JSONArray jsonArray =new JSONArray(jsonString);
                for(int i = 0 ;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id");
                    String complain = jsonObject.getString("complain");
                    String address = jsonObject.getString("address");
                    String area_code = jsonObject.getString("area_code");
                    String mobile_no = jsonObject.getString("mobile_no");
                    String photo = jsonObject.getString("photo");
                    String time = jsonObject.getString("time");
                    String status = jsonObject.getString("status");
                    String comment = jsonObject.getString("comment");
                    int volid = jsonObject.getInt("volid");
                    Complains c =new Complains(complain,id+"",mobile_no , time,area_code,photo,status,comment,volid, address);
                    newComplainList.add(c);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return newComplainList;
        }

        @Override
        protected void onPostExecute(final List<Complains> newComplainList) {
            super.onPostExecute(newComplainList);
            if(pd.isShowing())
                pd.dismiss();
            adapter = new NewComplaintAdapter(newComplainList,getActivity());
            adapter.setItemClickListener(new NewComplaintAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    acceptComplain(newComplainList.get(position).getComplain_id());
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    private void acceptComplain(String complainId) {
        new AcceptComplain().execute(complainId);
    }

    private class AcceptComplain extends AsyncTask<String,Void,String[]> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String[] doInBackground(String... params) {
            String complainId=params[0];
            SessionManager sm = new SessionManager(getActivity());
            String volid = sm.getVolid();
            String[] success =new String[1];
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("complain_id",complainId));
            param.add(new BasicNameValuePair("volid",volid));
            String jsonString = new ServiceCall().makeServiceCall(Config.ACCEPT_COMPLAIN_URL,ServiceCall.POST,param);
            Log.d("ACCEPT Request", jsonString);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                boolean error = jsonObject.getBoolean("error");
                if (error) {
                    success[0] = "true";
                } else {
                    success[0] = "false";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(String s[]) {
            super.onPostExecute(s);
            pd.dismiss();
            if (s[0] == "false") {
                new AlertDialog.Builder(getActivity())
                        .setMessage(s[1])
                        .setTitle("Complain Accepted")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(true)
                        .show();
            } else {
                new AlertDialog.Builder(getActivity())
                        .setMessage(s[1])
                        .setTitle("Unable to accept complain try again later")
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
