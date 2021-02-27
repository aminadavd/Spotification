package com.dvir.spotification;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.APIInterface;
import com.dvir.spotification.retrofit.Episodes;
import com.dvir.spotification.retrofit.ResponseJson;


import com.dvir.spotification.spotify.SpotifyUtil;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dvir.spotification.MainActivity.CHANNEL_ID;
import static com.dvir.spotification.MainActivity.CLIENT_ID;


/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
@SuppressLint("SpecifyJobSchedulerIdRange")
public class TestJobService extends JobService {
    //    private static final String TAG = "SyncService";
    public static final int ID = 63820;
    public String accessToken = "";
    SpotifyItemPOJO pojo;

    @Override
    public boolean onStartJob(JobParameters params) {
        Date now = new Date();
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        NoteUtil.writeTolog(getApplicationContext(), formatter.format(now) + " == >  RUNNING JOB \n");
        pojo = NoteUtil.getData(getApplicationContext());
        SpotifyUtil su = new SpotifyUtil();
        //      accessToken = su.getAccessToken();

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface = retrofit1.create(APIInterface.class);

        String headerString = CLIENT_ID + ":" + MainActivity.SECRET_CLIENT_ID;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(headerString.getBytes());
        Call<ResponseJson> call = apiInterface.getToken(authHeader, "client_credentials");
        call.enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                ResponseJson rj = response.body();
                accessToken = rj.getAccessToken();

                doWhatYouNeed();
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                call.cancel();

            }
        });


        //    SchedulingUtil.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }


    private void doWhatYouNeed() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface1 = retrofit.create(APIInterface.class);
        SpotifyItemPOJO updatedPojo = new SpotifyItemPOJO();
        List<Item> updatedItemList = new ArrayList<>();
        if (pojo != null && pojo.getItemsList() != null) {
            //  Item item = pojo.getItemsList().get(pojo.getItemsList().size()-1);

            for (Item item : pojo.getItemsList()) {

                String id = item.getItemId();
                long latest = item.getLastUpdateDate();
                String lastEpisodeId = item.getLastEpisodeId();
                Call<Episodes> call1 = apiInterface1.searchEpisodes("Bearer " + accessToken, id, "2", "US");
                call1.enqueue(new Callback<Episodes>() {
                    @Override
                    public void onResponse(Call<Episodes> call, Response<Episodes> response) {
                        Episodes episodes = response.body();
                        Episodes.Episode episode1 = episodes.getItemsList().get(0);
                        String dateString = episode1.getReleaseDate();
                        String episodeId = episode1.getId();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            long episodeDate = date.getTime();

                            if (episodeDate == 0) {
                                item.setLastUpdateDate(episodeDate);
                                item.setLastEpisodeId(episodeId);
                            } else if (episodeDate > latest || !episodeId.equals(lastEpisodeId)) {
                                sendPush(episode1.getUri(), item.getName(), episode1.getName());
                                item.setLastUpdateDate(episodeDate);
                                item.setLastEpisodeId(episodeId);
                                NoteUtil.replaceOne(getApplicationContext(), item);
                                //Toast.makeText(getApplicationContext(), "Succccessssssssss", Toast.LENGTH_LONG).show();
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<Episodes> call, Throwable t) {
                        call.cancel();
                        updatedItemList.add(item);

                    }
                });

            }

        }
    }


    private void sendPush(String uri, String showName, String episodeName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + getApplicationContext().getPackageName()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.spotification)
                .setContentTitle("New episode is available!")
                .setContentText(showName + ": " + episodeName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        //       .bigText("Push to open in Spotify"))
                        .bigText(showName + ": " + episodeName))
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.spotification_background, "PLAY IN SPOTIFY",
                        pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        Random random = new Random();
        int id = random.nextInt();
        notificationManager.notify(id, builder.build());


    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }


}