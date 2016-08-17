package com.tomaszrykala.discogs.ui.list;

import android.support.annotation.NonNull;

import com.tomaszrykala.discogs.data.ListItem;
import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;

import java.util.List;

import static com.tomaszrykala.discogs.ui.impl.DiscogsModel.ALL_RESULTS_FETCHED;

public class ListPresenterImpl implements ListMvp.ListPresenter {

    private final BaseMvp.Model mAppModel;
    private ListMvp.ListView mView;

    private List<Release> mReleases;
    private boolean mIsRefreshing;
    private int mNextPage;

    public ListPresenterImpl(BaseMvp.Model appModel) {
        mAppModel = appModel;
    }

    @Override public void onRefreshRequested() {
        if (mNextPage == ALL_RESULTS_FETCHED) {
            mView.showSnackBar("All results already fetched.", "Re-fetch?");
        } else {
            mView.showSnackBar("Fetch next page?", "Yes");
        }
    }

    @Override
    public void onRequestCancel() {
        mView.showLoading(false);
    }

    @Override
    public void onRefresh() {
        mReleases = mAppModel.getPersisted();
        mIsRefreshing = (mNextPage == ALL_RESULTS_FETCHED && !mReleases.isEmpty());
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

        if (mNextPage > 0) {
            mAppModel.fetch(getCallback(reset), mNextPage);
        } else {
            mAppModel.fetch(getCallback(reset));
        }
    }

    @NonNull private BaseMvp.Model.Callback getCallback(final boolean reset) {
        return new BaseMvp.Model.Callback() {

            @Override public void onSuccess(List<ListItem> items, boolean fromCache, int nextPage) {
                if (mView != null) {
                    mView.showLoading(false);
                    mView.onLoadSuccess(items);
                }
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
