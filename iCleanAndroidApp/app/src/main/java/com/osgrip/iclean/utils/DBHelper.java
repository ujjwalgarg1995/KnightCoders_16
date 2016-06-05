package com.osgrip.iclean.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.osgrip.iclean.Modals.Complains;
import com.osgrip.iclean.Modals.Pending;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranjal on 11-Jan-16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION  = 3;
    private static final String DATABASE_NAME = "swachhAppDemo.db";

    private static final String TABLE_COMPLAINS = "complains";
    private static final String COMPLAINS_COL_ID = "_id";
    private static final String COMPLAINS_COL_COMPLAIN = "complain";
    private static final String COMPLAINS_COL_COMPLAIN_ID = "complainId";
    private static final String COMPLAINS_COL_ADDRESS = "address";
    private static final String COMPLAINS_COL_TIME = "time";
    private static final String COMPLAINS_COL_WARD = "ward";
    private static final String COMPLAINS_COL_IMAGE = "image";
    private static final String COMPLAINS_COL_STATUS = "status";
    private static final String COMPLAINS_COL_COMMENT = "comment";

    private static final String TABLE_PENDING = "pending";
    private static final String PENDING_COL_ID = "_id";
    private static final String PENDING_COL_COMPLAIN = "complain";
    //private static final String PENDING_COL_COMPLAIN_ID = "complainId";
    private static final String PENDING_COL_ADDRESS = "address";
    private static final String PENDING_COL_TIME = "time";
    private static final String PENDING_COL_WARD = "ward";
    private static final String PENDING_COL_IMAGE = "image";
    private static final String PENDING_COL_IMAGE_PATH = "imgPath";

    //private static final String PENDING_COL_STATUS = "status";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_COMPLAINS + " (" +
                COMPLAINS_COL_ID + " INTEGER PRIMARY KEY," +
                COMPLAINS_COL_COMPLAIN + " TEXT, " +
                COMPLAINS_COL_COMPLAIN_ID + " TEXT, " +
                COMPLAINS_COL_ADDRESS + " TEXT, " +
                COMPLAINS_COL_TIME + " TEXT, " +
                COMPLAINS_COL_WARD + " TEXT, " +
                COMPLAINS_COL_IMAGE + " TEXT, " +
                COMPLAINS_COL_STATUS + " TEXT, " +
                COMPLAINS_COL_COMMENT + " TEXT " +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PENDING + " (" +
                PENDING_COL_ID + " INTEGER PRIMARY KEY," +
                PENDING_COL_COMPLAIN + " TEXT, " +
                PENDING_COL_ADDRESS + " TEXT, " +
                PENDING_COL_TIME + " TEXT, " +
                PENDING_COL_WARD + " TEXT, " +
                PENDING_COL_IMAGE + " TEXT, " +
                PENDING_COL_IMAGE_PATH + " TEXT " +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPLAINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDING);
        onCreate(db);
    }

    public boolean insertIntoComplains(Complains c){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMPLAINS_COL_COMPLAIN,c.getComplain());
        values.put(COMPLAINS_COL_COMPLAIN_ID,c.getComplain_id());
        values.put(COMPLAINS_COL_ADDRESS,c.getAddress());
        values.put(COMPLAINS_COL_TIME,c.getTime());
        values.put(COMPLAINS_COL_WARD,c.getAreaCode());
        values.put(COMPLAINS_COL_IMAGE,c.getImage());
        values.put(COMPLAINS_COL_STATUS,c.getStatus());
        values.put(COMPLAINS_COL_COMMENT,c.getComment());
        long result = db.insert(TABLE_COMPLAINS, null, values);
        db.close();
        return result != -1;
    }

    public boolean insertIntoPending(Pending c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PENDING_COL_COMPLAIN,c.getComplain());
        values.put(PENDING_COL_ADDRESS,c.getAddress());
        values.put(PENDING_COL_TIME,c.getTime());
        values.put(PENDING_COL_WARD,c.getWard());
        values.put(PENDING_COL_IMAGE,c.getImage());
        values.put(PENDING_COL_IMAGE_PATH,c.getImgPath());
        long result = db.insert(TABLE_PENDING, null, values);
        db.close();
        return result != -1;
    }

    public List<Complains> getComplains() {
        List<Complains> complainsList = new ArrayList<Complains>();
        Complains complains;
        String countQuery = "SELECT  * FROM " + TABLE_COMPLAINS + " ORDER BY " + COMPLAINS_COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            do {
                complains=new Complains(cursor.getInt(0),cursor.getString(1),cursor.getString(2),"",cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),0,cursor.getString(3));
                complainsList.add(complains);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return complainsList;
    }

    public List<Pending> getPending() {
        List<Pending> pendingList = new ArrayList<Pending>();
        Pending pending;
        String countQuery = "SELECT  * FROM " + TABLE_PENDING + " ORDER BY " + PENDING_COL_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor.moveToFirst()) {
            do {
                pending=new Pending(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                pendingList.add(pending);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pendingList;
    }

    public void updateComplain(String[] id, String status[], String comment[]) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i =0; i<id.length;i++){
            ContentValues values = new ContentValues();
            values.put(COMPLAINS_COL_STATUS, status[i]);
            values.put(COMPLAINS_COL_COMMENT, comment[i]);
            db.update(TABLE_COMPLAINS,values,COMPLAINS_COL_COMPLAIN_ID+" = "+id[i],null);
        }
    }
    public void deleteFromPending(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_PENDING+" WHERE " + PENDING_COL_ID + " = " + id);
    }
}
