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
    ListMvp.ListPresenter provideListPresenter() {
        return new ListMvp.ListPresenter() {

            final ArrayList<Release> mMockReleaseData = getMockReleaseData();
            ListMvp.ListView mListView;

            @Override
            public void onRequestCancel() {
                // no-op
            }

            @Override
            public void onRefresh() {
                mListView.onLoadSuccess(mMockReleaseData);
            }

            @Override
            public void load() {
                mListView.onLoadSuccess(mMockReleaseData);
            }

            @Override
            public void setView(ListMvp.ListView view) {
                mListView = view;
                mListView.onLoadSuccess(mMockReleaseData);
            }

            @Override
            public void releaseView() {
                mListView = null;
            }
        };
    }

    @Provides
    DetailMvp.DetailPresenter provideDetailPresenter() {
        return new DetailMvp.DetailPresenter() {

            DetailMvp.DetailView mDetailView;

            @Override
            public void load(String id) {
                final ArrayList<Release> mockReleaseData = getMockReleaseData();
                for (int i = 0; i < mockReleaseData.size(); i++) {
                    final Release release = mockReleaseData.get(i);
                    if (release.getId().equals(Integer.parseInt(id))) {
                        mDetailView.onLoadSuccess(release);
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

    static ArrayList<Release> getMockReleaseData() {
        final ArrayList<Release> releaseArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final Release release = new Release();
            // final String key = String.valueOf(200 + i);
            final int id = 200 + i;
            release.setId(id);
//            final Images images = new Images();
//            images.set_default("https://iambloggingat.files.wordpress.com/2010/06/gerty.png?w=595");
            // release.setImages(images);
            // final Heading heading = new Heading();
            release.setArtist("Dixon and Ame :: " + id);
            release.setTitle("Essential Mix :: " + id);
            // release.setHeading(heading);
            releaseArrayList.add(release);
        }
        return releaseArrayList;
    }
}
