package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class CounterStateValueKey {
    int id;
    int mainCode;
    int possibleCode;
    String title;
    int clientOrder;
    boolean canEnterNumber;
    boolean isMane;
    boolean canNumberBeLessThanPre;
    boolean isTavizi;
    boolean isActive;
    int counterMeterStateType;
    boolean shouldEnterNumber;
    boolean isXarab;
    boolean isFaqed;
    int counterStateOrReportTypeId;

    public boolean isCanEnterNumber() {
        return canEnterNumber;
    }

    public void setCanEnterNumber(boolean canEnterNumber) {
        this.canEnterNumber = canEnterNumber;
    }

    public boolean isCanNumberBeLessThanPre() {
        return canNumberBeLessThanPre;
    }

    public void setCanNumberBeLessThanPre(boolean canNumberBeLessThanPre) {
        this.canNumberBeLessThanPre = canNumberBeLessThanPre;
    }

    public int getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(int clientOrder) {
        this.clientOrder = clientOrder;
    }

    public int getCounterMeterStateType() {
        return counterMeterStateType;
    }

    public void setCounterMeterStateType(int counterMeterStateType) {
        this.counterMeterStateType = counterMeterStateType;
    }

    public int getCounterStateOrReportTypeId() {
        return counterStateOrReportTypeId;
    }

    public void setCounterStateOrReportTypeId(int counterStateOrReportTypeId) {
        this.counterStateOrReportTypeId = counterStateOrReportTypeId;
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

    public boolean isFaqed() {
        return isFaqed;
    }

    public void setFaqed(boolean faqed) {
        isFaqed = faqed;
    }

    public boolean isMane() {
        return isMane;
    }

    public void setMane(boolean mane) {
        isMane = mane;
    }

    public boolean isTavizi() {
        return isTavizi;
    }

    public void setTavizi(boolean tavizi) {
        isTavizi = tavizi;
    }

    public boolean isXarab() {
        return isXarab;
    }

    public void setXarab(boolean xarab) {
        isXarab = xarab;
    }

    public int getMainCode() {
        return mainCode;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public int getPossibleCode() {
        return possibleCode;
    }

    public void setPossibleCode(int possibleCode) {
        this.possibleCode = possibleCode;
    }

    public boolean isShouldEnterNumber() {
        return shouldEnterNumber;
    }

    public void setShouldEnterNumber(boolean shouldEnterNumber) {
        this.shouldEnterNumber = shouldEnterNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
