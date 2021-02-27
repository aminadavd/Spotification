package com.dvir.spotification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.dvir.spotification.note.NoteUtil;
import com.dvir.spotification.note.SpotifyItemPOJO;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class printAllData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_all_data);
        String text = NoteUtil.getlog(getApplicationContext());

//        SpotifyItemPOJO pojo = NoteUtil.getData(getApplicationContext());
//        Gson gson = new Gson();
//        String text = gson.toJson(pojo, SpotifyItemPOJO.class);
        TextView textView = findViewById(R.id.textView2);

        textView.setText(text);

        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}