package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

import java.io.File;

/**
 * Created by Leon on 12/25/2017.
 */

public class MultimediaData extends SugarRecord {
    int idCustom;
    String billId;
    String path;
    String saveTimeStamp;
    int trackNumber;
    int multimediaTypeId;
    long size;
    boolean isSent;

    public MultimediaData(int idCustom, String billId, String path, String saveTimeStamp, int trackNumber,
                          int multimediaTypeId, boolean isSent) {
        this.idCustom = idCustom;
        this.billId = billId;
        this.path = path;
        this.saveTimeStamp = saveTimeStamp;
        this.trackNumber = trackNumber;
        this.multimediaTypeId = multimediaTypeId;
        File f = new File(path);
        long size_ = f.length();
        this.size = size_;
        this.isSent = isSent;
    }

    public MultimediaData() {
    }

    public int getIdCustom() {
        return idCustom;
    }

    public void setIdCustom(int idCustom) {
        this.idCustom = idCustom;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSaveTimeStamp() {
        return saveTimeStamp;
    }

    public void setSaveTimeStamp(String saveTimeStamp) {
        this.saveTimeStamp = saveTimeStamp;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public int getMultimediaTypeId() {
        return multimediaTypeId;
    }

    public void setMultimediaTypeId(int multimediaTypeId) {
        this.multimediaTypeId = multimediaTypeId;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        this.isSent = sent;
    }
}
