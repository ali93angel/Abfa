package com.app.leon.abfa.Fragments;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

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

public class LowFragment extends DialogFragment {
    Context context;
    Unbinder unbinder;
    View view;
    @BindView(R.id.fragmentFrameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    OnOffLoad onOffLoad;
    int counterStatePosition, counterStateCode, number;

    public LowFragment() {
    }

    public static LowFragment newInstance(OnOffLoad onOffLoad, int counterStatePosition,
                                          int counterStateCode, int number) {
        LowFragment lowFragment = new LowFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BundleEnum.ON_OFFLOAD.getValue(), new Gson().toJson(onOffLoad));
        bundle.putInt(BundleEnum.COUNTER_STATE_POSITION.getValue(), counterStatePosition);
        bundle.putInt(BundleEnum.COUNTER_STATE_CODE.getValue(), counterStateCode);
        bundle.putInt(BundleEnum.NUMBER.getValue(), number);
        lowFragment.setArguments(bundle);
        return lowFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        view = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.low_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        if (getArguments() != null) {
            String jsonBundle = getArguments().getString(BundleEnum.ON_OFFLOAD.getValue());
            onOffLoad = new Gson().fromJson(jsonBundle, OnOffLoad.class);
            counterStateCode = getArguments().getInt(BundleEnum.COUNTER_STATE_CODE.getValue());
            counterStatePosition = getArguments().getInt(BundleEnum.COUNTER_STATE_POSITION.getValue());
            number = getArguments().getInt(BundleEnum.NUMBER.getValue());
        }
        context = getActivity();
        initialize();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ReadActivity) (getActivity())).updateOnOffLoadByCounter(onOffLoad,
                        counterStatePosition, counterStateCode, number, HighLowStateEnum.LOW.getValue());
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
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

}
