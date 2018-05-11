package com.example.dell.readsms;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class SMS_DATABASE extends SQLiteOpenHelper {
    public SMS_DATABASE(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "SmsDatabase.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE SMS(SMSID INTEGER PRIMARY KEY AUTOINCREMENT , SMSBODY TEXT ,ADDRESS TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SMS");
        onCreate(db);
    }
    public void insert_SMS(String smsbody,String address){
        ContentValues contentValues=new ContentValues();
        contentValues.put("SMSBODY",smsbody);
        contentValues.put("ADDRESS",address);
        this.getWritableDatabase().insertOrThrow("SMS","",contentValues);
    }
    public String[] List_All_Message(){
        List<String> stringList=new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor =this.getReadableDatabase().rawQuery("SELECT * FROM SMS",null);
            while (cursor.moveToNext()) {
                stringList.add(cursor.getString(2) + " =>   " + cursor.getString(1)+"\n");
            }
        String[] temp=new String[stringList.size()];
            for(int i=0;i<stringList.size();i++){
                temp[i]=stringList.get(i);
            }
       return temp;
    }
    public void DeleteAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM SMS");
        db.close();
    }
}
