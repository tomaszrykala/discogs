package com.tomaszrykala.discogs;

import com.tomaszrykala.discogs.data.ListItem;
import com.tomaszrykala.discogs.data.ReleaseListItem;
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

    private final ArrayList<ListItem> mMockReleaseData = getMockReleaseData();

    @Provides
    ListMvp.ListPresenter provideListPresenter() {
        return new ListMvp.ListPresenter() {

            ListMvp.ListView mListView;

            @Override public void onRefreshRequested() {
                onRefresh(); // TODO: update test
            }

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
                for (int i = 0; i < mMockReleaseData.size(); i++) {
                    final ListItem listItem = mMockReleaseData.get(i);
                    final String listItemId = listItem.getId();
                    if (listItemId.equals(id)) {
                        final Release release = new Release();
                        release.setId(Integer.valueOf(listItemId));
                        release.setThumb(listItem.getThumbUrl());
                        release.setArtist(listItem.getSubtitle());
                        release.setTitle(listItem.getTitle());

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

    private ArrayList<ListItem> getMockReleaseData() {
        final ArrayList<ListItem> releaseArrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            final int id = 200 + i;
            releaseArrayList.add(
                    new ReleaseListItem(String.valueOf(id), "Essential Mix :: " + id, "Dixon and Ame :: " + id, "url"));
        }
        return releaseArrayList;
    }
}
