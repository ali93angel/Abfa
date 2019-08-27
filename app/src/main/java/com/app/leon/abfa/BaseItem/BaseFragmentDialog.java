package com.app.leon.abfa.BaseItem;

import android.app.DialogFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.leon.abfa.R;

import java.util.Stack;

/**
 * Created by Leon on 1/4/2018.
 */

public abstract class BaseFragmentDialog extends DialogFragment {
    View view;
    Typeface typeface;

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanseState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = FragmentDialogView(inflater, parent, savedInstanseState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "font/BYekan_3.ttf");
        FrameLayout frameLayout = view.findViewById(R.id.fragmentFrameLayout);
        setFont(frameLayout, typeface);
        initialize();
        return view;
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

    public abstract View FragmentDialogView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    public abstract void initialize();

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }
}
