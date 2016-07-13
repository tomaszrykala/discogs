package com.tomaszrykala.discogs.data.remote;

import com.google.gson.GsonBuilder;
import com.tomaszrykala.discogs.data.model.Label;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DiscogsService {

    @GET("labels/333447/releases?per_page=" + MAX_RESULTS)
    Call<Label> getLabel();

    @GET("labels/333447/releases?per_page=" + MAX_RESULTS + "&page={page}")
    Call<Label> getLabel(@Path("page") int page);

    String ENDPOINT = "http://api.discogs.com/";
    int MAX_RESULTS = 100;

    class Creator {

        public static DiscogsService create() {
            final GsonConverterFactory gsonNoExpose = GsonConverterFactory.create(
                    new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create());

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addConverterFactory(gsonNoExpose)
                    .build();

            return retrofit.create(DiscogsService.class);
        }
    }
}