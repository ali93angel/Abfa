package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.leon.abfa.Activities.LocationActivity;
import com.app.leon.abfa.Activities.ReadActivity;
import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.Location;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.Models.InterCommunation.UploadLocation;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

public class PlaceFragment extends BaseFragment {
    Context context;
    View view, viewFocus;
    @BindView(R.id.editText1)
    EditText editText1;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.editText4)
    EditText editText4;
    @BindView(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    SharedPreferenceManager sharedPreferenceManager;
    Unbinder unbinder;

    public PlaceFragment() {
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.place_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        context = null;
        unbinder.unbind();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    void saveLocation(int d1, int d2, int l1, int l2) {
        ((LocationActivity) getActivity()).onOffLoad.l1 = l1;
        ((LocationActivity) getActivity()).onOffLoad.l2 = l2;
        ((LocationActivity) getActivity()).onOffLoad.d1 = d1;
        ((LocationActivity) getActivity()).onOffLoad.d2 = d2;
        ((LocationActivity) getActivity()).onOffLoad.save();
        ((ReadActivity) (getActivity())).setOnOffloads(((LocationActivity) getActivity()).onOffLoad);
        ReadFragment.onOffLoad = ((LocationActivity) getActivity()).onOffLoad;
    }

    void uploadLocation(int d1, int d2, int l1, int l2) {
        String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
        Retrofit retrofit = NetworkHelper.getInstance(true, token);
        final IAbfaService location = retrofit.create(IAbfaService.class);
        Call<SimpleMessage> call = location.counterPosition(
                new Location(d1, d2, l1, l2, ((LocationActivity) getActivity()).onOffLoad.trackNumber,
                        ((LocationActivity) getActivity()).onOffLoad.billId));
        UploadLocation uploadLocation = new UploadLocation();
        HttpClientWrapper.callHttpAsync(call, uploadLocation, getActivity(), ProgressType.SHOW.getValue());
    }

    private void setButtonSubmitOnClickListener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText1.getText().length() < 1 && editText2.getText().length() < 1 &&
                        editText3.getText().length() < 1 && editText4.getText().length() < 1) {
                    viewFocus = editText2;
                    viewFocus.requestFocus();
                    editText2.setError(getString(R.string.error_empty_all));
                    return;
                }
                int d1 = editText2.getText().length() > 0 ? Integer.valueOf(editText2.getText().toString()) : 0;
                int d2 = editText1.getText().length() > 0 ? Integer.valueOf(editText1.getText().toString()) : 0;
                int l1 = editText4.getText().length() > 0 ? Integer.valueOf(editText4.getText().toString()) : 0;
                int l2 = editText3.getText().length() > 0 ? Integer.valueOf(editText3.getText().toString()) : 0;
                saveLocation(d1, d2, l1, l2);
                uploadLocation(d1, d2, l1, l2);
            }
        });
    }

    @Override
    public void initialize() {
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        if (((LocationActivity) getActivity()).onOffLoad.l1 != null)
            editText4.setText(String.valueOf(((LocationActivity) getActivity()).onOffLoad.l1));

        if (((LocationActivity) getActivity()).onOffLoad.l2 != null)
            editText3.setText(String.valueOf(((LocationActivity) getActivity()).onOffLoad.l2));

        if (((LocationActivity) getActivity()).onOffLoad.l1 != null)
            editText2.setText(String.valueOf(((LocationActivity) getActivity()).onOffLoad.d1));

        if (((LocationActivity) getActivity()).onOffLoad.d2 != null)
            editText1.setText(String.valueOf(((LocationActivity) getActivity()).onOffLoad.d2));
        viewFocus = editText2;
        viewFocus.requestFocus();
        setEditText1OnFocusChangeListener();
        setEditText2OnFocusChangeListener();
        setEditText3OnFocusChangeListener();
        setEditText4OnFocusChangeListener();
        setButtonSubmitOnClickListener();
    }

    void setEditText4OnFocusChangeListener() {
        editText4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayout4.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    editText4.setBackground(getResources().getDrawable(R.drawable.border_white_));
                    editText4.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayout4.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    editText4.setBackground(getResources().getDrawable(R.drawable.border_gray__));
                    editText4.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }

    void setEditText3OnFocusChangeListener() {
        editText3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayout3.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    editText3.setBackground(getResources().getDrawable(R.drawable.border_white_));
                    editText3.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayout3.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    editText3.setBackground(getResources().getDrawable(R.drawable.border_gray__));
                    editText3.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }

    void setEditText2OnFocusChangeListener() {
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayout2.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    editText2.setBackground(getResources().getDrawable(R.drawable.border_white_));
                    editText2.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayout2.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    editText2.setBackground(getResources().getDrawable(R.drawable.border_gray__));
                    editText2.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }

    void setEditText1OnFocusChangeListener() {
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    linearLayout1.setBackground(getResources().getDrawable(R.drawable.border_orange_white));
                    editText1.setBackground(getResources().getDrawable(R.drawable.border_white_));
                    editText1.setTextColor(getResources().getColor(R.color.orange2));
                } else {
                    linearLayout1.setBackground(getResources().getDrawable(R.drawable.border_gray_));
                    editText1.setBackground(getResources().getDrawable(R.drawable.border_gray__));
                    editText1.setTextColor(getResources().getColor(R.color.gray2));
                }
            }
        });
    }
}
