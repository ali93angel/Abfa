package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.MultimediaData;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class MultimediaService {
    public static List<MultimediaData> getMultimediaData(String bill_Id, String type) {
        List<MultimediaData> multimediaDataList = new ArrayList<>();
        if (getMultimediaSize() > 0) {
            multimediaDataList = MultimediaData.find(MultimediaData.class,
                    "BILL_ID=? AND Multimedia_Type_Id = ?", bill_Id, type);
        }
        return multimediaDataList;
    }

    public static long getMultimediaSize() {
        long count = MultimediaData.count(MultimediaData.class);
        return count;
    }

    public static MultimediaData addMultimedia(int type, String bill_Id, int trackNumber, String file) {
        Date date = new Date();
        MultimediaData multimediaData = new MultimediaData(0, bill_Id, file,
                date.toString(), trackNumber, type, false);
        multimediaData.save();
        return multimediaData;
    }

    public static List<MultimediaData> getMultimediaDataUnsent(String trackNumber) {
        List<MultimediaData> multimediaDataList = new ArrayList<>();
        if (getMultimediaSize() > 0) {
            multimediaDataList = MultimediaData.find(MultimediaData.class,
                    "IS_SENT  <> 1 AND TRACK_NUMBER = ?", trackNumber);
        }
        return multimediaDataList;
    }
}
