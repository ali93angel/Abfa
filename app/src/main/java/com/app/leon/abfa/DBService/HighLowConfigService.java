package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.HighLowConfig;
import com.app.leon.abfa.Models.InterCommunation.HighLowZoneProiorityViewModel;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class HighLowConfigService {
    public static void addHighLowConfig(HighLowZoneProiorityViewModel highLowZoneProiorityViewModel) {
        HighLowConfig HighLowConfig =
                new HighLowConfig(highLowZoneProiorityViewModel);
        HighLowConfig.save();
    }

    public static void addHighLowConfigs(List<HighLowZoneProiorityViewModel> highLowZoneProiorityViewModels) {
        List<HighLowConfig> HighLowConfigs = new ArrayList<>();
        for (HighLowZoneProiorityViewModel highLowZoneProiorityViewModel : highLowZoneProiorityViewModels) {
            HighLowConfig HighLowConfig = new HighLowConfig(highLowZoneProiorityViewModel);
            HighLowConfigs.add(HighLowConfig);
        }
        SugarRecord.saveInTx(HighLowConfigs);
    }

    public static List<HighLowConfig> getHighLowConfig() {
        List<HighLowConfig> HighLowConfigs = HighLowConfig.listAll(HighLowConfig.class);
        return HighLowConfigs;
    }

    public static List<HighLowConfig> getHighLowConfigFindById(int idCustom) {
        List<HighLowConfig> HighLowConfigs = HighLowConfig.find(HighLowConfig.class,
                "ID_CUSTOM = ?", String.valueOf(idCustom));
        return HighLowConfigs;
    }

    public static void removeHighLowConfig(int id) {
        String whereQuery = "ID_CUSTOM = ?";
        HighLowConfig highLowConfig = HighLowConfig.find(HighLowConfig.class, whereQuery, String.valueOf(id)).get(0);
        highLowConfig.delete();
    }

    public static void removeAllHighLowAlghorithm() {
        HighLowConfig.deleteAll(HighLowConfig.class);
    }
}
