package com.tomaszrykala.discogs.dagger.module;

import com.tomaszrykala.discogs.data.local.RealmService;
import com.tomaszrykala.discogs.data.remote.DiscogsService;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.DetailMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;
import com.tomaszrykala.discogs.mvp.impl.DetailPresenterImpl;
import com.tomaszrykala.discogs.mvp.impl.DiscogsModel;
import com.tomaszrykala.discogs.mvp.impl.ListPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MvpModule {

    @Provides ListMvp.ListPresenter provideListPresenter(BaseMvp.Model model) {
        return new ListPresenterImpl(model);
    }

    @Provides DetailMvp.DetailPresenter provideDetailPresenter(BaseMvp.Model model) {
        return new DetailPresenterImpl(model);
    }

    @Provides @Singleton BaseMvp.Model provideDiscogsModel(RealmService realmService, DiscogsService discogsService) {
        return new DiscogsModel(realmService, discogsService);
    }
}
