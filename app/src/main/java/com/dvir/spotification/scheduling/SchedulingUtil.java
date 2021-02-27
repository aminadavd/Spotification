package com.dvir.spotification.scheduling;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.dvir.spotification.TestJobService;
import com.dvir.spotification.note.NoteUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.dvir.spotification.MainActivity.WORK_TAG;

public class SchedulingUtil {
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, TestJobService.class);
        NoteUtil.writeTolog(context, new Date().toString() + " ==> Scheduing Job \n " );
        JobInfo jobInfo = new JobInfo.Builder(TestJobService.ID, serviceComponent)
                // setOverrideDeadline runs it immediately - you must have at least one constraint
                // https://stackoverflow.com/questions/51064731/firing-jobservice-without-constraints
                .setRequiresCharging(false)
//                .setPersisted(true)
                .setOverrideDeadline(3 * 1000)
                .setMinimumLatency(60 * 60000)

         //       .setImportantWhileForeground(true)
           //     .setRequiresDeviceIdle(false)
             //   .setRequiresStorageNotLow(false)
              //  .setPeriodic(20 * 60000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).build();

        //        JobInfo.Builder builder = new JobInfo.Builder(TestJobService.ID, serviceComponent);
////        builder.setMinimumLatency(1 * 1000); // wait at least
////        builder.setOverrideDeadline(3 * 1000); // maximum delay
//       builder.setPeriodic(10000);
//       builder.setPersisted(true);


        //builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED); // require unmetered network
        //builder.setRequiresDeviceIdle(true); // device should be idle
        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(jobInfo);
    }
    public static void scheduleWorkRequest(Context context) {

//        WorkManager
//                .getInstance(context).getWorkInfosByTagLiveData(WORK_TAG).observe(owner, listOfWorkInfo -> {
//            if (listOfWorkInfo !=null && !listOfWorkInfo.isEmpty()) {
//                WorkInfo workInfo = listOfWorkInfo.get(0);
//                boolean dontschedule = workInfo.getState().equals(WorkInfo.State.ENQUEUED) ||
//                        workInfo.getState().equals(WorkInfo.State.RUNNING);
//                if (!dontschedule) {
//                    SchedulingUtil.scheduleWorkRequest(context);
//                }
//            } else {
//                SchedulingUtil.scheduleWorkRequest(context);
//            }
//        });


        PeriodicWorkRequest workRequest =
                new PeriodicWorkRequest.
                        Builder(SpotificationWorker.class, 1, TimeUnit.HOURS)
                        .addTag(WORK_TAG)
                        .build();

//        WorkRequest workRequest =
//                new OneTimeWorkRequest.
//                        Builder(SpotificationWorker.class)
//                        .addTag(WORK_TAG)
//                        .setInitialDelay(15, TimeUnit.SECONDS)
//                        .build();

        WorkManager
                .getInstance(context)
                .enqueueUniquePeriodicWork("myAmazingWork",
                        ExistingPeriodicWorkPolicy.KEEP,
                        workRequest);


                //.enqueue(workRequest);
    }
}
