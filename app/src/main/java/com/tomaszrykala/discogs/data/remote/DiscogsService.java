package com.tomaszrykala.discogs.data.remote;

import com.tomaszrykala.discogs.BuildConfig;
import com.tomaszrykala.discogs.data.model.Label;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DiscogsService {

    // contstants
    int MAX_RESULTS = 100;

    String CONSUMER_KEY_AND_SECRET_SUFFIX =
            "&key=" + BuildConfig.DISCOGS_CONSUMER_KEY + "&secret=" + BuildConfig.DISCOGS_CONSUMER_SECRET;

    // api calls
    @GET("labels/333447/releases?per_page=" + MAX_RESULTS + CONSUMER_KEY_AND_SECRET_SUFFIX)
    Call<Label> getLabel();

    @GET("labels/333447/releases?per_page=" + MAX_RESULTS + "&page={page}" + CONSUMER_KEY_AND_SECRET_SUFFIX)
    Call<Label> getLabel(@Path("page") int page);
}