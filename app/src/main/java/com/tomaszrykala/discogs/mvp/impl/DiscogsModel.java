package com.tomaszrykala.discogs.mvp.impl;

import android.support.annotation.NonNull;

import com.tomaszrykala.discogs.data.local.RealmService;
import com.tomaszrykala.discogs.data.model.Label;
import com.tomaszrykala.discogs.data.model.Pagination;
import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.data.remote.DiscogsService;
import com.tomaszrykala.discogs.mvp.BaseMvp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Response;

public class DiscogsModel implements BaseMvp.Model {

    public static final int ALL_RESULTS_FETCHED = -1;
    private final RealmService mRealmService;
    private final DiscogsService mDiscogsService;

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
            mCall.enqueue(getCallback(callback));
        } else {
            callback.onSuccess(releases, 1);
        }
    }

    @Override public void fetch(Callback callback, int page) {
        mDiscogsService.getLabel(page).enqueue(getCallback(callback));
    }

    @NonNull private retrofit2.Callback<Label> getCallback(final Callback callback) {
        return new retrofit2.Callback<Label>() {
            @Override public void onResponse(Call<Label> call, Response<Label> response) {
                final Label label = response.body();

                final Pagination pagination = label.getPagination();
                final int pages = pagination.getPages();
                final Integer page = pagination.getPage();
                final int nextPage = pages > page ? page + 1 : ALL_RESULTS_FETCHED;

                final List<Release> releaseList = label.getReleases();
                final Set<Release> releaseSet = new HashSet<>(releaseList);
                final List<Release> releaseSortedList = new ArrayList<>(releaseSet);
                Collections.sort(releaseSortedList, Release.COMPARATOR);
                callback.onSuccess(releaseSortedList, nextPage);
                mCall = null;
            }

            @Override public void onFailure(Call<Label> call, Throwable t) {
                callback.onFail(t.getMessage());
                mCall = null;
            }
        };
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
