package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class KarbariGroup extends SugarRecord {
    int idCustom;
    String title;

    public KarbariGroup(int idCustom, String title) {
        this.idCustom = idCustom;
        this.title = title;
    }

    public KarbariGroup(com.app.leon.abfa.Models.InterCommunation.KarbariGroup karbariGroup) {
        setIdCustom(karbariGroup.getId());
        setTitle(karbariGroup.getTitle());
    }

    public KarbariGroup() {
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
