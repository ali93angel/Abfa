package com.app.leon.abfa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class StopBackgroundService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Status", "onReceive");
        Intent intent1 = new Intent(context, BackgroundToziService.class);
        context.stopService(intent1);
    }
}
