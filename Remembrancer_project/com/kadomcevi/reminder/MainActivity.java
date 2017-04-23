package com.kadomcevi.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ThemedActivity {
    private static final int CM_DELETE_ID = 1;
    private static final int CM_EDIT_ID = 2;
    private static final String DATEKEY = "datetime";
    private static final String TXTKEY = "memo";
    Cursor cursor;
    DB db;
    PendingIntent intent;
    ListView lvData;
    memoAdapter scAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0064R.layout.activity_main);
        this.db = new DB(this);
        this.db.open();
        this.cursor = this.db.getAllData();
        String[] from = new String[CM_EDIT_ID];
        from[0] = DB.COLUMN_DATE;
        from[CM_DELETE_ID] = DB.COLUMN_TXT;
        this.scAdapter = new memoAdapter(this, C0064R.layout.list_item, this.cursor, from, new int[]{C0064R.id.text2, C0064R.id.text1});
        this.lvData = (ListView) findViewById(C0064R.id.lvMain);
        this.lvData.setAdapter(this.scAdapter);
        registerForContextMenu(this.lvData);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(C0064R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, item.getTitle(), 0).show();
        if (item.getItemId() == C0064R.id.NewMemo) {
            startActivityForResult(new Intent(this, MemoActivity.class), CM_DELETE_ID);
        }
        if (item.getItemId() == C0064R.id.SetOptions) {
            startActivity(new Intent(this, PrefActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        startActivityForResult(new Intent(this, MemoActivity.class), CM_DELETE_ID);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || resultCode != -1) {
            return;
        }
        String memo;
        Date datetime;
        long id;
        Calendar cal;
        if (requestCode == CM_DELETE_ID) {
            memo = data.getStringExtra(TXTKEY);
            try {
                datetime = new SimpleDateFormat("ddMMyy HHmm").parse(data.getStringExtra(DATEKEY));
                id = this.db.addRec(memo, datetime.getTime());
                this.cursor.requery();
                cal = Calendar.getInstance();
                cal.setTime(datetime);
                new MemoManager(this).setReminder(Long.valueOf(id), cal);
                Log.d("NEW ID", Long.toString(id));
            } catch (ParseException e) {
            }
        } else if (requestCode == CM_EDIT_ID) {
            id = data.getLongExtra(DB.COLUMN_ID, 0);
            memo = data.getStringExtra(TXTKEY);
            try {
                datetime = new SimpleDateFormat("ddMMyy HHmm").parse(data.getStringExtra(DATEKEY));
                this.db.updateRec(id, memo, datetime.getTime());
                this.cursor.requery();
                cal = Calendar.getInstance();
                cal.setTime(datetime);
                new MemoManager(this).setReminder(Long.valueOf(id), cal);
                Log.d("UPDATE ID", Long.toString(id));
            } catch (ParseException e2) {
            }
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CM_DELETE_ID, 0, C0064R.string.del_memo);
        menu.add(0, CM_EDIT_ID, 0, C0064R.string.edit_memo);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CM_DELETE_ID) {
            AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
            this.db.delRec(acmi.id);
            this.cursor.requery();
            AlarmManager manager = (AlarmManager) getSystemService("alarm");
            Intent i = new Intent(this, OnAlarmReceiver.class);
            i.putExtra(DB.COLUMN_ID, acmi.id);
            manager.cancel(PendingIntent.getBroadcast(this, (int) acmi.id, i, 134217728));
            Log.d("MainActivity", "Remove alarm for intent id=" + Long.toString(acmi.id));
            return true;
        } else if (item.getItemId() != CM_EDIT_ID) {
            return super.onContextItemSelected(item);
        } else {
            Intent intent = new Intent(this, MemoActivity.class);
            long id = ((AdapterContextMenuInfo) item.getMenuInfo()).id;
            String memo = this.db.getMemo(id);
            long dt = this.db.getDateTime(id);
            intent.putExtra(DB.COLUMN_ID, id);
            startActivityForResult(intent, CM_EDIT_ID);
            return true;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.db.close();
    }
}
