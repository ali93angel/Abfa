package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class ReportValueKey {
    int id;
    int code;
    String title;
    boolean isAhad;
    boolean isKarbari;
    boolean canNumberBeLessThanPre;
    boolean isTavizi;
    boolean isActive;

    public ReportValueKey(boolean canNumberBeLessThanPre, int code, int id, boolean isActive, boolean isAhad, boolean isKarbari, boolean isTavizi, String title) {
        this.canNumberBeLessThanPre = canNumberBeLessThanPre;
        this.code = code;
        this.id = id;
        this.isActive = isActive;
        this.isAhad = isAhad;
        this.isKarbari = isKarbari;
        this.isTavizi = isTavizi;
        this.title = title;
    }

    public boolean isCanNumberBeLessThanPre() {
        return canNumberBeLessThanPre;
    }

    public void setCanNumberBeLessThanPre(boolean canNumberBeLessThanPre) {
        this.canNumberBeLessThanPre = canNumberBeLessThanPre;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public boolean isTavizi() {
        return isTavizi;
    }

    public void setTavizi(boolean tavizi) {
        isTavizi = tavizi;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
