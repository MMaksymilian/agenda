package com.example.agenda;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.*;
import com.example.agenda.adapter.CustomAdapter;
import com.example.agenda.db.Appointment;
import com.example.agenda.db.DatabaseTable;
import com.exina.android.calendar.CalendarView;
import com.exina.android.calendar.Cell;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity implements CalendarView.OnCellTouchListener {  // SearchView.OnQueryTextListener,

    DatabaseTable db;
    public static final String CUSTOM_ACTION = "com.example.agenda.PERFORM_SEARCH";
    public static final String MIME_TYPE = "vnd.android.cursor.dir/vnd.exina.android.calendar.date";
    public static final String QUERY_DATE = "com.example.agenda.query_date";
    public static final String CURR_APP_LIST = "com.example.agenda.current_apointment_list";
    public static final int REQUEST_CODE_EDIT = 121;
    CalendarView mView = null;
    TextView mHit;
    private GridView gridView;
    Locale locale = new Locale("pl", "POL");
    private DateFormat df = new SimpleDateFormat("MMM yyyy", locale);
    private List<Calendar> availableMonths;

    {
        availableMonths = new ArrayList<Calendar>();
        Calendar now = new GregorianCalendar();
        /*najstarszy dzieñ w liscie dostépnych z zapytania*/
        for (int i = 0; i < 13; i++) {
            Calendar calenderQueryOption = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
            calenderQueryOption.add(Calendar.MONTH, i);
            availableMonths.add(calenderQueryOption);
        }
    }

    @Override
    protected void onStart() {
        super.onResume();
        db = new DatabaseTable(this);
        if (CUSTOM_ACTION.equals(getIntent().getAction())) {
            Calendar calendar = (Calendar)getIntent().getSerializableExtra(QUERY_DATE);
            Calendar nextMonthCalendar = Calendar.getInstance();
            nextMonthCalendar.setTime(calendar.getTime());
            nextMonthCalendar.add(Calendar.MONTH, 1);
            List<Appointment> list = db.getAppointmentsInRangeDistinctDate(calendar.getTimeInMillis(), nextMonthCalendar.getTimeInMillis());
            StringBuilder builder = new StringBuilder();
            for(Appointment appointment : list) {
                builder.append("data: " + new Date(appointment.getDate()) + " ");
            }
            Toast.makeText(this , builder, Toast.LENGTH_LONG).show();

        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ActionBar actionBar = getActionBar();
        actionBar.show();
/*
        handleIntent(getIntent());
*/
        mView = (CalendarView) findViewById(R.id.calendar);
        mView.setOnCellTouchListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new CustomAdapter(this, availableMonths, df));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setAction(CUSTOM_ACTION);
                intent.putExtra(QUERY_DATE, (Calendar) parent.getAdapter().getItem(position));
                startActivity(intent);
            }
        });

/*        if(getIntent().getAction().equals(Intent.ACTION_PICK))
            findViewById(R.id.hint).setVisibility(View.INVISIBLE);*/

/*        onActivityResult();
        startActivity(new Intent(Intent.ACTION_VIEW).setDataAndType(null, CalendarActivity.MIME_TYPE));*/
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }*/

/*    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
*//*
            Cursor c = db.getWordMatches(query, null);
*//*
            *//*TODO co robi się przy zapytaniu*//*
        }
    }*/

    public void onTouch(Cell cell) {
/*        Intent intent = getIntent();
        String action = intent.getAction();*/
/*        if(Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {*/
        int year = mView.getYear();
        int month = mView.getMonth();
        int day = cell.getDayOfMonth();

        // FIX issue 6: make some correction on month and year
        if (cell instanceof CalendarView.GrayCell) {
            // oops, not pick current month...
            if (day < 15) {
                // pick one beginning day? then a next month day
                if (month == 11) {
                    month = 0;
                    year++;
                } else {
                    month++;
                }

            } else {
                // otherwise, previous month
                if (month == 0) {
                    month = 11;
                    year--;
                } else {
                    month--;
                }
            }
        }

        Intent intent = new Intent(MainActivity.this, EditActivity.class);
        Calendar markedDate = new GregorianCalendar(year, month, day);
/*        ArrayList<Appointment> currentAppointmentList = db.getAppointmentsByDay(markedDate.getTimeInMillis());
        intent.putExtra(CURR_APP_LIST, currentAppointmentList);
        startActivityForResult(intent, REQUEST_CODE_EDIT);*/
        Toast.makeText(this, "you've marked the " + day + "th", Toast.LENGTH_LONG).show();
/*            this.setResult(RESULT_OK, ret);
            finish();*/
        return;
/*        }*/
/*        int day = cell.getDayOfMonth();
        if(mView.firstDay(day))
            mView.previousMonth();
        else if(mView.lastDay(day))
            mView.nextMonth();
        else
            return;

        mHandler.post(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, DateUtils.getMonthString(mView.getMonth(), DateUtils.LENGTH_LONG) + " " + mView.getYear(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }


/*    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {

        }
    }
}
