package com.tomaszrykala.discogs;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.tomaszrykala.discogs.dagger.component.AppComponent;
import com.tomaszrykala.discogs.dagger.component.DaggerAppComponent;
import com.tomaszrykala.discogs.dagger.module.ModelModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DiscogsApp extends Application {

    private static DiscogsApp sInstance;

    @Override public void onCreate() {
        super.onCreate();
        sInstance = this;
        LeakCanary.install(sInstance);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(sInstance).build());
    }

    public static DiscogsApp getInstance() {
        return sInstance;
    }

    public AppComponent getComponent() {
        return DaggerAppComponent.builder().modelModule(new ModelModule()).build();

    }
}
