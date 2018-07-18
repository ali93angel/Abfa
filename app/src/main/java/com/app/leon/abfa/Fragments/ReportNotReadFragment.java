package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.ReadStatusEnum;
import com.app.leon.abfa.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReportActivity.notRead;
import static com.app.leon.abfa.Activities.ReportActivity.onOffLoadSize;

public class ReportNotReadFragment extends BaseFragment {
    Context context;
    View view;
    @BindView(R.id.textViewTotal)
    TextView textViewTotal;
    @BindView(R.id.textViewNotRead)
    TextView textViewNotRead;
    @BindView(R.id.buttonContinue)
    Button buttonContinue;
    Unbinder unbinder;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.report_not_read_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
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

    private void setButtonContinueOnClickLIstener() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReadActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleEnum.READ_STATUS.getValue(), ReadStatusEnum.UNREAD.getValue());
                intent.putExtra(BundleEnum.READ_STATUS.getValue(), bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initialize() {
        textViewTotal.setText(String.valueOf(onOffLoadSize));
        textViewNotRead.setText(String.valueOf(notRead));
        setButtonContinueOnClickLIstener();
    }
}
