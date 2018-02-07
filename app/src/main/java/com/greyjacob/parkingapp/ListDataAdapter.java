package com.greyjacob.parkingapp;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyakupoglu on 29-6-2017.
 */

public class ListDataAdapter extends ArrayAdapter {
    List list = new ArrayList();
    public ListDataAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    static class LayoutHandler{
        TextView NAME, PERIOD, TARIFF;
    }
    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        LayoutHandler layoutHandler;
        if (row==null) {
            LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_layout,parent,false);
            layoutHandler = new LayoutHandler();
            layoutHandler.NAME =  row.findViewById(R.id.park_name);
            layoutHandler.PERIOD = row.findViewById(R.id.period);
            layoutHandler.TARIFF = row.findViewById(R.id.tariff);
            row.setTag(layoutHandler);
        } else {
            layoutHandler = (LayoutHandler) row.getTag();
                    }
        DataProvider dataProvider = (DataProvider) this.getItem(position);  //Anlamiyorum Bu gereklimi
        layoutHandler.NAME.setText(dataProvider.getParkingname());
        layoutHandler.PERIOD.setText(dataProvider.getPeriod());
        layoutHandler.TARIFF.setText(dataProvider.getTariff());

        return row;
    }
}
