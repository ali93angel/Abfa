package com.app.leon.abfa.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.leon.abfa.Models.ViewModels.SpinnerDataModel;
import com.app.leon.abfa.R;

import java.util.ArrayList;

/**
 * Created by Leon on 1/19/2018.
 */


public class
SpinnerGisAdapter extends ArrayAdapter<SpinnerDataModel> {
    private final int groupid;
    private final ArrayList<SpinnerDataModel> list;
    private final LayoutInflater inflater;

    public SpinnerGisAdapter(Activity context, int groupid, int id, ArrayList<SpinnerDataModel>
            list) {
        super(context, id, list);
        this.list = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groupid = groupid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = inflater.inflate(groupid, parent, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img);
        imageView.setImageResource(list.get(position).getImageId());
        TextView textView = (TextView) itemView.findViewById(R.id.txt);
        textView.setText(list.get(position).getText());
        return itemView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup
            parent) {
        return getView(position, convertView, parent);

    }
}
