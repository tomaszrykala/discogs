package com.tomaszrykala.discogs.mvp.impl;

import com.tomaszrykala.discogs.data.local.RealmService;
import com.tomaszrykala.discogs.data.model.Label;
import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.data.remote.DiscogsService;
import com.tomaszrykala.discogs.mvp.BaseMvp;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DiscogsModel implements BaseMvp.Model {

    final RealmService mRealmService;
    final DiscogsService mDiscogsService;
    private Call<Label> mCall;
    private boolean mWasModelEmpty;

    public DiscogsModel(RealmService realmService, DiscogsService discogsService) {
        mRealmService = realmService;
        mDiscogsService = discogsService;
    }

    @Override public void reset() {
        mRealmService.resetRealm();
        mWasModelEmpty = true;
    }

    @Override
    public List<Release> getPersistedCharts() {
        return mRealmService.copyFromRealm();
    }

    @Override public Release getChart(String id) {
        return mRealmService.getChart(id);
    }

    @Override public void fetchCharts(final Callback callback) {
        final List<Release> charts = mRealmService.getCharts();
        mWasModelEmpty = charts.isEmpty();
        if (mWasModelEmpty) {
            mCall = mDiscogsService.getLabel();
            mCall.enqueue(new retrofit2.Callback<Label>() {
                @Override public void onResponse(Call<Label> call, Response<Label> response) {
                    final List<Release> chartList = response.body().getReleases();
                    callback.onSuccess(chartList);
                    mCall = null;
                }

                @Override public void onFailure(Call<Label> call, Throwable t) {
                    callback.onFail(t.getMessage());
                    mCall = null;
                }
            });
        } else {
            callback.onSuccess(charts);
        }
    }

    @Override
    public void persistCharts(List<Release> list) {
        if (mWasModelEmpty) {
            mRealmService.setChartList(list);
        }
        mWasModelEmpty = false;
    }

    @Override public void cancelFetchCharts() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
    }
}
