package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.CounterStateValueKey;

import java.util.List;

/**
 * Created by Leon on 12/31/2017.
 */

public class
CounterStateValueKeyService {
    public static List<CounterStateValueKey> getCounterStateValueKey() {
        List<CounterStateValueKey> counterStateValueKeys = CounterStateValueKey.listAll(CounterStateValueKey.class);
        return counterStateValueKeys;
    }

    public static List<CounterStateValueKey> getCounterStateValueKeyIsMane() {
        List<CounterStateValueKey> counterStateValueKeys = CounterStateValueKey.find(
                CounterStateValueKey.class, "IS_MANE = 1");
        return counterStateValueKeys;
    }

    public static void removeCounterStateValueKey(int id) {
        String whereQuery = "idCustom = ?";
        CounterStateValueKey counterStateValueKey =
                CounterStateValueKey
                        .find(CounterStateValueKey.class, whereQuery, String.valueOf(id)).get(0);
        counterStateValueKey.delete();
    }
}
