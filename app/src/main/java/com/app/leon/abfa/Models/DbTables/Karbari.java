package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class Karbari extends SugarRecord {
    int idCustom;
    String title;
    int karbariGroupId;
    boolean hasVibrate;
    int karbariRateTypeId;

    public Karbari(com.app.leon.abfa.Models.InterCommunation.Karbari karbari) {
        setIdCustom(karbari.getId());
        setTitle(karbari.getTitle());
        setKarbariGroupId(karbari.getKarbariGroupId());
        setHasVibrate(karbari.isHasVibrate());
        setKarbariRateTypeId(karbari.getKarbariRateTypeId());
    }

    public Karbari() {
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

    public int getKarbariGroupId() {
        return karbariGroupId;
    }

    public void setKarbariGroupId(int karbariGroupId) {
        this.karbariGroupId = karbariGroupId;
    }

    public boolean isHasVibrate() {
        return hasVibrate;
    }

    public void setHasVibrate(boolean hasVibrate) {
        this.hasVibrate = hasVibrate;
    }

    public int getKarbariRateTypeId() {
        return karbariRateTypeId;
    }

    public void setKarbariRateTypeId(int karbariRateTypeId) {
        this.karbariRateTypeId = karbariRateTypeId;
    }
}
