package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class Karbari {
    int id;
    String title;
    int karbariGroupId;
    boolean hasVibrate;
    int karbariRateTypeId;

    public Karbari(boolean hasVibrate, int id, int karbariGroupId, int karbariRateTypeId, String title) {
        this.hasVibrate = hasVibrate;
        this.id = id;
        this.karbariGroupId = karbariGroupId;
        this.karbariRateTypeId = karbariRateTypeId;
        this.title = title;
    }

    public boolean isHasVibrate() {
        return hasVibrate;
    }

    public void setHasVibrate(boolean hasVibrate) {
        this.hasVibrate = hasVibrate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKarbariGroupId() {
        return karbariGroupId;
    }

    public void setKarbariGroupId(int karbariGroupId) {
        this.karbariGroupId = karbariGroupId;
    }

    public int getKarbariRateTypeId() {
        return karbariRateTypeId;
    }

    public void setKarbariRateTypeId(int karbariRateTypeId) {
        this.karbariRateTypeId = karbariRateTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
