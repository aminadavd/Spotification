package com.dvir.spotification.spotify;

import android.content.Context;
import android.widget.Toast;

import com.dvir.spotification.MainActivity;
import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.APIInterface;
import com.dvir.spotification.retrofit.Episodes;
import com.dvir.spotification.retrofit.ResponseJson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.dvir.spotification.MainActivity.CLIENT_ID;

public class SpotifyUtil {
    private  String accessToken = "";

    public  String getAccessToken(){

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface  = retrofit1.create(APIInterface.class);

        String headerString = CLIENT_ID + ":" + MainActivity.SECRET_CLIENT_ID;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(headerString.getBytes());
        Call<ResponseJson> call =   apiInterface.getToken(authHeader , "client_credentials");


        try
        {
            Response<ResponseJson> response = call.execute();
            ResponseJson rj =  response.body();
            accessToken = rj.getAccessToken();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return accessToken;
    }

    public Episodes.Episode getLatestEpisode (Context context, String showId) {
        final Episodes.Episode[] episode = {null};
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface1 = retrofit.create(APIInterface.class);
        SpotifyItemPOJO updatedPojo = new SpotifyItemPOJO();
        List<Item> updatedItemList = new ArrayList<>();

        Call<Episodes> call1 = apiInterface1.searchEpisodes("Bearer " + accessToken, showId, "1", "US");
        call1.enqueue(new Callback<Episodes>() {
            @Override
            public void onResponse(Call<Episodes> call, Response<Episodes> response) {
                Episodes episodes = response.body();
                episode[0] = episodes.getItemsList().get(0);

            }

            @Override
            public void onFailure(Call<Episodes> call, Throwable t) {
                call.cancel();
            }
        });
        return episode[0];
    }

    }
