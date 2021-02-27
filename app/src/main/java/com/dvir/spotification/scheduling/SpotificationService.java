package com.dvir.spotification.scheduling;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dvir.spotification.MainActivity;
import com.dvir.spotification.R;
import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.APIInterface;
import com.dvir.spotification.retrofit.Episodes;
import com.dvir.spotification.retrofit.ResponseJson;
import com.dvir.spotification.spotify.SpotifyUtil;

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

public class SpotificationService extends Service {
    public String accessToken = "";
    SpotifyItemPOJO pojo;
        @Nullable
    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Intent broadcastIntent = new Intent(this, RestartBroadcastReceiver.class);

        sendBroadcast(broadcastIntent);
       // stoptimertask();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        // Do the work here--in this case, upload the images.
        pojo = NoteUtil.getData(getApplicationContext());
        SpotifyUtil su = new SpotifyUtil();


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
        return Service.START_STICKY;
    }


    private void doWhatYouNeed(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface1  = retrofit.create(APIInterface.class);
        SpotifyItemPOJO updatedPojo = new SpotifyItemPOJO();
        List<Item> updatedItemList = new ArrayList<>();
        if (pojo!=null && pojo.getItemsList()!=null) {
            //  Item item = pojo.getItemsList().get(pojo.getItemsList().size()-1);

            for (Item item : pojo.getItemsList()) {

                String id = item.getItemId();
                long latest = item.getLastUpdateDate();
                Call<Episodes> call1 = apiInterface1.searchEpisodes("Bearer " + accessToken, id, "2", "US");
                call1.enqueue(new Callback<Episodes>() {
                    @Override
                    public void onResponse(Call<Episodes> call, Response<Episodes> response) {
                        Episodes episodes = response.body();
                        Episodes.Episode episode1 = episodes.getItemsList().get(1);
                        String dateString = episode1.getReleaseDate();

                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                            long episodeDate = date.getTime();
                            if (episodeDate > latest) {

                                sendPush(episode1.getUri(), item.getName(), episode1.getName());
//                                    item.setLastUpdateDate(episodeDate);
//                                    NoteUtil.replaceOne(getApplicationContext(), item);
                                Toast.makeText(getApplicationContext(), "Succccessssssssss", Toast.LENGTH_LONG).show();

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
                .setContentText(showName + ": " +episodeName)
                .setStyle(new NotificationCompat.BigTextStyle()
                        //       .bigText("Push to open in Spotify"))
                        .bigText(showName + ": " +episodeName))
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.spotification_background, "PLAY IN SPOTIFY",
                        pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        Random random = new Random();
        int id = random.nextInt();
        notificationManager.notify(id, builder.build());


    }

}
