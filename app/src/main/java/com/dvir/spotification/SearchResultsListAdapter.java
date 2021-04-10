package com.dvir.spotification;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;


public class SearchResultsListAdapter extends ArrayAdapter {

    private List<String> imagesList;
    private List<String> namesList;
    private Activity context;
    int textWidth;
    int imageWidth;

    public SearchResultsListAdapter(@NonNull Activity context, List<String> imagesList, List<String> namesList) {
        super(context, R.layout.search_item);
        this.context = context;
        this.imagesList = imagesList;
        this.namesList = namesList;

        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        textWidth = (int) (metrics.widthPixels * 0.72);
        imageWidth = (int) (metrics.widthPixels * 0.265);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.search_item, null, true);
        TextView textView = (TextView) row.findViewById(R.id.name);

        textView.setLayoutParams(new LinearLayout.LayoutParams(textWidth, imageWidth));
        ImageView imageView = (ImageView) row.findViewById(R.id.imageViewFlag);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageWidth));

        if (!imagesList.get(position).isEmpty()) {
            Picasso.with(context).load(imagesList.get(position)).into(imageView);
        }

        textView.setText(namesList.get(position));
//        textView.setOnClickListener
//                (new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        imageView.callOnClick();
//                    }
//                });

        return  row;
    }

    @Override
    public int getCount() {

        return imagesList.size();
    }


}
