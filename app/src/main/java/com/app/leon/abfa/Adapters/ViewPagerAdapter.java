package com.app.leon.abfa.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.app.leon.abfa.Fragments.ReadFragment;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;

import java.util.List;

/**
 * Created by Leon on 2/11/2018.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    static int position;
    private final List<OnOffLoad> onOffLoads;
    private final Context context;
    List<Integer> spinnerItemSelected;

    public ViewPagerAdapter(Context context, FragmentManager fm, List<OnOffLoad> onOffLoads, List<Integer> spinnerItemSelected) {
        super(fm);
        this.onOffLoads = onOffLoads;
        this.context = context;
        this.spinnerItemSelected = spinnerItemSelected;
    }

    public static int getPosition() {
        return position;
    }

    @Override
    public Fragment getItem(int position) {
        return ReadFragment.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
//        if (position % 2 == 0)
//            return ReadFragment.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
//        else
//            return ReadFragment_.newInstance(onOffLoads.get(position), position, spinnerItemSelected.get(position));
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
