package com.dvir.spotification.retrofit;

import com.google.gson.annotations.SerializedName;

public class RequestParam {

    @SerializedName("grant_type")
    private String grantType;

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
