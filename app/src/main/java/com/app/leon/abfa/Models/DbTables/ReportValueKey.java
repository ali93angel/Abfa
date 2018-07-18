package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class ReportValueKey extends SugarRecord {
    int idCustom;
    int code;
    String title;
    boolean isAhad;
    boolean isKarbari;
    boolean canNumberBeLessThanPre;
    boolean isTavizi;
    boolean isActive;

    public ReportValueKey(com.app.leon.abfa.Models.InterCommunation.ReportValueKey reportValueKey) {
        setIdCustom(reportValueKey.getId());
        setActive(reportValueKey.isActive());
        setAhad(reportValueKey.isAhad());
        setCanNumberBeLessThanPre(reportValueKey.isCanNumberBeLessThanPre());
        setTitle(reportValueKey.getTitle());
        setCode(reportValueKey.getCode());
        setKarbari(reportValueKey.isKarbari());
        setTavizi(reportValueKey.isTavizi());
    }

    public ReportValueKey() {
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAhad() {
        return isAhad;
    }

    public void setAhad(boolean ahad) {
        isAhad = ahad;
    }

    public boolean isKarbari() {
        return isKarbari;
    }

    public void setKarbari(boolean karbari) {
        isKarbari = karbari;
    }

    public boolean isCanNumberBeLessThanPre() {
        return canNumberBeLessThanPre;
    }

    public void setCanNumberBeLessThanPre(boolean canNumberBeLessThanPre) {
        this.canNumberBeLessThanPre = canNumberBeLessThanPre;
    }

    public boolean isTavizi() {
        return isTavizi;
    }

    public void setTavizi(boolean tavizi) {
        isTavizi = tavizi;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
