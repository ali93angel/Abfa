package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.leon.abfa.Activities.ReadReportActivity;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AhadFragment extends DialogFragment {
    Unbinder unbinder;
    Context context;
    View view;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.editTextNon)
    EditText editTextNon;
    @BindView(R.id.editTextMaskooni)
    EditText editTextMaskooni;
    @BindView(R.id.fragmentFrameLayout)
    FrameLayout frameLayout;
    FontManager fontManager;

    public AhadFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.ahad_fragment, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
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

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextMaskooni.getText().length() > 0 || editTextNon.getText().length() > 0) {
                    if (editTextMaskooni.getText().length() > 0 && editTextNon.getText().length() > 0) {
                        ReadReportActivity.UpdateOnOffLoadByAhad(
                                Integer.valueOf(editTextMaskooni.getText().toString()),
                                Integer.valueOf(editTextNon.getText().toString()));
                    } else if (editTextMaskooni.getText().length() > 0) {
                        ReadReportActivity.UpdateOnOffLoadByAhad(
                                Integer.valueOf(editTextMaskooni.getText().toString()), 0);
                    } else if (editTextNon.getText().length() > 0) {
                        ReadReportActivity.UpdateOnOffLoadByAhad(0,
                                Integer.valueOf(editTextNon.getText().toString()));
                    }
                    dismiss();
                }
            }
        });
    }

    void initialize() {
        fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        setButtonSubmitOnClickListener();
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void setFont(ViewGroup viewTree, Typeface typeface) {
        Stack<ViewGroup> stackOfViewGroup = new Stack<ViewGroup>();
        stackOfViewGroup.push(viewTree);
        while (!stackOfViewGroup.isEmpty()) {
            ViewGroup tree = stackOfViewGroup.pop();
            for (int i = 0; i < tree.getChildCount(); i++) {
                View child = tree.getChildAt(i);
                if (child instanceof ViewGroup) {
                    stackOfViewGroup.push((ViewGroup) child);
                } else if (child instanceof Button) {
                    ((Button) child).setTypeface(typeface);
                } else if (child instanceof EditText) {
                    ((EditText) child).setTypeface(typeface);
                } else if (child instanceof TextView) {
                    ((TextView) child).setTypeface(typeface);
                } else if (child instanceof ListView) {
                    TextView textView = (TextView) ((ListView) child).getChildAt(0);
                    textView.setTypeface(typeface);
                    textView = (TextView) ((ListView) child).getChildAt(2);
                    textView.setTypeface(typeface);
                    CheckedTextView checkedTextView = (CheckedTextView) ((ListView) child).getChildAt(0);
                    checkedTextView.setTypeface(typeface);
                    checkedTextView = (CheckedTextView) ((ListView) child).getChildAt(1);
                    checkedTextView.setTypeface(typeface);
                    checkedTextView = (CheckedTextView) ((ListView) child).getChildAt(2);
                    checkedTextView.setTypeface(typeface);
                } else if (child instanceof CheckedTextView) {
                    ((CheckedTextView) child).setTypeface(typeface);
                }
            }
        }
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
