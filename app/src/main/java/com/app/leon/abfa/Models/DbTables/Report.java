package com.app.leon.abfa.Models.DbTables;

import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.app.leon.abfa.Models.InterCommunation.CrReport;
import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class Report extends SugarRecord {
    String idCustom;
    String billId;
    int offLoadStateId;
    int reportCode;
    String saveTimeStamp;
    int trackNumber;

    public Report(CrReport crReport) {
        setIdCustom(crReport.getId().toString());
        setBillId(crReport.getBillId());
        setOffLoadState(OffloadStateEnum.INSERTED.getValue());
        setReportCode(crReport.getReportCode());
        setTrackNumber(crReport.getTrackNumber());
    }

    public Report() {
    }

    public String getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(String idCustom) {
        this.idCustom = idCustom;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public int getOffLoadState() {
        return offLoadStateId;
    }

    public void setOffLoadState(int offLoadStateId) {
        this.offLoadStateId = offLoadStateId;
    }

    public int getReportCode() {
        return reportCode;
    }

    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }

    public String getSaveTimeStamp() {
        return saveTimeStamp;
    }

    public void setSaveTimeStamp(String saveTimeStamp) {
        this.saveTimeStamp = saveTimeStamp;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }
}
