package com.tomaszrykala.discogs.ui.impl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.tomaszrykala.discogs.data.ListItem;
import com.tomaszrykala.discogs.data.ReleaseListItem;
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
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DiscogsModel implements BaseMvp.Model {

    private final String TAG = DiscogsModel.class.getSimpleName();

    public static final int ALL_RESULTS_FETCHED = -1;

    private final RealmService mRealmService;
    private final DiscogsService mDiscogsService;

    private Call<Label> mCall;
    private boolean mWasModelEmpty;
    private int mNextPage;

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
            Observable.from(releases)
                    .map(new Func1<Release, ListItem>() {
                        @Override public ListItem call(Release release) {
                            return getListItem(release);
                        }
                    })
                    .toList()
                    .subscribe(new Action1<List<ListItem>>() {
                        @Override public void call(List<ListItem> listItems) {
                            callback.onSuccess(listItems, true, ALL_RESULTS_FETCHED);
                        }
                    });
        }
    }

    @Override public void fetch(Callback callback, int page) {
        getLabel(callback, mDiscogsService.getLabel(page));
    }

    private void getLabel(final Callback callback, @NonNull final Observable<Label> labelObservable) {
        labelObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<Label>() {
                    @Override public void call(Label label) {
                        mNextPage = getNextPage(label);
                    }
                })
                .map(new Func1<Label, List<Release>>() {
                    @Override public List<Release> call(Label label) {
                        return getSortedReleases(label);
                    }
                })
                .doOnNext(new Action1<List<Release>>() {
                    @Override public void call(List<Release> releases) {
                        if (mWasModelEmpty || mNextPage != 0) {
                            Log.d(TAG, "DiscogsModel.releases = [" + releases.size() + "]");
                            persist(releases);
                        }
                    }
                })
                .flatMapIterable(new Func1<List<Release>, Iterable<Release>>() {
                    @Override public Iterable<Release> call(List<Release> releases) {
                        return releases;
                    }
                })
                .map(new Func1<Release, ListItem>() {
                    @Override public ListItem call(Release release) {
                        return getListItem(release);
                    }
                })
                .toList()
                .subscribe(new Observer<List<ListItem>>() {
                    @Override public void onCompleted() {
                        Log.d(TAG, "DiscogsModel.onCompleted");
                    }

                    @Override public void onError(Throwable e) {
                        callback.onFail(e.getMessage());
                    }

                    @Override public void onNext(List<ListItem> releaseListItems) {
                        callback.onSuccess(releaseListItems, false, mNextPage);
                    }
                });
    }

    @NonNull private ListItem getListItem(Release release) {
        return new ReleaseListItem(
                String.valueOf(release.getId()),
                release.getTitle(),
                release.getArtist(),
                release.getThumb());
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
