package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Models.DbTables.ReadingConfig;
import com.app.leon.abfa.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReadSettingActivity.readingConfigs;

public class ReadSettingDeleteFragment extends BaseFragment {
    public static int selected;
    Context context;
    View view;
    @BindView(R.id.buttonDelete)
    Button buttonDelete;
    @BindView(R.id.spinner)
    Spinner spinner;
    String[] items;
    ArrayAdapter<String> adapter;
    Unbinder unbinder;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.read_setting_delete_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    private void setButtonDeleteOnClickListener() {
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("selected ", String.valueOf(selected));
                DeleteFragment deleteFragment = new DeleteFragment();
                deleteFragment.show(getFragmentManager(), "حذف داده ها");

            }
        });
    }

    @Override
    public void initialize() {
        setButtonDeleteOnClickListener();
        setupSpinner();
        setSpinnerOnItemSelectedListener();
    }

    void setSpinnerOnItemSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setupSpinner() {
        if (readingConfigs != null) {
            items = new String[readingConfigs.size() + 1];
            items[0] = "همه موارد";
            for (int i = 0; i < readingConfigs.size(); i++) {
                ReadingConfig readingConfig = readingConfigs.get(i);
                items[i + 1] = String.valueOf(readingConfig.getTrackNumber());
            }
        } else {
            items = new String[1];
            items[0] = "همه موارد";
        }
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item_, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(typeface);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeSmall));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeSmall));
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
//                ((TextView) v).setWidth(4 * size.x / 5);
                return v;
            }
        };
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
}
