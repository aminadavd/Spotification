package com.dvir.spotification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.scheduling.SchedulingUtil;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;

import com.google.android.gms.ads.InterstitialAd;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SelectedItemScreen extends AppCompatActivity {

    private String showId = "";
    private String showName = "";
    private String imageHref = "";
    SpotifyItemPOJO pojo;
    String type = "";
    String accessToken = "";

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_item_screen);

        Intent intent = getIntent();
        String[] values = intent.getStringArrayExtra("selectedItemValues");

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        showName = values[0];
        String description = values[1];
        imageHref = values[2];
        showId = values[3];
        type = values[4];


        boolean isExist = false;
        pojo = NoteUtil.getData(this);
        if (pojo != null && pojo.getItemsList() != null) {
            isExist = pojo.getItemsList().stream().anyMatch(str -> str.getItemId().equals(showId));
        }

        TextView tvName = (TextView) findViewById(R.id.itemName);
        TextView tvDes = (TextView) findViewById(R.id.itemDescription);
        ImageView image = (ImageView) findViewById(R.id.selectedItemImage);

        Picasso.with(this).load(imageHref).into(image);

        tvName.setText(showName);
        tvDes.setText(description);

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

                    if (pojo == null) {
                        pojo = new SpotifyItemPOJO();
                        List<Item> itemList = new ArrayList<>();
                        pojo.setItemsList(itemList);
                    }

                    pojo.getItemsList().add(item);
                    NoteUtil.saveToFile(SelectedItemScreen.this, pojo);

                    Toast.makeText(getApplicationContext(), "You will get notifications for: " + showName, Toast.LENGTH_SHORT).show();
                     SchedulingUtil.scheduleWorkRequest(getApplicationContext());

                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }

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
}
