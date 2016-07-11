package com.tomaszrykala.discogs.mvp.impl;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.DetailMvp;

public class DetailPresenterImpl implements DetailMvp.DetailPresenter {

    private DetailMvp.DetailView mView;

    private final BaseMvp.Model mAppModel;

    private Release mRelease;

    public DetailPresenterImpl(BaseMvp.Model appModel) {
        mAppModel = appModel;
    }

    @Override
    public void load(String id) {
        mRelease = mAppModel.get(id);
        if (mRelease != null) {
            mView.onLoadSuccess(mRelease);
        } else {
            mView.onLoadFail("Failed to load: " + id);
        }
    }

    @Override
    public void onFabClick() {
        if (mRelease != null && mRelease.getArtist() != null) {
            final String share = mRelease.getArtist();
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
