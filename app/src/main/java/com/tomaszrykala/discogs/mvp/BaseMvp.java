package com.tomaszrykala.discogs.mvp;

import com.tomaszrykala.discogs.data.ListItem;
import com.tomaszrykala.discogs.data.model.Release;

import java.util.List;

public interface BaseMvp {

    interface Model {

        void reset();

        Release get(String id);

        List<Release> getPersisted();

        void persist(List<Release> list);

        void fetch(Callback callback);

        void fetch(Callback callback, int page);

        interface Callback {
//            void onSuccess(List<Release> list, boolean fromCache, int nextPage);

            void onSuccess(List<ListItem> items, boolean fromCache, int nextPage);

            void onFail(String error);
        }

        void cancelFetch();
    }

    interface View {

        void showLoading(boolean isLoading);

        void onLoadFail(String error);
    }

    interface Presenter<V extends View> {

        void setView(V view);

        void releaseView();
    }
}
