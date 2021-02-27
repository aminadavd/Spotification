package com.dvir.spotification.scheduling;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartBroadcastReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
      //  SchedulingUtil.scheduleJob(context);
        //context.startService(new Intent(context, SpotificationService.class));
    }
}