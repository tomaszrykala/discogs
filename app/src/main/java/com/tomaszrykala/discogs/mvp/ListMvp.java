package com.tomaszrykala.discogs.mvp;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.List;

public interface ListMvp extends BaseMvp {

    interface ListView extends View {

        void onLoadSuccess(List<Release> list);

        void showSnackBar(String message, String action);
    }

    interface ListPresenter extends Presenter<ListView> {

        void onRefreshRequested();

        void onRequestCancel();

        void onRefresh();

        void load();
    }
}
