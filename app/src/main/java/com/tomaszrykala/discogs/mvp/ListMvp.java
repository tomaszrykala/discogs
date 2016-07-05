package com.tomaszrykala.discogs.mvp;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.List;

public interface ListMvp extends BaseMvp {

    interface ListView extends View {

        void onLoadSuccess(List<Release> list);
    }

    interface ListPresenter extends Presenter<ListView> {

        void onRequestCancel();

        void onRefresh();

        void load();
    }
}
