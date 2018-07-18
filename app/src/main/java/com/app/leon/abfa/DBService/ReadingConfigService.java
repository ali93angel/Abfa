package com.app.leon.abfa.DBService;

import com.app.leon.abfa.Models.DbTables.ReadingConfig;

import java.util.List;

/**
 * Created by Leon on 1/3/2018.
 */

public class ReadingConfigService {
    public static List<ReadingConfig> getReadingConfigs() {
        List<ReadingConfig> readingConfigs = ReadingConfig.listAll(ReadingConfig.class);
        return readingConfigs;
    }

    public static List<ReadingConfig> getReadingConfigsNoArchive(boolean isActive) {
        List<ReadingConfig> readingConfigs;
        if (!isActive)
            readingConfigs = ReadingConfig.find(ReadingConfig.class,
                    "IS_ARCHIVE IS NULL OR IS_ARCHIVE <> 1");
        else
            readingConfigs = ReadingConfig.find(ReadingConfig.class,
                    "IS_ACTIVE = 1 AND IS_ARCHIVE <> 1");
        return readingConfigs;
    }

    public static List<ReadingConfig> getReadingConfigsNoArchiveByTrackNumber(int trackNumber, String listNumber) {
        List<ReadingConfig> readingConfigs = ReadingConfig.find(ReadingConfig.class,
                "(IS_ARCHIVE IS NULL OR IS_ARCHIVE <> ?) AND TRACK_NUMBER = ? AND LIST_NUMBER = ?",
                "1", String.valueOf(trackNumber), listNumber);
        return readingConfigs;
    }

    public static void removeReadingConfig(int id) {
        String whereQuery = "idCustom = ?";
        ReadingConfig readingConfig =
                ReadingConfig
                        .find(ReadingConfig.class, whereQuery, String.valueOf(id)).get(0);
        readingConfig.delete();
    }

    public static long readingConfigSize() {
        return ReadingConfig.count(ReadingConfig.class);
    }

    public static void updateReadingConfigArchiveByTrackNumber(boolean isArchive, boolean isActive,
                                                               int trackNumber, String listNumber) {
        List<ReadingConfig> readingConfigs = getReadingConfigsNoArchiveByTrackNumber(trackNumber, listNumber);
        if (readingConfigs.size() > 0) {
            for (ReadingConfig readingConfig : readingConfigs) {
                readingConfig.setArchive(isArchive);
                readingConfig.setActive(isActive);
                readingConfig.save();
            }
        }
    }

    public static void updateReadingConfigArchive(boolean isArchive, boolean isActive) {
        List<ReadingConfig> readingConfigs = getReadingConfigsNoArchive(false);
        if (readingConfigs.size() > 0) {
            for (ReadingConfig readingConfig : readingConfigs) {
                readingConfig.setArchive(isArchive);
                readingConfig.setActive(isActive);
                readingConfig.save();
            }
        }
    }
}
