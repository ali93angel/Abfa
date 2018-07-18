package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class ReadingConfig extends SugarRecord {
    int idCustom;
    int alalPercent;
    int imagePercent;
    int zoneId;
    String zoneTitle;
    String fullName;
    int trackNumber;
    String listNumber;
    boolean hasPreNumber;
    boolean isOnQeraatCode;
    boolean isBazdid;
    Boolean isActive;
    Boolean isArchive;

    public ReadingConfig() {
    }

    public ReadingConfig(com.app.leon.abfa.Models.InterCommunation.ReadingConfig readingConfig) {
        setIdCustom(readingConfig.getId());
        setAlalPercent(readingConfig.getAlalPercent());
        setImagePercent(readingConfig.getImagePercent());
        setZoneId(readingConfig.getZoneId());
        setZoneTitle(readingConfig.getZoneTitle());
        setFullName(readingConfig.getFullName());
        setTrackNumber(readingConfig.getTrackNumber());
        setListNumber(readingConfig.getListNumber());
        setHasPreNumber(readingConfig.isHasPreNumber());
        setOnQeraatCode(readingConfig.isOnQeraatCode());
        setBazdid(readingConfig.isBazdid());
        setActive(true);
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getArchive() {
        return isArchive;
    }

    public void setArchive(Boolean archive) {
        isArchive = archive;
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
    }

    public int getAlalPercent() {
        return alalPercent;
    }

    public void setAlalPercent(int alalPercent) {
        this.alalPercent = alalPercent;
    }

    public int getImagePercent() {
        return imagePercent;
    }

    public void setImagePercent(int imagePercent) {
        this.imagePercent = imagePercent;
    }

    public int getZoneId() {
        return zoneId;
    }

    public void setZoneId(int zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneTitle() {
        return zoneTitle;
    }

    public void setZoneTitle(String zoneTitle) {
        this.zoneTitle = zoneTitle;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public boolean isHasPreNumber() {
        return hasPreNumber;
    }

    public void setHasPreNumber(boolean hasPreNumber) {
        this.hasPreNumber = hasPreNumber;
    }

    public boolean isOnQeraatCode() {
        return isOnQeraatCode;
    }

    public void setOnQeraatCode(boolean onQeraatCode) {
        isOnQeraatCode = onQeraatCode;
    }

    public boolean isBazdid() {
        return isBazdid;
    }

    public void setBazdid(boolean bazdid) {
        isBazdid = bazdid;
    }
}
