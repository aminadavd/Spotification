package com.dvir.spotification.note;

import android.content.Context;

import com.dvir.spotification.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NoteUtil {

    public static final String fileName = "data.json";
    public static final String אtokenFileName = "token.json";
    public static final String  logName = "service.exe.log";

    public static boolean saveToFile(Context context, SpotifyItemPOJO pojo){
            Gson gson = new Gson();
            String data = gson.toJson(pojo,SpotifyItemPOJO.class);

            File filesDir = context.getFilesDir();
            try {
                File gpxfile = new File(filesDir, fileName);
                FileWriter writer = new FileWriter(gpxfile);
                writer.write(data);
                writer.flush();
                writer.close();
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return true;
    }

    public static void writeTolog(Context context, String data){

        File filesDir = context.getFilesDir();
        String log = getlog(context);
        data = log + " \n" + data;
        try {
            File gpxfile = new File(filesDir, logName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();

        }
    }
    public static String getlog(Context context){
        File filesDir = context.getFilesDir();

        File gpxfile = new File(filesDir, logName);
        StringBuffer  data = new StringBuffer();
        InputStream is = null;
        try {
            is = new FileInputStream(gpxfile);

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String readLine = null;
            try {
                while ((readLine = br.readLine()) != null) {
                    data.append(readLine);
                }

                // Close the InputStream and BufferedReader
                is.close();
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            };


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data.toString();
    }


    public static SpotifyItemPOJO getData (Context context){

            File filesDir = context.getFilesDir();

            File gpxfile = new File(filesDir, fileName);
        StringBuffer  data = new StringBuffer();
        InputStream is = null;
        try {
            is = new FileInputStream(gpxfile);

             BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String readLine = null;
            try {
                while ((readLine = br.readLine()) != null) {
                    data.append(readLine);
                }

                // Close the InputStream and BufferedReader
                is.close();
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            };


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            Gson gson = new Gson();
            SpotifyItemPOJO pojo = gson.fromJson(data.toString(), SpotifyItemPOJO.class);
            return  pojo;
    }

    public static boolean replaceOne(Context context, Item item){
        SpotifyItemPOJO pojo = getData(context);
        List<Item> upadtedList = new ArrayList<>();
        upadtedList.add(item);
        pojo.getItemsList().forEach(litem -> {
            if (!litem.getItemId().equals(item.getItemId())) {
                upadtedList.add(litem);
            }
        });
        pojo.setItemsList(upadtedList);
        saveToFile(context, pojo);

        return true;
    }

    public static boolean saveTokenFile(Context context, SpotifyItemPOJO pojo){
        Gson gson = new Gson();
        String data = gson.toJson(pojo,SpotifyItemPOJO.class);

        File filesDir = context.getFilesDir();
        try {
            File gpxfile = new File(filesDir, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(data);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
