package com.tomaszrykala.discogs.data.remote;

import com.tomaszrykala.discogs.data.model.Label;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface DiscogsService {

    // api constants
    int MAX_RESULTS = 100;

    // demo account key & secret
    String API_KEY = "HToYCsqnEPPlxEcMGOnR";
    String API_SECRET = "eIwJatslNmlySmFhuZYVIxtIxbLxjICM";
    String CONSUMER_KEY_AND_SECRET_SUFFIX = "&key=" + API_KEY + "&secret=" + API_SECRET;

    // api calls
    @GET("labels/333447/releases?per_page=" + MAX_RESULTS + CONSUMER_KEY_AND_SECRET_SUFFIX)
    Observable<Label> getLabel();

    @GET("labels/333447/releases?per_page=" + MAX_RESULTS + CONSUMER_KEY_AND_SECRET_SUFFIX)
    Observable<Label> getLabel(@Query("page") int page);
}