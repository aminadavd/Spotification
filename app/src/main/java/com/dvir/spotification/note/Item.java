package com.dvir.spotification.note;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("item_id")
    public String itemId;

    @SerializedName("type")
    public String type;

    @SerializedName("name")
    public String name;

    @SerializedName("href")
    public String href;

    @SerializedName("last_updated")
    public Long lastUpdateDate;

    @SerializedName("last_episode_id")
    public String lastEpisodeId;

    @SerializedName("description")
    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastEpisodeId() {
        return lastEpisodeId;
    }

    public void setLastEpisodeId(String lastEpisodeId) {
        this.lastEpisodeId = lastEpisodeId;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Long lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}