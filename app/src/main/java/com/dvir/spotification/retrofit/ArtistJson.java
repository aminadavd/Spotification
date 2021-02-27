package com.dvir.spotification.retrofit;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistJson {
    @SerializedName("artists")
    public Artist artists;

    public Artist getArtists() {
        return artists;
    }

    public void setArtists(Artist artists) {
        this.artists = artists;
    }

    public class Artist {
        @SerializedName("href")
        public String href;

        @SerializedName("items")
        public List<ArtistItem> itemsList;

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

        public List<ArtistItem> getItemsList() {
            return itemsList;
        }

        public void setItemsList(List<ArtistItem> itemsList) {
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

        public class ArtistItem {

            @SerializedName("id")
            public String id;

            @SerializedName("name")
            public String name;

            @SerializedName("external_urls")
            public ExternalUrl externalUrl;

            @SerializedName("followers")
            public Followers followers;


            @SerializedName("images")
            public List<Image> imageList;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public ExternalUrl getExternalUrl() {
                return externalUrl;
            }

            public void setExternalUrl(ExternalUrl externalUrl) {
                this.externalUrl = externalUrl;
            }

            public Followers getFollowers() {
                return followers;
            }

            public void setFollowers(Followers followers) {
                this.followers = followers;
            }

            public List<Image> getImageList() {
                return imageList;
            }

            public void setImageList(List<Image> imageList) {
                this.imageList = imageList;
            }
        }

        public class ExternalUrl {
            @SerializedName("spotify")
            public String spotify;

        }

        public class Followers {
            @SerializedName("href")
            public String href;

            @SerializedName("total")
            public String total;
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
