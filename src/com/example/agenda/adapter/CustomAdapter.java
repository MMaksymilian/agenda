package com.example.agenda.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.agenda.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Maksymilian
 * Date: 22.01.13
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class CustomAdapter extends BaseAdapter {

    private List<Calendar> dates = new ArrayList<Calendar>();
    private Context context;
    private DateFormat dateFormat;

    public CustomAdapter(Context ctx, List<Calendar> dts, DateFormat df) {
        super();
        context = ctx;
        dates = dts;
        dateFormat = df;
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dates.get(position).getTimeInMillis();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if(convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate(R.layout.dates, null);
            TextView textView = (TextView) gridView.findViewById(R.id.dateString);
            String dateString = dateFormat.format(dates.get(position).getTime());
            textView.setText(dateString);

        } else {
            gridView = (View) convertView;
        }
        return gridView;
    }
}
