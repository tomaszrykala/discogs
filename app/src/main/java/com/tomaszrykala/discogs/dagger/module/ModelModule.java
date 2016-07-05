package com.tomaszrykala.discogs.dagger.module;

import com.tomaszrykala.discogs.data.local.RealmService;
import com.tomaszrykala.discogs.data.remote.DiscogsService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class ModelModule {

    @Provides @Singleton Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton RealmService provideRealmService(final Realm realm) {
        return new RealmService(realm);
    }

    @Provides
    @Singleton
    DiscogsService provideDiscogsService() {
        return DiscogsService.Creator.create();
    }

}
