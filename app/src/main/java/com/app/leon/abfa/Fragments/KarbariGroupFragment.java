package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.app.leon.abfa.DBService.KarbariGroupService;
import com.app.leon.abfa.DBService.KarbariService;
import com.app.leon.abfa.Models.DbTables.Karbari;
import com.app.leon.abfa.Models.DbTables.KarbariGroup;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReadReportActivity.UpdateOnOffLoadByKarbari;

public class KarbariGroupFragment extends DialogFragment {
    Context context;
    Unbinder unbinder;
    View view;
    @BindView(R.id.spinnerGroup)
    Spinner spinnerGroup;
    @BindView(R.id.spinnerType)
    Spinner spinnerType;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    List<Karbari> karbaris;
    List<KarbariGroup> karbariGroups;

    public KarbariGroupFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.karbari_group_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
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

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateOnOffLoadByKarbari(karbaris.get(spinnerType.getSelectedItemPosition()).getIdCustom());
                dismiss();
            }
        });
    }

    public void initialize() {
        FrameLayout frameLayout = view.findViewById(R.id.fragmentFrameLayout);
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setupSpinner();
        setSpinnerOnItemSelectedListener();
        initializeSpinnerType();
        setButtonSubmitOnClickListener();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void setSpinnerOnItemSelectedListener() {
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                initializeSpinnerType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    void setupSpinner() {
        karbariGroups = KarbariGroupService.getKarbariGroups();
        ArrayList<String> s = new ArrayList<String>();
        for (int i = 0; i < karbariGroups.size(); i++) {
            s.add(karbariGroups.get(i).getTitle());
        }
        String[] items = new String[karbariGroups.size()];
        for (int i = 0; i < karbariGroups.size(); i++) {
            items[i] = karbariGroups.get(i).getTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(typeface);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
                Display display = getActivity().getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                ((TextView) v).setWidth(4 * size.x / 5);
                return v;
            }
        };
        spinnerGroup.setAdapter(adapter);
        spinnerGroup.setSelection(0);
    }

    void initializeSpinnerType() {
        karbaris = KarbariService.getKarbarisByGroupId(
                String.valueOf(spinnerGroup.getSelectedItemPosition() + 1));
        ArrayList<String> s = new ArrayList<String>();
        for (int i = 0; i < karbaris.size(); i++) {
            s.add(karbaris.get(i).getTitle());
        }
        String[] items = new String[karbaris.size()];
        for (int i = 0; i < karbaris.size(); i++) {
            items[i] = karbaris.get(i).getTitle();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(typeface);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                Typeface externalFont = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(getActivity().getResources().getDimension(R.dimen.textSizeMedium));
//                Display display = getActivity().getWindowManager().getDefaultDisplay();
//                Point size = new Point();
//                display.getSize(size);
//                ((TextView) v).setWidth(4 * size.x / 5);
                return v;
            }
        };
        spinnerType.setAdapter(adapter);
        spinnerType.setSelection(0);
    }


}
