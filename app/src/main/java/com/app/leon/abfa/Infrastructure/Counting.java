package com.app.leon.abfa.Infrastructure;

import com.app.leon.abfa.Models.DbTables.HighLowConfig;
import com.app.leon.abfa.Models.DbTables.OnOffLoad;
import com.app.leon.abfa.Utils.CalendarTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Leon on 1/11/2018.
 */

public class Counting {

    public static long findDifferent(String preDate) {
        CalendarTool calendarToolPre = new CalendarTool();
        calendarToolPre.setIranianDate(1300 + Integer.valueOf(preDate.substring(0, 2)),
                Integer.valueOf(preDate.substring(3, 5)),
                Integer.valueOf(preDate.substring(6, 8)));

        String date = calendarToolPre.getGregorianYear() + "-";
        if (calendarToolPre.getGregorianMonth() < 10)
            date += "0";
        date += calendarToolPre.getGregorianMonth() + "-";
        if (calendarToolPre.getGregorianDay() < 10)
            date += "0";
        date += calendarToolPre.getGregorianDay();

        Date convertedDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date1 = new Date();
        long difference = Math.abs(date1.getTime() - convertedDate.getTime());
        Long differenceDates = difference / (24 * 60 * 60 * 1000);
        return differenceDates;
    }

    public static int highLowCounter(int number, OnOffLoad onOffLoad, String preDate,
                                     List<HighLowConfig> highLowConfigs) {
        if (onOffLoad.preNumber != null && onOffLoad.preNumber >= 0) {
            int use = number - onOffLoad.preNumber;
            float avg;
            if (onOffLoad.tedadKhali != null)
                avg = use / (float) (onOffLoad.tedadKol - onOffLoad.tedadKhali);
            else
                avg = use / (float) onOffLoad.tedadKol;
            avg = avg / (float) findDifferent(preDate);
            avg = avg * 30;

//            List<HighLowConfig> highLowConfigs = HighLowConfigService.getHighLowConfig();
            HighLowConfig highLowConfig = null;
            for (HighLowConfig highLowConfig1 : highLowConfigs) {
                if (highLowConfig1.zoneId == onOffLoad.zoneId)
                    highLowConfig = highLowConfig1;
            }
            if (highLowConfig != null) {
                float high = onOffLoad.preAverage * (100 + highLowConfig.highPercentBound) / (float) 100;
                float low = onOffLoad.preAverage * (100 - highLowConfig.lowPercentBound) / (float) 100;
                if (avg >= low && avg <= high)
                    return 0;
                else if (avg < low)
                    return -1;
                else if (avg > high)
                    return 1;
            }
            return 0;
        }
        return 0;
    }
}
