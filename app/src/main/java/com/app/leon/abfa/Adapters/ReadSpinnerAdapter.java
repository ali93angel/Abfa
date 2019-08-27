package com.app.leon.abfa.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.leon.abfa.R;

import java.util.List;

/**
 * Created by Leon on 2/17/2018.
 */

public class ReadSpinnerAdapter extends ArrayAdapter<RowItem> {
    LayoutInflater flater;
    Context context;

    public ReadSpinnerAdapter(Activity context, int resourceId, int textViewId, List<RowItem> list) {
        super(context, resourceId, textViewId, list);
        this.context = context;
        flater = context.getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RowItem rowItem = getItem(position);
        View rowView = flater.inflate(R.layout.item_read_spinner, null, true);
        TextView textViewItem = rowView.findViewById(R.id.title);
        textViewItem.setText(rowItem.getTitle());
        Typeface externalFont = Typeface.createFromAsset(context.getAssets(), "font/BYekan_3.ttf");
        textViewItem.setTypeface(externalFont);
        textViewItem.setTextSize(context.getResources().getDimension(R.dimen.textSizeSmall));
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View rowView = super.getDropDownView(position, convertView, parent);
//        Display display = Activity.getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        ((TextView) rowView).setWidth(4 * size.x / 5);
        return rowView(convertView, position);
    }

    private View rowView(View convertView, int position) {
        RowItem rowItem = getItem(position);
        viewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            holder = new viewHolder();
            flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = flater.inflate(R.layout.item_read_spinner, null, false);
            holder.textViewTitle = rowView.findViewById(R.id.title);
            Typeface externalFont = Typeface.createFromAsset(context.getAssets(), "font/BYekan_3.ttf");
            holder.textViewTitle.setTypeface(externalFont);
            holder.textViewTitle.setTextSize(context.getResources().getDimension(R.dimen.textSizeSmall));
            rowView.setTag(holder);
        } else {
            holder = (viewHolder) rowView.getTag();
        }
        holder.textViewTitle.setText(rowItem.getTitle());
        return rowView;
    }

    private class viewHolder {
        TextView textViewTitle;
    }
}

