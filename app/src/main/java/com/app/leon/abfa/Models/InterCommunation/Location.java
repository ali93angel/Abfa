package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 1/14/2018.
 */

public class Location {
    int d1, d2, l1, l2, trackNumber;
    String billId;

    public Location(int d1, int d2, int l1, int l2, int trackNumber, String billId) {
        this.d1 = d1;
        this.d2 = d2;
        this.l1 = l1;
        this.l2 = l2;
        this.trackNumber = trackNumber;
        this.billId = billId;
    }

    public int getD1() {
        return d1;
    }

    public void setD1(int d1) {
        this.d1 = d1;
    }

    public int getD2() {
        return d2;
    }

    public void setD2(int d2) {
        this.d2 = d2;
    }

    public int getL1() {
        return l1;
    }

    public void setL1(int l1) {
        this.l1 = l1;
    }

    public int getL2() {
        return l2;
    }

    public void setL2(int l2) {
        this.l2 = l2;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }
}
