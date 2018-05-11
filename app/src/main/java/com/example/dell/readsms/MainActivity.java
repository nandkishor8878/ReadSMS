package com.example.dell.readsms;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    SMS_DATABASE sms_db_controllar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView =(ListView) findViewById(R.id.listView);
        sms_db_controllar=new SMS_DATABASE(this,"",null,1);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.refresh_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scaninbox:
                ReadAllMessgae();
                String[] massagearray=sms_db_controllar.List_All_Message();
                ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, massagearray);
                listView.setAdapter(adapter);
                return true;
            case R.id.deleteinbox:
                Toast.makeText(MainActivity.this,"Database Deleted.....Please refresh",Toast.LENGTH_SHORT).show();
                sms_db_controllar.DeleteAll();
                String[] marray={};
                ArrayAdapter<String> adapt= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, marray);
                listView.setAdapter(adapt);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    // this will scan each message for some keywords and return true of matched
    public boolean scan(String s){
     //write code here
        String[] mykeywords={"OTP","PNR","आधार","Username"};
        String [] keywords = s.split(" ");
        Map<String,Integer>MapKeywords= new HashMap<>();
        for (int i=0;i<mykeywords.length;i++){
            MapKeywords.put(mykeywords[i],1);
        }
        int i_kw=0;
        while (i_kw<keywords.length) {
            if(MapKeywords.containsKey(keywords[i_kw])){
                return true;
            }
            i_kw++;
        }
        return false;
    }
    public void ReadAllMessgae(){
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor=getContentResolver().query(uri,null,null,null,null);
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                //An array that stores body and address of each message
                String smsbody = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String smsaddress = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                if (scan(smsbody)) {
                    sms_db_controllar.insert_SMS(smsbody, smsaddress);
                }
            }
        }
        else{
            Toast.makeText(MainActivity.this,"Inbox is empty",Toast.LENGTH_SHORT).show();
        }
    }
    public void Drop(){

    }
}