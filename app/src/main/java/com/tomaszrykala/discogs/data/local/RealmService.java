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
        final List<Release> releases = getReleases();
        return mRealm.copyFromRealm(releases);
    }

    public void closeRealm() {
        mRealm.close();
    }

    public Release getRelease(String id) {
        final RealmQuery<Release> where = mRealm.where(Release.class);
        return where.equalTo("id", Integer.parseInt(id)).findFirst();
    }

    public List<Release> getReleases() {
        return mRealm.where(Release.class).findAll();
    }

    public void setReleaseList(List<Release> releaseList) {
        final Iterator<Release> iterator = releaseList.iterator();
        while (iterator.hasNext()) {
            final Release release = iterator.next();
            try {
                mRealm.beginTransaction();

                final Release realmRelease = mRealm.createObject(Release.class);
                realmRelease.setId(release.getId());
                realmRelease.setTitle(release.getTitle());
                realmRelease.setArtist(release.getArtist());
                realmRelease.setThumb(release.getThumb());

//                final Images realmImages = mRealm.createObject(Images.class);
//                realmImages._default = release.images._default;
//                realmRelease.setImages(realmImages);
//
//                final Share realmShare = mRealm.createObject(Share.class);
//                realmShare.subject = release.share.subject;
//                realmShare.text = release.share.text;
//                realmShare.href = release.share.href;
//                realmShare.image = release.share.image;
//                realmShare.twitter = release.share.twitter;
//                realmRelease.setShare(realmShare);

                mRealm.copyToRealmOrUpdate(realmRelease);
            } catch (RealmPrimaryKeyConstraintException e) {
                Log.d(RealmService.class.getSimpleName(), e.getMessage(), e);
            } finally {
                mRealm.commitTransaction();
            }
        }
    }
}
