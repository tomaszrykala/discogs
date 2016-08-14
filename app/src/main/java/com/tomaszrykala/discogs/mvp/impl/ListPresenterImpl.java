package com.tomaszrykala.discogs.mvp.impl;

import android.support.annotation.NonNull;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;

import java.util.List;

import static com.tomaszrykala.discogs.mvp.impl.DiscogsModel.ALL_RESULTS_FETCHED;

public class ListPresenterImpl implements ListMvp.ListPresenter {

    private final BaseMvp.Model mAppModel;
    private ListMvp.ListView mView;

    private List<Release> mReleases;
    private boolean mIsRefreshing;
    private int mNextPage;

    public ListPresenterImpl(BaseMvp.Model appModel) {
        mAppModel = appModel;
    }

    @Override
    public void onRequestCancel() {
        mView.showLoading(false);
    }

    @Override
    public void onRefresh() {
        mReleases = mAppModel.getPersisted();
        // TODO: next page of results aren't being persisted
        mIsRefreshing = !mReleases.isEmpty();
        if (mIsRefreshing) mAppModel.reset();
        doLoad(mIsRefreshing);
    }

    @Override
    public void load() {
        doLoad(false);
    }

    @Override
    public void setView(ListMvp.ListView view) {
        mView = view;
        doLoad(false);
    }

    @Override
    public void releaseView() {
        mAppModel.cancelFetch();
        mView = null;
    }

    private void doLoad(final boolean reset) {
        mView.showLoading(true);
        mIsRefreshing = false;
        mAppModel.cancelFetch();

        if (mNextPage == ALL_RESULTS_FETCHED) {
            mView.onLoadFail("No more results to fetch.");
        } else if (mNextPage > 0) {
            mAppModel.fetch(getCallback(reset), mNextPage);
        } else {
            mAppModel.fetch(getCallback(reset));
        }
    }

    @NonNull private BaseMvp.Model.Callback getCallback(final boolean reset) {
        return new BaseMvp.Model.Callback() {

            @Override
            public void onSuccess(List<Release> list, int nextPage) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadSuccess(list);
                }
                mAppModel.persist(list);
                mNextPage = nextPage;
            }

            @Override
            public void onFail(String error) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadFail(error);
                }

                if (reset) {
                    mAppModel.persist(mReleases);
                }
            }
        };
    }
}
