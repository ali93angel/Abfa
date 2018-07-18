package com.app.leon.abfa.Models.DbTables;

import com.app.leon.abfa.Models.InterCommunation.HighLowAlgorithmViewModel;
import com.orm.SugarRecord;

/**
 * Created by Leon on 12/28/2017.
 */

public class HighLowAlgorithm extends SugarRecord {
    int idCustom;
    String title;

    public HighLowAlgorithm(HighLowAlgorithmViewModel highLowAlgorithmViewModel) {
        idCustom = highLowAlgorithmViewModel.getId();
        title = highLowAlgorithmViewModel.getTitle();
    }

    public HighLowAlgorithm() {
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setId(int id) {
        this.idCustom = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
