package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.HighLowStateEnum;
import com.app.leon.abfa.Models.Enums.ReadStatusEnum;
import com.app.leon.abfa.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadSize;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadsHigh;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadsLow;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadsStandard;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadsZero;
import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadActiveSizeByUsingType;

public class ReportTotalFragment extends BaseFragment {
    public static int state;
    Context context;
    View view;
    @BindView(R.id.pieChart)
    PieChart pieChart;
    @BindView(R.id.textViewZero)
    TextView textViewZero;
    @BindView(R.id.textViewHigh)
    TextView textViewHigh;
    @BindView(R.id.textViewLow)
    TextView textViewLow;
    @BindView(R.id.textViewStandard)
    TextView textViewStandard;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.linearLayoutNormal)
    LinearLayout linearLayoutNormal;
    @BindView(R.id.linearLayoutZero)
    LinearLayout linearLayoutZero;
    @BindView(R.id.linearLayoutLow)
    LinearLayout linearLayoutLow;
    @BindView(R.id.linearLayoutHigh)
    LinearLayout linearLayoutHigh;
    Unbinder unbinder;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_total_fragment, parent, false);
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
        initializeOnOffLoad();
        setupChart();
        setLinearLayoutNormalOnClickListener();
        setLinearLayoutHighOnClickListener();
        setLinearLayoutLowOnClickListener();
        setLinearLayoutZeroOnClickListener();
    }

    void setLinearLayoutNormalOnClickListener() {
        linearLayoutNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = HighLowStateEnum.NORMAL.getValue();
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.STATE.getValue());
                intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                startActivity(intent);
            }
        });
    }

    void setLinearLayoutLowOnClickListener() {
        linearLayoutLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = HighLowStateEnum.LOW.getValue();
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.STATE.getValue());
                intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                startActivity(intent);
            }
        });
    }

    void setLinearLayoutHighOnClickListener() {
        linearLayoutHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = HighLowStateEnum.HIGH.getValue();
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.STATE.getValue());
                intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                startActivity(intent);
            }
        });
    }

    void setLinearLayoutZeroOnClickListener() {
        linearLayoutZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = HighLowStateEnum.ZERO.getValue();
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.STATE.getValue());
                intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                startActivity(intent);
            }
        });
    }


    void setupChart() {
        pieChart.addPieSlice(new PieModel("مصرف صفر", onOffLoadsZero, getResources().getColor(R.color.blue1)));
        pieChart.addPieSlice(new PieModel("عادی", onOffLoadsStandard, getResources().getColor(R.color.green2)));
        pieChart.addPieSlice(new PieModel("کم مصرف", onOffLoadsLow, getResources().getColor(R.color.yellow1)));
        pieChart.addPieSlice(new PieModel("پرمصرف", onOffLoadsHigh, getResources().getColor(R.color.red1)));
        pieChart.startAnimation();
    }

    void initializeOnOffLoad() {
        onOffLoadsZero = onOffLoadsHigh = onOffLoadsStandard = onOffLoadsLow = 0;
        onOffLoadsZero = getOnOffLoadActiveSizeByUsingType(context, HighLowStateEnum.ZERO.getValue());
        onOffLoadsHigh = getOnOffLoadActiveSizeByUsingType(context, HighLowStateEnum.HIGH.getValue());
        onOffLoadsLow = getOnOffLoadActiveSizeByUsingType(context, HighLowStateEnum.LOW.getValue());
        onOffLoadsStandard = getOnOffLoadActiveSizeByUsingType(context, HighLowStateEnum.NORMAL.getValue());

        textViewHigh.setText(String.valueOf(onOffLoadsHigh));
        textViewLow.setText(String.valueOf(onOffLoadsLow));
        textViewZero.setText(String.valueOf(onOffLoadsZero));
        textViewStandard.setText(String.valueOf(onOffLoadsStandard));
        textViewTotal.setText(String.valueOf(onOffLoadSize));
    }
}
