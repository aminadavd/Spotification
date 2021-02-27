package com.dvir.spotification.retrofit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowsJson {
    @SerializedName("shows")
    public Shows shows;

    public Shows getShows() {
        return shows;
    }

    public void setShows(Shows shows) {
        this.shows = shows;
    }

    public class Shows {
    @SerializedName("href")
    public String href;

    @SerializedName("items")
    public List<Item> itemsList;

    @SerializedName("limit")
    public Integer limit;
    @SerializedName("next")
    public String next;
    @SerializedName("offset")
    public Integer offset;
    @SerializedName("previous")
    public String previous;
    @SerializedName("total")
    public Integer total;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public class Item {

        @SerializedName("available_markets")
        public List<String> availableMarketsList;

        @SerializedName("copyrights")
        public List<String> copyrights;

        @SerializedName("description")
        public String description;

        @SerializedName("explicit")
                public boolean isExplicit;

        @SerializedName("external_urls")
        public ExternalUrl externalUrl;

        @SerializedName("href")
        public String href;

        @SerializedName("id")
        public String id;

        @SerializedName("images")
        public List<Image> imageList;

        @SerializedName("is_externally_hosted")
        public boolean isExternallyHosted;

        @SerializedName("languages")
        public List<String> languages;

        @SerializedName("media_type")
        public String mediaType;


        @SerializedName("name")
        public String name;

        @SerializedName("publisher")
        public String publisher;

        @SerializedName("total_episodes")
        public Integer totalEpisodes;

        @SerializedName("type")
        public String type;

        @SerializedName("uri")
        public String uri;

        public List<String> getAvailableMarketsList() {
            return availableMarketsList;
        }

        public void setAvailableMarketsList(List<String> availableMarketsList) {
            this.availableMarketsList = availableMarketsList;
        }

        public List<String> getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(List<String> copyrights) {
            this.copyrights = copyrights;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isExplicit() {
            return isExplicit;
        }

        public void setExplicit(boolean explicit) {
            isExplicit = explicit;
        }

        public ExternalUrl getExternalUrl() {
            return externalUrl;
        }

        public void setExternalUrl(ExternalUrl externalUrl) {
            this.externalUrl = externalUrl;
        }

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<Image> getImageList() {
            return imageList;
        }

        public void setImageList(List<Image> imageList) {
            this.imageList = imageList;
        }

        public boolean isExternallyHosted() {
            return isExternallyHosted;
        }

        public void setExternallyHosted(boolean externallyHosted) {
            isExternallyHosted = externallyHosted;
        }

        public List<String> getLanguages() {
            return languages;
        }

        public void setLanguages(List<String> languages) {
            this.languages = languages;
        }

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public Integer getTotalEpisodes() {
            return totalEpisodes;
        }

        public void setTotalEpisodes(Integer totalEpisodes) {
            this.totalEpisodes = totalEpisodes;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public class ExternalUrl {
        @SerializedName("spotify")
        public String spotify;

    }

    public class Image {
        @SerializedName("height")
        public Integer height;

        @SerializedName("url")
        public String url;

        @SerializedName("width")
        public Integer width;

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }
    }
}
}
