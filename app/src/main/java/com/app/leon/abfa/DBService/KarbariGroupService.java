package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.KarbariGroup;

import java.util.List;

/**
 * Created by Leon on 1/4/2018.
 */

public class KarbariGroupService {
    public static List<KarbariGroup> getKarbariGroups() {
        List<KarbariGroup> karbariGroups = KarbariGroup.listAll(KarbariGroup.class);
        return karbariGroups;
    }
}
