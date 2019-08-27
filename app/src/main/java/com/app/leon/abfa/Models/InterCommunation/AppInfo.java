package com.app.leon.abfa.Models.InterCommunation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leon on 12/24/2017.
 */

public class AppInfo {
    @SerializedName("description ")
    String description;
    @SerializedName("fileSizeBytes")
    String fileSizeBytes;
    @SerializedName("insertDateJalali")
    String insertDateJalali;
    @SerializedName("versionName")
    String versionName;

    public AppInfo(String description, String fileSizeBytes, String insertDateJalali, String versionName) {
        this.description = description;
        this.fileSizeBytes = fileSizeBytes;
        this.insertDateJalali = insertDateJalali;
        this.versionName = versionName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileSizeBytes() {
        return fileSizeBytes;
    }

    public void setFileSizeBytes(String fileSizeBytes) {
        this.fileSizeBytes = fileSizeBytes;
    }

    public String getInsertDateJalali() {
        return insertDateJalali;
    }

    public void setInsertDateJalali(String insertDateJalali) {
        this.insertDateJalali = insertDateJalali;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
