package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.HighLowAlgorithm;
import com.app.leon.abfa.Models.InterCommunation.HighLowAlgorithmViewModel;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class HighLowAlgorithmService {
    public static void addHighLowAlgorithm(int idCustom, String title) {
        HighLowAlgorithm highLowAlgorithm =
                new HighLowAlgorithm(new HighLowAlgorithmViewModel(idCustom, title));
        highLowAlgorithm.save();
    }

    public static void addHighLowAlgorithms(int[] idCustoms, String titles[]) {
        List<HighLowAlgorithm> highLowAlgorithms = new ArrayList<>();
        for (int i = 1; i < idCustoms.length; i++) {
            HighLowAlgorithm highLowAlgorithm = new HighLowAlgorithm(
                    new HighLowAlgorithmViewModel(idCustoms[i], titles[i]));
            highLowAlgorithms.add(highLowAlgorithm);
        }
        SugarRecord.saveInTx(highLowAlgorithms);
    }

    public static List<HighLowAlgorithm> getHighLowAlgorithm() {
        List<HighLowAlgorithm> highLowAlgorithms = HighLowAlgorithm.listAll(HighLowAlgorithm.class);
        return highLowAlgorithms;
    }

    public static List<HighLowAlgorithm> getHighLowAlgorithmFindById(int idCustom) {
        List<HighLowAlgorithm> highLowAlgorithms = HighLowAlgorithm.find(HighLowAlgorithm.class,
                "ID_CUSTOM = ?", String.valueOf(idCustom));
        return highLowAlgorithms;
    }

    public static void removeHighLowALgorithm(int id) {
        String whereQuery = "ID_CUSTOM = ?";
        HighLowAlgorithm highLowAlgorithm = HighLowAlgorithm.find(HighLowAlgorithm.class, whereQuery, String.valueOf(id)).get(0);
        highLowAlgorithm.delete();
    }

    public static void removeAllHighLowAlghorithm() {
        HighLowAlgorithm.deleteAll(HighLowAlgorithm.class);
    }
}
