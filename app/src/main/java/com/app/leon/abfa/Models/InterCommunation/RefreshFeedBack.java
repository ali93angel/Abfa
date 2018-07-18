package com.app.leon.abfa.Models.InterCommunation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leon on 12/24/2017.
 */

public class RefreshFeedBack {
    @SerializedName("access")
    String access;
    @SerializedName("refresh_token")
    String refresh_token;

    public RefreshFeedBack(String access, String refresh_token) {
        this.access = access;
        this.refresh_token = refresh_token;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
