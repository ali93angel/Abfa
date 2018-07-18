package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.Karbari;

import java.util.List;

/**
 * Created by Leon on 1/4/2018.
 */

public class KarbariService {
    public static List<Karbari> getKarbaris() {
        List<Karbari> karbaris = Karbari.listAll(Karbari.class);
        return karbaris;
    }

    public static List<Karbari> getKarbarisByGroupId(String id) {
        List<Karbari> karbaris = Karbari.find(Karbari.class, "KARBARI_GROUP_ID = ?", id);
        return karbaris;

    }
}
