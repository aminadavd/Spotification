package com.dvir.spotification.note;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpotifyItemPOJO {

    @SerializedName("items")
    public List<Item> itemsList;

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

}
