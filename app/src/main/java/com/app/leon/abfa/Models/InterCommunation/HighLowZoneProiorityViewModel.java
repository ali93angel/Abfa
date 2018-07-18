package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class HighLowZoneProiorityViewModel {
    int id;
    int zoneId;
    int karbariRateTypeId;
    String title;
    int proiority1AlogorithmId;
    int proiority2AlogorithmId;
    int proiority3AlogorithmId;
    int highPercentBound;
    int lowPercentBound;
    int highConstBound;
    int lowConstBound;
    int highZarfiatPercentBound;
    int lowZarfiatPercentBound;

    public int getHighConstBound() {
        return highConstBound;
    }

    public void setHighConstBound(int highConstBound) {
        this.highConstBound = highConstBound;
    }

    public int getHighPercentBound() {
        return highPercentBound;
    }

    public void setHighPercentBound(int highPercentBound) {
        this.highPercentBound = highPercentBound;
    }

    public int getHighZarfiatPercentBound() {
        return highZarfiatPercentBound;
    }

    public void setHighZarfiatPercentBound(int highZarfiatPercentBound) {
        this.highZarfiatPercentBound = highZarfiatPercentBound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKarbariRateTypeId() {
        return karbariRateTypeId;
    }

    public void setKarbariRateTypeId(int karbariRateTypeId) {
        this.karbariRateTypeId = karbariRateTypeId;
    }

    public int getLowConstBound() {
        return lowConstBound;
    }

    public void setLowConstBound(int lowConstBound) {
        this.lowConstBound = lowConstBound;
    }

    public int getLowPercentBound() {
        return lowPercentBound;
    }

    public void setLowPercentBound(int lowPercentBound) {
        this.lowPercentBound = lowPercentBound;
    }

    public int getLowZarfiatPercentBound() {
        return lowZarfiatPercentBound;
    }

    public void setLowZarfiatPercentBound(int lowZarfiatPercentBound) {
        this.lowZarfiatPercentBound = lowZarfiatPercentBound;
    }

    public int getProiority1AlogorithmId() {
        return proiority1AlogorithmId;
    }

    public void setProiority1AlogorithmId(int proiority1AlogorithmId) {
        this.proiority1AlogorithmId = proiority1AlogorithmId;
    }

    public int getProiority2AlogorithmId() {
        return proiority2AlogorithmId;
    }

    public void setProiority2AlogorithmId(int proiority2AlogorithmId) {
        this.proiority2AlogorithmId = proiority2AlogorithmId;
    }

    public int getProiority3AlogorithmId() {
        return proiority3AlogorithmId;
    }

    public void setProiority3AlogorithmId(int proiority3AlogorithmId) {
        this.proiority3AlogorithmId = proiority3AlogorithmId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }
}
