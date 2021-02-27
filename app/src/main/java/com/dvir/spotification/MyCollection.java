package com.dvir.spotification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.dvir.spotification.note.Item;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;
import com.dvir.spotification.retrofit.ShowsJson;

import java.util.ArrayList;
import java.util.List;

public class MyCollection  extends AppCompatActivity {


    private String showId = "";
    private String type = "";
    private String imageHref = "";
    SpotifyItemPOJO pojo;
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        Context context = getApplicationContext();

        Intent intent1 = getIntent();
        String[] values = intent1.getStringArrayExtra("selectedItemValues");

        type = values[0];

        List<String> images = new ArrayList<>();
        List<String> namesList = new ArrayList<>();

        pojo = NoteUtil.getData(context);

        for (Item show : pojo.getItemsList()) {
            if (show.getType().equals(type) ) {
                String urlString = show.getHref();
                images.add(urlString);
                namesList.add(show.getName());
            }
        }
        ListView listView = findViewById(R.id.myShows);

        SearchResultsListAdapter adapter = new SearchResultsListAdapter(MyCollection.this, images, namesList);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
            Item item = pojo.getItemsList().get(position);

            String[] selectedItemValues = new String[5];

            selectedItemValues[0] = item.getName();
            selectedItemValues[1] = ""; //TBD - SAVE DESCRIPTION
            selectedItemValues[2] = item.getHref();
            selectedItemValues[3] = item.getItemId();

            intent.putExtra("selectedItemValues", selectedItemValues);
            startActivity(intent);
        });


    }
}