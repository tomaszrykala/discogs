package com.tomaszrykala.discogs.dagger.component;

import com.tomaszrykala.discogs.dagger.module.ModelModule;
import com.tomaszrykala.discogs.dagger.module.MvpModule;
import com.tomaszrykala.discogs.ui.detail.DetailActivity;
import com.tomaszrykala.discogs.ui.list.ListActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ModelModule.class, MvpModule.class})
public interface AppComponent {

    void inject(ListActivity listActivity);

    void inject(DetailActivity mainActivity);
}
