package com.kadomcevi.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class InfoActivity extends ThemedActivity implements OnClickListener {
    int f1D;
    int f2M;
    int f3Y;
    Button btnOK;
    DB db;
    EditText etInfo;
    int hh;
    private long id;
    int mm;

    public InfoActivity() {
        this.hh = 10;
        this.mm = 0;
        this.f3Y = 2014;
        this.f2M = 0;
        this.f1D = 1;
        this.id = 0;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0064R.layout.info);
        this.etInfo = (EditText) findViewById(C0064R.id.etInfo);
        this.btnOK = (Button) findViewById(C0064R.id.btnOK);
        this.btnOK.setOnClickListener(this);
        this.db = new DB(this);
        this.db.open();
        Intent intent = getIntent();
        if (intent.hasExtra(DB.COLUMN_ID)) {
            this.id = intent.getLongExtra(DB.COLUMN_ID, 0);
            this.etInfo.setText(new StringBuilder(String.valueOf(new SimpleDateFormat("dd-MM-yyyy '" + getString(C0064R.string.at) + "' HH:mm:ss").format(new Date(this.db.getDateTime(this.id))))).append("\n").append(this.db.getMemo(this.id)).toString());
        }
        this.db.close();
    }

    public void onClick(View v) {
        if (v.getId() == C0064R.id.btnOK) {
            AlarmManager manager = (AlarmManager) getSystemService("alarm");
            Intent i = new Intent(this, OnAlarmReceiver.class);
            i.putExtra(DB.COLUMN_ID, this.id);
            manager.cancel(PendingIntent.getBroadcast(this, (int) this.id, i, 134217728));
            Log.d("InfoActivity", "Remove alarm for intent id=" + Long.toString(this.id));
            setResult(-1, new Intent());
            finish();
        }
    }
}
