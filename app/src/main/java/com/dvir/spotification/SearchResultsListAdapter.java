package com.dvir.spotification;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchResultsListAdapter extends ArrayAdapter {

    private List<String> imagesList;
    private List<String> namesList;
    private Activity context;

    public SearchResultsListAdapter(@NonNull Activity context, List<String> imagesList, List<String> namesList) {
        super(context, R.layout.search_item);
        this.context = context;
        this.imagesList = imagesList;
        this.namesList = namesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.search_item, null, true);
        TextView textViewCountry = (TextView) row.findViewById(R.id.name);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewFlag);
        if (!imagesList.get(position).isEmpty()) {
            Picasso.with(context).load(imagesList.get(position)).into(imageFlag);
        }

        textViewCountry.setText(namesList.get(position));

//        TextView pos =  row.findViewById(R.id.position);
//        pos.setText(position);

        return  row;
    }

    @Override
    public int getCount() {

        return imagesList.size();
    }


}
