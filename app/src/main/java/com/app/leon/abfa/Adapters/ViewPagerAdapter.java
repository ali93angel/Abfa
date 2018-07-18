package com.app.leon.abfa.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.app.leon.abfa.Fragments.ReadFragment;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;

import java.util.List;

/**
 * Created by Leon on 2/11/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private final List<OnOffLoad> onOffLoads;
    static int position;
    private final Context context;
    List<Integer> spinnerItemSelected;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<OnOffLoad> onOffLoads, List<Integer> spinnerItemSelected) {
        super(fm);
        this.onOffLoads = onOffLoads;
        this.context = context;
        this.spinnerItemSelected = spinnerItemSelected;
    }

    @Override
    public Fragment getItem(int position) {
        return ReadFragment.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
//        if (position % 2 == 0)
//            return ReadFragment.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
//        else
//            return ReadFragment_.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
    }

    public static int getPosition() {
        return position;
    }

    @Override
    public int getCount() {
        return onOffLoads.size();
    }

    @Override
    public int getItemPosition(Object object) {
        notifyDataSetChanged();
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment) object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove((Fragment) object);
        trans.commit();
        super.destroyItem(container, position, object);
    }
}
