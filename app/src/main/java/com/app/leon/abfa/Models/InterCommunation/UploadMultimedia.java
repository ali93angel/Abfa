package com.app.leon.abfa.Models.InterCommunation;

import android.util.Log;

import com.app.leon.abfa.Models.DbTables.MultimediaData;
import com.app.leon.abfa.Utils.ICallback;

import static com.app.leon.abfa.Fragments.UploadFragment.setMultimedia;

/**
 * Created by Leon on 1/14/2018.
 */

public class UploadMultimedia
        implements ICallback<SimpleMessage> {
    MultimediaData multimediaData;

    public UploadMultimedia(MultimediaData multimediaData) {
        this.multimediaData = multimediaData;
    }

    @Override
    public void execute(SimpleMessage simpleMessage) {
        setMultimedia(multimediaData);
        Log.e("feedback", simpleMessage.getMessage());
    }
}
