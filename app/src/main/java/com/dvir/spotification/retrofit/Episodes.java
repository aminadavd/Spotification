package com.dvir.spotification.retrofit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Episodes {
    @SerializedName("href")
    public String href;

    @SerializedName("items")
    public List<Episode> itemsList;

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

    public List<Episode> getItemsList() {
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

    public class Episode {

        @SerializedName("audio_preview_url")
        public String audioPreviewUrl;

        @SerializedName("description")
        public String description;

        @SerializedName("duration_ms")
        public Integer durationMs;

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

        @SerializedName("is_playable")
        public boolean isPlayable;

        @SerializedName("language")
        public String language;

        @SerializedName("languages")
        public List<String> languages;

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

        public String getAudioPreviewUrl() {
            return audioPreviewUrl;
        }

        public String getDescription() {
            return description;
        }

        public Integer getDurationMs() {
            return durationMs;
        }

        public boolean isExplicit() {
            return isExplicit;
        }

        public ExternalUrl getExternalUrl() {
            return externalUrl;
        }

        public String getHref() {
            return href;
        }

        public String getId() {
            return id;
        }

        public List<Image> getImageList() {
            return imageList;
        }

        public boolean isExternallyHosted() {
            return isExternallyHosted;
        }

        public boolean isPlayable() {
            return isPlayable;
        }

        public String getLanguage() {
            return language;
        }

        public List<String> getLanguages() {
            return languages;
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
