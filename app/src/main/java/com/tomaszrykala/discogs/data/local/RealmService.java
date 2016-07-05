package com.tomaszrykala.discogs.data.local;

import android.util.Log;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class RealmService {

    private final Realm mRealm;

    public RealmService(Realm realm) {
        mRealm = realm;
    }

    public void resetRealm() {
        mRealm.beginTransaction();
        mRealm.deleteAll();
        mRealm.commitTransaction();
    }

    public List<Release> copyFromRealm() {
        final List<Release> releases = getCharts();
        return mRealm.copyFromRealm(releases);
    }

    public void closeRealm() {
        mRealm.close();
    }

    public Release getChart(String id) {
        final RealmQuery<Release> where = mRealm.where(Release.class);
        return where.equalTo("id", Integer.parseInt(id)).findFirst();
    }

    public List<Release> getCharts() {
        return mRealm.allObjects(Release.class);
    }

    public void setChartList(List<Release> chartList) {
        final Iterator<Release> iterator = chartList.iterator();
        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            final Release chart = iterator.next();
            try {
                mRealm.beginTransaction();

                final Release realmChart = mRealm.createObject(Release.class);
                realmChart.setId(chart.getId());
                realmChart.setTitle(realmChart.getTitle());
                realmChart.setArtist(realmChart.getArtist());

//                final Images realmImages = mRealm.createObject(Images.class);
//                realmImages._default = chart.images._default;
//                realmChart.setImages(realmImages);
//
//                final Share realmShare = mRealm.createObject(Share.class);
//                realmShare.subject = chart.share.subject;
//                realmShare.text = chart.share.text;
//                realmShare.href = chart.share.href;
//                realmShare.image = chart.share.image;
//                realmShare.twitter = chart.share.twitter;
//                realmChart.setShare(realmShare);

                mRealm.copyToRealmOrUpdate(realmChart);
            } catch (RealmPrimaryKeyConstraintException e) {
                Log.d(RealmService.class.getSimpleName(), e.getMessage(), e);
            } finally {
                mRealm.commitTransaction();
            }
        }
    }
}
