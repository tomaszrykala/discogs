package com.tomaszrykala.discogs.mvp;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.List;

public interface BaseMvp {

    interface Model {

        void reset();

        Release getChart(String id);

        List<Release> getPersistedCharts();

        void persistCharts(List<Release> list);

        void fetchCharts(Callback callback);

        interface Callback {
            void onSuccess(List<Release> list);

            void onFail(String error);
        }

        void cancelFetchCharts();
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
