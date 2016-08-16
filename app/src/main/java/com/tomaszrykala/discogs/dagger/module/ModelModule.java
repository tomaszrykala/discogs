package com.tomaszrykala.discogs.dagger.module;

import com.google.gson.GsonBuilder;
import com.tomaszrykala.discogs.data.local.RealmService;
import com.tomaszrykala.discogs.data.remote.DiscogsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ModelModule {

    @Provides
    @Singleton
    RealmService provideRealmService() {
        return new RealmService(Realm.getDefaultInstance());
    }

    @Provides
    @Singleton
    DiscogsService provideDiscogsService() {
        return Creator.create();
    }

    private static class Creator {

        static final String ENDPOINT = "http://api.discogs.com/";

        public static DiscogsService create() {

            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build();

            return retrofit.create(DiscogsService.class);
        }
    }
}
