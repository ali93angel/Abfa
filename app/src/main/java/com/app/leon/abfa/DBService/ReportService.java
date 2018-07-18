package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.Report;
import com.app.leon.abfa.Models.DbTables.ReportValueKey;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class ReportService {
    public static List<Report> getReport() {
        List<Report> reports = Report.listAll(Report.class);
        return reports;
    }

    public static List<Report> getReportByState(int offloadState) {
        List<Report> reports = Report.find(Report.class, "OFF_LOAD_STATE_ID = ?",
                String.valueOf(offloadState));
        return reports;
    }

    public static List<Report> getReportByStateTrackNumber(int offloadState, String trackNumber) {
        List<Report> reports = Report.find(Report.class, "OFF_LOAD_STATE_ID = ? AND TRACK_NUMBER = ?",
                String.valueOf(offloadState), trackNumber);
        return reports;
    }

    public static void UpdateReportByState(List<Report> reports, int offloadState) {
        for (Report report : reports) {
            report.setOffLoadState(offloadState);
            report.save();
        }
    }

    public static List<Report> getReportByTrackBillID(String bill_Id, int trackNumber) {
        return Report.find(Report.class, "BILL_ID=? AND TRACK_NUMBER = ?", bill_Id,
                String.valueOf(trackNumber));
    }

    public static void removeReport(int id) {
        String whereQuery = "ID_CUSTOM = ?";
        Report report = Report.find(Report.class, whereQuery, String.valueOf(id)).get(0);
        report.delete();
    }

    public static void removeReportByReportCode(int i, String bill_Id, int trackNumber,
                                                List<ReportValueKey> reportValueKeys) {
        List<Report> reports_ = new ArrayList<>();
        long count = Report.count(Report.class);
        if (count > 0) {
            reports_ = Report.find(Report.class, "BILL_ID=? AND TRACK_NUMBER = ? And REPORT_CODE = ?",
                    bill_Id, String.valueOf(trackNumber),
                    String.valueOf(reportValueKeys.get(i).getCode()));
            if (reports_ != null) {
                SugarRecord.deleteInTx(reports_);
            } else {
            }
        }

    }

    public static long reportSize() {
        return Report.count(Report.class);
    }

    public static void addReport(int i, String bill_Id, int trackNumber, int onOffLoadState,
                                 List<ReportValueKey> reportValueKeys) {
        Report report = new Report();
        Date date = new Date();
        report.setSaveTimeStamp(date.toString());
        report.setBillId(bill_Id);
        report.setTrackNumber(trackNumber);
        report.setOffLoadState(onOffLoadState);
        report.setReportCode(reportValueKeys.get(i).getCode());
        report.save();
    }

}
