package com.tomaszrykala.discogs.mvp;

import com.tomaszrykala.discogs.data.model.Release;

public interface DetailMvp extends BaseMvp {

    interface DetailView extends View {

        void onLoadSuccess(Release release);

        void share(String message);
    }

    interface DetailPresenter extends Presenter<DetailView> {

        void load(String id);

        void onFabClick();
    }
}
