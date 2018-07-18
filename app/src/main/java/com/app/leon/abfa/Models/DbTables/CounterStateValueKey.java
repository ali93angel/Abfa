package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class CounterStateValueKey extends SugarRecord {
    int idCustom;
    int mainCode;
    int possibleCode;
    int clientOrder;
    int counterMeterStateType;
    int counterStateOrReportTypeId;
    String title;
    boolean canEnterNumber;
    boolean isMane;//// TODO: 1/4/2018
    boolean canNumberBeLessThanPre;
    boolean isTavizi;//todo onOffLoad
    boolean isXarab;//// TODO: 1/4/2018 on off load
    boolean isActive;
    boolean isFaqed;
    boolean shouldEnterNumber;

    public CounterStateValueKey() {
    }

    public CounterStateValueKey(com.app.leon.abfa.Models.InterCommunation.CounterStateValueKey counterStateValueKey) {
        idCustom = counterStateValueKey.getId();
        mainCode = counterStateValueKey.getMainCode();
        possibleCode = counterStateValueKey.getPossibleCode();
        title = counterStateValueKey.getTitle();
        clientOrder = counterStateValueKey.getClientOrder();
        canEnterNumber = counterStateValueKey.isCanEnterNumber();
        isMane = counterStateValueKey.isMane();
        canNumberBeLessThanPre = counterStateValueKey.isCanNumberBeLessThanPre();
        isTavizi = counterStateValueKey.isTavizi();
        isActive = counterStateValueKey.isActive();
        counterMeterStateType = counterStateValueKey.getCounterMeterStateType();
        shouldEnterNumber = counterStateValueKey.isShouldEnterNumber();
        isXarab = counterStateValueKey.isXarab();
        isFaqed = counterStateValueKey.isFaqed();
        counterStateOrReportTypeId = counterStateValueKey.getCounterStateOrReportTypeId();
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClientOrder() {
        return clientOrder;
    }

    public void setClientOrder(int clientOrder) {
        this.clientOrder = clientOrder;
    }

    public boolean canEnterNumber() {
        return canEnterNumber;
    }

    public void setCanEnterNumber(boolean canEnterNumber) {
        this.canEnterNumber = canEnterNumber;
    }

    public boolean isMane() {
        return isMane;
    }

    public void setMane(boolean mane) {
        isMane = mane;
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

    public int getCounterMeterStateType() {
        return counterMeterStateType;
    }

    public void setCounterMeterStateType(int counterMeterStateType) {
        this.counterMeterStateType = counterMeterStateType;
    }

    public boolean shouldEnterNumber() {
        return shouldEnterNumber;
    }

    public void setShouldEnterNumber(boolean shouldEnterNumber) {
        this.shouldEnterNumber = shouldEnterNumber;
    }

    public boolean isXarab() {
        return isXarab;
    }

    public void setXarab(boolean xarab) {
        isXarab = xarab;
    }

    public boolean isFaqed() {
        return isFaqed;
    }

    public void setFaqed(boolean faqed) {
        isFaqed = faqed;
    }

    public int getCounterStateOrReportTypeId() {
        return counterStateOrReportTypeId;
    }

    public void setCounterStateOrReportTypeId(int counterStateOrReportTypeId) {
        this.counterStateOrReportTypeId = counterStateOrReportTypeId;
    }
}
