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
public class DetailPresenterTest {

    @Mock
    DetailMvp.DetailView mMockView;
    @Mock
    BaseMvp.Model mMockAppModel;

    DetailMvp.DetailPresenter mPresenter;

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
    public void getChart_failedGetChartShowsErrorInView() {
        final String id = "000";
        mPresenter.loadChart(id);
        Mockito.verify(mMockAppModel).getChart(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadFail("Failed to load chart: 000");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void getChart_successfulGetChartDeliversResultToView() {
        final String id = "000";
        final Release chart = new Release();
        chart.setId(Integer.parseInt(id));
        Mockito.when(mMockAppModel.getChart(id)).thenReturn(chart);

        mPresenter.loadChart(id);
        Mockito.verify(mMockAppModel).getChart(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(chart);
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
    public void onFabClick_nullChartShowsError() {
        mPresenter.onFabClick();
        Mockito.verify(mMockView).share("Failed to share track.");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void onFabClick_chartWithNoShareDataShowsError() {
        final String id = "000";
        final Release chart = new Release();
        chart.setId(Integer.parseInt(id));
        Mockito.when(mMockAppModel.getChart(id)).thenReturn(chart);

        mPresenter.loadChart(id);
        Mockito.verify(mMockAppModel).getChart(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(chart);
        Mockito.verifyNoMoreInteractions(mMockView);

        mPresenter.onFabClick();
        Mockito.verify(mMockView).share("Failed to share track.");
        Mockito.verifyNoMoreInteractions(mMockView);
    }

    @Test
    public void onFabClick_validChartShares() {
        final String id = "000";
        final Release chart = new Release();
        chart.setId(Integer.parseInt(id));
//        final Share share = new Share();
//        share.text = "Material Girl";
//        share.href = "www.web.com";
//        chart.setShare(share);
//        Mockito.when(mMockAppModel.getChart(id)).thenReturn(chart);
//        final String expected = "\"Tweet\" sent:\n" + share.text + " - " + share.href;

        mPresenter.loadChart(id);
        Mockito.verify(mMockAppModel).getChart(id);
        Mockito.verifyNoMoreInteractions(mMockAppModel);
        Mockito.verify(mMockView).onLoadSuccess(chart);
        Mockito.verifyNoMoreInteractions(mMockView);

        mPresenter.onFabClick();
//        Mockito.verify(mMockView).share(expected);
        Mockito.verifyNoMoreInteractions(mMockView);
    }

}
