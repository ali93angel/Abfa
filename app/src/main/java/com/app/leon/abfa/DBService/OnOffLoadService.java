package com.app.leon.abfa.DBService;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Models.Enums.OffloadStateEnum;
import com.orm.SugarDb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class OnOffLoadService {

    public static long getOnOffLoadSize() {
        return OnOffLoad.count(OnOffLoad.class);
    }

    public static List<OnOffLoad> getOnOffLoad() {
        List<OnOffLoad> onOffLoads = OnOffLoad.listAll(OnOffLoad.class);
        return onOffLoads;
    }

    public static List<OnOffLoad> getOnOffLoadByOffLoadState(int offloadState) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
        String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE  R.IS_ACTIVE=1 AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)" +
                " AND C.OFF_LOAD_STATE_ID = " + offloadState;
        onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
        return onOffLoads;
    }

    public static List<OnOffLoad> getOnOffLoadByOffLoadUnsent(String trackNumber) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
        String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "WHERE  R.IS_ACTIVE = 1 AND C.IS_ARCHIVE <> 1 " + "AND C.TRACK_NUMBER = " + trackNumber +
                " AND C.OFF_LOAD_STATE_ID = " + OffloadStateEnum.REGISTERD.getValue();
        onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
        return onOffLoads;
    }

    public static long getOnOffLoadReadUnsentSize(Context context, String trackNumber) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER = R.TRACK_NUMBER " +
                "WHERE R.IS_ACTIVE = 1 AND C.IS_ARCHIVE <> 1 AND C.TRACK_NUMBER = " + trackNumber;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }

    public static long getOnOffLoadReadManeSize(Context context, String trackNumber) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD O " +
                "JOIN READING_CONFIG R ON O.TRACK_NUMBER = R.TRACK_NUMBER " +
                "JOIN COUNTER_STATE_VALUE_KEY K ON O.COUNTER_STATE_CODE = K.ID_CUSTOM " +
                "WHERE  R.IS_ACTIVE = 1 AND O.IS_ARCHIVE <> 1 AND K.IS_MANE = 1 " +
                "AND O.TRACK_NUMBER = " + trackNumber;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }

    public static long getOnOffLoadReadUnseenSize(Context context, String trackNumber) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE  R.IS_ACTIVE=1 AND C.COUNTER_STATE_CODE IS NULL AND " +
                "C.IS_ARCHIVE <> 1 AND C.TRACK_NUMBER = " + trackNumber;
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }

    public static List<OnOffLoad> getOnOffLoadByReadState(int readState) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
        String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                " WHERE  R.IS_ACTIVE=1 AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)" +
                " AND C.HIGH_LOW_STATE_ID = " + readState;
        onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
        return onOffLoads;
    }

    public static List<OnOffLoad> getOnOffLoadRead() {
        List<OnOffLoad> onOffLoads = new ArrayList<>();

        String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  R.IS_ACTIVE=1 AND C.IS_ARCHIVE <> 1 ";
        onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
        return onOffLoads;
    }

    public static Long getOnOffLoadActiveSize(Context context) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  R.IS_ACTIVE=1 AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }

    public static Long getOnOffLoadActiveSizeByUsingType(Context context, int id) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  R.IS_ACTIVE=1 AND C.HIGH_LOW_STATE_ID = " + String.valueOf(id) +
                " AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;

    }

    public static long getOnOffLoadReadUnReadSize(Context context) {
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  (R.IS_ACTIVE=1) AND (C.COUNTER_STATE_CODE IS NULL)" +
                " AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        return count;
    }

    public static List<OnOffLoad> getOnOffLoadReadUnRead(String... s) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
        String where = "C.TRACK_NUMBER = ";
        if (s.length > 0)
            where = where + s[0];
        else where = where + "C.TRACK_NUMBER";
        if (getOnOffLoadSize() > 0) {
            String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                    "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                    "  WHERE  (R.IS_ACTIVE=1) AND (C.COUNTER_STATE_CODE IS NULL)" +
                    " AND C.IS_ARCHIVE <> 1 AND " + where;
            onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
            return onOffLoads;
        }
        return onOffLoads;
    }

    public static List<OnOffLoad> getOnOffLoadAllIsMane(List<CounterStateValueKey> counterStateValueKeys) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
//         = getCounterStateValueKeyIsMane();
        String in = "";
        for (int i = 0; i < counterStateValueKeys.size(); i++) {
            in += String.valueOf(counterStateValueKeys.get(i).getIdCustom());
            if (i + 1 < counterStateValueKeys.size())
                in += ",";
        }
        if (getOnOffLoadSize() > 0) {
            String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                    "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                    "  WHERE  (R.IS_ACTIVE=1) AND (C.COUNTER_STATE_CODE IN (" + in + "))" +
                    " AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";//TODO
            onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
            return onOffLoads;
        }
        return onOffLoads;
    }

    public static long getOnOffLoadAllIsManeSize(Context context, List<CounterStateValueKey> counterStateValueKeys) {
//         = getCounterStateValueKeyIsMane();
        String in = "";
        for (int i = 0; i < counterStateValueKeys.size(); i++) {
            in += String.valueOf(counterStateValueKeys.get(i).getIdCustom());
            if (i + 1 < counterStateValueKeys.size())
                in += ",";
        }
        SugarDb sugarDb;
        sugarDb = new SugarDb(context);
        SQLiteDatabase database = sugarDb.getDB();
        long count = 0;
        String rawQuery = "SELECT COUNT(*) FROM ON_OFF_LOAD C " +
                "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                "  WHERE  (R.IS_ACTIVE=1) AND (C.COUNTER_STATE_CODE IN (" + in + "))" +
                " AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
        SQLiteStatement query = database.compileStatement(rawQuery);
        try {
            count = query.simpleQueryForLong();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            query.close();
        }
        Log.e("size", String.valueOf(count));
        return count;
    }

    public static List<OnOffLoad> getOnOffLoadIsMane(Long position) {
        List<OnOffLoad> onOffLoads = new ArrayList<>();
        if (getOnOffLoadSize() > 0) {
            String query = "SELECT C.* FROM ON_OFF_LOAD C " +
                    "JOIN READING_CONFIG R ON C.TRACK_NUMBER=R.TRACK_NUMBER " +
                    "  WHERE  (R.IS_ACTIVE=1) AND (C.COUNTER_STATE_CODE IN (" +
                    String.valueOf(position) + "))" + " AND (C.IS_ARCHIVE IS NULL OR C.IS_ARCHIVE <> 1)";
            Log.e("query ", query);
            onOffLoads = OnOffLoad.findWithQuery(OnOffLoad.class, query, null);
            return onOffLoads;
        }
        return onOffLoads;
    }


}
