package com.tomaszrykala.discogs;

import com.tomaszrykala.discogs.data.model.Release;
import com.tomaszrykala.discogs.mvp.BaseMvp;
import com.tomaszrykala.discogs.mvp.DetailMvp;
import com.tomaszrykala.discogs.mvp.impl.DetailPresenterImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DetailPresenterImplTest {

    @Mock
    DetailMvp.DetailView mMockView;
    @Mock
    BaseMvp.Model mMockAppModel;

    private DetailMvp.DetailPresenter mPresenter;

    @Before
    public void setUp() {
        mPresenter = new DetailPresenterImpl(mMockAppModel);
        mPresenter.setView(mMockView);

        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @After
    public void tearDown() {
        mPresenter.releaseView();
    }

    @Test
    public void getRelease_failedGetReleaseShowsErrorInView() {
        final String id = "000";
        mPresenter.load(id);
        Mockito.verify(mMockAppModel).get(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadFail("Failed to load: 000");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void getRelease_successfulGetReleaseDeliversResultToView() {
        final String id = "000";
        final Release Release = new Release();
        Release.setId(Integer.parseInt(id));
        Mockito.when(mMockAppModel.get(id)).thenReturn(Release);

        mPresenter.load(id);
        Mockito.verify(mMockAppModel).get(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(Release);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void setView_doesNoInteractions() {
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void releaseView_doesNoInteractions() {
        mPresenter.releaseView();
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void onFabClick_nullReleaseShowsError() {
        mPresenter.onFabClick();
        Mockito.verify(mMockView).share("Failed to share track.");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void onFabClick_ReleaseWithNoShareDataShowsError() {
        final String id = "000";
        final Release Release = new Release();
        Release.setId(Integer.parseInt(id));
        Mockito.when(mMockAppModel.get(id)).thenReturn(Release);

        mPresenter.load(id);
        Mockito.verify(mMockAppModel).get(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(Release);
        Mockito.verifyNoMoreInteractions(mMockView);

        mPresenter.onFabClick();
        Mockito.verify(mMockView).share("Failed to share track.");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void onFabClick_validReleaseShares() {
        final String id = "000";
        final Release release = new Release();
        release.setId(Integer.parseInt(id));
        release.setArtist("Madonna");
        Mockito.when(mMockAppModel.get(id)).thenReturn(release);
        final String expected = "\"Tweet\" sent:\n" + release;

        mPresenter.load(id);
        Mockito.verify(mMockAppModel).get(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(release);
        Mockito.verifyNoMoreInteractions(mMockView);

        mPresenter.onFabClick();
        Mockito.verify(mMockView).share(expected);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

}
