package com.example.ageless_v1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "reminders.db";
    private static final int DATABASE_VERSION = 1;

    public ReminderDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the reminders table
        db.execSQL("CREATE TABLE reminders ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT, "
                + "time TEXT, "
                + "days TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not needed in this example
    }
}
