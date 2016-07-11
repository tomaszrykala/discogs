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
    public List<Release> getPersisted() {
        return mRealmService.copyFromRealm();
    }

    @Override public Release get(String id) {
        return mRealmService.getRelease(id);
    }

    @Override public void fetch(final Callback callback) {
        final List<Release> releases = mRealmService.getReleases();
        mWasModelEmpty = releases.isEmpty();
        if (mWasModelEmpty) {
            mCall = mDiscogsService.getLabel();
            mCall.enqueue(new retrofit2.Callback<Label>() {
                @Override public void onResponse(Call<Label> call, Response<Label> response) {
                    final List<Release> releaseList = response.body().getReleases();
                    callback.onSuccess(releaseList);
                    mCall = null;
                }

                @Override public void onFailure(Call<Label> call, Throwable t) {
                    callback.onFail(t.getMessage());
                    mCall = null;
                }
            });
        } else {
            callback.onSuccess(releases);
        }
    }

    @Override
    public void persist(List<Release> list) {
        if (mWasModelEmpty) {
            mRealmService.setReleaseList(list);
        }
        mWasModelEmpty = false;
    }

    @Override public void cancelFetch() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
    }
}
