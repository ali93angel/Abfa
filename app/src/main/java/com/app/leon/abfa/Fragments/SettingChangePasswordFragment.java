package com.app.leon.abfa.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.leon.abfa.BaseItem.BaseFragment;
import com.app.leon.abfa.Infrastructure.IAbfaService;
import com.app.leon.abfa.Models.Enums.DialogType;
import com.app.leon.abfa.Models.Enums.ProgressType;
import com.app.leon.abfa.Models.Enums.SharedReferenceKeys;
import com.app.leon.abfa.Models.InterCommunation.SimpleMessage;
import com.app.leon.abfa.R;
import com.app.leon.abfa.Utils.CustomDialog;
import com.app.leon.abfa.Utils.HttpClientWrapper;
import com.app.leon.abfa.Utils.ICallback;
import com.app.leon.abfa.Utils.NetworkHelper;
import com.app.leon.abfa.Utils.SharedPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SettingChangePasswordFragment extends BaseFragment {
    Context context;
    @BindView(R.id.editTextOldPassword)
    EditText editTextOldPassword;
    @BindView(R.id.editTextNewPassword)
    EditText editTextNewPassword;
    @BindView(R.id.editTextNewPasswordRepeat)
    EditText editTextNewPasswordRepeat;
    @BindView(R.id.buttonChangePassword)
    Button buttonChangePassword;
    View view, viewFocus;
    SharedPreferenceManager sharedPreferenceManager;
    Unbinder unbinder;

    void showMessage(String message) {
        new CustomDialog(DialogType.Green, getActivity(), message,
                getString(R.string.dear_user),
                getString(R.string.error), getString(R.string.accepted));
    }

    @Override
    public View FragmentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.setting_change_password_fragment, parent, false);
        unbinder = ButterKnife.bind(this, view);
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

    void setButtonChangePasswordOnClickListener() {
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPass = editTextOldPassword.getText().toString();
                String newPass = editTextNewPassword.getText().toString();
                String newPassConfirm = editTextNewPasswordRepeat.getText().toString();
                if (currentPass.length() < 6) {
                    viewFocus = editTextOldPassword;
                    viewFocus.requestFocus();
                    editTextOldPassword.setError(getString(R.string.error_short_length));
                    return;
                }
                if (newPass.length() < 6) {
                    viewFocus = editTextNewPassword;
                    viewFocus.requestFocus();
                    editTextNewPassword.setError(getString(R.string.error_short_length));
                    return;
                }
                if (newPassConfirm.length() < 6) {
                    viewFocus = editTextNewPasswordRepeat;
                    viewFocus.requestFocus();
                    editTextNewPasswordRepeat.setError(getString(R.string.error_short_length));
                    return;
                }
                if (!newPass.equals(newPassConfirm)) {
                    viewFocus = editTextNewPasswordRepeat;
                    viewFocus.requestFocus();
                    editTextNewPasswordRepeat.setError(getString(R.string.error_passwords_is_not_match));
                    return;
                }
                String token = sharedPreferenceManager.get(SharedReferenceKeys.TOKEN.getValue());
                Retrofit retrofit = NetworkHelper.getInstance(true, token);
                final IAbfaService changePassword = retrofit.create(IAbfaService.class);
                ChangePassword changePasswordExe = new ChangePassword();
                Call<SimpleMessage> call = changePassword.changePassword(
                        new com.app.leon.abfa.Models.InterCommunation.ChangePassword(currentPass, newPass, newPassConfirm));
                HttpClientWrapper.callHttpAsync(call, changePasswordExe, getActivity(), ProgressType.SHOW.getValue());
            }
        });
    }

    @Override
    public void initialize() {
        context = getActivity();
        sharedPreferenceManager = new SharedPreferenceManager(context);
        setButtonChangePasswordOnClickListener();
    }

    class ChangePassword implements ICallback<SimpleMessage> {
        @Override
        public void execute(SimpleMessage simpleMessage) {
            showMessage(simpleMessage.getMessage());
        }
    }
}
