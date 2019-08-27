package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.app.leon.abfa.DBService.ReadingConfigService;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.Crypto;
import com.app.leon.abfa.Utils.FontManager;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.leon.abfa.Activities.ReadSettingActivity.readingConfigs;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadArchive;
import static com.app.leon.abfa.DBService.UpdateOnOffLoad.updateOnOffLoadArchiveByTrackNumber;
import static com.app.leon.abfa.Fragments.ReadSettingDeleteFragment.selected;

public class DeleteFragment extends DialogFragment {
    Unbinder unbinder;
    Context context;
    View view, viewFocus;
    @BindView(R.id.buttonSubmit)
    Button buttonSubmit;
    @BindView(R.id.buttonExit)
    Button buttonExit;
    SharedPreferenceManager sharedPreferenceManager;
    @BindView(R.id.editTextUsername)
    EditText editTextUsername;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.imageViewPassword)
    ImageView imageViewPassword;
    @BindView(R.id.fragmentFrameLayout)
    FrameLayout frameLayout;

    public DeleteFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        view = inflater.inflate(R.layout.delete_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = getActivity();
        initialize();
        return view;
    }

    private void setButtonSubmitOnClickLIstener() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                if (editTextPassword.getText().toString().length() < 1) {
                    cancel = true;
                    viewFocus = editTextPassword;
                    viewFocus.requestFocus();
                    editTextPassword.setError(getString(R.string.error_empty));
                }
                if (!cancel && editTextUsername.getText().toString().length() < 1) {
                    cancel = true;
                    viewFocus = editTextUsername;
                    viewFocus.requestFocus();
                    editTextUsername.setError(getString(R.string.error_empty));
                }
                if (!cancel) {
                    String username = "tsw_" + editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();
                    String username_ = sharedPreferenceManager.get(SharedReferenceKeys.USERNAME.getValue());
                    String password_ = Crypto.decrypt(sharedPreferenceManager.get(
                            SharedReferenceKeys.PASSWORD.getValue()).toString());
                    if (username.equals(username_) && password.equals(password_)) {
                        if (selected == 0) {
                            ReadingConfigService.updateReadingConfigArchive(true, false);
                            updateOnOffLoadArchive(false);
                        } else {
                            ReadingConfigService.updateReadingConfigArchiveByTrackNumber(true, false,
                                    readingConfigs.get(selected - 1).getTrackNumber(),
                                    readingConfigs.get(selected - 1).getListNumber());
                            updateOnOffLoadArchiveByTrackNumber(true,
                                    readingConfigs.get(selected - 1).getTrackNumber());
                        }
                        Toast.makeText(getActivity(), R.string.delete, Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getActivity(), R.string.error_is_not_match, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setImageViewPasswordOnClickListener() {
        imageViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassword.getInputType() != InputType.TYPE_CLASS_NUMBER)
                    editTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                else
                    editTextPassword.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                FontManager fontManager = new FontManager(getActivity());
                fontManager.setFont(frameLayout);
            }
        });
    }

    void initialize() {
        sharedPreferenceManager = new SharedPreferenceManager(getActivity());
        FontManager fontManager = new FontManager(getActivity());
        fontManager.setFont(frameLayout);
        setImageViewPasswordOnClickListener();
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setButtonSubmitOnClickLIstener();
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
