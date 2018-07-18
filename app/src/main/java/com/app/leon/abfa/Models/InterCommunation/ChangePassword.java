package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/24/2017.
 */

public class ChangePassword {
    String CurrentPassword;
    String NewPassword;
    String NewPasswordConfirm;

    public ChangePassword(String currentPassword, String newPassword, String newPasswordConfirm) {
        CurrentPassword = currentPassword;
        NewPassword = newPassword;
        NewPasswordConfirm = newPasswordConfirm;
    }

    public String getCurrentPassword() {
        return CurrentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        CurrentPassword = currentPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getNewPasswordConfirm() {
        return NewPasswordConfirm;
    }

    public void setNewPasswordConfirm(String newPasswordConfirm) {
        NewPasswordConfirm = newPasswordConfirm;
    }
}
