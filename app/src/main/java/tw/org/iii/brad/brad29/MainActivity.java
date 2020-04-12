package tw.org.iii.brad.brad29;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ContentResolver cr;
    private Uri uri = Settings.System.CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_EXTERNAL_STORAGE,},
                   123);


        }else{
            init();
        }


    }

    private void init(){
        cr = getContentResolver();
        // content://Settings 設定資料
        // content://ContactsContract 聯絡人
        // content://Calllog
        // content://MediaStore
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    public void test1(View view) {
        // select * from Settings
        Cursor c = cr.query(uri, null, null, null, null);

//        for (int i=0; i< c.getColumnCount(); i++){
//            String field = c.getColumnName(i);
//            Log.v("brad", field);
//        }

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex("name"));
            String value = c.getString(c.getColumnIndex("value"));
            Log.v("brad", name + ":" + value);
        }
        c.close();
    }

    public void test2(View view){
//        Cursor c = cr.query(uri, null,
//                "name = ?",
//                new String[]{Settings.System.SCREEN_BRIGHTNESS},
//                null);
//        c.moveToNext();
//        String v = c.getString(c.getColumnIndex("value"));
//        Log.v("brad", "ｖ = " + v);
//        c.close();

        try {
            int v = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
            Log.v("brad", "ｖ = " + v);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void test3(View view) {
        uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);

//        for (int i=0; i< c.getColumnCount(); i++){
//            String field = c.getColumnName(i);
//            Log.v("brad", field);
//        }

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex("display_name"));
            Log.v("brad", name );
        }
        c.close();
    }
    public void test4(View view) {
        uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor c = cr.query(uri, null, null, null, null);

//        for (int i=0; i< c.getColumnCount(); i++){
//            String field = c.getColumnName(i);
//            Log.v("brad", field);
//        }

        while (c.moveToNext()){
            String name =
                    c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone =
                    c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.v("brad", name +":" + phone);
        }
        Log.v("brad", "count:" + c.getCount());
        c.close();
    }

    public void test5(View view){
        // Calllog.Calls.CONTENT_URI
        uri = CallLog.Calls.CONTENT_URI;
        // CallLog.Calls.CACHED_NAME
        // CallLog.Calls.NUMBER
        // CallLog.Calls.TYPE => INCOMING, OUTGOING, MISSED
        // CallLog.Calls.DATE
        // CallLog.Calls.DURATION ==> sec
        Cursor c = cr.query(uri, null, null, null, null);

        while (c.moveToNext()){
            String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String num = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
            String type = "";
            if (CallLog.Calls.INCOMING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "in";
            }else if(CallLog.Calls.OUTGOING_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "out";
            }else if (CallLog.Calls.MISSED_TYPE == c.getInt(c.getColumnIndex(CallLog.Calls.TYPE))){
                type = "miss";
            }

            Log.v("brad", type + ":" + name + ":" + num);
        }

    }

}
