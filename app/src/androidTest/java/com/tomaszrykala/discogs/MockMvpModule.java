package com.tomaszrykala.discogs;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.DetailMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;

import org.mockito.Mockito;

import java.util.ArrayList;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMvpModule {

    @Provides
    @Singleton
    ListMvp.ListPresenter provideListPresenter() {
        return new ListMvp.ListPresenter() {

            final ArrayList<Release> mMockChartData = getMockChartData();
            ListMvp.ListView mListView;

            @Override
            public void onRequestCancel() {
                // no-op
            }

            @Override
            public void onRefresh() {
                mListView.onLoadSuccess(mMockChartData);
            }

            @Override
            public void load() {
                mListView.onLoadSuccess(mMockChartData);
            }

            @Override
            public void setView(ListMvp.ListView view) {
                mListView = view;
                mListView.onLoadSuccess(mMockChartData);
            }

            @Override
            public void releaseView() {
                mListView = null;
            }
        };
    }

    @Provides
    @Singleton
    DetailMvp.DetailPresenter provideDetailPresenter() {
        return new DetailMvp.DetailPresenter() {

            DetailMvp.DetailView mDetailView;

            @Override
            public void loadChart(String id) {
                final ArrayList<Release> mockChartData = getMockChartData();
                for (int i = 0; i < mockChartData.size(); i++) {
                    final Release chart = mockChartData.get(i);
                    if (chart.getId().equals(Integer.parseInt(id))) {
                        mDetailView.onLoadSuccess(chart);
                        break;
                    }
                }
            }

            @Override
            public void onFabClick() {
                // no-op
            }

            @Override
            public void setView(DetailMvp.DetailView view) {
                mDetailView = view;
            }

            @Override
            public void releaseView() {
                mDetailView = null;
            }
        };
    }

    @Provides
    @Singleton
    BaseMvp.Model provideDiscogsModel() {
        return Mockito.mock(BaseMvp.Model.class); // TODO: or specific test impl like above :-)
    }

    static ArrayList<Release> getMockChartData() {
        final ArrayList<Release> chartArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final Release chart = new Release();
            // final String key = String.valueOf(200 + i);
            final int id = 200 + i;
            chart.setId(id);
//            final Images images = new Images();
//            images.set_default("https://iambloggingat.files.wordpress.com/2010/06/gerty.png?w=595");
            // chart.setImages(images);
            // final Heading heading = new Heading();
            chart.setArtist("Dixon and Ame :: " + id);
            chart.setTitle("Essential Mix :: " + id);
            // chart.setHeading(heading);
            chartArrayList.add(chart);
        }
        return chartArrayList;
    }
}
