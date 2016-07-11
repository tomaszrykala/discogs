package com.tomaszrykala.discogs.mvp.impl;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;

import java.util.List;

public class ListPresenterImpl implements ListMvp.ListPresenter {

    private final BaseMvp.Model mAppModel;
    private ListMvp.ListView mView;

    private List<Release> mReleases;
    private boolean mIsRefreshing;

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
        mAppModel.cancelFetch();
        mAppModel.fetch(new BaseMvp.Model.Callback() {
            @Override
            public void onSuccess(List<Release> list) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadSuccess(list);
                }
                mAppModel.persist(list);
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
        });
        mIsRefreshing = false;
    }
}
