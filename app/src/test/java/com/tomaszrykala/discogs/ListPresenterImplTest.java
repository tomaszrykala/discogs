package com.tomaszrykala.discogs;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.ListMvp;
import com.tomaszrykala.discogs.mvp.impl.ListPresenterImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class ListPresenterImplTest {

    @Mock
    ListMvp.ListView mMockView;
    @Mock
    BaseMvp.Model mMockAppModel;

    private ListMvp.ListPresenter mPresenter;

    @Captor
    ArgumentCaptor<ListMvp.Model.Callback> mCaptor;

    @Before
    public void setUp() {
        mPresenter = new ListPresenterImpl(mMockAppModel);
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        mPresenter.setView(mMockView);
        Mockito.verify(mMockView).showLoading(true);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @After
    public void tearDown() {
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void setView_performsLoadAfterSet() {
        performSuccessfulLoadTest(1);
    }

    @Test
    public void load_showsLoadingAndPerformsLoad() {
        Mockito.verify(mMockView).showLoading(true);
        performSuccessfulLoadTest(1);
    }

    private void performSuccessfulLoadTest(int times) {
        Mockito.verify(mMockView, Mockito.times(times)).showLoading(true);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel, Mockito.times(times)).cancelFetch();
        Mockito.verify(mMockAppModel, Mockito.times(times)).fetch(mCaptor.capture());
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        final ArrayList<Release> releases = new ArrayList<>();
        mCaptor.getValue().onSuccess(releases, 1); // test pagination
        Mockito.verify(mMockView).onLoadSuccess(releases);
        Mockito.verify(mMockView).showLoading(false);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel).persist(releases);
    }

    @Test
    public void load_failedLoadPerformsOnLoadFailOnView() {
        Mockito.verify(mMockView).showLoading(true);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel).fetch(mCaptor.capture());
        final String error = "Load failed";
        mCaptor.getValue().onFail(error);
        Mockito.verify(mMockAppModel).cancelFetch();
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        Mockito.verify(mMockView).onLoadFail(error);
        Mockito.verify(mMockView).showLoading(false);
    }

    @Test
    public void load_successfulLoadPerformsOnLoadSuccessOnView() {
        performSuccessfulLoadTest(1);
    }

    @Test
    public void releaseView_doesNoInteractions() {
        performSuccessfulLoadTest(1);
        mPresenter.releaseView();
        Mockito.verify(mMockAppModel, Mockito.times(2)).cancelFetch();
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        final ArrayList<Release> releases = new ArrayList<>();
        mCaptor.getValue().onSuccess(releases, 1); // test pagination
        Mockito.verify(mMockView).onLoadSuccess(releases);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel, Mockito.times(2)).persist(releases);
    }

    @Test
    public void requestCancel_dismissedLoading() {
        performSuccessfulLoadTest(1);
        mPresenter.onRequestCancel();
        Mockito.verify(mMockAppModel).cancelFetch();
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        final ArrayList<Release> releases = new ArrayList<>();
        mCaptor.getValue().onSuccess(releases, 1); // test pagination
        Mockito.verify(mMockView, Mockito.times(3)).showLoading(false);
        Mockito.verify(mMockView, Mockito.times(2)).onLoadSuccess(releases);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel, Mockito.times(2)).persist(releases);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
    }

    @Test
    public void onRefresh_resetsModelAndPerformsLoad() {
        mPresenter.onRefresh();
        Mockito.verify(mMockAppModel).getPersisted();
        final int invocations = 2;
        performSuccessfulLoadTest(invocations);

        Mockito.verify(mMockAppModel, Mockito.times(invocations)).cancelFetch();
        Mockito.verifyNoMoreInteractions(mMockAppModel);

        final ArrayList<Release> releases = new ArrayList<>();
        mCaptor.getValue().onSuccess(releases, 1); // test pagination
        Mockito.verify(mMockView, Mockito.times(invocations)).showLoading(false);
        Mockito.verify(mMockView, Mockito.times(invocations)).onLoadSuccess(releases);
        Mockito.verifyNoMoreInteractions(mMockView);

        Mockito.verify(mMockAppModel, Mockito.times(invocations)).persist(releases);
    }
}
