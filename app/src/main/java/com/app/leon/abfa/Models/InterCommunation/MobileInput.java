package com.app.leon.abfa.Models.InterCommunation;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Leon on 12/26/2017.
 */

public class MobileInput {
    @SerializedName("myWorks")
    ArrayList<MyWorks> myWorks = new ArrayList<MyWorks>();
    @SerializedName("readingConfigs")
    ArrayList<ReadingConfig> readingConfigs = new ArrayList<ReadingConfig>();
    @SerializedName("counterStateValueKeys")
    ArrayList<CounterStateValueKey> counterStateValueKeys = new ArrayList<CounterStateValueKey>();
    @SerializedName("reports")
    ArrayList<CrReport> reports = new ArrayList<CrReport>();
    @SerializedName("reportValueKeys")
    ArrayList<ReportValueKey> reportValueKeys = new ArrayList<ReportValueKey>();
    @SerializedName("highLowWrapper")
    HighLowWrapper highLowWrapper;
    @SerializedName("karbariWrapper")
    KarbariWrapper karbariWrapper;


    public MobileInput(ArrayList<CounterStateValueKey> counterStateValueKeys,
                       HighLowWrapper highLowWrapper, KarbariWrapper karbariWrapper,
                       ArrayList<MyWorks> myWorks, ArrayList<ReadingConfig> readingConfigs,
                       ArrayList<CrReport> reports, ArrayList<ReportValueKey> reportValueKeys) {
        this.counterStateValueKeys = counterStateValueKeys;
        this.highLowWrapper = highLowWrapper;
        this.karbariWrapper = karbariWrapper;
        this.myWorks = myWorks;
        this.readingConfigs = readingConfigs;
        this.reports = reports;
        this.reportValueKeys = reportValueKeys;
    }

    public ArrayList<CounterStateValueKey> getCounterStateValueKeys() {
        return counterStateValueKeys;
    }

    public void setCounterStateValueKeys(ArrayList<CounterStateValueKey> counterStateValueKeys) {
        this.counterStateValueKeys = counterStateValueKeys;
    }

    public HighLowWrapper getHighLowWrapper() {
        return highLowWrapper;
    }

    public void setHighLowWrapper(HighLowWrapper highLowWrapper) {
        this.highLowWrapper = highLowWrapper;
    }

    public KarbariWrapper getKarbariWrapper() {
        return karbariWrapper;
    }

    public void setKarbariWrapper(KarbariWrapper karbariWrapper) {
        this.karbariWrapper = karbariWrapper;
    }

    public ArrayList<MyWorks> getMyWorks() {
        return myWorks;
    }

    public void setMyWorks(ArrayList<MyWorks> myWorks) {
        this.myWorks = myWorks;
    }

    public ArrayList<ReadingConfig> getReadingConfigs() {
        return readingConfigs;
    }

    public void setReadingConfigs(ArrayList<ReadingConfig> readingConfigs) {
        this.readingConfigs = readingConfigs;
    }

    public ArrayList<CrReport> getReports() {
        return reports;
    }

    public void setReports(ArrayList<CrReport> reports) {
        this.reports = reports;
    }

    public ArrayList<ReportValueKey> getReportValueKeys() {
        return reportValueKeys;
    }

    public void setReportValueKeys(ArrayList<ReportValueKey> reportValueKeys) {
        this.reportValueKeys = reportValueKeys;
    }
}
