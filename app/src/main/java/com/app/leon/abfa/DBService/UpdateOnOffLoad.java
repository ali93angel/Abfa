package com.app.leon.abfa.DBService;

import android.util.Log;

import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;

import java.util.List;

import static com.app.leon.abfa.DBService.OnOffLoadService.getOnOffLoadSize;

/**
 * Created by Leon on 1/28/2018.
 */

public class UpdateOnOffLoad {
    public static void updateOnOffLoadByNavigation(String bill_Id, int trackNumber,
                                                   String possibleCounterSerial, String possibleAddress,
                                                   String possibleEshterak, String possibleMobile,
                                                   String possiblePhoneNumber, Integer tedadKhali) {
        List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                "BILL_ID=? AND TRACK_NUMBER = ? AND (IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1)",
                bill_Id, String.valueOf(trackNumber));
        Log.e(bill_Id, String.valueOf(trackNumber));
        if (onOffLoads.size() > 0) {
            OnOffLoad onOffLoad = onOffLoads.get(0);
            if (possibleCounterSerial.length() > 0) {
                onOffLoad.possibleCounterSerial = possibleCounterSerial;
            }
            if (possibleAddress.length() > 0) {
                onOffLoad.possibleAddress = possibleAddress;
            }
            if (possibleEshterak.length() > 0) {
                onOffLoad.possibleEshterak = possibleEshterak;
            }
            if (possibleMobile.length() > 0) {
                onOffLoad.possibleMobile = possibleMobile;
            }
            if (possiblePhoneNumber.length() > 0) {
                onOffLoad.possiblePhoneNumber = possiblePhoneNumber;
            }
            if (tedadKhali != null) {
                onOffLoad.tedadKhali = tedadKhali;
            }
            onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
            onOffLoad.save();
        }

    }


    public static void updateOnOffLoadByDescription(String bill_Id, int trackNumber, String description) {
        if (getOnOffLoadSize() > 0) {
            List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                    "BILL_ID=? AND TRACK_NUMBER = ?  AND IS_ARCHIVE <> 1",
                    bill_Id, String.valueOf(trackNumber));
            if (onOffLoads.size() > 0) {
                Log.e(bill_Id, String.valueOf(trackNumber));
                for (OnOffLoad onOffLoad : onOffLoads) {
                    onOffLoad.description = description;
                    onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
                    onOffLoad.save();
                }
            }
        }
    }

    public static void updateOnOffLoadByKarbari(int karbari, String bill_Id, int trackNumber) {
        if (getOnOffLoadSize() > 0) {
            List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                    "BILL_ID=? AND TRACK_NUMBER = ? AND (IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1)",
                    bill_Id, String.valueOf(trackNumber));
            if (onOffLoads.size() > 0) {
                Log.e(bill_Id, String.valueOf(trackNumber));
                for (OnOffLoad onOffLoad : onOffLoads) {
                    onOffLoad.possibleKarbariCode = karbari;
                    onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
                    onOffLoad.save();
                }
            }
        }
    }

    public static void updateOnOffLoadByAhad(int ahadAsli, int ahadGheyreAsli, String bill_Id, int trackNumber) {
        if (getOnOffLoadSize() > 0) {
            List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                    "BILL_ID=? AND TRACK_NUMBER = ?  AND (IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1)",
                    bill_Id, String.valueOf(trackNumber));
            if (onOffLoads.size() > 0) {
                Log.e(bill_Id, String.valueOf(trackNumber));
                for (OnOffLoad onOffLoad : onOffLoads) {
                    onOffLoad.possibleTedadMaskooni = ahadAsli;
                    onOffLoad.possibleTedadTejari = ahadGheyreAsli;
                    onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
                    onOffLoad.save();
                }
            }
        }
    }

    public static void updateOnOffLoadByCounterSerialKey(String counterSerial, int counterStateCode,
                                                         int counterStatePosition, OnOffLoad onOffLoad,
                                                         int accuracy, Double latitude, Double longitude
    ) {
        onOffLoad.possibleCounterSerial = counterSerial;
        onOffLoad.counterStateCode = counterStateCode;
        onOffLoad.counterStatePosition = counterStatePosition;
        onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
        onOffLoad.gisAccuracy = accuracy;
        onOffLoad.latitude = latitude;
        onOffLoad.longitude = longitude;
        onOffLoad.save();
    }

    public static void updateOnOffLoadByCounterNumber(
            int counterStateCode, int counterStatePosition, int number,
            OnOffLoad onOffLoad, int highLowState, int accuracy, Double latitude, Double longitude) {
        onOffLoad.counterStateCode = counterStateCode;
        onOffLoad.counterStatePosition = counterStatePosition;
        onOffLoad.counterNumber = number;
        onOffLoad.highLowStateId = highLowState;
        onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
        onOffLoad.gisAccuracy = accuracy;
        onOffLoad.latitude = latitude;
        onOffLoad.longitude = longitude;
        onOffLoad.save();
    }

    public static void updateOnOffLoadWithoutCounterNumber(
            int counterStateCode, int counterStatePosition,
            OnOffLoad onOffLoad, int accuracy, Double latitude, Double longitude) {
        onOffLoad.counterStateCode = counterStateCode;
        onOffLoad.counterStatePosition = counterStatePosition;
        onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
        onOffLoad.gisAccuracy = accuracy;
        onOffLoad.latitude = latitude;
        onOffLoad.longitude = longitude;
        onOffLoad.save();
    }

    public static void updateOnOffLoadArchive(boolean isArchive) {
        String isArchiveString = isArchive ? "1" : "0";
        String updateQuery = "UPDATE ON_OFF_LOAD SET IS_ARCHIVE = " + isArchiveString +
                " WHERE (IS_ARCHIVE <> 0)";
        OnOffLoad.executeQuery(updateQuery);
    }

    public static void updateOnOffLoadArchiveByTrackNumber(boolean isArchive, int trackNumber) {
//        "SELECT C.* FROM ON_OFF_LOAD C " +
//                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
//                "  WHERE  R.IS_ACTIVE=1 AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
        String isArchiveString = isArchive ? "1" : "0";
        String updateQuery = "UPDATE ON_OFF_LOAD SET IS_ARCHIVE = " + isArchiveString +
                " WHERE (IS_ARCHIVE <> 1) AND (TRACK_NUMBER=" + String.valueOf(trackNumber) + ")";
        OnOffLoad.executeQuery(updateQuery);
    }

    public static void updateOnOffLoadByCounterSerial(String counterSerial, String bill_Id, int trackNumber) {
        if (getOnOffLoadSize() > 0) {
            List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                    "BILL_ID=? AND TRACK_NUMBER = ? AND (IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1)",
                    bill_Id, String.valueOf(trackNumber));
            if (onOffLoads.size() > 0) {
                for (OnOffLoad onOffLoad : onOffLoads) {
                    onOffLoad.possibleCounterSerial = counterSerial;
                    onOffLoad.offLoadStateId = OffloadStateEnum.REGISTERD.getValue();
                    onOffLoad.save();
                }
            }
        }
    }

    public static void UpdateOnOffLoadSend(int offLoadStateId, String bill_Id, int trackNumber) {
        if (getOnOffLoadSize() > 0) {
            List<OnOffLoad> onOffLoads = OnOffLoad.find(OnOffLoad.class,
                    "BILL_ID=? AND TRACK_NUMBER = ?  AND (IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1)",
                    bill_Id, String.valueOf(trackNumber));
            if (onOffLoads.size() > 0) {
                for (OnOffLoad onOffLoad : onOffLoads) {
                    onOffLoad.offLoadStateId = offLoadStateId;
                    onOffLoad.save();
                }
            }
        }
    }

    public static void UpdateOnOffLoadSend(OnOffLoad onOffLoad, int offLoadStateId) {
        onOffLoad.offLoadStateId = offLoadStateId;
        onOffLoad.save();
    }

    public static void UpdateOnOffLoadSend(List<OnOffLoad> onOffLoads, int offLoadStateId) {
        for (OnOffLoad onOffLoad : onOffLoads) {
            Log.e(onOffLoad.idCusotm, String.valueOf(offLoadStateId));
            onOffLoad.offLoadStateId = offLoadStateId;
            onOffLoad.save();
        }
    }
}
