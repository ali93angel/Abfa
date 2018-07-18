package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.QeireMojaz;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Leon on 1/4/2018.
 */

public class QeireMojazService {
    public static List<QeireMojaz> getQeireMojaz() {
        List<QeireMojaz> qeireMojazs = QeireMojaz.listAll(QeireMojaz.class);
        return qeireMojazs;
    }

    public static void removeQeireMojaz(int id) {
        String whereQuery = "idCustom = ?";
        QeireMojaz qeireMojaz = QeireMojaz.find(QeireMojaz.class, whereQuery, String.valueOf(id)).get(0);
        qeireMojaz.delete();
    }

    public static void removeAllQeireMojaz() {
        QeireMojaz.deleteAll(QeireMojaz.class);
    }

    public static List<QeireMojaz> getQeireMojaz(int idCustom) {
        QeireMojaz.listAll(QeireMojaz.class);
        List<QeireMojaz> qeireMojaz = QeireMojaz.find(QeireMojaz.class,
                "ID_CUSTOM = ?", String.valueOf(idCustom));
        return qeireMojaz;
    }

    public static QeireMojaz addQeireMojaz(int idCustom, int gisAccuracy, double latitude,
                                           double longitude, String imagePath, String nextEshterak,
                                           int offLoadState, String postalCode, String preEshterak,
                                           int tedadVahed, int trackNumber, String address) {
        QeireMojaz qeireMojaz = new QeireMojaz(idCustom, gisAccuracy, latitude, longitude, imagePath,
                nextEshterak, offLoadState, postalCode, preEshterak, tedadVahed, trackNumber, address);
        qeireMojaz.save();
        return qeireMojaz;
    }

    public static void addQeireMojazs(List<QeireMojaz> qeireMojazs) {
        SugarRecord.saveInTx(qeireMojazs);
    }

    public static List<QeireMojaz> getQeireMojazUnsent(String trackNumber) {
        QeireMojaz.listAll(QeireMojaz.class);
        List<QeireMojaz> qeireMojaz = QeireMojaz.find(QeireMojaz.class,
                "(Off_Load_State <> ?) AND TRACK_NUMBER = ?",
                String.valueOf(OffloadStateEnum.SENT.getValue()), trackNumber);
        return qeireMojaz;
    }
}
