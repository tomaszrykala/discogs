package com.tomaszrykala.discogs.data.remote;

import com.google.gson.GsonBuilder;
import com.tomaszrykala.discogs.data.model.Label;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface DiscogsService {

    @GET("labels/333447/releases")
    Call<Label> getLabel();

    String ENDPOINT = "http://api.discogs.com/";

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