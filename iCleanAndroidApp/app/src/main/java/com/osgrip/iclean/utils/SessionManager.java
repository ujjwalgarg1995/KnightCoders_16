package com.osgrip.iclean.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pranjal on 06-Jan-16.
 */
public class SessionManager {
    private static final String VOL_ID = "volid";
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "SwachhAppDemoPref";
    private static final String IS_NEW = "new";
    private static final String NAME = "name";
    private static final String MOBILE_NO = "mobile_no";
    private static final String TYPE = "type";
    public static final int CITIZEN = 1;
    public static final int VOLUNTEER = 2;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getMobileNumber(){
        return pref.getString(MOBILE_NO,"");
    }
    public void createCitizen(String name, String mno, boolean is_new){
        // Storing login value as TRUE
        editor.putString(NAME,name);
        editor.putString(MOBILE_NO, mno);
        editor.putInt(TYPE, CITIZEN);
        editor.putBoolean(IS_NEW, is_new);

        // Storing name in pref

        // commit changes
        editor.commit();
    }

    public int getType() {
        return pref.getInt(TYPE,0);
    }

    public void createVolunteer(String username, String password,String volid, boolean is_new){
        // Storing login value as TRUE
        editor.putString(NAME,username);
        editor.putString(MOBILE_NO, password);
        editor.putBoolean(IS_NEW, is_new);
        editor.putString(VOL_ID,volid);
        editor.putInt(TYPE, VOLUNTEER);

        // Storing name in pref

        // commit changes
        editor.commit();
    }

    public boolean isNew(){
        return pref.getBoolean(IS_NEW, true);
    }

    public String getVolid() {
        return pref.getString(VOL_ID,null);
    }
}
