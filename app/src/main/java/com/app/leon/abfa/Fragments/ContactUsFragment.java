package com.app.leon.abfa.Fragments;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.FontManager;

import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ContactUsFragment extends DialogFragment {
    Unbinder unbinder;
    Context context;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    @BindView(R.id.buttonCall)
    Button buttonCall;
    View view;

    public ContactUsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.contact_us_fragment, container, false);
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

    private void setButtonCallOnClickListener() {
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:09357366741"));
                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.e("call permission :", "Denied");
                    Toast.makeText(context, getString(R.string.access), Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(callIntent);
            }
        });
    }

    public void initialize() {
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.fragmentFrameLayout);
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setButtonCallOnClickListener();
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
