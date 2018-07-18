package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/28/2017.
 */

public class KarbariRateType extends SugarRecord {
    int idCustom;
    String title;

    public KarbariRateType(int idCustom, String title) {
        this.idCustom = idCustom;
        this.title = title;
    }

    public KarbariRateType() {
    }

    public KarbariRateType(com.app.leon.abfa.Models.InterCommunation.KarbariRateType karbariRateType) {
        setIdCustom(karbariRateType.getId());
        setTitle(karbariRateType.getTitle());
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
