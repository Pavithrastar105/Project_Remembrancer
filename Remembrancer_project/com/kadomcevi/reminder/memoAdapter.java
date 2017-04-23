package com.kadomcevi.reminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class memoAdapter extends SimpleCursorAdapter {
    private Context appContext;
    private Cursor cr;
    private final LayoutInflater inflater;
    private int layout;
    private Context mContext;

    public memoAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cr = c;
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return this.inflater.inflate(this.layout, null);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        TextView dtS = (TextView) view.findViewById(C0064R.id.text2);
        TextView txtS = (TextView) view.findViewById(C0064R.id.text1);
        int date_index = cursor.getColumnIndexOrThrow(DB.COLUMN_DATE);
        int text_index = cursor.getColumnIndexOrThrow(DB.COLUMN_TXT);
        dtS.setText(new SimpleDateFormat("dd-MM-yyyy '" + context.getResources().getString(C0064R.string.at) + "' HH:mm:ss").format(new Date(Long.parseLong(cursor.getString(date_index), 10))));
        txtS.setText(cursor.getString(text_index));
    }
}
