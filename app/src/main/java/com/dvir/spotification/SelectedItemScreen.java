package com.dvir.spotification;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.APIInterface;
import com.dvir.spotification.retrofit.Albums;
import com.dvir.spotification.retrofit.Episodes;
import com.dvir.spotification.retrofit.ShowsJson;
import com.dvir.spotification.scheduling.SchedulingUtil;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;


import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectedItemScreen extends AppCompatActivity {

    private String showId = "";
    private String showName = "";
    private String imageHref = "";
    SpotifyItemPOJO pojo;
    String type = "";
    String accessToken = "";
    List<Episodes.Episode> episodesList = null;
    List<Albums.Album> albumList = null;
    private AdView mAdView;

    public static  AdRequest adRequest;
    public static InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item_screen);

        Intent intent = getIntent();
        String[] values = intent.getStringArrayExtra("selectedItemValues");

        ImageView imageView = findViewById(R.id.gifPlace);

        /* from internet*/
        Glide.with(this)
                .load(R.raw.loading)
                //.load("https://media.giphy.com/media/98uBZTzlXMhkk/giphy.gif")
                .into(imageView);

        adRequest = new AdRequest.Builder().build();
   //     InterstitialAdmob();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        InterstitialAd.load(this,"ca-app-pub-1171969261272427/7922597483", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        //  Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when fullscreen content failed to show.
//                Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
//                Log.d("TAG", "The ad was shown.");
                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                loadAdError.getMessage();
                mInterstitialAd = null;
            }
        });



        showName = values[0];
        String description = values[1];
        imageHref = values[2];
        showId = values[3];
        type = values[4];
        accessToken = values[5];

        TextView editText = findViewById(R.id.label);

        editText.setText(type.equals("show") ? "Latest Episodes:" :  "Albums");

        boolean isExist = false;
        pojo = NoteUtil.getData(this);
        if (pojo != null && pojo.getItemsList() != null) {
            isExist = pojo.getItemsList().stream().anyMatch(str -> str.getItemId().equals(showId));
        }

        TextView tvName = (TextView) findViewById(R.id.itemName);
        tvName.setText(showName);

        TextView tvDes = (TextView) findViewById(R.id.itemDescription);
        if (type.equals("artist")) {
            tvDes.setVisibility(View.GONE);
        } else {
            tvDes.setText(description);
            tvDes.setMovementMethod(new ScrollingMovementMethod());
        }

        ImageView image = (ImageView) findViewById(R.id.selectedItemImage);
        Picasso.with(this).load(imageHref).into(image);

        setLastItemsList();
        Switch switch1 = (Switch) findViewById(R.id.switch1);

        switch1.setChecked(isExist);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Item item = new Item();
                    item.setItemId(showId);
                    item.setType(type);
                    item.setName(showName);
                    item.setHref(imageHref);
                    item.setLastUpdateDate(0L);
                    item.setDescription(description);
                    if (pojo == null) {
                        pojo = new SpotifyItemPOJO();
                        List<Item> itemList = new ArrayList<>();
                        pojo.setItemsList(itemList);
                    }

                    pojo.getItemsList().add(item);
                    NoteUtil.saveToFile(SelectedItemScreen.this, pojo);

                    Toast.makeText(getApplicationContext(), "You will get notifications for: " + showName, Toast.LENGTH_SHORT).show();
                     SchedulingUtil.scheduleWorkRequest(getApplicationContext());

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(SelectedItemScreen.this);
                    } else {
                        //Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }

//                    if (mInterstitialAd!=null){
//                        mInterstitialAd.show(SelectedItemScreen.this);
//                    }
//                    if (mInterstitialAd.isLoaded()) {
//                        mInterstitialAd.show();
//                    }

                    // SchedulingUtil.scheduleJob(getApplicationContext());
                } else {
                    try {

                        List<Item> updatedItemList = new ArrayList<>();

                        pojo.getItemsList().stream().forEach(litem -> {
                            if (!litem.getItemId().equals(showId)) {
                                updatedItemList.add(litem);
                            }
                        });
                        pojo.setItemsList(updatedItemList);
                        NoteUtil.saveToFile(SelectedItemScreen.this, pojo);
                        Toast.makeText(getApplicationContext(), "You will stop getting notifications for: " + showName, Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {

                    }
                }
            }
        });


    }

    public void setLastItemsList () {
        List<String> images = new ArrayList<>();
        List<String> namesList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.SPOTIFY_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface1 = retrofit.create(APIInterface.class);

       if (type.equals("show")) {
           Call<Episodes> call1 = apiInterface1.searchEpisodes("Bearer " + accessToken, showId, "50", "US"); // TBD - put all markets
           call1.enqueue(new Callback<Episodes>() {
               @Override
               public void onResponse(Call<Episodes> call, Response<Episodes> response) {
                   Episodes episodes = response.body();
                  if (episodes!= null) {
                      episodesList = episodes.getItemsList();

                      for (Episodes.Episode ep : episodes.getItemsList()) {
                          images.add(ep.getImageList().get(0).url);
                          namesList.add(ep.getName());
                      }
                      SearchResultsListAdapter adapter = new SearchResultsListAdapter(SelectedItemScreen.this, images, namesList);
                      ListView listView = findViewById(R.id.resList2);
                      listView.setAdapter(adapter);

                      listView.setOnItemClickListener((parent, view, position, id) -> {
                          Episodes.Episode current = episodesList.get(position);

                          Uri uri = Uri.parse(current.getUri()); // missing 'http://' will cause crashed
                          Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                          startActivity(intent);
                      });

                      ImageView imageView = findViewById(R.id.gifPlace);
                        imageView.setVisibility(View.INVISIBLE);
                      LinearLayout linearLayout = findViewById(R.id.linLayout);
                      linearLayout.removeView(imageView);
                      listView.setVisibility(View.VISIBLE);
                  }
               }

               @Override
               public void onFailure(Call<Episodes> call, Throwable t) {
                   call.cancel();


               }
           });
       } else if (type.equals("artist")) {

           Call<Albums> call1 = apiInterface1.searchAlbums("Bearer " + accessToken, showId, "50", "US"); // TBD - put all markets
           call1.enqueue(new Callback<Albums>() {
               @Override
               public void onResponse(Call<Albums> call, Response<Albums> response) {
                   Albums albums = response.body();
                   albumList = albums.getItemsList();

                   for (Albums.Album album : albumList) {
                       images.add(album.getImageList().get(0).url);
                       namesList.add(album.getName());
                   }
                   SearchResultsListAdapter adapter = new SearchResultsListAdapter(SelectedItemScreen.this, images, namesList);
                   ListView listView = findViewById(R.id.resList2);
                   listView.setAdapter(adapter);

                   listView.setOnItemClickListener((parent, view, position, id) -> {
                       Albums.Album  current = albumList.get(position);

                       Uri uri = Uri.parse(current.getUri()); // missing 'http://' will cause crashed
                       Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                       startActivity(intent);
                   });
                   ImageView imageView = findViewById(R.id.gifPlace);
                   imageView.setVisibility(View.INVISIBLE);
                   LinearLayout linearLayout = findViewById(R.id.linLayout);
                   linearLayout.removeView(imageView);
                   listView.setVisibility(View.VISIBLE);
               }

               @Override
               public void onFailure(Call<Albums> call, Throwable t) {
                   call.cancel();


               }
           });



       }


    }

    @Override
    public void onBackPressed() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(SelectedItemScreen.this);
        }
        super.onBackPressed();
    }



    public void InterstitialAdmob() {
        InterstitialAd.load(SelectedItemScreen.this,"ca-app-pub-3940256099942544~3347511713", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        mInterstitialAd=null;

                        //// perform your code that you wants todo after ad dismissed or closed


                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        mInterstitialAd = null;

                        /// perform your action here when ad will not load
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        mInterstitialAd = null;

                    }
                });

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;


            }

        });

    }
}
