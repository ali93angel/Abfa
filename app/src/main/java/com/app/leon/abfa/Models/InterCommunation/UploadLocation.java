package com.app.leon.abfa.Models.InterCommunation;

import android.util.Log;

import com.app.leon.abfa.Utils.ICallback;

/**
 * Created by Leon on 1/14/2018.
 */

public class UploadLocation
        implements ICallback<SimpleMessage> {
    @Override
    public void execute(SimpleMessage simpleMessage) {
        Log.e("feedback", simpleMessage.getMessage());
    }
}
