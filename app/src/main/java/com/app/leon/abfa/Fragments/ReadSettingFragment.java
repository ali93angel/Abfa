package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.DBService.ReadingConfigService;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReadSettingActivity.readingConfigs;

public class ReadSettingFragment extends BaseFragment {
    Context context;
    View view;
    @BindView(R.id.listViewRead)
    ListView listViewRead;
    String[] listItems;
    Unbinder unbinder;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.read_setting_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    @Override
    public void initialize() {
        context = getActivity();
        setupListView();
    }

    void setupListView() {
        long count = ReadingConfigService.readingConfigSize();
        if (count > 0) {
            int counter = 0;
            readingConfigs = ReadingConfigService.getReadingConfigsNoArchive(false);
            listItems = new String[readingConfigs.size()];
            for (ReadingConfig readingConfig : readingConfigs) {
                listItems[counter] = String.valueOf(readingConfig.getTrackNumber()) + "*" +
                        readingConfig.getListNumber();
                counter++;
            }
            if (readingConfigs.size() > 0) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.item_spinner, listItems) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        final CheckedTextView textView = (CheckedTextView) view
                                .findViewById(android.R.id.text1);
                        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                        textView.setTypeface(typeface);
                        textView.setChecked(true);
                        final ReadingConfig readingConfig = readingConfigs.get(position);
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (textView.isChecked()) {
                                    textView.setChecked(false);
                                } else {
                                    textView.setChecked(true);
                                }
                                readingConfig.setActive(textView.isChecked());
                                readingConfig.save();
                            }
                        });
                        return view;
                    }
                };
                listViewRead.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listViewRead.setAdapter(arrayAdapter);
                int counter_ = 0;
                for (ReadingConfig readingConfig : readingConfigs) {
                    if (readingConfig.getActive() != null && readingConfig.getActive())
                        listViewRead.setItemChecked(counter_, true);
                    counter_++;
                }
            }
        }
    }
}