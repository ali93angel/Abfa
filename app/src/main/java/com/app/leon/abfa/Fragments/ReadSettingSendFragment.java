package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.R;

public class ReadSettingSendFragment extends BaseFragment {
    Context context;
    View view;

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.read_setting_send_fragment, parent, false);
        context = getActivity();
        return view;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}
