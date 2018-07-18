package com.app.leon.abfa.Models.InterCommunation;

/**
 * Created by Leon on 12/26/2017.
 */

public class ReadingConfig {
    int id;
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

    public ReadingConfig(int id, int alalPercent, int imagePercent, int zoneId, String zoneTitle, int trackNumber, String listNumber, boolean hasPreNumber, String fullName, boolean isOnQeraatCode) {
        this.id = id;
        this.alalPercent = alalPercent;
        this.imagePercent = imagePercent;
        this.zoneId = zoneId;
        this.zoneTitle = zoneTitle;
        this.trackNumber = trackNumber;
        this.listNumber = listNumber;
        this.hasPreNumber = hasPreNumber;
        this.fullName = fullName;
        this.isOnQeraatCode = isOnQeraatCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBazdid() {
        return isBazdid;
    }

    public void setBazdid(boolean bazdid) {
        isBazdid = bazdid;
    }

    public int getAlalPercent() {
        return alalPercent;
    }

    public void setAlalPercent(int alalPercent) {
        this.alalPercent = alalPercent;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isHasPreNumber() {
        return hasPreNumber;
    }

    public void setHasPreNumber(boolean hasPreNumber) {
        this.hasPreNumber = hasPreNumber;
    }

    public int getImagePercent() {
        return imagePercent;
    }

    public void setImagePercent(int imagePercent) {
        this.imagePercent = imagePercent;
    }

    public boolean isOnQeraatCode() {
        return isOnQeraatCode;
    }

    public void setOnQeraatCode(boolean onQeraatCode) {
        isOnQeraatCode = onQeraatCode;
    }

    public String getListNumber() {
        return listNumber;
    }

    public void setListNumber(String listNumber) {
        this.listNumber = listNumber;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
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

}
