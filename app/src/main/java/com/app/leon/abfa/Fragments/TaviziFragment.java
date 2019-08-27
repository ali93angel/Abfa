package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.fragment.app.DialogFragment;

import com.app.leon.abfa.Activities.ReadReportActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaviziFragment extends DialogFragment {
    Context context;
    View view;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.editTextSerial)
    EditText editTextSerial;
    Unbinder unbinder;

    public TaviziFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.tavizi_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
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

    void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View focusView;
                if (editTextSerial.getText().length() < 1) {
                    focusView = editTextSerial;
                    focusView.requestFocus();
                    editTextSerial.setError(getActivity().getString(R.string.error_empty));
                } else {
                    ReadReportActivity.UpdateOnOffLoadByCounterSerial(
                            editTextSerial.getText().toString());
                    dismiss();
                }
            }
        });
    }

    void initialize() {
        FrameLayout frameLayout = view.findViewById(R.id.fragmentFrameLayout);
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        setButtonSubmitOnClickListener();
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
