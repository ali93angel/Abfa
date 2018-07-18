package com.app.leon.abfa.Models.InterCommunation;

import java.util.ArrayList;

/**
 * Created by Leon on 12/26/2017.
 */

public class KarbariWrapper {
    ArrayList<Karbari> karbaries = new ArrayList<Karbari>();
    ArrayList<KarbariRateType> karbariRateTypes = new ArrayList<KarbariRateType>();
    ArrayList<KarbariGroup> karbariGroups = new ArrayList<KarbariGroup>();

    public KarbariWrapper(ArrayList<Karbari> karbaries, ArrayList<KarbariGroup> karbariGroups, ArrayList<KarbariRateType> karbariRateTypes) {
        this.karbaries = karbaries;
        this.karbariGroups = karbariGroups;
        this.karbariRateTypes = karbariRateTypes;
    }

    public ArrayList<Karbari> getKarbaries() {
        return karbaries;
    }

    public void setKarbaries(ArrayList<Karbari> karbaries) {
        this.karbaries = karbaries;
    }

    public ArrayList<KarbariGroup> getKarbariGroups() {
        return karbariGroups;
    }

    public void setKarbariGroups(ArrayList<KarbariGroup> karbariGroups) {
        this.karbariGroups = karbariGroups;
    }

    public ArrayList<KarbariRateType> getKarbariRateTypes() {
        return karbariRateTypes;
    }

    public void setKarbariRateTypes(ArrayList<KarbariRateType> karbariRateTypes) {
        this.karbariRateTypes = karbariRateTypes;
    }
}
