package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.MultimediaType;
import com.orm.SugarTransactionHelper;

import java.util.ArrayList;

/**
 * Created by Leon on 1/3/2018.
 */

public class MultimediaTypeService {
    public static void addMultimediaType(int i, String name) {
        MultimediaType multimediaType = new MultimediaType(1, "صدا");
        multimediaType.save();
    }

    public static void addMultimediaTypes(String[] names) {
        final ArrayList<MultimediaType> multimediaTypes = new ArrayList<>();
        for (int i = 0; i < names.length; i++)
            multimediaTypes.add(new MultimediaType(i, names[i]));
        SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
            @Override
            public void manipulateInTransaction() {
                MultimediaType.deleteAll(MultimediaType.class);
                MultimediaType.saveInTx(multimediaTypes);
            }
        });
    }

    public static long multimediaTypeSize() {
        return MultimediaType.count(MultimediaType.class);
    }
}
