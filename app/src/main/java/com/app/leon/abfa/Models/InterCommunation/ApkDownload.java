package com.app.leon.abfa.Models.InterCommunation;

import com.app.leon.abfa.Utils.ICallback;

import okhttp3.ResponseBody;


/**
 * Created by Leon on 12/24/2017.
 */

public class ApkDownload
        implements ICallback<ResponseBody> {
    @Override
    public void execute(ResponseBody responseBody) {
//        writeResponseBodyToDisk(responseBody);
    }
}
