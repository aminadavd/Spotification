package com.dvir.spotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.Albums;
import com.dvir.spotification.retrofit.Episodes;
import com.dvir.spotification.retrofit.ShowsJson;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class MyCollection  extends AppCompatActivity {


    private String showId = "";
    private String type = "";
    private String imageHref = "";
    SpotifyItemPOJO pojo;
    String accessToken = "";
    List<Episodes.Episode> episodesList = null;
    List<Albums.Album> albumList = null;
    List<Item> itemList = new ArrayList<>();
    private AdView mAdView;
    public static  AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);



        Context context = getApplicationContext();

        Intent intent1 = getIntent();
        String[] values = intent1.getStringArrayExtra("selectedItemValues");

        type = values[0];
        accessToken = values[1];

        String title = type.equals("show") ? "My Shows" :  "My Artists";

        adRequest = new AdRequest.Builder().build();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                LinearLayout linearLayout = findViewById(R.id.linLayCol);
                linearLayout.removeView(mAdView);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }



            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });



        getSupportActionBar().setTitle(title);
        List<String> images = new ArrayList<>();
        List<String> namesList = new ArrayList<>();

        pojo = NoteUtil.getData(context);

        for (Item item : pojo.getItemsList()) {
            if (item.getType().equals(type) ) {
                String urlString = item.getHref();
                images.add(urlString);
                namesList.add(item.getName());
                itemList.add(item);
            }
        }
        ListView listView = findViewById(R.id.myShows);

        SearchResultsListAdapter adapter = new SearchResultsListAdapter(MyCollection.this, images, namesList);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width = metrics.widthPixels;
//        listView.setLayoutParams(new ViewGroup.LayoutParams(width, 78));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
            Item item = itemList.get(position);

            String[] selectedItemValues = new String[6];

            selectedItemValues[0] = item.getName();
            selectedItemValues[1] = item.getDescription(); //TBD - SAVE DESCRIPTION
            selectedItemValues[2] = item.getHref();
            selectedItemValues[3] = item.getItemId();
            selectedItemValues[4] = type;
            selectedItemValues[5] = accessToken;

            intent.putExtra("selectedItemValues", selectedItemValues);
            startActivity(intent);
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}