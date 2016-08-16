package com.tomaszrykala.discogs.ui.impl;

import android.support.annotation.NonNull;
import android.util.Log;

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
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
            getLabel(callback, mDiscogsService.getLabel());
        } else {
            callback.onSuccess(releases, true, ALL_RESULTS_FETCHED);
        }
    }

    @Override public void fetch(Callback callback, int page) {
        getLabel(callback, mDiscogsService.getLabel(page));
    }

    private void getLabel(final Callback callback, @NonNull final Observable<Label> labelObservable) {
        labelObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Label>() {
                    @Override public void onCompleted() {
                        Log.d(DiscogsModel.this.getClass().getSimpleName(), "getLabel.onCompleted()");
                    }

                    @Override public void onError(Throwable e) {
                        callback.onFail(e.getMessage());
                    }

                    @Override public void onNext(Label label) {
                        callback.onSuccess(getSortedReleases(label), false, getNextPage(label));
                    }
                });
    }

    private int getNextPage(Label label) {
        final Pagination pagination = label.getPagination();
        final int pages = pagination.getPages();
        final Integer page = pagination.getPage();
        return pages > page ? page + 1 : ALL_RESULTS_FETCHED;
    }

    @NonNull private List<Release> getSortedReleases(Label label) {
        final List<Release> releaseList = label.getReleases();
        final Set<Release> releaseSet = new HashSet<>(releaseList);
        final List<Release> releaseSortedList = new ArrayList<>(releaseSet);
        Collections.sort(releaseSortedList, Release.COMPARATOR);
        return releaseSortedList;
    }

    @Override public void persist(List<Release> list) {
        mRealmService.setReleaseList(list);
        mWasModelEmpty = false;
    }

    @Override public void cancelFetch() {
        if (mCall != null) {
            mCall.cancel();
            mCall = null;
        }
    }
}
