package com.app.leon.abfa.Models.DbTables;

import com.orm.SugarRecord;

/**
 * Created by Leon on 12/28/2017.
 */

public class OffLoadState extends SugarRecord {
    public int idCustom;
    public String title;
    public boolean needSend;

    public OffLoadState() {
    }

}
