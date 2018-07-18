package com.app.leon.abfa.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.leon.abfa.Infrastructure.Counting;
import com.app.leon.abfa.Models.DbTables.Karbari;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

/**
 * Created by Leon on 12/25/2017.
 */

public class ViewPagerAdapterRead extends PagerAdapter {
    FragmentManager fragmentManager;
    List<OnOffLoad> onOffLoads;
    List<Karbari> karbaris;
    ArrayAdapter adapter;
    LayoutInflater inflater;
    Context context;
    List<ReadingConfig> readingConfigs;

    public ViewPagerAdapterRead(FragmentManager fragmentManager, List<OnOffLoad> onOffLoads,
                                List<Karbari> karbaris, ArrayAdapter adapter, Context context,
                                List<ReadingConfig> readingConfigs) {
//        super(fragmentManager);
        this.fragmentManager = fragmentManager;
        this.onOffLoads = onOffLoads;
        this.karbaris = karbaris;
        this.adapter = adapter;
        this.context = context;
        this.readingConfigs = readingConfigs;
    }

//    @Override
//    public Fragment getItem(int position) {
//        if (position % 2 == 0)
//            return ReadFragment.newInstance(onOffLoads.get(position), position);
//        else return ReadFragment_.newInstance(onOffLoads.get(position), position);
//    }

    @Override
    public int getCount() {
        return onOffLoads.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        BaseActivity.setFont(container, context);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.read_fragment, container, false);
        final MainViewPagerHolder viewPagerHolder = new MainViewPagerHolder(itemView);
        viewPagerHolder.SetTextAndConfig(onOffLoads.get(position), readingConfigs, karbaris, adapter);
        (container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
        View view = (View) object;
        view = null;
    }

    class MainViewPagerHolder {
        public TextView textViewPreNumber, textViewPreDate, textViewBranch, textViewSiphon, textViewAhadForosh,
                textViewAhadMasraf, textViewName, textViewSerial, textViewCode, textViewKarbari,
                textViewAhadAsli, textViewRadif;
        public ExpandableTextView expandableTextView;
        public Spinner spinner;

        public MainViewPagerHolder(View view) {
            textViewPreNumber = (TextView) view.findViewById(R.id.textViewPreNumber);
            textViewPreDate = (TextView) view.findViewById(R.id.textViewPreDate);
            textViewBranch = (TextView) view.findViewById(R.id.textViewBranch);
            textViewSerial = (TextView) view.findViewById(R.id.textViewSerial);
            textViewSiphon = (TextView) view.findViewById(R.id.textViewSiphon);
            textViewAhadMasraf = (TextView) view.findViewById(R.id.textViewAhadMasraf);
            textViewRadif = (TextView) view.findViewById(R.id.textViewRadif);
            textViewAhadAsli = (TextView) view.findViewById(R.id.textViewAhadAsli);
            textViewName = (TextView) view.findViewById(R.id.textViewName);
            textViewAhadForosh = (TextView) view.findViewById(R.id.textViewAhadForosh);
            textViewKarbari = (TextView) view.findViewById(R.id.textViewKarbari);
            textViewCode = (TextView) view.findViewById(R.id.textViewCode);
            expandableTextView = (ExpandableTextView) view.findViewById(R.id.expandableTextViewAddress);
            spinner = (Spinner) view.findViewById(R.id.spinner);
        }

        public void SetTextAndConfig(OnOffLoad onOffLoad, List<ReadingConfig> readingConfigs,
                                     List<Karbari> karbaris, ArrayAdapter adapter) {
            if (onOffLoad.preNumber != null) {
                textViewPreNumber.setText(String.valueOf(onOffLoad.preNumber));
            }
            String date = onOffLoad.preDate.substring(0, 2) + "/" + onOffLoad.preDate.substring(2, 4) + "/" + onOffLoad.preDate.substring(4, 6);
            textViewPreDate.setText(date);
            Counting.findDifferent(textViewPreDate.getText().toString());
            textViewBranch.setText(onOffLoad.getQotrCustom());
            textViewSiphon.setText(onOffLoad.getSifoonQotrCustom());
            if (onOffLoad.tedadNonMaskooni != null) {
                textViewAhadForosh.setText(String.valueOf(onOffLoad.tedadNonMaskooni));
            }
            textViewName.setText(onOffLoad.name.trim() + " " + onOffLoad.family.trim());
            expandableTextView.setText(onOffLoad.address);
            for (Karbari karbari : karbaris) {
                if (onOffLoad.karbariCode != null && onOffLoad.karbariCode == karbari.getIdCustom()) {
                    textViewKarbari.setText(karbari.getTitle());
                }
            }
            textViewSerial.setText(onOffLoad.counterSerial);
            if (onOffLoad.tedadMaskooni != null) {
                textViewAhadAsli.setText(String.valueOf(onOffLoad.tedadMaskooni));
            }
            if (onOffLoad.ahadMasraf != null) {
                textViewAhadMasraf.setText(String.valueOf(onOffLoad.ahadMasraf));
            }
            textViewRadif.setText(String.valueOf(onOffLoad.radif));
            for (ReadingConfig readingConfig : readingConfigs) {
                if (readingConfig.getTrackNumber() == onOffLoad.trackNumber) {
                    if (readingConfig.isOnQeraatCode()) {
                        textViewCode.setText(onOffLoad.qeraatCode);
                    } else {
                        textViewCode.setText(onOffLoad.eshterak);
                    }
                }
            }
            spinner.setAdapter(adapter);
            if (onOffLoad.counterStatePosition != null) {
                spinner.setSelection(onOffLoad.counterStatePosition);
            } else
                spinner.setSelection(0);
        }
    }

}
