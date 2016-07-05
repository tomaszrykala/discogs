package com.tomaszrykala.discogs.mvp.impl;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.DetailMvp;

public class DetailPresenterImpl implements DetailMvp.DetailPresenter {

    private DetailMvp.DetailView mView;

    private final BaseMvp.Model mAppModel;

    private Release mChart;

    public DetailPresenterImpl(BaseMvp.Model appModel) {
        mAppModel = appModel;
    }

    @Override
    public void loadChart(String id) {
        mChart = mAppModel.getChart(id);
        if (mChart != null) {
            mView.onLoadSuccess(mChart);
        } else {
            mView.onLoadFail("Failed to load chart: " + id);
        }
    }

    @Override
    public void onFabClick() {
        if (mChart != null && mChart.getArtist() != null) {
            final String share = mChart.getArtist();
            mView.share("\"Tweet\" sent:\n" + share);
//            mView.share("\"Tweet\" sent:\n" + share.text + " - " + share.href);
        } else {
            mView.share("Failed to share track.");
        }
    }

    @Override
    public void setView(DetailMvp.DetailView view) {
        mView = view;
    }

    @Override
    public void releaseView() {
        mView = null;
    }
}
