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
class MockMvpModule {

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
        return Mockito.mock(BaseMvp.Model.class); // ...or specific test impl like above
    }

    private ArrayList<Release> getMockReleaseData() {
        final ArrayList<Release> releaseArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final Release release = new Release();
            final int id = 200 + i;
            release.setId(id);
            release.setThumb("");
            release.setArtist("Dixon and Ame :: " + id);
            release.setTitle("Essential Mix :: " + id);

            // TODO
//            realmRelease.setYear(release.getYear());
//            realmRelease.setCatno(release.getCatno());
//            realmRelease.setFormat(release.getFormat());
//            realmRelease.setResourceUrl(release.getResourceUrl());

            releaseArrayList.add(release);
        }
        return releaseArrayList;
    }
}
