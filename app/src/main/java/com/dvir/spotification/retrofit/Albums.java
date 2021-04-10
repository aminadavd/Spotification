package com.dvir.spotification.retrofit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Albums {
    @SerializedName("href")
    public String href;

    @SerializedName("items")
    public List<Album> itemsList;

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

    public List<Album> getItemsList() {
        return itemsList;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getNext() {
        return next;
    }

    public Integer getOffset() {
        return offset;
    }

    public String getPrevious() {
        return previous;
    }

    public Integer getTotal() {
        return total;
    }

    public class Album {

        @SerializedName("id")
        public String id;

        @SerializedName("images")
        public List<Image> imageList;

        @SerializedName("name")
        public String name;

        @SerializedName("release_date")
        public String releaseDate;

        @SerializedName("release_date_precision")
        public String releaseDatePrecision;

        @SerializedName("type")
        public String type;

        @SerializedName("uri")
        public String uri;



        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public List<Image> getImageList() {
            return imageList;
        }


        public String getName() {
            return name;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public String getReleaseDatePrecision() {
            return releaseDatePrecision;
        }

        public String getType() {
            return type;
        }

        public String getUri() {
            return uri;
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
    }
}
