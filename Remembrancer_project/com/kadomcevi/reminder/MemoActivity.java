package com.kadomcevi.reminder;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MemoActivity extends ThemedActivity implements OnClickListener {
    private static final int DATE_DIALOG = 2;
    private static final int TIME_DIALOG = 1;
    int f4D;
    int f5M;
    int f6Y;
    Button btnChangeTheme;
    ImageButton btnDate;
    Button btnOK;
    ImageButton btnTime;
    DB db;
    OnDateSetListener dtCallBack;
    EditText etDate;
    EditText etMemo;
    EditText etTime;
    int hh;
    int mm;
    OnTimeSetListener tmCallBack;

    /* renamed from: com.kadomcevi.reminder.MemoActivity.1 */
    class C00611 implements OnTimeSetListener {
        C00611() {
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            MemoActivity.this.hh = hourOfDay;
            MemoActivity.this.mm = minute;
            EditText editText = MemoActivity.this.etTime;
            Object[] objArr = new Object[MemoActivity.DATE_DIALOG];
            objArr[0] = Integer.valueOf(MemoActivity.this.hh);
            objArr[MemoActivity.TIME_DIALOG] = Integer.valueOf(MemoActivity.this.mm);
            editText.setText(String.format("%02d%02d", objArr));
        }
    }

    /* renamed from: com.kadomcevi.reminder.MemoActivity.2 */
    class C00622 implements OnDateSetListener {
        C00622() {
        }

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            MemoActivity.this.f6Y = year - 2000;
            MemoActivity.this.f5M = monthOfYear + MemoActivity.TIME_DIALOG;
            MemoActivity.this.f4D = dayOfMonth;
            MemoActivity.this.etDate.setText(String.format("%02d%02d%02d", new Object[]{Integer.valueOf(MemoActivity.this.f4D), Integer.valueOf(MemoActivity.this.f5M), Integer.valueOf(MemoActivity.this.f6Y)}));
        }
    }

    public MemoActivity() {
        this.hh = 10;
        this.mm = 0;
        this.f6Y = 2014;
        this.f5M = 0;
        this.f4D = TIME_DIALOG;
        this.tmCallBack = new C00611();
        this.dtCallBack = new C00622();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0064R.layout.memo);
        this.etMemo = (EditText) findViewById(C0064R.id.etMemo);
        this.etDate = (EditText) findViewById(C0064R.id.etDate);
        this.etTime = (EditText) findViewById(C0064R.id.etTime);
        this.btnOK = (Button) findViewById(C0064R.id.btnOK);
        this.btnOK.setOnClickListener(this);
        this.btnTime = (ImageButton) findViewById(C0064R.id.btnTime);
        this.btnTime.setOnClickListener(this);
        this.btnDate = (ImageButton) findViewById(C0064R.id.btnDate);
        this.btnDate.setOnClickListener(this);
        this.db = new DB(this);
        this.db.open();
        Intent intent = getIntent();
        if (intent.hasExtra(DB.COLUMN_ID)) {
            long id = intent.getLongExtra(DB.COLUMN_ID, 0);
            this.etMemo.setText(this.db.getMemo(id));
            Date dt = new Date(this.db.getDateTime(id));
            DateFormat df = new SimpleDateFormat("ddMMyy");
            DateFormat tf = new SimpleDateFormat("HHmm");
            String dateS = df.format(dt);
            String timeS = tf.format(dt);
            this.etDate.setText(dateS);
            long l = Long.parseLong(dateS);
            this.f6Y = ((int) (l % 100)) + 2000;
            this.f5M = ((int) ((l / 100) % 100)) - 1;
            this.f4D = (int) ((l / 10000) % 100);
            this.etTime.setText(timeS);
            l = Long.parseLong(timeS);
            this.hh = ((int) l) / 100;
            this.mm = ((int) l) % 100;
        }
    }

    public void onClick(View v) {
        Calendar calendar;
        Intent curintent = getIntent();
        if (v.getId() == C0064R.id.btnOK) {
            Intent intent = new Intent();
            if (curintent.hasExtra(DB.COLUMN_ID)) {
                intent.putExtra(DB.COLUMN_ID, Long.valueOf(curintent.getLongExtra(DB.COLUMN_ID, 0)));
            }
            intent.putExtra("memo", this.etMemo.getText().toString());
            String dateS = this.etDate.getText().toString();
            String timeS = this.etTime.getText().toString();
            try {
                java.util.Date datetime = new SimpleDateFormat("ddMMyy HHmm").parse(new StringBuilder(String.valueOf(dateS)).append(" ").append(timeS).toString());
                intent.putExtra("datetime", new StringBuilder(String.valueOf(dateS)).append(" ").append(timeS).toString());
                setResult(-1, intent);
                finish();
            } catch (ParseException e) {
                setResult(0, intent);
                finish();
                return;
            } catch (NumberFormatException e2) {
                setResult(0, intent);
                finish();
                return;
            }
        }
        long l;
        if (v.getId() == C0064R.id.btnTime) {
            try {
                l = Long.parseLong(this.etTime.getText().toString());
                this.hh = ((int) l) / 100;
                this.mm = ((int) l) % 100;
            } catch (NumberFormatException e3) {
                calendar = Calendar.getInstance();
                this.hh = calendar.get(11);
                this.mm = calendar.get(12);
            }
            showDialog(TIME_DIALOG);
        } else if (v.getId() == C0064R.id.btnDate) {
            try {
                l = Long.parseLong(this.etDate.getText().toString());
                this.f6Y = ((int) (l % 100)) + 2000;
                this.f5M = ((int) ((l / 100) % 100)) - 1;
                this.f4D = (int) ((l / 10000) % 100);
            } catch (NumberFormatException e4) {
                calendar = Calendar.getInstance();
                this.f6Y = calendar.get(TIME_DIALOG);
                this.f5M = calendar.get(DATE_DIALOG);
                this.f4D = calendar.get(5);
            }
            showDialog(DATE_DIALOG);
        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == TIME_DIALOG) {
            Log.d("OnCreate Dialog", "TIME");
            return new TimePickerDialog(this, this.tmCallBack, this.hh, this.mm, true);
        } else if (id != DATE_DIALOG) {
            return super.onCreateDialog(id);
        } else {
            Log.d("OnCreate Dialog", "DATE");
            return new DatePickerDialog(this, this.dtCallBack, this.f6Y, this.f5M, this.f4D);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        this.db.close();
    }
}
