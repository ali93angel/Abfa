package com.app.leon.abfa.Models.DbTables;

import com.app.leon.abfa.Models.InterCommunation.HighLowZoneProiorityViewModel;
import com.orm.SugarRecord;

/**
 * Created by Leon on 12/25/2017.
 */

public class HighLowConfig extends SugarRecord {
    public int idCustom;
    public int zoneId;
    public int karbariRateTypeId;
    public String title;
    public int proiority1AlogorithmId;
    public int proiority2AlogorithmId;
    public int proiority3AlogorithmId;
    public int highPercentBound;
    public int lowPercentBound;
    public int highConstBound;
    public int lowConstBound;
    public int highZarfiatPercentBound;
    public int lowZarfiatPercentBound;

    public HighLowConfig(HighLowZoneProiorityViewModel highLowZoneProiorityViewModel) {
        idCustom = highLowZoneProiorityViewModel.getId();
        zoneId = highLowZoneProiorityViewModel.getZoneId();
        karbariRateTypeId = highLowZoneProiorityViewModel.getKarbariRateTypeId();
        title = highLowZoneProiorityViewModel.getTitle();
        proiority1AlogorithmId = highLowZoneProiorityViewModel.getProiority1AlogorithmId();
        proiority2AlogorithmId = highLowZoneProiorityViewModel.getProiority2AlogorithmId();
        proiority3AlogorithmId = highLowZoneProiorityViewModel.getProiority3AlogorithmId();
        highPercentBound = highLowZoneProiorityViewModel.getHighPercentBound();
        lowPercentBound = highLowZoneProiorityViewModel.getLowPercentBound();
        highConstBound = highLowZoneProiorityViewModel.getHighConstBound();
        lowConstBound = highLowZoneProiorityViewModel.getLowConstBound();
        highZarfiatPercentBound = highLowZoneProiorityViewModel.getHighZarfiatPercentBound();
        lowZarfiatPercentBound = highLowZoneProiorityViewModel.getLowZarfiatPercentBound();
    }

    public HighLowConfig() {
    }
}
