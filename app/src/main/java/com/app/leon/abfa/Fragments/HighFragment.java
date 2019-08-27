package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.fragment.app.DialogFragment;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.HighLowStateEnum;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HighFragment extends DialogFragment {
    Unbinder unbinder;
    Context context;
    View view;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.fragmentFrameLayout)
    FrameLayout frameLayout;
    OnOffLoad onOffLoad;
    int counterStatePosition, counterStateCode, number;

    public HighFragment() {
    }

    public static HighFragment newInstance(OnOffLoad onOffLoad, int counterStatePosition,
                                           int counterStateCode, int number) {
        HighFragment highFragment = new HighFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleEnum.ON_OFFLOAD.getValue(), new Gson().toJson(onOffLoad));
        bundle.putInt(BundleEnum.COUNTER_STATE_POSITION.getValue(), counterStatePosition);
        bundle.putInt(BundleEnum.COUNTER_STATE_CODE.getValue(), counterStateCode);
        bundle.putInt(BundleEnum.NUMBER.getValue(), number);
        highFragment.setArguments(bundle);
        return highFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.high_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        if (getArguments() != null) {
            String jsonBundle = getArguments().getString(BundleEnum.ON_OFFLOAD.getValue());
            onOffLoad = new Gson().fromJson(jsonBundle, OnOffLoad.class);
            counterStateCode = getArguments().getInt(BundleEnum.COUNTER_STATE_CODE.getValue());
            counterStatePosition = getArguments().getInt(BundleEnum.COUNTER_STATE_POSITION.getValue());
            number = getArguments().getInt(BundleEnum.NUMBER.getValue());
        }
        initialize();
        return view;
    }

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReadActivity) (getActivity())).updateOnOffLoadByCounter(onOffLoad,
                        counterStatePosition, counterStateCode, number, HighLowStateEnum.HIGH.getValue());
                dismiss();
            }
        });
    }

    void initialize() {
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setButtonSubmitOnClickListener();
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
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
