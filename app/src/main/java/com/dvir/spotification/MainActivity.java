package com.dvir.spotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Room;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.retrofit.APIInterface;
import com.dvir.spotification.retrofit.ArtistJson;
import com.dvir.spotification.retrofit.ResponseJson;
import com.dvir.spotification.retrofit.ShowsJson;

import com.dvir.spotification.scheduling.SchedulingUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CLIENT_ID = "522fd1e3f5c74822a3cc00541dd067fe";
    public static final String WORK_TAG = "SPOTIFICATION_NOTIFY_JOB";
    public static final String SECRET_CLIENT_ID = "660595bab5e441db91554cfd387ef476";
    public static final int REQUEST_CODE = 1337;
    public static final String REDIRECT_URI = "yourcustomprotocol://callback";
    public static final String CHANNEL_ID = "638";
    private String accessToken = "";
    private static final String SPOTIFY_SEARCH_API = "https://api.spotify.com/v1/search";
    public static final String SPOTIFY_BASE_API = "https://api.spotify.com/";
    public static final String SPOTIFY_WEB_API_ENDPOINT = "https://accounts.spotify.com";
    //    APIInterface apiInterface;
    List<ShowsJson.Shows.Item> items = new ArrayList<>();
    List<ArtistJson.Artist.ArtistItem> artistItems = new ArrayList<>();
    private DrawerLayout drawer;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        Context context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.mainLayout);
        NavigationView nv = findViewById(R.id.nav_view);
        nv.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                LinearLayout linearLayout = findViewById(R.id.linLayoutMain);
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



//
//        AuthenticationRequest.Builder builder =
//                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
//
//        builder.setScopes(new String[]{"streaming", "playlist-read-private", "user-library-read"});
//        builder.setShowDialog(true);
//        AuthenticationRequest request = builder.build();
//
//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

//

        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl(SPOTIFY_WEB_API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIInterface apiInterface = retrofit1.create(APIInterface.class);

//        String authHeader = "Basic " + Base64.getEncoder().encodeToString(CLIENT_ID.getBytes()) + ":"
//                + Base64.getEncoder().encodeToString(SECRET_CLIENT_ID.getBytes());
        String headerString = CLIENT_ID + ":" + SECRET_CLIENT_ID;
        String authHeader = "Basic " + Base64.getEncoder().encodeToString(headerString.getBytes());

//        RequestParam rp = new RequestParam();
//        rp.setGrantType("client_credentials");
//
//        Gson gson = new Gson();
//        String body = gson.toJson(rp, RequestParam.class);
        Call<ResponseJson> call = apiInterface.getToken(authHeader, "client_credentials");
        call.enqueue(new Callback<ResponseJson>() {
            @Override
            public void onResponse(Call<ResponseJson> call, Response<ResponseJson> response) {
                ResponseJson rj = response.body();
                accessToken = rj.getAccessToken();
            }

            @Override
            public void onFailure(Call<ResponseJson> call, Throwable t) {
                call.cancel();

            }
        });


        SearchView searchView = (SearchView) findViewById(R.id.searchText);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchInSpotify(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

//        final Button searchButton = (Button) findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {

//            public void onClick(View v) {
//
//                ImageView gif = findViewById(R.id.gifPlaceMain);
//            if (gif != null) {
//                gif.setVisibility(View.VISIBLE);
//                Glide.with(MainActivity.this)
//                        .load(R.raw.loading)
//                        //.load("https://media.giphy.com/media/98uBZTzlXMhkk/giphy.gif")
//                        .into(gif);
//            }
//
////            LinearLayout linearLayout = findViewById(R.id.linLayout);
////                      linearLayout.removeView(imageView);
////                      listView.setVisibility(View.VISIBLE);
////                EditText searchTextBox = (EditText) findViewById(R.id.searchText);
//                String searchText = simpleSearchView.get
//                if (!searchText.isEmpty()) {
//                    RadioGroup rg =  findViewById(R.id.typeGroup);//radioGroup.getCheckedRadioButtonId()
//                    int typeId = rg.getCheckedRadioButtonId();
//                    String qType = ((typeId == R.id.radio_show) ? "show" : "artist");
//
//                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
//                    Retrofit retrofit = new Retrofit.Builder()
//                            .baseUrl(SPOTIFY_BASE_API)
//                            .addConverterFactory(GsonConverterFactory.create())
//                            .build();
//                    APIInterface apiInterface1 = retrofit.create(APIInterface.class);
//
////                    Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, "show", "10" );
//                    //Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, "show", "20", "US");
//                    if (qType.equals("show")) {
//                        Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, qType, "50", "US");
//                        call.enqueue(new Callback<ShowsJson>() {
//                            @Override
//                            public void onResponse(Call<ShowsJson> call, Response<ShowsJson> response) {
//                                ShowsJson shows = response.body();
//                                ListView listView = findViewById(R.id.resList);
//
//                                items = shows.getShows().getItemsList();
//                                List<String> images = new ArrayList<>();
//                                List<String> namesList = new ArrayList<>();
//                                for (ShowsJson.Shows.Item item : items) {
//
//                                    String urlString = item.getImageList().get(1).getUrl();
//                                    images.add(urlString);
//                                    namesList.add(item.getName());
//                                }
//                                SearchResultsListAdapter adapter = new SearchResultsListAdapter(MainActivity.this, images, namesList);
//                                listView.setAdapter(adapter);
//
//                                listView.setOnItemClickListener((parent, view, position, id) -> {
//                                    Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
//                                    ShowsJson.Shows.Item item = items.get(position);
//
//                                    String[] selectedItemValues = new String[6];
//
//                                    selectedItemValues[0] = item.getName();
//                                    selectedItemValues[1] = item.getDescription();
//                                    selectedItemValues[2] = item.getImageList().get(1).getUrl();
//                                    selectedItemValues[3] = item.getId();
//                                    selectedItemValues[4] = "show";
//                                    selectedItemValues[5] = accessToken;
//
//                                    intent.putExtra("selectedItemValues", selectedItemValues);
//                                    startActivity(intent);
//                                });
//
//                                ImageView imageView = findViewById(R.id.gifPlaceMain);
//                                if (imageView != null) {
//                                    imageView.setVisibility(View.INVISIBLE);
//                                    LinearLayout linearLayout = findViewById(R.id.linLayoutMain);
//                                    linearLayout.removeView(imageView);
//                                }
//                                listView.setVisibility(View.VISIBLE);
//                            }
//
//
//                            @Override
//                            public void onFailure(Call<ShowsJson> call, Throwable t) {
//                                call.cancel();
//
//                            }
//                        });
//                    } else if (qType.equals("artist")) {
//                        Call<ArtistJson> call = apiInterface1.searchForArtist("Bearer " + accessToken, searchText, qType, "20", "US");
//                        call.enqueue(new Callback<ArtistJson>() {
//                            @Override
//                            public void onResponse(Call<ArtistJson> call, Response<ArtistJson> response) {
//                                ArtistJson artistJson = response.body();
//                                ListView listView = findViewById(R.id.resList);
//
//                                artistItems  = artistJson.getArtists().getItemsList();
//                                List<String> images = new ArrayList<>();
//                                List<String> namesList = new ArrayList<>();
//                                for (ArtistJson.Artist.ArtistItem item : artistItems) {
//                                    String urlString = "" ;
//                                    if (item.getImageList() != null && item.getImageList().size() > 1) {
//                                        urlString = item.getImageList().get(1).getUrl();
//                                    } else if (item.getImageList().size() == 1) {
//                                        urlString = item.getImageList().get(0).getUrl();
//                                    } else {// TBD - no image found
//
//                                    }
//
//                                    images.add(urlString);
//                                    namesList.add(item.getName());
//                                }
//                                SearchResultsListAdapter adapter = new SearchResultsListAdapter(MainActivity.this, images, namesList);
//                                listView.setAdapter(adapter);
//                                listView.setOnItemClickListener((parent, view, position, id) -> {
//                                    Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
//                                    ArtistJson.Artist.ArtistItem item = artistItems.get(position);
//
//                                    String[] selectedItemValues = new String[6];
//
//                                    selectedItemValues[0] = item.getName();
//                                    selectedItemValues[1] = "";
//                                    selectedItemValues[2] = item.getImageList().get(1).getUrl();
//                                    selectedItemValues[3] = item.getId();
//                                    selectedItemValues[4] = "artist";
//                                    selectedItemValues[5] = accessToken;
//                                    intent.putExtra("selectedItemValues", selectedItemValues);
//                                    startActivity(intent);
//                                });
//                                ImageView imageView = findViewById(R.id.gifPlaceMain);
//                                if (imageView != null) {
//                                    imageView.setVisibility(View.INVISIBLE);
//                                    LinearLayout linearLayout = findViewById(R.id.linLayoutMain);
//                                    linearLayout.removeView(imageView);
//
//                                }
//                                listView.setVisibility(View.VISIBLE);
//                            }
//
//
//                            @Override
//                            public void onFailure(Call<ArtistJson> call, Throwable t) {
//                                call.cancel();
//
//                            }
//                        });
//
//                    }
//                }
//            }
//        });

    }

    private void searchInSpotify (String searchText) {
        SearchView searchView = (SearchView) findViewById(R.id.searchText);
        ImageView gif = findViewById(R.id.gifPlaceMain);
        if (gif != null) {
            gif.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this)
                    .load(R.raw.loading)
                    //.load("https://media.giphy.com/media/98uBZTzlXMhkk/giphy.gif")
                    .into(gif);
        }

//            LinearLayout linearLayout = findViewById(R.id.linLayout);
//                      linearLayout.removeView(imageView);
//                      listView.setVisibility(View.VISIBLE);
//                EditText searchTextBox = (EditText) findViewById(R.id.searchText);

        if (!searchText.isEmpty()) {
            RadioGroup rg =  findViewById(R.id.typeGroup);//radioGroup.getCheckedRadioButtonId()
            int typeId = rg.getCheckedRadioButtonId();
            String qType = ((typeId == R.id.radio_show) ? "show" : "artist");

//            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SPOTIFY_BASE_API)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            APIInterface apiInterface1 = retrofit.create(APIInterface.class);

//                    Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, "show", "10" );
            //Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, "show", "20", "US");
            if (qType.equals("show")) {
                Call<ShowsJson> call = apiInterface1.searchSpotify("Bearer " + accessToken, searchText, qType, "50", "US");
                call.enqueue(new Callback<ShowsJson>() {
                    @Override
                    public void onResponse(Call<ShowsJson> call, Response<ShowsJson> response) {
                        ShowsJson shows = response.body();
                        ListView listView = findViewById(R.id.resList);

                        items = shows.getShows().getItemsList();
                        List<String> images = new ArrayList<>();
                        List<String> namesList = new ArrayList<>();
                        for (ShowsJson.Shows.Item item : items) {

                            String urlString = item.getImageList().get(1).getUrl();
                            images.add(urlString);
                            namesList.add(item.getName());
                        }
                        SearchResultsListAdapter adapter = new SearchResultsListAdapter(MainActivity.this, images, namesList);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
                            ShowsJson.Shows.Item item = items.get(position);

                            String[] selectedItemValues = new String[6];

                            selectedItemValues[0] = item.getName();
                            selectedItemValues[1] = item.getDescription();
                            selectedItemValues[2] = item.getImageList().get(1).getUrl();
                            selectedItemValues[3] = item.getId();
                            selectedItemValues[4] = "show";
                            selectedItemValues[5] = accessToken;

                            intent.putExtra("selectedItemValues", selectedItemValues);
                            startActivity(intent);
                        });

                        ImageView imageView = findViewById(R.id.gifPlaceMain);
                        if (imageView != null) {
                            imageView.setVisibility(View.INVISIBLE);
                            LinearLayout linearLayout = findViewById(R.id.linLayoutMain);
                            linearLayout.removeView(imageView);
                        }
                        listView.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void onFailure(Call<ShowsJson> call, Throwable t) {
                        call.cancel();

                    }
                });
            } else if (qType.equals("artist")) {
                Call<ArtistJson> call = apiInterface1.searchForArtist("Bearer " + accessToken, searchText, qType, "20", "US");
                call.enqueue(new Callback<ArtistJson>() {
                    @Override
                    public void onResponse(Call<ArtistJson> call, Response<ArtistJson> response) {
                        ArtistJson artistJson = response.body();
                        ListView listView = findViewById(R.id.resList);

                        artistItems  = artistJson.getArtists().getItemsList();
                        List<String> images = new ArrayList<>();
                        List<String> namesList = new ArrayList<>();
                        for (ArtistJson.Artist.ArtistItem item : artistItems) {
                            String urlString = "" ;
                            if (item.getImageList() != null && item.getImageList().size() > 1) {
                                urlString = item.getImageList().get(1).getUrl();
                            } else if (item.getImageList().size() == 1) {
                                urlString = item.getImageList().get(0).getUrl();
                            } else {// TBD - no image found

                            }

                            images.add(urlString);
                            namesList.add(item.getName());
                        }
                        SearchResultsListAdapter adapter = new SearchResultsListAdapter(MainActivity.this, images, namesList);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Intent intent = new Intent(view.getContext(), SelectedItemScreen.class);
                            ArtistJson.Artist.ArtistItem item = artistItems.get(position);

                            String[] selectedItemValues = new String[6];

                            selectedItemValues[0] = item.getName();
                            selectedItemValues[1] = "";
                            selectedItemValues[2] = item.getImageList().get(1).getUrl();
                            selectedItemValues[3] = item.getId();
                            selectedItemValues[4] = "artist";
                            selectedItemValues[5] = accessToken;
                            intent.putExtra("selectedItemValues", selectedItemValues);
                            startActivity(intent);
                        });
                        ImageView imageView = findViewById(R.id.gifPlaceMain);
                        if (imageView != null) {
                            imageView.setVisibility(View.INVISIBLE);
                            LinearLayout linearLayout = findViewById(R.id.linLayoutMain);
                            linearLayout.removeView(imageView);

                        }
                        listView.setVisibility(View.VISIBLE);
                    }


                    @Override
                    public void onFailure(Call<ArtistJson> call, Throwable t) {
                        call.cancel();

                    }
                });

            }
        }
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    ;

    @Override
    protected void onStop() {

        super.onStop();
        //   SchedulingUtil.scheduleWorkRequest(getApplicationContext());
//       SchedulingUtil.scheduleJob(getApplicationContext());
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
////intent.getBundleExtra("EXTRA_AUTH_RESPONSE");
//            switch (response.getType()) {
//                // Response was successful and contains auth token
//                case TOKEN:
//                    // Handle successful response
//                    accessToken = response.getAccessToken();
//                    response.getCode();
//
//                    Toast.makeText(getApplicationContext(),"Successfull login to Spotify!",Toast.LENGTH_SHORT).show();
//
//
//                    break;
//
//                // Auth flow returned an error
//                case ERROR:
//                    // Handle error response
//                    Toast.makeText(getApplicationContext(),"Error while login to Spotify! Try again",Toast.LENGTH_SHORT).show();
//                    break;
//
//                // Most likely auth flow was cancelled
//                default:
//                    // Handle other cases
//            }
//        }
//    }

    //    public void searchForLatestEpisose(){
//        Call<Episodes> call = apiInterface.doGetListResources("Bearer " + accessToken);
////                    call.request().a  eader(""
//        call.enqueue(new Callback<Episodes>() {
//            @Override
//            public void onResponse(Call<Episodes> call, Response<Episodes> response) {
//                Episodes episodes = response.body();
//                List<Episodes.Item> items = episodes.itemsList;
//                for (Episodes.Item item : items){
//
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<Episodes> call, Throwable t) {
//                call.cancel();
//
//            }
//        });
//    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_show:
                if (checked)
                    // Pirates are the best
                    break;
            case R.id.radio_artist:
                if (checked)
                    // Ninjas rule
                    break;
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "myTv";
            String description = "itsmylife";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_shows) {
            Intent intent = new Intent(getApplicationContext(), MyCollection.class);

            String[] selectedItemValues = new String[2];

            selectedItemValues[0] = "show";
            selectedItemValues[1] = accessToken;
            intent.putExtra("selectedItemValues", selectedItemValues);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_artists) {
            NoteUtil.getlog(getApplicationContext());
            Intent intent1 = new Intent(getApplicationContext(), MyCollection.class);
            String[] selectedItemValues1 = new String[2];

            selectedItemValues1[0] = "artist";
            selectedItemValues1[1] = accessToken;

            intent1.putExtra("selectedItemValues", selectedItemValues1);
            startActivity(intent1);
//        } else if ( item.getItemId() == R.id.showLog) {
//            NoteUtil.getlog(getApplicationContext());
//            Intent intent2 = new Intent(getApplicationContext(), printAllData.class);
//            startActivity(intent2);
        } else if ( item.getItemId() == R.id.about) {
            Intent intent2 = new Intent(getApplicationContext(), AboutPage.class);
             startActivity(intent2);
        }



//        switch (item.getItemId()) {
//            case R.id.nav_shows:
//                Intent intent = new Intent(getApplicationContext(), MyCollection.class);
//
//                String[] selectedItemValues = new String[2];
//
//                selectedItemValues[0] = "show";
//                selectedItemValues[1] = accessToken;
//                intent.putExtra("selectedItemValues", selectedItemValues);
//                startActivity(intent);
//
//                startActivity(intent);
//                break;
//
//            case R.id.nav_artists:
//                NoteUtil.getlog(getApplicationContext());
//                Intent intent1 = new Intent(getApplicationContext(), MyCollection.class);
//                String[] selectedItemValues1 = new String[2];
//
//                selectedItemValues1[0] = "artist";
//                selectedItemValues1[1] = accessToken;
//
//                intent1.putExtra("selectedItemValues", selectedItemValues1);
//                startActivity(intent1);
//                break;
//            case R.id.showLog:
//                NoteUtil.getlog(getApplicationContext());
//                Intent intent2 = new Intent(getApplicationContext(), printAllData.class);
//                startActivity(intent2);
//                break;
//
//
//            case R.id.about:
//                break;
//        }
        return true;
    }

    //        final Button printData = (Button) findViewById(R.id.button2);
//        printData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                NoteUtil.getlog(getApplicationContext());
//////                SchedulingUtil.scheduleWorkRequest(getApplicationContext());
//////                SchedulingUtil.scheduleJob(getApplicationContext());
////                Intent intent = new Intent(v.getContext(), printAllData.class);
////                startActivity(intent);
//            }
//        });
}