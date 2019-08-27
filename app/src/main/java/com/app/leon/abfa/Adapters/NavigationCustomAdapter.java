package com.app.leon.abfa.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.app.leon.abfa.R;

import java.util.List;

/**
 * Created by Leon on 12/4/2017.
 */

public class NavigationCustomAdapter extends ArrayAdapter<NavigationCustomAdapter.DrawerItem> {
    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public NavigationCustomAdapter(Context context, int layoutResourceID,
                                   List<DrawerItem> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public DrawerItem getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPosition(DrawerItem item) {
        return super.getPosition(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        DrawerItemHolder drawerHolder;
        convertView = null;
        DrawerItem dItem;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        if (position == 0) {
            drawerHolder = new DrawerItemHolder();
            convertView = inflater.inflate(R.layout.item_navigation_, parent, false);
            drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            dItem = this.drawerItemList.get(position);
            drawerHolder.imageViewIcon.setImageDrawable(convertView.getResources().getDrawable(
                    dItem.getImgResID()));
//            drawerHolder.linear = (LinearLayout) convertView.findViewById(R.id.linear);
//            drawerHolder.linear.setY(75);
        }
//        if (position==5){
//            drawerHolder = new DrawerItemHolder();
//            convertView = inflater.inflate(R.layout.item_navigation__, parent, false);
//            drawerHolder.textViewTitle = (TextView) convertView
//                    .findViewById(R.id.textViewTitle);
//            drawerHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
//            dItem = (DrawerItem) this.drawerItemList.get(position);
//            drawerHolder.imageViewIcon.setImageDrawable(convertView.getResources().getDrawable(
//                    dItem.getImgResID()));
//            drawerHolder.textViewTitle.setText(dItem.getItemName());
//        }
        else {
            drawerHolder = new DrawerItemHolder();
            convertView = inflater.inflate(layoutResID, parent, false);
            if (position == 5)
                convertView = inflater.inflate(R.layout.item_navigation__, parent, false);
            drawerHolder.textViewTitle = convertView
                    .findViewById(R.id.textViewTitle);
            if (position == 9)
                drawerHolder.textViewTitle.setTextColor(context.getResources().getColor(R.color.red4));
            drawerHolder.imageViewIcon = convertView.findViewById(R.id.imageViewIcon);
            dItem = this.drawerItemList.get(position);
            drawerHolder.imageViewIcon.setImageDrawable(convertView.getResources().getDrawable(
                    dItem.getImgResID()));
            drawerHolder.textViewTitle.setText(dItem.getItemName());
        }
        convertView.setTag(drawerHolder);
        return convertView;
    }

    private static class DrawerItemHolder {
        TextView textViewTitle;
        ImageView imageViewIcon, imageViewSeperator;
        LinearLayout linear;
    }

    public static class DrawerItem {

        String ItemName;
        int imgResID;

        public DrawerItem(String itemName, int imgResID) {
            super();
            ItemName = itemName;
            this.imgResID = imgResID;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public int getImgResID() {
            return imgResID;
        }

        public void setImgResID(int imgResID) {
            this.imgResID = imgResID;
        }

    }
}
