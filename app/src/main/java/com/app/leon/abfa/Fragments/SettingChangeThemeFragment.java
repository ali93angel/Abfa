package com.app.leon.abfa.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Models.Enums.BundleEnum;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettingChangeThemeFragment extends BaseFragment {
    static String theme = "1";
    Context context;
    @BindView(R.id.buttonChangeTheme)
    Button buttonChangeTheme;
    @BindView(R.id.textViewGreen)
    TextView textViewGreen;
    @BindView(R.id.textViewBlueL)
    TextView textViewBlueL;
    @BindView(R.id.textViewBlueH)
    TextView textViewBlueH;
    @BindView(R.id.textViewBlack)
    TextView textViewBlack;
    @BindView(R.id.imageViewGreen)
    ImageView imageViewGreen;
    @BindView(R.id.imageViewBlueL)
    ImageView imageViewBlueL;
    @BindView(R.id.imageViewBlueH)
    ImageView imageViewBlueH;
    @BindView(R.id.imageViewBlack)
    ImageView imageViewBlack;
    View view;
    SharedPreferenceManager sharedPreferenceManager;
    Unbinder unbinder;

    public static void changeTheme(Activity activity) {
        Intent intent = new Intent(activity, activity.getClass());
        intent.putExtra(BundleEnum.THEME.getValue(), theme);
        activity.finish();
        activity.startActivity(intent);
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_change_theme_fragment, parent, false);
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

    void setButtonChangeThemeClickListener() {
        buttonChangeTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferenceManager.put(SharedReferenceKeys.THEME_STABLE.getValue(), theme);
                sharedPreferenceManager.apply();
                Toast.makeText(getActivity(), getActivity().getString(R.string.theme_changed), Toast.LENGTH_SHORT).show();
                changeTheme(getActivity());
            }
        });
    }

    @Override
    public void initialize() {
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        setOnTextViewClickListener();
        setOnImageViewClickListener();
        setButtonChangeThemeClickListener();

    }

    void setOnTextViewClickListener() {
        textViewBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "4";
                changeTheme(getActivity());
            }
        });
        textViewBlueH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "3";
                changeTheme(getActivity());
            }
        });
        textViewBlueL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "1";
                changeTheme(getActivity());
            }
        });
        textViewGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "2";
                changeTheme(getActivity());
            }
        });
    }

    void setOnImageViewClickListener() {
        imageViewBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "4";
                changeTheme(getActivity());
            }
        });
        imageViewBlueH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "3";
                changeTheme(getActivity());
            }
        });
        imageViewBlueL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "1";
                changeTheme(getActivity());
            }
        });
        imageViewGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                theme = "2";
                changeTheme(getActivity());
            }
        });
    }
}
