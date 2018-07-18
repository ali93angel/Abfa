package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.ReadStatusEnum;
import com.app.leon.abfa.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReportActivity.allIsMane;
import static com.app.leon.abfa.Activities.ReportActivity.counterStateValueKeys;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadSize;
import static com.app.leon.abfa.Activities.ReportActivity.selected;

public class ReportTemporaryFragment extends BaseFragment {
    Context context;
    View view;
    @BindView(R.id.spinner)
    Spinner spinner;
    String[] items;
    ArrayAdapter<String> adapter;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewAl)
    TextView textViewAl;
    Unbinder unbinder;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_temporary_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void initialize() {
        textViewAl.setText(String.valueOf(allIsMane));
        textViewTotal.setText(String.valueOf(onOffLoadSize));
        setupSpinner();
        setSpinnerOnItemSelectedListener();
    }

    void setSpinnerOnItemSelectedListener() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    if (i != 1) {
                        selected = counterStateValueKeys.get(i - 2).getIdCustom();
                        Intent intent = new Intent(getActivity(), ReadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.CUSTOM_MANE.getValue());
                        intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getActivity(), ReadActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.ALL_MANE.getValue());
                        intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void setupSpinner() {
        items = new String[counterStateValueKeys.size() + 2];
        items[0] = "انتخاب کنید";
        items[1] = "همه موارد";
        for (int i = 0; i < counterStateValueKeys.size(); i++) {
            items[i + 2] = counterStateValueKeys.get(i).getTitle();
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
    }
}
