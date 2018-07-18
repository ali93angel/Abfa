package com.app.leon.abfa.Models.InterCommunation;

import com.app.leon.abfa.Models.DbTables.Report;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 1/11/2018.
 */

public class UploadReadData {
    @SerializedName("myReports")
    List<Report> reports = new ArrayList<>();
    @SerializedName("myWorks")
    List<OffLoadParams> offLoads = new ArrayList<OffLoadParams>();
    @SerializedName("isOverall")
    boolean isOverall;

    public UploadReadData(List<OffLoadParams> offLoads, List<Report> reports, String id, boolean isOverall) {
        this.offLoads = offLoads;
        this.reports = reports;
        this.isOverall = isOverall;
    }
}
