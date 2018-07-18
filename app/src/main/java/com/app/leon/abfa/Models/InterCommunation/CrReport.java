package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class CrReport {
    String id;
    String billId;
    String listNumber;
    int zoneId;
    int reportCode;
    int trackNumber;

    public CrReport(String id, String billId, String listNumber, int zoneId, int reportCode, int trackNumber) {
        this.id = id;
        this.billId = billId;
        this.listNumber = listNumber;
        this.zoneId = zoneId;
        this.reportCode = reportCode;
        this.trackNumber = trackNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public int getReportCode() {
        return reportCode;
    }

    public void setReportCode(int reportCode) {
        this.reportCode = reportCode;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }
}
