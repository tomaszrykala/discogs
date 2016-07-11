package com.tomaszrykala.discogs.util;

import com.tomaszrykala.discogs.data.model.Release;

import java.util.ArrayList;
import java.util.List;

public class ListItem {

    public static List<ReleaseListItem> toReleaseListItems(List<Release> releases) {
        List<ReleaseListItem> items = new ArrayList<>();
        for (int i = 0, size = releases.size(); i < size; i++) {
            final Release release = releases.get(i);
            final String key = String.valueOf(release.getId());
            final String title = release.getTitle();
            final String artist = release.getArtist();
            // final String aDefault = images.get_default();
            final ReleaseListItem item = new ReleaseListItem(key, title, artist, null);
            items.add(item);
        }
        return items;
    }

    public static class ReleaseListItem {
        public final String id;
        public final String title;
        public final String artist;
        public final String artUrl;

        ReleaseListItem(String id, String title, String artist, String artUrl) {
            this.id = id;
            this.title = title;
            this.artist = artist;
            this.artUrl = artUrl;
        }

        @Override
        public String toString() {
            return "ReleaseListItem{" +
                    "artist='" + artist + '\'' +
                    ", id='" + id + '\'' +
                    ", title='" + title + '\'' +
                    ", artUrl='" + artUrl + '\'' +
                    '}';
        }
    }
}
