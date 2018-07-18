package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/28/2017.
 */

public class MultimediaType extends SugarRecord {
    int idCustom;
    String title;

    public MultimediaType(int idCustom, String title) {
        this.idCustom = idCustom;
        this.title = title;
    }

    public MultimediaType() {
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
