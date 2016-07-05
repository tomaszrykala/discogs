package com.tomaszrykala.discogs.mvp.impl;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;

import java.util.List;

public class ListPresenterImpl implements ListMvp.ListPresenter {

    private boolean mIsRefreshing;

    private ListMvp.ListView mView;

    private final BaseMvp.Model mAppModel;
    private List<Release> mPersistedCharts;

    public ListPresenterImpl(BaseMvp.Model appModel) {
        mAppModel = appModel;
    }

    @Override
    public void onRequestCancel() {
        mView.showLoading(false);
    }

    @Override
    public void onRefresh() {
        mPersistedCharts = mAppModel.getPersistedCharts();
        mIsRefreshing = !mPersistedCharts.isEmpty();
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
        mAppModel.cancelFetchCharts();
        mView = null;
    }

    private void doLoad(final boolean reset) {
        mView.showLoading(true);
        mAppModel.cancelFetchCharts();
        mAppModel.fetchCharts(new BaseMvp.Model.Callback() {
            @Override
            public void onSuccess(List<Release> list) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadSuccess(list);
                }
                mAppModel.persistCharts(list);
            }

            @Override
            public void onFail(String error) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadFail(error);
                }

                if (reset) {
                    mAppModel.persistCharts(mPersistedCharts);
                }
            }
        });
        mIsRefreshing = false;
    }
}
