package com.app.leon.abfa.Models.InterCommunation;

import com.app.leon.abfa.Models.DbTables.QeireMojaz;
import com.app.leon.abfa.Utils.ICallback;

import static com.app.leon.abfa.Fragments.UploadFragment.setQeireMojaz;

/**
 * Created by Leon on 1/14/2018.
 */

public class Upload
        implements ICallback<SimpleMessage> {
    com.app.leon.abfa.Models.DbTables.QeireMojaz qeireMojaz;

    public Upload(QeireMojaz qeireMojaz) {
        this.qeireMojaz = qeireMojaz;
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        setQeireMojaz(qeireMojaz);
    }
}
