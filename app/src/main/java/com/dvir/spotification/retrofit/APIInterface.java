package com.dvir.spotification.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("/v1/shows/{id}/episodes")
    Call<Episodes> searchEpisodes(@Header("Authorization") String token, @Path("id") String id,  @Query("limit") String limit,  @Query("market") String market);


    @GET("/v1/search")
    Call<ShowsJson> searchSpotify(@Header("Authorization") String token,
                                  @Query("q") String query, @Query("type") String type,@Query("limit") String limit,  @Query("market") String market);

    @GET("/v1/search")
    Call<ArtistJson> searchForArtist(@Header("Authorization") String token,
                                  @Query("q") String query, @Query("type") String type,@Query("limit") String limit,  @Query("market") String market);


    @GET("/v1/search")
    Call<JsonObject> searchSpotifyReturnJson(@Header("Authorization") String token, @Query("q") String query, @Query("type") String type, @Query("limit") String limit);


    @FormUrlEncoded
  @POST("/api/token")
    Call<ResponseJson> getToken(@Header("Authorization") String basicAuth,
                                @Field("grant_type") String grantType);
}
