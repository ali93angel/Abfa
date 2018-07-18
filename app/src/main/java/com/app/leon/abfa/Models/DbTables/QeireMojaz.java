package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class QeireMojaz extends SugarRecord {
    public Boolean isSent;
    int idCustom;
    int gisAccuracy;
    double latitude;
    double longitude;
    String imagePath;
    String nextEshterak;
    int offLoadState;
    String postalCode;
    String preEshterak;
    int tedadVahed;
    String address;
    int trackNumber;

    public QeireMojaz() {
    }

    public QeireMojaz(int idCustom, int gisAccuracy, double latitude, double longitude,
                      String imagePath, String nextEshterak, int offLoadState, String postalCode,
                      String preEshterak, int tedadVahed, int trackNumber, String address) {
        this.idCustom = idCustom;
        this.gisAccuracy = gisAccuracy;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imagePath = imagePath;
        this.nextEshterak = nextEshterak;
        this.offLoadState = offLoadState;
        this.postalCode = postalCode;
        this.preEshterak = preEshterak;
        this.tedadVahed = tedadVahed;
        this.trackNumber = trackNumber;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
    }

    public int getGisAccuracy() {
        return gisAccuracy;
    }

    public void setGisAccuracy(int gisAccuracy) {
        this.gisAccuracy = gisAccuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNextEshterak() {
        return nextEshterak;
    }

    public void setNextEshterak(String nextEshterak) {
        this.nextEshterak = nextEshterak;
    }

    public int getOffLoadState() {
        return offLoadState;
    }

    public void setOffLoadState(int offLoadState) {
        this.offLoadState = offLoadState;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPreEshterak() {
        return preEshterak;
    }

    public void setPreEshterak(String preEshterak) {
        this.preEshterak = preEshterak;
    }

    public int getTedadVahed() {
        return tedadVahed;
    }

    public void setTedadVahed(int tedadVahed) {
        this.tedadVahed = tedadVahed;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }
}
