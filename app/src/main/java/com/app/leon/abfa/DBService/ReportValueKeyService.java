package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.ReportValueKey;

import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class ReportValueKeyService {
    public static List<ReportValueKey> getReportValueKey() {
        List<ReportValueKey> reportValueKeys = ReportValueKey.listAll(ReportValueKey.class);
        return reportValueKeys;
    }

    public static void removeReportValueKey(int id) {
        String whereQuery = "idCustom = ?";
        ReportValueKey reportValueKey =
                ReportValueKey
                        .find(ReportValueKey.class, whereQuery, String.valueOf(id)).get(0);
        reportValueKey.delete();
    }

    public static long reportValueKeySize() {
        return ReportValueKey.count(ReportValueKey.class);
    }
}
