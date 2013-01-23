package com.example.agenda.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;
import android.util.Log;
import com.example.agenda.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maksymilian
 * Date: 19.01.13
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseTable {
    private static final String TAG = "AgendaTable";

    private static final String DATABASE_NAME = "AgendaDB.db";
    private static final String APPOINTMENT_TABLE = "Appointment";
    private static final int DATABASE_VERSION = 1;

    private final DatabaseOpenHelper mDatabaseOpenHelper;

    public DatabaseTable(Context context) {
        this.mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    private Cursor query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(APPOINTMENT_TABLE);

        Cursor cursor = builder.query(mDatabaseOpenHelper.getReadableDatabase(),
                columns, selection, selectionArgs, null, null, null);

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }


    public long addAppointment(Appointment appointment) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Appointment.KEY_DATE, appointment.date);
        initialValues.put(Appointment.KEY_HOUR, appointment.hour);
        initialValues.put(Appointment.KEY_APPOINTMENT_TYPE, appointment.appointmentType);
        initialValues.put(Appointment.KEY_APPOINTMENT_AIM, appointment.aim);
        return mDatabaseOpenHelper.mDatabase.insert(APPOINTMENT_TABLE, null, initialValues);
    }

    public long updateAppointment(Appointment appointment) {
        if (appointment.get_id() == 0) {
            return -1;
        }
        ContentValues initialValues = new ContentValues();
        initialValues.put(Appointment.KEY_DATE, appointment.date);
        initialValues.put(Appointment.KEY_HOUR, appointment.hour);
        initialValues.put(Appointment.KEY_APPOINTMENT_TYPE, appointment.appointmentType);
        initialValues.put(Appointment.KEY_APPOINTMENT_AIM, appointment.aim);
        return mDatabaseOpenHelper.mDatabase.update(APPOINTMENT_TABLE, initialValues, Appointment.KEY_ID + " =? ", new String[]{String.valueOf(appointment.get_id())});
    }

    public void deleteAppointment(Appointment appointment) {
        mDatabaseOpenHelper.mDatabase.delete(APPOINTMENT_TABLE, Appointment.KEY_ID + " =?", new String[]{String.valueOf(appointment.get_id())});
    }

    public ArrayList<Appointment> getAppointmentsByDay(long dayMidnight) {
        ArrayList<Appointment> resultList = new ArrayList<Appointment>();
        Cursor cursor = mDatabaseOpenHelper.mDatabase.query(APPOINTMENT_TABLE, new String[]{Appointment.KEY_ID, Appointment.KEY_DATE, Appointment.KEY_HOUR, Appointment.KEY_APPOINTMENT_TYPE, Appointment.KEY_APPOINTMENT_AIM},
                Appointment.KEY_DATE + "=?", new String[]{String.valueOf(dayMidnight)}, null, null, Appointment.KEY_HOUR);
        if(cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                Appointment appointment = new Appointment(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getLong(3), cursor.getString(4));
                resultList.add(appointment);
            }
        }
        return resultList;
    }

    public List<Appointment> getAppointmentsInRangeDistinctDate(long rangeStart, long rangeEnd) {
        List<Appointment> resultList = new ArrayList<Appointment>();
        String query = "SELECT " + Appointment.KEY_DATE + " as ap_date, " + " COUNT(*) as ap_day_count from " + APPOINTMENT_TABLE + " o WHERE ap_date BETWEEN  ? AND ? GROUP BY  ap_date";
        Cursor cursor = mDatabaseOpenHelper.mDatabase.rawQuery(query, new String[]{String.valueOf(rangeStart), String.valueOf(rangeEnd)});
        if(cursor != null && cursor.moveToFirst()) {
            while(cursor.moveToNext()) {
                resultList.add(new Appointment(cursor.getLong(0), 0, 0, null));
            }
        }
        return resultList;
    }

    private class DatabaseOpenHelper extends SQLiteOpenHelper {

        Context mHelperContext;
        SQLiteDatabase mDatabase;

        private static final String FTS_TABLE_CREATE = "CREATE TABLE " + APPOINTMENT_TABLE + "  ( " +
                Appointment.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Appointment.KEY_DATE + " INTEGER, " +
                Appointment.KEY_HOUR + " INTEGER, " + Appointment.KEY_APPOINTMENT_TYPE + " INTEGER, " + Appointment.KEY_APPOINTMENT_AIM + " TEXT )";

        DatabaseOpenHelper(Context context) {
            super(context, DATABASE_NAME, null , DATABASE_VERSION);
            mHelperContext = context;
            mDatabase = getWritableDatabase();
        }

        private void loadDictionary() {
/*            new Thread(new Runnable() {
                @Override
                public void run() {*/
            try {
                loadWords();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
/*                }
            }).start();*/
        }

        private void loadWords() throws IOException {
            final Resources resources = mHelperContext.getResources();
            InputStream inputStream = resources.openRawResource(R.raw.definitions);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] strings = TextUtils.split(line, "-");
                    if (strings.length < 4) {
                        continue;
                    }
                    Appointment newAppointment = new Appointment(Long.parseLong(strings[0].trim()), Long.parseLong(strings[1].trim()),
                            Long.parseLong(strings[2].trim()), strings[3].trim());
                    long id = addAppointmentInit(newAppointment);
                    if (id < 0) {
                        Log.e(TAG, "unable to add word: " + strings[0].trim());
                    }
                }
            } finally {
                reader.close();
            }
        }

        private long addAppointmentInit(Appointment appointment) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(Appointment.KEY_DATE, appointment.date);
            initialValues.put(Appointment.KEY_HOUR, appointment.hour);
            initialValues.put(Appointment.KEY_APPOINTMENT_TYPE, appointment.appointmentType);
            initialValues.put(Appointment.KEY_APPOINTMENT_AIM, appointment.aim);
            return mDatabase.insert(APPOINTMENT_TABLE, null, initialValues);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            mDatabase = db;
            /*begin transction endTransaction setTransactionSuccessful*/
            mDatabase.execSQL(FTS_TABLE_CREATE);
            loadDictionary();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version  " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + APPOINTMENT_TABLE);
            onCreate(db);
        }
    }
}
